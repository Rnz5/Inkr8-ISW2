import {onSchedule} from "firebase-functions/v2/scheduler";
import {db, FieldValue} from "../firebase/admin";

export const meritReleaseController = onSchedule("every 3 hours", async () => {
  const usersWithHold = await db.collection("users")
    .where("meritHold", ">", 0)
    .get();

  if (usersWithHold.empty) {
    console.log("meritReleaseController: No users with merit in hold.");
    return;
  }

  const writer = db.bulkWriter();
  let processedCount = 0;

  writer.onWriteError((error) => {
    console.error(`meritReleaseController: Error writing for ${error.documentRef.path}:`, error.message);
    return false;
  });

  usersWithHold.docs.forEach((doc) => {
    const data = doc.data();
    const merit = data.merit ?? 0;
    const meritHold = data.meritHold ?? 0;
    const meritCap = data.meritCap ?? 50000;

    if (merit < meritCap) {
      const remainingCapSpace = meritCap - merit;
      const releaseAmount = Math.min(300, meritHold, remainingCapSpace);

      if (releaseAmount > 0) {
        writer.update(doc.ref, {
          merit: FieldValue.increment(releaseAmount),
          meritHold: FieldValue.increment(-releaseAmount),
        });

        const txRef = doc.ref.collection("meritTransactions").doc();
        writer.set(txRef, {
          amount: releaseAmount,
          reason: "MERIT_RELEASE",
          timestamp: Date.now(),
          balanceAfter: merit + releaseAmount,
        });
        processedCount++;
      }
    }
  });

  await writer.close();

  if (processedCount > 0) {
    console.log(`meritReleaseController: Released merit for ${processedCount} users.`);
  } else {
    console.log("meritReleaseController: No merit released (all users at cap).");
  }
});
