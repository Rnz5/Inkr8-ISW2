export type PlayMode = "PRACTICE" | "RANKED";
export type WritingMode = "STANDARD" | "ON_TOPIC";
export type GameStatus = "DRAFT" | "PENDING" | "EVALUATED" | "FAILED";
export interface Word { text: string; definition: string; sentence: string; phonetic?: string; partOfSpeech?: string; rarity?: string }
export interface Challenge { words: Word[]; topic: string | null; theme: string | null; advanced?: string[] }
export interface Season { id: string; index: number; start: number; end: number }
export interface User { id: string; name: string; email: string | null; merit: number; rating: number; seasonIndex: number; username?: string | null }
export interface ConstraintResult { label: string; fulfilled: boolean }
export interface Evaluation {
  score: number; feedback: string; spelling?: string; vocabulary?: string; coherence?: string; creativity?: string;
  topicRelevant?: boolean; constraints?: ConstraintResult[]; feedbackError?: string; constraintsError?: string;
}
export class EvaluationTimeout extends Error {}
export interface Game {
  id: string; authorId: string; mode: PlayMode; writingMode: WritingMode; status: GameStatus;
  challenge: Challenge; content: string; seasonIndex: number; timestamp: number; queuedAt?: number;
  evaluation?: Evaluation & { meritEarned: number };
}
export class DomainError extends Error {
  constructor(public readonly code: "invalid-argument" | "failed-precondition" | "not-found" | "permission-denied", message: string) { super(message); }
}
