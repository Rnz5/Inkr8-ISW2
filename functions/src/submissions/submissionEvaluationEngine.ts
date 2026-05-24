import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {db, FieldValue} from "../firebase/admin";
import {OPENAI_API_KEY, evaluateWithR8} from "../r8/evaluateWithR8";
import {calculateMerit} from "../utils/meritCalculator";
import {calculateNewRating} from "../utils/ratingCalculator";
import {onRankedCompleted} from "../utils/reputationManager";
import {pruneOldSubmissions} from "./pruneOldSubmissions";
import {getLeagueFromRating} from "../utils/leagueManager";

function getUtcDayInt(): number {
  const now = new Date();
  return now.getUTCFullYear() * 10000 + (now.getUTCMonth() + 1) * 100 + now.getUTCDate();
}

function getStreakMultiplier(streak: number): number {
  if (streak >= 7) return 1.12;
  if (streak >= 6) return 1.10;
  if (streak >= 5) return 1.08;
  if (streak >= 4) return 1.06;
  if (streak >= 3) return 1.05;
  if (streak >= 2) return 1.03;
  return 1.0;
}

export const submissionEvaluationEngine = onDocumentCreated(
  {
    document: "submissions/{submissionId}",
    region: "us-central1",
    secrets: [OPENAI_API_KEY],
    timeoutSeconds: 540,
    memory: "512MiB",
  },
  async (event) => {
    const snapshot = event.data;
    if (!snapshot) {
      console.log("submissionEvaluationEngine: no snapshot");
      return;
    }

    const data = snapshot.data();
    if (!data) {
      console.log("submissionEvaluationEngine: no data",
        {submissionId: snapshot.id});
      return;
    }

    const submissionRef = snapshot.ref;

    try {
      console.log("submissionEvaluationEngine triggered", {
        submissionId: snapshot.id,
        authorId: data.authorId ?? null,
        playmode: data.playmode ?? null,
        gamemode: data.gamemode ?? null,
        status: data.status ?? null,
      });

      const apiKey = OPENAI_API_KEY.value();
      const playmode = data.playmode ?? "PRACTICE";
      const authorId = data.authorId;

      if (playmode === "TOURNAMENT") {
        console.log("submissionEvaluationEngine: skipped, TOURNAMENT playmode handled by tournamentEvaluationEngine", {
          submissionId: snapshot.id,
        });
        return;
      }

      if (!authorId) {
        console.log("submissionEvaluationEngine: missing authorId", {
          submissionId: snapshot.id,
        });

        await submissionRef.update({
          status: "FAILED",
          evaluationError: "Missing authorId",
        });

        return;
      }

      if (data.status && data.status !== "PENDING") {
        console.log("submissionEvaluationEngine: skipped, not PENDING", {
          submissionId: snapshot.id,
          status: data.status,
        });
        return;
      }

      const content = data.content ?? "";
      const qualityCheck = isContentLowQuality(content);

      if (qualityCheck.isLowQuality) {
        console.log("submissionEvaluationEngine: low quality content rejected", {
          submissionId: snapshot.id,
          reason: qualityCheck.reason,
        });

        await submissionRef.update({
          status: "FAILED",
          evaluationError: qualityCheck.reason ?? "Invalid content quality.",
        });

        if (playmode === "RANKED") {
          await db.collection("users").doc(authorId).update({
            currentlyInRanked: false,
            rankedSessionStartedAt: FieldValue.delete(),
          });
        }
        return;
      }

      const requiredWords = Array.isArray(data.wordsUsed) ?
        data.wordsUsed
          .map((w: { word?: string } | string) =>
            typeof w === "string" ? w : (w.word ?? "")
          )
          .filter((word: string) => word.length > 0) :
        [];

      console.log("submissionEvaluationEngine: calling R8", {
        submissionId: snapshot.id,
        requiredWordsCount: requiredWords.length,
        wordCount: data.wordCount ?? 0,
      });

      const startOfTodayMs = new Date();
      startOfTodayMs.setUTCHours(0, 0, 0, 0);

      const todaySubmissionsSnap = await db.collection("submissions")
        .where("authorId", "==", authorId)
        .where("timestamp", ">=", startOfTodayMs.getTime())
        .get();

      const submissionsToday = todaySubmissionsSnap.size;

      const userSnapForContext = await db.collection("users").doc(authorId).get();
      const userDataForContext = userSnapForContext.data() ?? {};
      const contextStreak = Number(userDataForContext.currentStreak ?? 0);
      const contextRecentScores = Array.isArray(userDataForContext.recentScores) ? userDataForContext.recentScores : [];

      const result = await evaluateWithR8({
        apiKey,
        content: content,
        gamemode: data.gamemode ?? "STANDARD",
        requiredWords,
        themeName: data.themeName ?? null,
        topicName: data.topicName ?? null,
        submissionsToday,
        currentStreak: contextStreak,
        recentScores: contextRecentScores,
      });

      console.log("submissionEvaluationEngine: evaluation completed", {
        submissionId: snapshot.id,
        finalScore: result.finalScore,
      });

      const userRef = db.collection("users").doc(authorId);

      await db.runTransaction(async (tx) => {
        const userSnap = await tx.get(userRef);
        if (!userSnap.exists) {
          throw new Error("User not found for submission evaluation");
        }

        const userData = userSnap.data() ?? {};
        const currentRating = Number(userData.rating ?? 0);
        const currentMerit = Number(userData.merit ?? 0);
        const meritCap = Number(userData.meritCap ?? 50000);
        const currentBestScore = Number(userData.bestScore ?? 0);
        const isPlaced = userData.isPlaced === true;

        const today = getUtcDayInt();
        const lastDay = Number(userData.lastSubmissionDay ?? 0);
        const currentStreak = Number(userData.currentStreak ?? 0);

        let newStreak = 1;
        if (lastDay > 0) {
          const yesterday = new Date();
          yesterday.setUTCDate(yesterday.getUTCDate() - 1);
          const yesterdayInt = yesterday.getUTCFullYear() * 10000 + (yesterday.getUTCMonth() + 1) * 100 + yesterday.getUTCDate();

          if (lastDay === today) {
            newStreak = currentStreak;
          } else if (lastDay === yesterdayInt) {
            newStreak = currentStreak + 1;
          } else {
            newStreak = 1;
          }
        }

        const streakMultiplier = getStreakMultiplier(newStreak);

        const meritEarned = Math.floor(calculateMerit(
          result.finalScore,
          data.wordCount ?? 0,
          data.gamemode,
          playmode === "RANKED" || playmode === "TOURNAMENT"
        ) * streakMultiplier);

        const liquidReward = meritToLiquid(currentMerit, meritEarned, meritCap);
        const holdReward = meritToHold(currentMerit, meritEarned, meritCap);
        const newMerit = currentMerit + liquidReward;

        let userUpdates: Record<string, unknown> = {
          merit: newMerit,
          currentStreak: newStreak,
          lastSubmissionDay: today,
          meritHold: FieldValue.increment(holdReward),
          submissionsCount: FieldValue.increment(1),
        };

        if (playmode === "RANKED" || playmode === "TOURNAMENT") {
          userUpdates.bestScore = Math.max(currentBestScore, result.finalScore);
        }

        let ratingChange = 0;

        if (playmode === "RANKED" || playmode === "TOURNAMENT") {
          const recentScores: number[] = Array.isArray(userData.recentScores) ? userData.recentScores : [];
          recentScores.push(result.finalScore);
          userUpdates.recentScores = recentScores.slice(-20);
        }

        if (playmode === "RANKED") {
          if (!isPlaced) {
            const played = (userData.placementMatchesPlayed ?? 0) + 1;
            const totalScore = (userData.totalPlacementScore ?? 0) + result.finalScore;

            if (played >= 6) {
              const avgScore = totalScore / 6;
              const initialRating = Math.min(120, Math.floor((avgScore / 100) * 120));

              userUpdates = {
                ...userUpdates,
                rating: initialRating,
                isPlaced: true,
                placementMatchesPlayed: 6,
                totalPlacementScore: totalScore,
                reputation: onRankedCompleted(Number(userData.reputation ?? 0)),
                currentlyInRanked: false,
                rankedSessionStartedAt: FieldValue.delete(),
              };

              ratingChange = initialRating;

              if (authorId !== "R8") {
                const newLeague = getLeagueFromRating(initialRating);
                const statsRef = db.collection("metadata").doc("rankings");
                tx.set(statsRef, {
                  leagueCounts: {
                    [newLeague]: FieldValue.increment(1),
                  },
                }, {merge: true});
              }
            } else {
              userUpdates = {
                ...userUpdates,
                placementMatchesPlayed: played,
                totalPlacementScore: totalScore,
                currentlyInRanked: false,
                rankedSessionStartedAt: FieldValue.delete(),
              };
            }
          } else {
            const newRating = calculateNewRating(
              currentRating,
              result.finalScore
            );

            ratingChange = newRating - currentRating;

            const newReputation = onRankedCompleted(
              Number(userData.reputation ?? 0)
            );

            userUpdates = {
              ...userUpdates,
              rating: newRating,
              reputation: newReputation,
              currentlyInRanked: false,
              rankedSessionStartedAt: FieldValue.delete(),
            };

            if (authorId !== "R8") {
              const oldLeague = getLeagueFromRating(currentRating);
              const newLeague = getLeagueFromRating(newRating);

              if (oldLeague !== newLeague) {
                const statsRef = db.collection("metadata").doc("rankings");
                tx.set(statsRef, {
                  leagueCounts: {
                    [oldLeague]: FieldValue.increment(-1),
                    [newLeague]: FieldValue.increment(1),
                  },
                }, {merge: true});
              }
            }
          }
        }

        tx.update(submissionRef, {
          evaluation: {
            submissionId: snapshot.id,
            finalScore: result.finalScore,
            feedback: result.feedback,
            meritEarned,
            meritToHold: holdReward,
            ratingChange,
            resultStatus: "EVALUATED",
          },
          status: "EVALUATED",
          matchStatus: playmode === "RANKED" ? "PENDING" : "UNMATCHED",
          evaluationError: FieldValue.delete(),
        });

        tx.update(userRef, userUpdates);

        if (liquidReward !== 0) {
          const txRef = userRef.collection("meritTransactions").doc();
          tx.set(txRef, {
            amount: liquidReward,
            reason: playmode === "RANKED" ? "RANKED_REWARD" : "PRACTICE_REWARD",
            timestamp: Date.now(),
            balanceAfter: newMerit,
          });
        }
      });

      if (playmode === "RANKED") {
        const authorUserSnap = await db.collection("users").doc(authorId).get();
        const authorName = authorUserSnap.data()?.name ?? "Unknown";
        const currentRating = Number(authorUserSnap.data()?.rating ?? 0);

        tryMatchRankedSubmission(
          snapshot.id,
          authorId,
          result.finalScore,
          currentRating,
          authorName
        ).catch((err) => {
          console.error("tryMatchRankedSubmission failed:", err);
        });
      }

      pruneOldSubmissions(authorId).catch((err) => {
        console.error("Non-critical background task (pruning) failed:", err);
      });
    } catch (error) {
      console.error("submissionEvaluationEngine failed", error);
      await submissionRef.update({
        status: "FAILED",
        evaluationError: error instanceof Error ? error.message : "Unknown failure",
      });
      const authorId = data.authorId;
      if (authorId && data.playmode === "RANKED") {
        try {
          await db.collection("users").doc(authorId).update({
            currentlyInRanked: false,
            rankedSessionStartedAt: FieldValue.delete(),
          });
          console.log("submissionEvaluationEngine: cleaned up session after failure");
        } catch (cleanupError) {
          console.error("submissionEvaluationEngine: failed cleanup", cleanupError);
        }
      }
    }
  }
);


