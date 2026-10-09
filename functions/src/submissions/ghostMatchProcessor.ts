import {onSchedule} from "firebase-functions/v2/scheduler";
import {db} from "../firebase/admin";

export const ghostMatchProcessor = onSchedule(
  {
    schedule: "every 1 hours",
    region: "us-central1",
  },
  async () => {
    const FORTY_EIGHT_HOURS_MS = 48 * 60 * 60 * 1000;
    const cutoff = Date.now() - FORTY_EIGHT_HOURS_MS;

    // find ranked submissions still PENDING older than 48 hours
    const pendingSnap = await db.collection("submissions")
      .where("playmode", "==", "RANKED")
      .where("status", "==", "EVALUATED")
      .where("matchStatus", "==", "PENDING")
      .where("timestamp", "<=", cutoff)
      .limit(50)
      .get();

    if (pendingSnap.empty) return;

    const BENCHMARK_SCORE = 65.0;

    for (const doc of pendingSnap.docs) {
      const data = doc.data();
      const authorId = data.authorId;
      const myScore = Number(data.evaluation?.finalScore ?? 0);

      const userRef = db.collection("users").doc(authorId);
      await db.runTransaction(async (tx) => {
        const currentSubmission = await tx.get(doc.ref);
        if (currentSubmission.get("status") !== "EVALUATED" ||
            currentSubmission.get("matchStatus") !== "PENDING") return;
        const userSnap = await tx.get(userRef);
        if (!userSnap.exists) return;

        const userData = userSnap.data() ?? {};
        const recentScores: number[] = Array.isArray(userData.recentScores) ? userData.recentScores : [];
        const isPlaced = userData.isPlaced === true;

        let ghostRatingChange = 0;
        let outcome = "DRAW";
        let opponentScore = BENCHMARK_SCORE;

        if (recentScores.length >= 3) {
          opponentScore = recentScores.slice(-10).reduce((a, b) => a + b, 0) / Math.min(recentScores.length, 10);
        }

        if (myScore > opponentScore + 2) {
          ghostRatingChange = 2;
          outcome = "WIN";
        } else if (myScore < opponentScore - 2) {
          ghostRatingChange = -4;
          outcome = "LOSS";
        } else {
          ghostRatingChange = 1;
          outcome = "DRAW";
        }

        const currentRating = Number(userData.rating ?? 0);

        tx.update(doc.ref, {
          "seasonRatingChange": isPlaced ? Math.max(0, currentRating + ghostRatingChange) - currentRating : 0,
          "matchStatus": "GHOST",
          "matchResult": {
            opponentId: "GHOST",
            opponentName: "R8 Average",
            opponentScore: opponentScore,
            outcome,
            ratingChange: ghostRatingChange,
          },
          "evaluation.ratingChange": ghostRatingChange,
        });

        if (isPlaced) {
          const newRating = Math.max(0, currentRating + ghostRatingChange);
          tx.update(userRef, {
            rating: newRating,
          });


        }
      });
    }
  }
);
