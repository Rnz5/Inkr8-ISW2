import {onCall, HttpsError} from "firebase-functions/v2/https";
import {db} from "../firebase/admin";
import {seasonActivation} from "../seasons/seasonLedger";

// Ordinary owner deletion must not outrun seasonal acknowledgement/settlement.
// No economic writes, backfill or new reward/cost. Historical assignments remain.
export const deleteSubmission = onCall({region: "us-central1"}, async (request) => {
  if (!request.auth) throw new HttpsError("unauthenticated", "You must be signed in.");
  const id = request.data?.submissionId;
  if (typeof id !== "string" || !id || id.includes("/")) {
    throw new HttpsError("invalid-argument", "A submission ID is required.");
  }
  const ref = db.collection("submissions").doc(id);
  return db.runTransaction(async (tx) => {
    const user = await tx.get(db.collection("users").doc(request.auth!.uid));
    if (user.get("accountClosed") === true) throw new HttpsError("permission-denied", "El acceso de esta cuenta está cerrado.");
    const submission = await tx.get(ref);
    if (!submission.exists) return {deleted: true};
    const data = submission.data()!;
    if (data.authorId !== request.auth!.uid) throw new HttpsError("permission-denied", "Not your submission.");
    if (data.status === "PENDING" || (data.playmode === "RANKED" &&
        !["FAILED", "NOT_EVALUABLE"].includes(data.status) && !["MATCHED", "GHOST"].includes(data.matchStatus))) {
      throw new HttpsError("failed-precondition", "El envío todavía está pendiente.");
    }
    if (data.playmode === "RANKED" && process.env.SEASON_ACTIVATED_AT_MS) {
      let activation: number;
      try {activation = seasonActivation();}
      catch (_) {throw new HttpsError("failed-precondition", "La temporada no está configurada.");}
      if (submission.createTime!.toMillis() >= activation) {
        const assignment = await tx.get(db.collection("seasonAssignments").doc(id));
        if (!assignment.exists || (assignment.get("eligible") !== false && assignment.get("terminal") !== true)) {
          throw new HttpsError("failed-precondition", "La temporada todavía está procesando este envío.");
        }
      }
    }
    tx.delete(ref);
    return {deleted: true};
  });
});
