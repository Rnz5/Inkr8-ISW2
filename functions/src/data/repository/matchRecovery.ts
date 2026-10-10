import {Firestore} from "firebase-admin/firestore";
import {Season} from "../../domain/model/models";

export class MatchRecovery {
  constructor(private db: Firestore) {}

  async recoverExpired(season: Season): Promise<void> {
    const expired = await this.db.collection("submissions").where("matchStatus", "==", "PENDING")
      .where("seasonIndex", "<", season.index).limit(200).get();
    for (const game of expired.docs) await this.db.runTransaction(async (tx) => {
      const fresh = await tx.get(game.ref);
      if (fresh.get("matchStatus") === "PENDING" && fresh.get("seasonIndex") < season.index) {
        tx.update(game.ref, {matchStatus: "EXPIRED"});
      }
    });
  }

  async retryPending(season: Season, retry: (id: string, season: Season) => Promise<void>): Promise<void> {
    const pending = await this.db.collection("submissions").where("seasonIndex", "==", season.index)
      .where("matchStatus", "==", "PENDING").orderBy("timestamp").limit(200).get();
    for (const game of pending.docs) await retry(game.id, season);
  }
}
