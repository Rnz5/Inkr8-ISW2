import {Firestore, Transaction} from "firebase-admin/firestore";
import {Season, User} from "../../domain/model/models";
import {currentUser} from "../../domain/policy/seasonPolicy";
import {league} from "../../domain/policy/gamePolicy";
// Caller completes all reads before invoking this write-only adapter.
export function writeCurrentUser(db: Firestore, tx: Transaction, user: User, season: Season): User {
  const normalized = currentUser(user, season);
  if (normalized.seasonIndex !== user.seasonIndex) {
    if (user.seasonIndex !== Number.MIN_SAFE_INTEGER) {
      tx.set(db.doc(`seasons/s${user.seasonIndex}/members/${user.id}`),
        {userId: user.id, name: user.name, rating: user.rating, league: league(user.rating)}, {merge: true});
    }
    tx.update(db.doc(`users/${user.id}`), {rating: 0, seasonIndex: season.index, league: 1});
  }
  tx.set(db.doc(`seasons/${season.id}`), season, {merge: true});
  return normalized;
}
export function writeMember(db: Firestore, tx: Transaction, user: User): void {
  tx.set(db.doc(`seasons/s${user.seasonIndex}/members/${user.id}`),
    {userId: user.id, name: user.name, rating: user.rating, league: league(user.rating)}, {merge: true});
}
