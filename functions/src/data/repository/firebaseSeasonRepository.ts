import {FieldPath, Firestore} from "firebase-admin/firestore";
import {Season} from "../../domain/model/models";
import {SeasonRepository} from "../../domain/repository/contracts";
import {mapUser} from "../dto/userDto";
import {writeCurrentUser, writeMember} from "./seasonWrites";
import {league} from "../../domain/policy/gamePolicy";
export class FirebaseSeasonRepository implements SeasonRepository {
  constructor(private db: Firestore) {}
  async resetAll(season: Season): Promise<void> {
    const jobRef = this.db.doc(`seasonResetJobs/${season.id}`), job = await jobRef.get();
    if (job.get("complete") === true) return;
    await this.db.doc(`seasons/${season.id}`).set(season, {merge: true});
    let cursor = String(job.get("cursor") ?? "");
    // Each invocation is bounded; the next scheduled run resumes unfinished pages.
    for (let page = 0; page < 5; page++) {
      let query = this.db.collection("users").orderBy(FieldPath.documentId()).limit(100);
      if (cursor) query = query.startAfter(cursor);
      const users = await query.get();
      for (let offset = 0; offset < users.size; offset += 10) {
        await Promise.all(users.docs.slice(offset, offset + 10).map((doc) => this.db.runTransaction(async (tx) => {
          const fresh = await tx.get(doc.ref);
          if (!fresh.exists) return;
          const user = mapUser(doc.id, fresh.data()!);
          if (user.seasonIndex >= season.index) return;
          const normalized = writeCurrentUser(this.db, tx, user, season);
          writeMember(this.db, tx, normalized);
        })));
      }
      if (users.size) cursor = users.docs[users.size - 1].id;
      const complete = users.size < 100;
      await this.db.runTransaction(async (tx) => {
        const fresh = await tx.get(jobRef);
        if (fresh.get("complete") === true || cursor < String(fresh.get("cursor") ?? "")) return;
        tx.set(jobRef, {cursor, complete, seasonIndex: season.index, updatedAt: Date.now()});
      });
      if (complete) return;
    }
  }
  async ranking(season: Season) {
    const members = await this.db.collection(`seasons/${season.id}/members`).orderBy("rating", "desc").limit(50).get();
    return members.docs.map((doc) => ({userId: doc.id, name: String(doc.get("name") ?? "Writer"), rating: Number(doc.get("rating") ?? 0), league: league(Number(doc.get("rating") ?? 0))}));
  }
}
