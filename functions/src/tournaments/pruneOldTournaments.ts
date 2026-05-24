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
      await Promise.all(cancelledSnapshot.docs.map((doc) => db.recursiveDelete(doc.ref)));
      console.log(`Pruned ${cancelledSnapshot.size} cancelled tournaments recursively.`);
    }

    const completedSnapshot = await db.collection("tournaments")
      .where("status", "==", "COMPLETED")
      .get();

    if (!completedSnapshot.empty) {
      const toDelete = completedSnapshot.docs.filter((doc) => {
        const data = doc.data();
        const timestamp = data.completedAt || data.createdAt || 0;
        return now - timestamp > TWENTY_FOUR_HOURS_MS;
      });

      if (toDelete.length > 0) {
        await Promise.all(toDelete.map((doc) => db.recursiveDelete(doc.ref)));
        console.log(`Pruned ${toDelete.length} completed tournaments recursively.`);
      }
    }
  } catch (error) {
    console.error("Error pruning old tournaments:", error);
  }
});
