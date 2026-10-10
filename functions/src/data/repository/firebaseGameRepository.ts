import {Firestore} from "firebase-admin/firestore";
import {Challenge, DomainError, Evaluation, Game, PlayMode, Season, WritingMode} from "../../domain/model/models";
import {Clock, GameRepository} from "../../domain/repository/contracts";
import {league, meritReward, RANKED_COST, validateWriting, wordCount} from "../../domain/policy/gamePolicy";
import {acceptsRating, seasonAt} from "../../domain/policy/seasonPolicy";
import {mapUser} from "../dto/userDto";
import {mapGame} from "../mapper/gameMapper";
import {writeCurrentUser, writeMember} from "./seasonWrites";
export class FirebaseGameRepository implements GameRepository {
  constructor(private db: Firestore, private clock: Clock) {}
  async start(id: string, userId: string, mode: PlayMode, writingMode: WritingMode, challenge: Challenge, season: Season, now: number): Promise<Game> {
    return this.db.runTransaction(async (tx) => {
      season = seasonAt(this.clock.now());
      const ref = this.db.doc(`submissions/${id}`), userRef = this.db.doc(`users/${userId}`);
      const [existing, snapshot] = await tx.getAll(ref, userRef);
      if (!snapshot.exists || snapshot.get("accountClosed") === true) throw new DomainError("permission-denied", "Perfil no disponible.");
      if (existing.exists) {
        const game = mapGame(existing);
        if (game.authorId !== userId || game.mode !== mode || game.writingMode !== writingMode) throw new DomainError("permission-denied", "El ID pertenece a otra partida.");
        return game;
      }
      const user = mapUser(userId, snapshot.data()!);
      const cost = mode === "RANKED" ? RANKED_COST : 0;
      if (user.merit < cost) throw new DomainError("failed-precondition", "Merit insuficiente para Ranked.");
      const normalized = writeCurrentUser(this.db, tx, user, season);
      const game: Game = {id, authorId: userId, mode, writingMode, challenge, status: "DRAFT", content: "", seasonIndex: season.index, timestamp: now};
      tx.create(ref, {...game, seasonId: season.id, entryCost: cost, schemaVersion: 2});
      tx.update(userRef, {merit: user.merit - cost, league: league(normalized.rating)});
      writeMember(this.db, tx, normalized);
      return game;
    });
  }
  async submit(id: string, userId: string, content: string): Promise<void> {
    await this.db.runTransaction(async (tx) => {
      const ref = this.db.doc(`submissions/${id}`);
      const [snapshot, user] = await tx.getAll(ref, this.db.doc(`users/${userId}`));
      const game = mapGame(snapshot);
      if (game.authorId !== userId || user.get("accountClosed") === true) throw new DomainError("permission-denied", "Partida no disponible.");
      if (game.status !== "DRAFT") {
        if (game.content === content) return;
        throw new DomainError("failed-precondition", "La partida ya fue enviada.");
      }
      validateWriting(content, game.writingMode);
      tx.update(ref, {content, status: "PENDING", wordCount: wordCount(content)});
    });
  }
  async retry(id: string, userId: string): Promise<void> {
    await this.db.runTransaction(async (tx) => {
      const ref = this.db.doc(`submissions/${id}`), snapshot = await tx.get(ref), game = mapGame(snapshot);
      if (game.authorId !== userId) throw new DomainError("permission-denied", "Partida no disponible.");
      if (game.status === "FAILED") tx.update(ref, {status: "PENDING", error: null});
    });
  }
  async get(id: string): Promise<Game> { return mapGame(await this.db.doc(`submissions/${id}`).get()); }
  async complete(game: Game, evaluation: Evaluation, season: Season): Promise<void> {
    await this.db.runTransaction(async (tx) => {
      season = seasonAt(this.clock.now());
      const ref = this.db.doc(`submissions/${game.id}`), userRef = this.db.doc(`users/${game.authorId}`);
      const [snapshot, userSnapshot] = await tx.getAll(ref, userRef);
      if (snapshot.get("status") !== "PENDING") return;
      if (!userSnapshot.exists || userSnapshot.get("accountClosed") === true) { tx.update(ref, {status: "FAILED", error: "Perfil no disponible."}); return; }
      const user = writeCurrentUser(this.db, tx, mapUser(game.authorId, userSnapshot.data()!), season);
      const reward = meritReward(evaluation.score, wordCount(game.content), game.mode === "RANKED");
      tx.update(userRef, {merit: user.merit + reward});
      tx.update(ref, {status: "EVALUATED", evaluation: {...evaluation, meritEarned: reward},
        matchStatus: game.mode === "RANKED" ? (acceptsRating(game.seasonIndex, season.index) ? "PENDING" : "EXPIRED") : "NONE", error: null});
      writeMember(this.db, tx, user);
    });
  }
  async fail(id: string, message: string): Promise<void> {
    await this.db.runTransaction(async (tx) => {
      const ref = this.db.doc(`submissions/${id}`), snapshot = await tx.get(ref);
      if (snapshot.get("status") === "PENDING") tx.update(ref, {status: "FAILED", error: message});
    });
  }
}
