import {onSchedule} from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";

export const pruneOldTournaments = onSchedule("every 12 hours", async () => {
  const db = admin.firestore();
  const now = Date.now();
  const TWENTY_FOUR_HOURS_MS = 24 * 60 * 60 * 1000;

  try {
    const cancelledSnapshot = await db.collection("tournaments")
      .where("status", "==", "CANCELLED")
      .get();

    if (!cancelledSnapshot.empty) {
      for (const doc of cancelledSnapshot.docs) {
        await db.recursiveDelete(doc.ref);
      }
      console.log(`Pruned ${cancelledSnapshot.size} cancelled tournaments recursively.`);
    }

    const completedSnapshot = await db.collection("tournaments")
      .where("status", "==", "COMPLETED")
      .get();

    let completedPrunedCount = 0;

    if (!completedSnapshot.empty) {
      for (const doc of completedSnapshot.docs) {
        const data = doc.data();
        const timestamp = data.completedAt || data.createdAt || 0;

        if (now - timestamp > TWENTY_FOUR_HOURS_MS) {
          await db.recursiveDelete(doc.ref);
          completedPrunedCount++;
        }
      }
      if (completedPrunedCount > 0) {
        console.log(`Pruned ${completedPrunedCount} completed tournaments recursively.`);
      }
    }
  } catch (error) {
    console.error("Error pruning old tournaments:", error);
  }
});
