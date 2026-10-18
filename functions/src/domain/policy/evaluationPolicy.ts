import {ConstraintResult, Evaluation, Game} from "../model/models";
import {wordCount} from "./gamePolicy";

export const TIMEOUT_MESSAGE = "La evaluación está tomando más de lo esperado. Te notificaremos cuando tu resultado esté listo";
export function containsWord(text: string, word: string): boolean {
  const escaped = word.normalize("NFKC").replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  return new RegExp(`(^|[^\\p{L}\\p{N}])${escaped}($|[^\\p{L}\\p{N}])`, "iu").test(text.normalize("NFKC"));
}
export function structureEvaluation(game: Game, result: Evaluation): Evaluation {
  const evaluation = {...result};
  if (![result.spelling, result.vocabulary, result.coherence, result.creativity].every((v) => typeof v === "string" && v.trim())) {
    evaluation.feedbackError = "No se pudo cargar el feedback detallado en este momento";
  }
  if (!game.challenge) return evaluation;
  const count = wordCount(game.content), max = game.writingMode === "STANDARD" ? 150 : 200;
  const constraints: ConstraintResult[] = [
    {label: `Entre 50 y ${max} palabras`, fulfilled: count >= 50 && count <= max},
    ...game.challenge.words.map((w) => ({label: `Usar «${w.text}»`, fulfilled: containsWord(game.content, w.text)})),
  ];
  if (game.challenge.topic) {
    if (typeof result.topicRelevant === "boolean") constraints.push({label: `Tema: ${game.challenge.topic}`, fulfilled: result.topicRelevant});
    else evaluation.constraintsError = "No se pudo validar el cumplimiento de restricciones";
  }
  for (const rule of game.challenge.advanced ?? []) {
    if (rule === "FIRST_PERSON") constraints.push({label: "Escribir en primera persona", fulfilled: /(^|[^\p{L}])(yo|me|mi|nosotros|nosotras|i|my|we|our)([^\p{L}]|$)/iu.test(game.content)});
    else if (rule === "QUESTION") constraints.push({label: "Incluir una pregunta", fulfilled: /[^.!?\n]+\?/u.test(game.content)});
    else evaluation.constraintsError = "No se pudo validar el cumplimiento de restricciones";
  }
  return {...evaluation, constraints};
}
