import {Firestore} from "firebase-admin/firestore";
import {Season} from "../../domain/model/models";
import {Clock} from "../../domain/repository/contracts";
import {league, ratingChange} from "../../domain/policy/gamePolicy";
import {mapGame} from "../mapper/gameMapper";
import {mapUser} from "../dto/userDto";
import {currentUser, seasonAt} from "../../domain/policy/seasonPolicy";
import {writeCurrentUser, writeMember} from "./seasonWrites";
export class Matchmaker {
  constructor(private db: Firestore, private clock: Clock) {}
  async pairCandidates(id: string, season: Season): Promise<boolean> {
    const own = await this.db.doc(`submissions/${id}`).get();
    if (own.get("matchStatus") !== "PENDING" || own.get("seasonIndex") !== season.index) return false;
    const candidates = await this.db.collection("submissions").where("seasonIndex", "==", season.index)
      .where("matchStatus", "==", "PENDING").orderBy("timestamp").limit(100).get();
    for (const candidate of candidates.docs) {
      if (candidate.id === id || candidate.get("authorId") === own.get("authorId")) continue;
      if (await this.pair(id, candidate.id, season)) return true;
    }
    return false;
  }
  private async pair(id: string, opponentId: string, season: Season): Promise<boolean> {
    return this.db.runTransaction(async (tx) => {
      if (seasonAt(this.clock.now()).index !== season.index) return false;
      const refs = [this.db.doc(`submissions/${id}`), this.db.doc(`submissions/${opponentId}`)];
      const [mine, theirs] = await tx.getAll(...refs);
      if (mine.get("matchStatus") !== "PENDING" || theirs.get("matchStatus") !== "PENDING" || mine.get("status") !== "EVALUATED" || theirs.get("status") !== "EVALUATED" || mine.get("seasonIndex") !== season.index || theirs.get("seasonIndex") !== season.index) return false;
      const a = mapGame(mine), b = mapGame(theirs);
      if (a.authorId === b.authorId || !a.evaluation || !b.evaluation) return false;
      const userRefs = [this.db.doc(`users/${a.authorId}`), this.db.doc(`users/${b.authorId}`)];
      const [ua, ub] = await tx.getAll(...userRefs);
      if (!ua.exists || !ub.exists || ua.get("accountClosed") === true || ub.get("accountClosed") === true) return false;
      const users = [mapUser(a.authorId, ua.data()!), mapUser(b.authorId, ub.data()!)];
      const normalized = users.map((u) => currentUser(u, season));
      if (Math.abs(normalized[0].rating - normalized[1].rating) > 30) return false;
      const scores = [a.evaluation.score, b.evaluation.score];
      for (let i = 0; i < 2; i++) {
        const other = 1 - i, user = writeCurrentUser(this.db, tx, users[i], season);
        const outcome = scores[i] === scores[other] ? "DRAW" : scores[i] > scores[other] ? "WIN" : "LOSS";
        const rating = Math.max(0, user.rating + ratingChange(user.rating, normalized[other].rating, outcome));
        tx.update(userRefs[i], {rating, league: league(rating)});
        tx.update(refs[i], {matchStatus: "MATCHED", match: {opponentName: normalized[other].name, opponentScore: scores[other], outcome, ratingChange: rating - user.rating}});
        writeMember(this.db, tx, {...user, rating});
      }
      return true;
    });
  }
}

