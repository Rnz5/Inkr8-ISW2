import {Challenge, Evaluation, Game, PlayMode, Season, User, Word, WritingMode} from "../model/models";
export interface Clock { now(): number }
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
}
export interface WritingEvaluator { evaluate(game: Game): Promise<Evaluation> }
export interface MatchRepository { match(id: string, season: Season): Promise<void>; recover(season: Season): Promise<void> }
export interface SeasonRepository {
  resetAll(season: Season): Promise<void>;
  ranking(season: Season): Promise<Array<{userId: string; name: string; rating: number; league: number}>>;
}
