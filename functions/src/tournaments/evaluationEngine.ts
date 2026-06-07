import {onDocumentUpdated} from "firebase-functions/v2/firestore";
import {db, FieldValue} from "../firebase/admin";
import {calculateRewardPercentages} from "../utils/tournamentRewards";
import {OPENAI_API_KEY, evaluateWithR8} from "../r8/evaluateWithR8";

type EvaluatedSubmission = {
  authorId: string;
  score: number;
  feedback: string;
  submissionRef: FirebaseFirestore.DocumentReference;
};

function getUtcDayInt(): number {
  const now = new Date();
  return now.getUTCFullYear() * 10000 + (now.getUTCMonth() + 1) * 100 + now.getUTCDate();
}

function meritToLiquid(current: number, earned: number, cap: number): number {
  if (current + earned > cap) return Math.max(0, cap - current);
  return earned;
}

function meritToHold(current: number, earned: number, cap: number): number {
  const liquid = meritToLiquid(current, earned, cap);
  return earned - liquid;
}

export const tournamentEvaluationEngine = onDocumentUpdated(
  {
    document: "tournaments/{tournamentId}",
    region: "us-central1",
    secrets: [OPENAI_API_KEY],
    timeoutSeconds: 540,
    memory: "512MiB",
  },
  async (event) => {
    const before = event.data?.before.data();
    const after = event.data?.after.data();

    if (!before || !after) return;

    if (before.status !== "EVALUATING" && after.status === "EVALUATING") {
      const tournamentId = event.params.tournamentId;
      const tournamentRef = db.collection("tournaments").doc(tournamentId);

      if (after.playersCount < after.minPlayers) {
        await tournamentRef.update({
          status: "CANCELLED",
          cancelledAt: Date.now(),
        });
        return;
      }

      const submissionsSnapshot = await tournamentRef
        .collection("submissions")
        .get();

      const apiKey = OPENAI_API_KEY.value();
      const evaluated: EvaluatedSubmission[] = [];

      for (const doc of submissionsSnapshot.docs) {
        const data = doc.data();
        let result;
        try {
          result = await evaluateWithR8({
            apiKey,
            content: data.content ?? "",
            gamemode: after.gamemode ?? "STANDARD",
            requiredWords: Array.isArray(after.requiredWords) ? after.requiredWords : [],
            themeName: after.themeName ?? null,
            topicName: after.topicName ?? null,
          });
        } catch (err) {
          console.error(`Evaluation failed for submission ${doc.id}:`, err);
          result = {finalScore: 0, feedback: "Evaluation unavailable."};
        }

        evaluated.push({
          authorId: data.authorId,
          score: result.finalScore,
          feedback: result.feedback,
          submissionRef: doc.ref,
        });
      }

      evaluated.sort((a, b) => b.score - a.score);

      const rewardPercentages = calculateRewardPercentages(evaluated.length);
      const today = getUtcDayInt();

      for (let i = 0; i < evaluated.length; i++) {
        const entry = evaluated[i];
        const rank = i + 1;
        const rewardPercent = rewardPercentages[i] ?? 0;
        const reward = Math.floor(after.prizePool * rewardPercent);

        const userRef = db.collection("users").doc(entry.authorId);

        await db.runTransaction(async (tx) => {
          const userSnap = await tx.get(userRef);
          if (!userSnap.exists) return;

          const userData = userSnap.data() || {};
          const currentMerit = userData.merit ?? 0;
          const meritCap = userData.meritCap ?? 50000;

          const liquid = reward > 0 ? meritToLiquid(currentMerit, reward, meritCap) : 0;
          const hold = reward > 0 ? meritToHold(currentMerit, reward, meritCap) : 0;
          const newMerit = currentMerit + liquid;

          const recentScores: number[] = Array.isArray(userData.recentScores) ? userData.recentScores : [];
          recentScores.push(entry.score);

          const lastDay = Number(userData.lastSubmissionDay ?? 0);
          const currentStreak = Number(userData.currentStreak ?? 0);
          let newStreak = currentStreak;

          if (lastDay !== today) {
            const yesterday = new Date();
            yesterday.setUTCDate(yesterday.getUTCDate() - 1);
            const yesterdayInt = yesterday.getUTCFullYear() * 10000 + (yesterday.getUTCMonth() + 1) * 100 + yesterday.getUTCDate();
            newStreak = (lastDay === yesterdayInt) ? currentStreak + 1 : 1;
          }

          const userUpdates: Record<string, any> = {
            merit: newMerit,
            meritHold: FieldValue.increment(hold),
            submissionsCount: FieldValue.increment(1),
            bestScore: Math.max(userData.bestScore || 0, entry.score),
            recentScores: recentScores.slice(-20),
            tournamentsPlayed: FieldValue.increment(1),
            totalMeritEarned: FieldValue.increment(reward),
            lastSubmissionDay: today,
            currentStreak: newStreak
          };

          if (rank === 1) {
            userUpdates.tournamentsWon = FieldValue.increment(1);
          }

          tx.update(userRef, userUpdates);

          tx.update(entry.submissionRef, {
            evaluation: {
              submissionId: entry.submissionRef.id,
              finalScore: entry.score,
              feedback: entry.feedback,
              meritEarned: reward,
              meritToHold: hold,
              ratingChange: 0,
              rankLeaderboard: rank,
              resultStatus: "EVALUATED",
            },
            status: "EVALUATED",
          });

          if (liquid !== 0) {
            const txRef = userRef.collection("meritTransactions").doc();
            tx.set(txRef, {
              amount: liquid,
              reason: "TOURNAMENT_REWARD",
              timestamp: Date.now(),
              balanceAfter: newMerit,
            });
          }
        });
      }

      const totalRevenue = after.entranceFee * after.playersCount;
      const hostProfit = totalRevenue - after.prizePool - after.systemFee;

      if (hostProfit > 0) {
        const hostRef = db.collection("users").doc(after.creatorId);
        await db.runTransaction(async (tx) => {
          const hostSnap = await tx.get(hostRef);
          if (!hostSnap.exists) return;

          const hostData = hostSnap.data() || {};
          const currentMerit = hostData.merit ?? 0;
          const meritCap = hostData.meritCap ?? 50000;

          const liquid = meritToLiquid(currentMerit, hostProfit, meritCap);
          const hold = meritToHold(currentMerit, hostProfit, meritCap);
          const newMerit = currentMerit + liquid;

          tx.update(hostRef, {
            merit: newMerit,
            meritHold: FieldValue.increment(hold),
            totalMeritEarned: FieldValue.increment(hostProfit),
          });

          if (liquid !== 0) {
            const txRef = hostRef.collection("meritTransactions").doc();
            tx.set(txRef, {
              amount: liquid,
              reason: "TOURNAMENT_REWARD",
              timestamp: Date.now(),
              balanceAfter: newMerit,
            });
          }
        });
      }

      await tournamentRef.update({
        status: "COMPLETED",
        completedAt: Date.now(),
      });
    }
  }
);
