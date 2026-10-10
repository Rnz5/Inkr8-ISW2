import {DomainError, WritingMode} from "../model/models";
export const INITIAL_MERIT = 1000;
export const RANKED_COST = 100;
export function league(rating: number): number {
  return rating < 30 ? 1 : rating < 60 ? 2 : rating < 90 ? 3 : rating < 120 ? 4 : rating < 150 ? 5 : 6;
}
export function wordCount(text: string): number { return text.trim().split(/\s+/u).filter(Boolean).length; }
export function validateWriting(text: string, mode: WritingMode): void {
  const count = wordCount(text);
  if (text.length > 10000 || count < 50 || count > (mode === "STANDARD" ? 150 : 200)) {
    throw new DomainError("invalid-argument", `Escribe entre 50 y ${mode === "STANDARD" ? 150 : 200} palabras.`);
  }
}
export function meritReward(score: number, count: number, ranked: boolean): number {
  if (!Number.isFinite(score) || score < 0 || score > 100) throw new DomainError("invalid-argument", "Puntuación inválida.");
  const base = score >= 95 ? 500 : score >= 90 ? 400 : score >= 80 ? 300 : score >= 70 ? 200 : score >= 60 ? 125 : score >= 40 ? 60 : 25;
  return Math.floor((base + Math.min(Math.floor(count / 10), 40)) * (ranked ? 1.5 : 1));
}
export function ratingChange(mine: number, opponent: number, outcome: "WIN" | "LOSS" | "DRAW"): number {
  const adjustment = Math.max(-5, Math.min(5, (opponent - mine) * 0.05));
  if (outcome === "DRAW") return 1;
  if (outcome === "LOSS") return Math.min(-1, Math.round(-6 + adjustment));
  return Math.max(1, Math.min(Math.round(4 + adjustment), mine >= 180 ? 2 : mine >= 150 ? 3 : Infinity));
}
