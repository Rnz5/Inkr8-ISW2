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

    // find ranked submissions still PENDING older than 48 hours which will be evaluated with their own average
    const pendingSnap = await db.collection("submissions")
      .where("playmode", "==", "RANKED")
      .where("status", "==", "EVALUATED")
      .where("matchStatus", "==", "PENDING")
      .where("timestamp", "<=", cutoff)
      .limit(50)
      .get();

    if (pendingSnap.empty) return;

    for (const doc of pendingSnap.docs) {
      const data = doc.data();
      const authorId = data.authorId;
      const myScore = Number(data.evaluation?.finalScore ?? 0);

      const userRef = db.collection("users").doc(authorId);
      const userSnap = await userRef.get();
      if (!userSnap.exists) continue;

      const userData = userSnap.data() ?? {};
      const recentScores: number[] = Array.isArray(userData.recentScores) ? userData.recentScores : [];

      let ghostRatingChange = 0;
      let outcome = "DRAW";

      if (recentScores.length >= 3) {
        const avg = recentScores.slice(-10).reduce((a, b) => a + b, 0) / Math.min(recentScores.length, 10);
        if (myScore > avg + 2) {
          ghostRatingChange = 3;
          outcome = "WIN";
        } else if (myScore < avg - 2) {
          ghostRatingChange = -2;
          outcome = "LOSS";
        } else {
          ghostRatingChange = 1;
          outcome = "DRAW";
        }
      } else {
        ghostRatingChange = 1;
        outcome = "DRAW";
      }

      const currentRating = Number(userData.rating ?? 0);

      await db.runTransaction(async (tx) => {
        tx.update(doc.ref, {
          matchStatus: "GHOST",
          matchResult: {
            opponentId: "GHOST",
            opponentName: "R8 Average",
            opponentScore: recentScores.length >= 3
              ? recentScores.slice(-10).reduce((a, b) => a + b, 0) / Math.min(recentScores.length, 10)
              : 60.0,
            outcome,
            ratingChange: ghostRatingChange,
          },
          "evaluation.ratingChange": ghostRatingChange,
        });

        tx.update(userRef, {
          rating: Math.max(0, currentRating + ghostRatingChange),
        });
      });
    }
  }
);
