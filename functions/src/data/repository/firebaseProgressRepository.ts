import {Firestore} from "firebase-admin/firestore";
import {ProgressReader} from "../../domain/repository/contracts";
import {Game} from "../../domain/model/models";
import {mapGame} from "../mapper/gameMapper";

export class FirebaseProgressRepository implements ProgressReader {
  constructor(private db: Firestore) {}
  async games(userId: string): Promise<Array<Game & {match?: {outcome: string}}>> {
    // The immutable evaluated submissions are the source of truth for lifetime statistics.
    const query = this.db.collection("submissions").where("authorId", "==", userId)
      .where("schemaVersion", "==", 2).orderBy("timestamp", "desc");
    const games: Array<Game & {match?: {outcome: string}}> = [];
    let page = await query.limit(250).get();
    while (!page.empty) {
      for (const doc of page.docs) {
        if (doc.get("mode") === "RANKED" && doc.get("status") === "EVALUATED") games.push(mapGame(doc));
      }
      if (page.size < 250) break;
      page = await query.startAfter(page.docs[page.size - 1]).limit(250).get();
    }
    return games;
  }
}
