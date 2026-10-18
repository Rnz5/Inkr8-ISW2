import {DomainError, EvaluationTimeout, PlayMode, WritingMode} from "../model/models";
import {Clock, ContentRepository, GameRepository, MatchRepository, WritingEvaluator} from "../repository/contracts";
import {seasonAt} from "../policy/seasonPolicy";
import {structureEvaluation} from "../policy/evaluationPolicy";
export class StartGame {
  constructor(private games: GameRepository, private content: ContentRepository, private clock: Clock) {}
  async execute(id: string, userId: string, mode: PlayMode, writingMode: WritingMode) {
    let challenge;
    try { challenge = (await this.games.get(id)).challenge; }
    catch (error) {
      if (!(error instanceof DomainError) || error.code !== "not-found") throw error;
      challenge = await this.content.challenge(writingMode);
    }
    return this.games.start(id, userId, mode, writingMode, challenge, seasonAt(this.clock.now()), this.clock.now());
  }
}
export class SubmitGame {
  constructor(private games: GameRepository) {}
  async execute(id: string, userId: string, content: string): Promise<void> {
    await this.games.submit(id, userId, content.trim());
  }
}

export class RetryEvaluation {
  constructor(private games: GameRepository) {}
  async execute(id: string, userId: string): Promise<void> {
    await this.games.retry(id, userId);
  }
}

export class EvaluateGame {
  constructor(private games: GameRepository, private evaluator: WritingEvaluator, private matches: MatchRepository, private clock: Clock) {}
  async execute(id: string): Promise<void> {
    const game = await this.games.get(id);
    if (game.status !== "PENDING") return;
    try {
      const result = await this.evaluator.evaluate(game);
      if (!Number.isFinite(result.score) || result.score < 0 || result.score > 100) {
        throw new DomainError("failed-precondition", "La evaluación recibida no es válida.");
      }
      try { await this.games.complete(game, structureEvaluation(game, result), seasonAt(this.clock.now())); }
      catch (error) {
        await this.games.fail(id, "No se pudo realizar la actualización. No se pudo conectarse al servidor.");
        throw error;
      }
    } catch (error) {
      if (error instanceof EvaluationTimeout) { await this.games.defer(id); return; }
      await this.games.fail(id, "La evaluación no pudo completarse. Reintenta.");
      throw error;
    }
    if (game.mode === "RANKED") await this.matches.match(id, seasonAt(this.clock.now()));
  }
}
