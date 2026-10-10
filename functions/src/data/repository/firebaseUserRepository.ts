import {Firestore} from "firebase-admin/firestore";
import {DomainError, Season, User} from "../../domain/model/models";
import {Clock, UserRepository} from "../../domain/repository/contracts";
import {INITIAL_MERIT, league} from "../../domain/policy/gamePolicy";
import {mapUser} from "../dto/userDto";
import {writeCurrentUser, writeMember} from "./seasonWrites";
import {seasonAt} from "../../domain/policy/seasonPolicy";
export class FirebaseUserRepository implements UserRepository {
  constructor(private db: Firestore, private clock: Clock) {}
  async assertOpen(id: string): Promise<void> {
    if ((await this.db.doc(`users/${id}`).get()).get("accountClosed") === true) throw new DomainError("permission-denied", "Esta cuenta está cerrada.");
  }
  async initialize(id: string, name: string, email: string | null, season: Season): Promise<User> {
    return this.db.runTransaction(async (tx) => {
      season = seasonAt(this.clock.now());
      const ref = this.db.doc(`users/${id}`);
      const snapshot = await tx.get(ref);
      if (snapshot.get("accountClosed") === true) throw new DomainError("permission-denied", "Esta cuenta está cerrada.");
      if (!snapshot.exists) {
        const user = {id, name: name || "Writer", email, merit: INITIAL_MERIT, rating: 0, seasonIndex: season.index};
        tx.create(ref, {...user, league: 1});
        tx.set(this.db.doc(`seasons/${season.id}`), season, {merge: true});
        writeMember(this.db, tx, user);
        return user;
      }
      const user = writeCurrentUser(this.db, tx, mapUser(id, snapshot.data()!), season);
      tx.update(ref, {league: league(user.rating)});
      writeMember(this.db, tx, user);
      return user;
    });
  }
  async updateName(id: string, name: string): Promise<void> {
    if (name.length < 2 || name.length > 20 || /[\p{C}]/u.test(name)) throw new DomainError("invalid-argument", "El nombre debe tener entre 2 y 20 caracteres visibles.");
    await this.db.runTransaction(async (tx) => {
      const ref = this.db.doc(`users/${id}`), snapshot = await tx.get(ref);
      if (!snapshot.exists || snapshot.get("accountClosed") === true) throw new DomainError("permission-denied", "Perfil no disponible.");
      const user = {...mapUser(id, snapshot.data()!), name};
      tx.update(ref, {name, league: league(user.rating)});
      if (user.seasonIndex !== Number.MIN_SAFE_INTEGER) writeMember(this.db, tx, user);
    });
  }
}
