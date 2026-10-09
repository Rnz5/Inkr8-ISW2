import {onSchedule} from "firebase-functions/v2/scheduler";
import {db, FieldValue} from "../firebase/admin";

export const rankedSessionCleaner = onSchedule("every 15 minutes", async () => {
  const sixtyMinutesAgo = Date.now() - (60 * 60 * 1000);

  try {
    const abandonedSessions = await db.collection("users")
      .where("currentlyInRanked", "==", true)
      .where("rankedSessionStartedAt", "<=", sixtyMinutesAgo)
      .get();

    if (abandonedSessions.empty) {
      return;
    }

    const batch = db.batch();

    abandonedSessions.docs.forEach((doc) => {
      batch.update(doc.ref, {
        currentlyInRanked: false,
        rankedSessionStartedAt: FieldValue.delete(),
      });

      console.log(`Cleaned up abandoned ranked session for user: ${doc.id}`);
    });

    await batch.commit();
  } catch (error) {
    console.error("Error in rankedSessionCleaner:", error);
  }
});