// quality check to filter out nonsense or highly repetitive content >:(
function isContentLowQuality(content: string): { isLowQuality: boolean; reason?: string } {
  const trimmed = content.trim();
  if (trimmed.length < 50) return {isLowQuality: true, reason: "Content too short (min 50 chars)"};

  const words = trimmed.split(/\s+/).filter((w) => w.length > 0);

  if (words.some((w) => w.length > 35)) {
    return {isLowQuality: true, reason: "Nonsense detected (excessive word length)"};
  }

  if (words.length >= 10) {
    const uniqueWords = new Set(words.map((w) => w.toLowerCase()));
    if (uniqueWords.size / words.length < 0.35) {
      return {isLowQuality: true, reason: "Repetitive content detected"};
    }
  }

  const letters = trimmed.replace(/[^a-zA-Z]/g, "");
  if (letters.length > 30) {
    const vowels = letters.match(/[aeiouAEIOU]/g) || [];
    const vowelRatio = vowels.length / letters.length;
    if (vowelRatio < 0.15 || vowelRatio > 0.8) {
      return {isLowQuality: true, reason: "Unnatural character distribution (nonsense)"};
    }

    const uniqueLetters = new Set(letters.toLowerCase().split(""));
    if (uniqueLetters.size < 8 && letters.length > 60) {
      return {isLowQuality: true, reason: "Low character diversity (nonsense)"};
    }
  }

  return {isLowQuality: false};
}

