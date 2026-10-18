export const MAX_MATCH_RATING_DELTA = 30;
export const BASELINE_WAIT_MS = 48 * 60 * 60 * 1000;
export const BASELINE_OPPONENT_NAME = "R8 (tu puntuación promedio)";
export function baselineScore(scores: number[], currentScore: number): number {
  const valid = scores.filter((score) => Number.isFinite(score) && score >= 0 && score <= 100);
  return valid.length ? valid.reduce((sum, score) => sum + score, 0) / valid.length : currentScore;
}

export function isRatingEligibleForMatch(ratingA: number, ratingB: number): boolean {
  return Math.abs(ratingA - ratingB) <= MAX_MATCH_RATING_DELTA;
}

export function isEligibleForBaseline(timestamp: number, now: number): boolean {
  return timestamp <= now - BASELINE_WAIT_MS;
}
