import {Firestore} from "firebase-admin/firestore";
import {Season} from "../../domain/model/models";
import {Clock} from "../../domain/repository/contracts";
import {league, ratingChange} from "../../domain/policy/gamePolicy";
import {baselineScore, BASELINE_OPPONENT_NAME, isEligibleForBaseline} from "../../domain/policy/matchingPolicy";
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
      if (game.status !== "EVALUATED" || game.seasonIndex !== season.index || !game.evaluation || !isEligibleForBaseline(game.queuedAt ?? game.timestamp, this.clock.now())) return;
      const userRef = this.db.doc(`users/${game.authorId}`), userSnapshot = await tx.get(userRef);
      if (!userSnapshot.exists || userSnapshot.get("accountClosed") === true) return;
      const history = await tx.get(this.db.collection("submissions").where("authorId", "==", game.authorId).where("schemaVersion", "==", 2));
      const scores = history.docs.filter((doc) => doc.get("mode") === "RANKED" && doc.get("status") === "EVALUATED")
        .map((doc) => Number(doc.get("evaluation.score"))).filter(Number.isFinite);
      const reference = baselineScore(scores, game.evaluation.score);
      const user = writeCurrentUser(this.db, tx, mapUser(game.authorId, userSnapshot.data()!), season);
      const outcome = game.evaluation.score === reference ? "DRAW" : game.evaluation.score > reference ? "WIN" : "LOSS";
      const rating = Math.max(0, user.rating + ratingChange(user.rating, user.rating, outcome));
      tx.update(userRef, {rating, league: league(rating)});
      tx.update(ref, {matchStatus: "MATCHED", matchError: null, match: {opponentName: BASELINE_OPPONENT_NAME, opponentScore: reference, outcome, ratingChange: rating - user.rating}});
      writeMember(this.db, tx, {...user, rating});
    });
  }
}

