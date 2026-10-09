import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {db, FieldValue} from "../firebase/admin";
import {OPENAI_API_KEY, evaluateWithR8} from "../r8/evaluateWithR8";
import {calculateMerit} from "../utils/meritCalculator";
import {pruneOldSubmissions} from "./pruneOldSubmissions";
import {recordSeasonSubmission, syncSeasonSubmission} from "../seasons/seasonLedger";
import {tryMatchRankedSubmission} from "./rankedMatching";

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

// A failed/repeated worker must not replace an already committed evaluation.
async function failPendingSubmission(
  submissionRef: import("firebase-admin/firestore").DocumentReference,
  evaluationError: string
): Promise<boolean> {
  return db.runTransaction(async (tx) => {
    const current = await tx.get(submissionRef);
    const status = current.get("status");
    if (!current.exists || (status && status !== "PENDING")) return false;
    tx.update(submissionRef, {status: "FAILED", evaluationError});
    return true;
  });
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
    const authorId = data.authorId;
    const playmode = data.playmode ?? "PRACTICE";
    // Preserve invalid/null primary behavior; use the alias only when absent.
    const gamemode = data.gamemode === undefined ? data.gamemodeName : data.gamemode;

    try {
      console.log("submissionEvaluationEngine triggered", {
        submissionId: snapshot.id,
        authorId: authorId ?? null,
        playmode: playmode ?? null,
        gamemode: gamemode ?? null,
        status: data.status ?? null,
      });

      const apiKey = OPENAI_API_KEY.value();

      if (playmode === "TOURNAMENT") {
        console.log("submissionEvaluationEngine: skipped, historical TOURNAMENT playmode is retired", {
          submissionId: snapshot.id,
        });
        return;
      }

      if (!authorId) {
        console.log("submissionEvaluationEngine: missing authorId", {
          submissionId: snapshot.id,
        });

        await failPendingSubmission(submissionRef, "Missing authorId");

        return;
      }

      if (data.status && data.status !== "PENDING") {
        console.log("submissionEvaluationEngine: skipped, not PENDING", {
          submissionId: snapshot.id,
          status: data.status,
        });
        return;
      }

      const persistedSubmission = await submissionRef.get();
      const persistedStatus = persistedSubmission.get("status");
      if (!persistedSubmission.exists || (persistedStatus && persistedStatus !== "PENDING")) return;

      await recordSeasonSubmission(snapshot).catch((err) => console.error("Season registration pending:", err));
      const content = data.content ?? "";
      const qualityCheck = isContentLowQuality(content);

      if (qualityCheck.isLowQuality) {
        console.log("submissionEvaluationEngine: low quality content rejected", {
          submissionId: snapshot.id,
          reason: qualityCheck.reason,
        });

        const failed = await failPendingSubmission(
          submissionRef, qualityCheck.reason ?? "Invalid content quality."
        );
        if (!failed) return;

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

      let submissionsToday = 0;
      try {
        const todaySubmissionsSnap = await db.collection("submissions")
          .where("authorId", "==", authorId)
          .where("timestamp", ">=", startOfTodayMs.getTime())
          .get();
        submissionsToday = todaySubmissionsSnap.size;
      } catch (indexErr) {
        console.warn("submissionEvaluationEngine: could not fetch daily submission count (composite index may be missing):", indexErr);
      }

      const userSnapForContext = await db.collection("users").doc(authorId).get();
      const userDataForContext = userSnapForContext.data() ?? {};
      const contextStreak = Number(userDataForContext.currentStreak ?? 0);
      const contextRecentScores = Array.isArray(userDataForContext.recentScores) ? userDataForContext.recentScores : [];

      const result = await evaluateWithR8({
        apiKey,
        content: content,
        gamemode: gamemode ?? "STANDARD",
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
        source: result.source,
      });

      const userRef = db.collection("users").doc(authorId);

      const evaluationCommitted = await db.runTransaction(async (tx) => {
        const currentSubmission = await tx.get(submissionRef);
        const currentStatus = currentSubmission.get("status");
        if (!currentSubmission.exists || (currentStatus && currentStatus !== "PENDING")) return false;
        const userSnap = await tx.get(userRef);
        if (!userSnap.exists) {
          throw new Error("User not found for submission evaluation");
        }

        const userData = userSnap.data() ?? {};
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
          gamemode,
          playmode === "RANKED" || playmode === "TOURNAMENT"
        ) * streakMultiplier);

        const liquidReward = meritToLiquid(currentMerit, meritEarned, meritCap);
        const holdReward = meritToHold(currentMerit, meritEarned, meritCap);
        const newMerit = currentMerit + liquidReward;

        const userUpdates: Record<string, unknown> = {
          merit: newMerit,
          currentStreak: newStreak,
          lastSubmissionDay: today,
          meritHold: FieldValue.increment(holdReward),
          submissionsCount: FieldValue.increment(1),
          totalMeritEarned: FieldValue.increment(meritEarned),
        };

        if (playmode === "RANKED" || playmode === "TOURNAMENT") {
          userUpdates.bestScore = Math.max(currentBestScore, result.finalScore);
        }

        let ratingChangeResult = 0;

        if (playmode === "RANKED" || playmode === "TOURNAMENT") {
          const recentScores: number[] = Array.isArray(userData.recentScores) ? userData.recentScores : [];
          recentScores.push(result.finalScore);
          // Fix #5: Cap recentScores at 50 entries to prevent document bloat and ensure fast loading.
          userUpdates.recentScores = recentScores.slice(-50);
        }

        if (playmode === "RANKED") {
          if (!isPlaced) {
            const played = (userData.placementMatchesPlayed ?? 0) + 1;
            const totalScore = (userData.totalPlacementScore ?? 0) + result.finalScore;

            if (played >= 6) {
              const avgScore = totalScore / 6;
              const initialRating = Math.min(120, Math.floor((avgScore / 100) * 120));

              userUpdates.rating = initialRating;
              userUpdates.isPlaced = true;
              userUpdates.placementMatchesPlayed = 6;
              userUpdates.totalPlacementScore = totalScore;
              userUpdates.currentlyInRanked = false;
              userUpdates.rankedSessionStartedAt = FieldValue.delete();

              ratingChangeResult = initialRating;

            } else {
              userUpdates.placementMatchesPlayed = played;
              userUpdates.totalPlacementScore = totalScore;
              userUpdates.currentlyInRanked = false;
              userUpdates.rankedSessionStartedAt = FieldValue.delete();
            }
          } else {
            userUpdates.currentlyInRanked = false;
            userUpdates.rankedSessionStartedAt = FieldValue.delete();
          }
        }

        tx.update(submissionRef, {
          evaluation: {
            submissionId: snapshot.id,
            finalScore: result.finalScore,
            feedback: result.feedback,
            meritEarned,
            meritToHold: holdReward,
            ratingChange: ratingChangeResult,
            resultStatus: "EVALUATED",
            source: result.source || "real",
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
        return true;
      });
      if (!evaluationCommitted) return;

      if (playmode === "RANKED") {
        const authorUserSnap = await db.collection("users").doc(authorId).get();
        const authorName = authorUserSnap.data()?.name ?? "Unknown";
        const authorRating = Number(authorUserSnap.data()?.rating ?? 0);

        await tryMatchRankedSubmission(
          snapshot.id,
          authorId,
          result.finalScore,
          authorRating,
          authorName
        ).catch((err) => {
          console.error("tryMatchRankedSubmission failed:", err);
        });
      }

      await syncSeasonSubmission(await submissionRef.get()).catch((err) => console.error("Season settlement pending:", err));
      pruneOldSubmissions(authorId).catch((err) => {
        console.error("Non-critical background task (pruning) failed:", err);
      });
    } catch (error) {
      console.error("submissionEvaluationEngine failed", error);
      const failed = await failPendingSubmission(
        submissionRef, error instanceof Error ? error.message : "Unknown failure"
      );
      if (!failed) return;
      if (authorId && playmode === "RANKED") {
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
