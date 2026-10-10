import {onDocumentUpdated} from "firebase-functions/v2/firestore";
import {defineSecret} from "firebase-functions/params";
import {DomainError} from "../../domain/model/models";
import {GameRepository, UserRepository} from "../../domain/repository/contracts";
import {EvaluateGame, StartGame} from "../../domain/usecase/gameUseCases";
import {authenticated, gameId} from "./callables";
export const openAiKey = defineSecret("OPENAI_API_KEY");
export function gameHandlers(users: UserRepository, games: GameRepository, start: StartGame, evaluate: EvaluateGame) {
  return {
    startGame: authenticated(users, async (uid, data) => {
      if (data.mode !== "PRACTICE" && data.mode !== "RANKED") throw new DomainError("invalid-argument", "Modo inválido.");
      if (data.writingMode !== "STANDARD" && data.writingMode !== "ON_TOPIC") throw new DomainError("invalid-argument", "Tipo de escritura inválido.");
      return start.execute(gameId(data.id), uid, data.mode, data.writingMode);
    }),
    submitGame: authenticated(users, async (uid, data) => {
      if (typeof data.content !== "string") throw new DomainError("invalid-argument", "Texto inválido.");
      await games.submit(gameId(data.id), uid, data.content.trim()); return {submitted: true};
    }),
    retryEvaluation: authenticated(users, async (uid, data) => { await games.retry(gameId(data.id), uid); return {queued: true}; }),
    submissionEvaluationEngine: onDocumentUpdated({document: "submissions/{submissionId}", region: "us-central1", secrets: [openAiKey], timeoutSeconds: 300, retry: true}, async (event) => {
      if (event.data?.after.get("status") === "PENDING" && event.data.before.get("status") !== "PENDING") await evaluate.execute(event.params.submissionId);
    }),
  };
}
