import {db} from "../firebase/admin";
import {calculateDynamicRatingChange} from "../utils/dynamicRatingChange";

export async function tryMatchRankedSubmission(
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

    const candidateUserSnap = await db.collection("users").doc(candidate.authorId).get();
    if (!candidateUserSnap.exists) continue;
    const candidateRating = Number(candidateUserSnap.data()?.rating ?? 0);

    if (Math.abs(authorRating - candidateRating) > RATING_RANGE) continue;

    const candidateScore = Number(candidate.evaluation?.finalScore ?? 0);
    const candidateRef = doc.ref;
    const candidateAuthorRef = db.collection("users").doc(candidate.authorId);

    const myScoreWins = authorScore > candidateScore;
    const isDraw = authorScore === candidateScore;

    const candidateName = candidateUserSnap.data()?.name ?? "Unknown";

    const matched = await db.runTransaction(async (tx) => {
      const mySubmissionSnap = await tx.get(mySubmissionRef);
      const candidateSubmissionSnap = await tx.get(candidateRef);
      if (mySubmissionSnap.get("status") !== "EVALUATED" ||
          mySubmissionSnap.get("matchStatus") !== "PENDING" ||
          candidateSubmissionSnap.get("status") !== "EVALUATED" ||
          candidateSubmissionSnap.get("matchStatus") !== "PENDING") return null;

      const submitterSnap = await tx.get(submitterRef);
      const candidateUserSnapTx = await tx.get(candidateAuthorRef);

      if (!submitterSnap.exists || !candidateUserSnapTx.exists) return null;

      const submitterData = submitterSnap.data();
      const candidateData = candidateUserSnapTx.data();

      const currentMyRating = Number(submitterData?.rating ?? 0);
      const currentTheirRating = Number(candidateData?.rating ?? 0);
      const myIsPlaced = submitterData?.isPlaced === true;
      const candidateIsPlaced = candidateData?.isPlaced === true;

      const myRatingChange = calculateDynamicRatingChange(
        currentMyRating,
        currentTheirRating,
        isDraw ? "DRAW" : myScoreWins ? "WIN" : "LOSS"
      );

      const theirRatingChange = calculateDynamicRatingChange(
        currentTheirRating,
        currentMyRating,
        isDraw ? "DRAW" : myScoreWins ? "LOSS" : "WIN"
      );

      tx.update(mySubmissionRef, {
        "seasonRatingChange": myIsPlaced ? Math.max(0, currentMyRating + myRatingChange) - currentMyRating : 0,
        "matchStatus": "MATCHED",
        "matchResult": {
          opponentId: candidate.authorId,
          opponentName: candidateName,
          opponentScore: candidateScore,
          outcome: isDraw ? "DRAW" : myScoreWins ? "WIN" : "LOSS",
          ratingChange: myRatingChange,
        },
        "evaluation.ratingChange": myRatingChange,
      });

      tx.update(candidateRef, {
        "seasonRatingChange": candidateIsPlaced ? Math.max(0, currentTheirRating + theirRatingChange) - currentTheirRating : 0,
        "matchStatus": "MATCHED",
        "matchResult": {
          opponentId: authorId,
          opponentName: authorName,
          opponentScore: authorScore,
          outcome: isDraw ? "DRAW" : myScoreWins ? "LOSS" : "WIN",
          ratingChange: theirRatingChange,
        },
        "evaluation.ratingChange": theirRatingChange,
      });

      if (myIsPlaced) {
        const newRating = Math.max(0, currentMyRating + myRatingChange);
        const myOutcome = isDraw ? "DRAW" : myScoreWins ? "WIN" : "LOSS";
        const myWinStreak = Number(submitterData?.rankedWinStreak ?? 0);
        const myLossStreak = Number(submitterData?.rankedLossStreak ?? 0);

        const newMyWinStreak = myOutcome === "WIN" ? myWinStreak + 1 : 0;
        const newMyLossStreak = myOutcome === "LOSS" ? myLossStreak + 1 : 0;

        tx.update(submitterRef, {
          rating: newRating,
          rankedWinStreak: newMyWinStreak,
          rankedLossStreak: newMyLossStreak,
        });

      }

      if (candidateIsPlaced) {
        const newRating = Math.max(0, currentTheirRating + theirRatingChange);
        const theirOutcome = isDraw ? "DRAW" : myScoreWins ? "LOSS" : "WIN";
        const theirWinStreak = Number(candidateData?.rankedWinStreak ?? 0);
        const theirLossStreak = Number(candidateData?.rankedLossStreak ?? 0);

        const newTheirWinStreak = theirOutcome === "WIN" ? theirWinStreak + 1 : 0;
        const newTheirLossStreak = theirOutcome === "LOSS" ? theirLossStreak + 1 : 0;

        tx.update(candidateAuthorRef, {
          rating: newRating,
          rankedWinStreak: newTheirWinStreak,
          rankedLossStreak: newTheirLossStreak,
        });

      }
      return true;
    });
    if (matched === null) continue;

    return;
  }

  // The evaluation transaction already queued this submission. Do not reset a concurrent match.
}
