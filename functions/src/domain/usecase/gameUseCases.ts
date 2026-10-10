import {DomainError, PlayMode, WritingMode} from "../model/models";
import {Clock, ContentRepository, GameRepository, MatchRepository, WritingEvaluator} from "../repository/contracts";
import {seasonAt} from "../policy/seasonPolicy";
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
export class EvaluateGame {
  constructor(private games: GameRepository, private evaluator: WritingEvaluator, private matches: MatchRepository, private clock: Clock) {}
  async execute(id: string): Promise<void> {
    const game = await this.games.get(id);
    if (game.status !== "PENDING") return;
    try {
      const result = await this.evaluator.evaluate(game);
      if (!Number.isFinite(result.score) || result.score < 0 || result.score > 100 || !result.feedback.trim()) {
        throw new DomainError("failed-precondition", "La evaluación recibida no es válida.");
      }
      await this.games.complete(game, result, seasonAt(this.clock.now()));
    } catch (error) {
      // Persist failure; an explicit authenticated retry can queue the same game without another debit.
      await this.games.fail(id, "La evaluación no pudo completarse. Reintenta.");
      throw error;
    }
    if (game.mode === "RANKED") await this.matches.match(id, seasonAt(this.clock.now()));
  }
}
