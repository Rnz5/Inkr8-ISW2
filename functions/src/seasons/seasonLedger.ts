import {db, FieldValue} from "../firebase/admin";
import {DocumentSnapshot} from "firebase-admin/firestore";

// New feature configuration, not recovered/original configuration. No historical backfill.
export function seasonActivation(): number {
  const value = Number(process.env.SEASON_ACTIVATED_AT_MS);
  if (!Number.isFinite(value) || value <= 0) throw new Error("Season activation is not configured.");
  return value;
}
export function utcSeason(confirmedAt: number) {
  const date = new Date(confirmedAt);
  const start = Date.UTC(date.getUTCFullYear(), date.getUTCMonth(), 1);
  const end = Date.UTC(date.getUTCFullYear(), date.getUTCMonth() + 1, 1);
  return {id: new Date(start).toISOString().slice(0, 7), start, end};
}
export async function recordSeasonSubmission(snapshot: DocumentSnapshot): Promise<void> {
  if (!process.env.SEASON_ACTIVATED_AT_MS || !snapshot.exists) return;
  const data = snapshot.data()!;
  const confirmedAt = snapshot.createTime!.toMillis();
  if (data.playmode !== "RANKED" || !data.authorId || confirmedAt < seasonActivation()) return;
  const period = utcSeason(confirmedAt);
  const assignmentRef = db.collection("seasonAssignments").doc(snapshot.id);
  const seasonRef = db.collection("seasons").doc(period.id);
  const memberRef = seasonRef.collection("members").doc(data.authorId);
  await db.runTransaction(async (tx) => {
    const assignment = await tx.get(assignmentRef);
    if (assignment.exists) return;
    const user = await tx.get(db.collection("users").doc(data.authorId));
    // A negative eligibility acknowledgement prevents delayed creation events from
    // silently enrolling historical/unplaced entries at closure.
    if (!user.exists || user.get("isPlaced") !== true) {
      tx.set(assignmentRef, {eligible: false, confirmedAt, authorId: data.authorId});
      return;
    }
    const season = await tx.get(seasonRef);
    const member = await tx.get(memberRef);
    if (season.get("status") === "CLOSED") throw new Error("Season already closed; late registration requires review.");
    tx.set(assignmentRef, {eligible: true, seasonId: period.id, authorId: data.authorId,
      confirmedAt, terminal: false, rating: 0, meritEarned: 0});
    if (!season.exists) tx.set(seasonRef, {start: period.start, end: period.end,
      status: Date.now() >= period.end ? "CLOSING" : "ACTIVE", pendingCount: 1});
    else tx.update(seasonRef, {pendingCount: FieldValue.increment(1)});
    if (!member.exists) tx.set(memberRef, {userId: data.authorId, name: user.get("name") ?? "",
      baselineRating: Number(user.get("rating") ?? 0), joinedAt: confirmedAt,
      rating: 0, meritEarned: 0, submissionCount: 0});
  });
}
export async function syncSeasonSubmission(snapshot: DocumentSnapshot): Promise<void> {
  if (!snapshot.exists || snapshot.get("playmode") !== "RANKED") return;
  const assignmentRef = db.collection("seasonAssignments").doc(snapshot.id);
  await db.runTransaction(async (tx) => {
    const assignment = await tx.get(assignmentRef);
    if (!assignment.exists || assignment.get("eligible") !== true || assignment.get("terminal") === true) return;
    const current = await tx.get(snapshot.ref);
    // The event snapshot remains available if ordinary archive pruning preceded delivery.
    const data = current.exists ? current.data()! : snapshot.data()!;
    const failed = data.status === "FAILED" || data.status === "NOT_EVALUABLE";
    const matched = data.status === "EVALUATED" && (data.matchStatus === "MATCHED" || data.matchStatus === "GHOST");
    if (!failed && !matched) return;
    const seasonRef = db.collection("seasons").doc(assignment.get("seasonId"));
    const memberRef = seasonRef.collection("members").doc(assignment.get("authorId"));
    const season = await tx.get(seasonRef);
    const member = await tx.get(memberRef);
    if (!season.exists || !member.exists || season.get("status") === "CLOSED") throw new Error("Invalid season settlement state.");
    // Actual clamped rating movement is recorded at the genuine rating transaction;
    // the existing displayed delta and economic formulas remain unchanged.
    const rating = failed ? 0 : Number(data.seasonRatingChange ?? 0);
    const merit = Number(data.evaluation?.meritEarned ?? 0);
    tx.update(assignmentRef, {terminal: true, rating, meritEarned: merit, status: data.status, matchStatus: data.matchStatus ?? null});
    tx.update(memberRef, {rating: FieldValue.increment(rating), meritEarned: FieldValue.increment(merit), submissionCount: FieldValue.increment(1)});
    tx.update(seasonRef, {pendingCount: FieldValue.increment(-1)});
  });
}
export function positioned<T extends {rating: number; userId: string}>(members: T[]): Array<T & {position: number}> {
  const sorted = [...members].sort((a, b) => b.rating - a.rating || a.userId.localeCompare(b.userId));
  // User ID only stabilizes display within a tie; all tied users keep the same rank.
  return sorted.map((member, index) => ({...member,
    position: sorted.findIndex((m) => m.rating === member.rating) + 1}));
}
export async function closeSeason(id: string, now = Date.now()): Promise<boolean> {
  const ref = db.collection("seasons").doc(id);
  return db.runTransaction(async (tx) => {
    const season = await tx.get(ref);
    if (!season.exists || Number(season.get("end")) > now) return false;
    if (season.get("status") === "CLOSED") return true;
    if (Number(season.get("pendingCount")) !== 0) {
      tx.update(ref, {status: "CLOSING"}); return false;
    }
    // Also account for persisted Ranked documents whose creation delivery is delayed.
    // Scan is deliberate until an authentic index/ingestion watermark is established.
    const submissions = await tx.get(db.collection("submissions").where("playmode", "==", "RANKED"));
    const start = Number(season.get("start")), end = Number(season.get("end"));
    for (const submission of submissions.docs) {
      const time = submission.createTime.toMillis();
      if (time < Math.max(start, seasonActivation()) || time >= end) continue;
      if (!(await tx.get(db.collection("seasonAssignments").doc(submission.id))).exists) {
        tx.update(ref, {status: "CLOSING"}); return false;
      }
    }
    // Mark closed atomically; readers derive immutable final positions from members.
    // No bulk rewrite or 500-write limit; settled members are immutable after closure.
    tx.update(ref, {status: "CLOSED", closedAt: now});
    return true;
  });
}