function meritToLiquid(current: number, earned: number, cap: number): number {
  if (current + earned > cap) return Math.max(0, cap - current);
  return earned;
}

function meritToHold(current: number, earned: number, cap: number): number {
  const liquid = meritToLiquid(current, earned, cap);
  return earned - liquid;
}

async function tryMatchRankedSubmission(
  submissionId: string,
  authorId: string,
  authorScore: number,
  authorRating: number,
  authorName: string
): Promise<void> {
  const RATING_RANGE = 20;
  const FORTY_EIGHT_HOURS_MS = 48 * 60 * 60 * 1000;
  const since = Date.now() - FORTY_EIGHT_HOURS_MS;

  const candidatesSnap = await db.collection("submissions")
    .where("playmode", "==", "RANKED")
    .where("status", "==", "EVALUATED")
    .where("matchStatus", "==", "PENDING")
    .where("timestamp", ">=", since)
    .get();

  const submitterRef = db.collection("users").doc(authorId);
  const mySubmissionRef = db.collection("submissions").doc(submissionId);

  for (const doc of candidatesSnap.docs) {
    const candidate = doc.data();

    if (candidate.authorId === authorId) continue;

    const alreadyMatchedSnap = await db.collection("submissions")
      .where("authorId", "==", authorId)
      .where("matchResult.opponentId", "==", candidate.authorId)
      .where("timestamp", ">=", since)
      .limit(1)
      .get();

    if (!alreadyMatchedSnap.empty) continue;

    const candidateUserSnap = await db.collection("users").doc(candidate.authorId).get();
    if (!candidateUserSnap.exists) continue;
    const candidateRating = Number(candidateUserSnap.data()?.rating ?? 0);

    if (Math.abs(authorRating - candidateRating) > RATING_RANGE) continue;

    const candidateScore = Number(candidate.evaluation?.finalScore ?? 0);
    const candidateRef = doc.ref;
    const candidateAuthorRef = db.collection("users").doc(candidate.authorId);

    const myScoreWins = authorScore > candidateScore;
    const isDraw = authorScore === candidateScore;

    const RATING_WIN = 8;
    const RATING_LOSS = -5;
    const RATING_DRAW = 1;

    const myRatingChange = isDraw ? RATING_DRAW : myScoreWins ? RATING_WIN : RATING_LOSS;
    const theirRatingChange = isDraw ? RATING_DRAW : myScoreWins ? RATING_LOSS : RATING_WIN;

    const candidateName = candidateUserSnap.data()?.name ?? "Unknown";

    await db.runTransaction(async (tx) => {
      const submitterSnap = await tx.get(submitterRef);
      const candidateUserSnapTx = await tx.get(candidateAuthorRef);

      if (!submitterSnap.exists || !candidateUserSnapTx.exists) return;

      const currentMyRating = Number(submitterSnap.data()?.rating ?? 0);
      const currentTheirRating = Number(candidateUserSnapTx.data()?.rating ?? 0);

      tx.update(mySubmissionRef, {
        matchStatus: "MATCHED",
        matchResult: {
          opponentId: candidate.authorId,
          opponentName: candidateName,
          opponentScore: candidateScore,
          outcome: isDraw ? "DRAW" : myScoreWins ? "WIN" : "LOSS",
          ratingChange: myRatingChange,
        },
        "evaluation.ratingChange": myRatingChange,
      });

      tx.update(candidateRef, {
        matchStatus: "MATCHED",
        matchResult: {
          opponentId: authorId,
          opponentName: authorName,
          opponentScore: authorScore,
          outcome: isDraw ? "DRAW" : myScoreWins ? "LOSS" : "WIN",
          ratingChange: theirRatingChange,
        },
        "evaluation.ratingChange": theirRatingChange,
      });

      tx.update(submitterRef, {
        rating: Math.max(0, currentMyRating + myRatingChange),
      });

      tx.update(candidateAuthorRef, {
        rating: Math.max(0, currentTheirRating + theirRatingChange),
      });
    });

    return;
  }

  await mySubmissionRef.update({ matchStatus: "PENDING" });
}
