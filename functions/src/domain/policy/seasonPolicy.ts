import {Season, User} from "../model/models";
export const SEASON_ANCHOR_MS = Date.parse("2026-10-12T00:00:00Z");
export const SEASON_DURATION_MS = 14 * 24 * 60 * 60 * 1000;
export function seasonAt(now: number): Season {
  const index = Math.floor((now - SEASON_ANCHOR_MS) / SEASON_DURATION_MS);
  const start = SEASON_ANCHOR_MS + index * SEASON_DURATION_MS;
  return {id: `s${index}`, index, start, end: start + SEASON_DURATION_MS};
}
export function currentUser(user: User, season: Season): User {
  return user.seasonIndex < season.index ? {...user, rating: 0, seasonIndex: season.index} : user;
}
export function acceptsRating(gameIndex: number, nowIndex: number): boolean { return gameIndex === nowIndex; }
