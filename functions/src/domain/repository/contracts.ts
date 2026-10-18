import {Challenge, Evaluation, Game, PlayMode, Season, User, Word, WritingMode} from "../model/models";
export interface Clock { now(): number }
export interface SubmissionIdentity { resolve(value: unknown): string }
export interface UserRepository {
  initialize(id: string, name: string, email: string | null, season: Season): Promise<User>;
  assertOpen(id: string): Promise<void>;
  updateName(id: string, name: string): Promise<void>;
}
export interface ContentRepository {
  dailyWord(day: number): Promise<Word>;
  challenge(mode: WritingMode): Promise<Challenge>;
}
export interface GameRepository {
  start(id: string, userId: string, mode: PlayMode, writingMode: WritingMode, challenge: Challenge, season: Season, now: number): Promise<Game>;
  submit(id: string, userId: string, content: string): Promise<void>;
  retry(id: string, userId: string): Promise<void>;
  get(id: string): Promise<Game>;
  complete(game: Game, evaluation: Evaluation, season: Season): Promise<void>;
  fail(id: string, message: string): Promise<void>;
  defer(id: string): Promise<void>;
  recoverEvaluations(): Promise<void>;
}
export interface WritingEvaluator { evaluate(game: Game): Promise<Evaluation> }
export interface MatchRepository { match(id: string, season: Season): Promise<void>; recover(season: Season): Promise<void> }
export interface SeasonReader {
  ranking(season: Season): Promise<Array<{userId: string; name: string; rating: number; league: number}>>;
  position(userId: string, season: Season): Promise<{userId: string; name: string; rating: number; league: number; position: number} | null>;
  history(userId: string, season: Season): Promise<SeasonHistory[]>;
}
export interface SeasonHistory { season: Season; position: number; rating: number; meritEarned: number }
export interface ProgressReader { games(userId: string): Promise<Array<Game & {match?: {outcome: string}}>> }
export interface SeasonResetter { resetAll(season: Season): Promise<void> }
export interface SeasonRepository extends SeasonReader, SeasonResetter {}
