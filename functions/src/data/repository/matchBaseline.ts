import {Firestore} from "firebase-admin/firestore";
import {Season} from "../../domain/model/models";
import {Clock} from "../../domain/repository/contracts";
import {league, ratingChange} from "../../domain/policy/gamePolicy";
import {mapGame} from "../mapper/gameMapper";
import {mapUser} from "../dto/userDto";
import {seasonAt} from "../../domain/policy/seasonPolicy";
import {writeCurrentUser, writeMember} from "./seasonWrites";
export class MatchBaseline {
  constructor(private db: Firestore, private clock: Clock) {}
  async resolve(id: string, season: Season): Promise<void> {
    await this.db.runTransaction(async (tx) => {
      if (seasonAt(this.clock.now()).index !== season.index) return;
      const ref = this.db.doc(`submissions/${id}`), snapshot = await tx.get(ref);
      if (!snapshot.exists || snapshot.get("matchStatus") !== "PENDING") return;
      const game = mapGame(snapshot);
      if (game.status !== "EVALUATED" || game.seasonIndex !== season.index || !game.evaluation || game.timestamp > this.clock.now() - 48 * 60 * 60 * 1000) return;
      const userRef = this.db.doc(`users/${game.authorId}`), userSnapshot = await tx.get(userRef);
      if (!userSnapshot.exists || userSnapshot.get("accountClosed") === true) return;
      const user = writeCurrentUser(this.db, tx, mapUser(game.authorId, userSnapshot.data()!), season);
      const outcome = game.evaluation.score === 65 ? "DRAW" : game.evaluation.score > 65 ? "WIN" : "LOSS";
      const rating = Math.max(0, user.rating + ratingChange(user.rating, user.rating, outcome));
      tx.update(userRef, {rating, league: league(rating)});
      tx.update(ref, {matchStatus: "MATCHED", match: {opponentName: "R8 (referencia de 65 puntos)", opponentScore: 65, outcome, ratingChange: rating - user.rating}});
      writeMember(this.db, tx, {...user, rating});
    });
  }
}

