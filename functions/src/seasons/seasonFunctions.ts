import {onCall, HttpsError} from "firebase-functions/v2/https";
import {onDocumentCreated, onDocumentUpdated} from "firebase-functions/v2/firestore";
import {onSchedule} from "firebase-functions/v2/scheduler";
import {db} from "../firebase/admin";
import {recordSeasonSubmission, syncSeasonSubmission, utcSeason, positioned, closeSeason, seasonActivation} from "./seasonLedger";

export const seasonSubmissionCreated = onDocumentCreated({document: "submissions/{submissionId}", region: "us-central1", retry: true}, async (event) => {
  if (event.data) {await recordSeasonSubmission(event.data); await syncSeasonSubmission(event.data);}
});
export const seasonSubmissionUpdated = onDocumentUpdated({document: "submissions/{submissionId}", region: "us-central1", retry: true}, async (event) => {
  if (event.data) await syncSeasonSubmission(event.data.after);
});
export const seasonClosure = onSchedule({schedule: "every 1 hours", region: "us-central1"}, async () => {
  if (!process.env.SEASON_ACTIVATED_AT_MS) return;
  const seasons = await db.collection("seasons").where("status", "in", ["ACTIVE", "CLOSING"]).get();
  for (const season of seasons.docs) if (Number(season.get("end")) <= Date.now()) {
    await closeSeason(season.id);
  }
});
async function ranking(id: string) {
  const seasonRef = db.collection("seasons").doc(id);
  const season = await seasonRef.get();
  const members = await seasonRef.collection("members").get();
  return {status: season.get("status") ?? "ACTIVE", ...utcSeason(Date.parse(id + "-01T00:00:00Z")),
    members: positioned(members.docs.map((m) => ({userId: m.id, name: String(m.get("name") ?? ""),
      rating: Number(m.get("rating") ?? 0), meritEarned: Number(m.get("meritEarned") ?? 0)})))};
}
export const getSeasonRanking = onCall({region: "us-central1"}, async (request) => {
  if (!request.auth) throw new HttpsError("unauthenticated", "You must be signed in.");
  try {seasonActivation(); return await ranking(utcSeason(Date.now()).id);}
  catch (_) {throw new HttpsError("failed-precondition", "No se pudo cargar el ranking de la temporada");}
});
export const getSeasonHistory = onCall({region: "us-central1"}, async (request) => {
  if (!request.auth) throw new HttpsError("unauthenticated", "You must be signed in.");
  try {
    seasonActivation(); const finished = await db.collection("seasons").where("status", "==", "CLOSED").get();
    const history = [];
    for (const season of finished.docs) {
      const result = await ranking(season.id); const member = result.members.find((m) => m.userId === request.auth!.uid);
      if (member) history.push({id: season.id, start: result.start, end: result.end,
        position: member.position, rating: member.rating, meritEarned: member.meritEarned});
    }
    return {history: history.sort((a, b) => b.start - a.start)};
  } catch (_) {throw new HttpsError("failed-precondition", "No se pudo cargar el historial de temporadas");}
});
