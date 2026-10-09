import {onCall, HttpsError} from "firebase-functions/v2/https";
import {getAuth} from "firebase-admin/auth";
import {db, FieldValue} from "../firebase/admin";

// New policy accepted by Marco: close Auth access, retain all historical data.
export async function assertAccountOpen(uid: string): Promise<void> {
  if ((await db.collection("users").doc(uid).get()).get("accountClosed") === true) {
    throw new HttpsError("permission-denied", "El acceso de esta cuenta está cerrado.");
  }
}
export const closeAccount = onCall({region: "us-central1"}, async (request) => {
  const uid = request.auth?.uid;
  if (!uid) throw new HttpsError("unauthenticated", "You must be signed in.");
  if (request.data?.userId && request.data.userId !== uid) {
    throw new HttpsError("permission-denied", "Only your own account can be closed.");
  }
  // Firestore marker first denies existing ID tokens even if Auth delivery fails.
  // Closing is retryable; never delete/reset profile, username, balances or ledgers.
  const ref = db.collection("users").doc(uid);
  await db.runTransaction(async (tx) => {
    const user = await tx.get(ref);
    if (!user.exists) throw new HttpsError("not-found", "User not found.");
    if (user.get("accountClosed") !== true) {
      tx.update(ref, {accountClosed: true, accountClosedAt: FieldValue.serverTimestamp()});
    }
  });
  try {
    await getAuth().updateUser(uid, {disabled: true});
    await getAuth().revokeRefreshTokens(uid);
  } catch (_) {
    throw new HttpsError("internal", "El acceso está bloqueado. Reintenta para completar el cierre de Auth.");
  }
  return {closed: true};
});
