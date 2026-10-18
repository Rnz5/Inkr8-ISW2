import {Clock, ProgressReader, SeasonReader} from "../repository/contracts";
import {seasonAt} from "../policy/seasonPolicy";

export class GetProgress {
  constructor(private progress: ProgressReader) {}
  async execute(userId: string) {
    const history = (await this.progress.games(userId)).filter((g) => g.mode === "RANKED" && g.status === "EVALUATED" && g.evaluation);
    const scores = history.map((g) => g.evaluation!.score);
    const resolved = history.filter((g) => g.match);
    return {history, statistics: {
      gamesPlayed: scores.length,
      averageScore: scores.length ? scores.reduce((a, b) => a + b, 0) / scores.length : 0,
      bestScore: scores.reduce((best, score) => Math.max(best, score), 0),
      winRate: resolved.length ? resolved.filter((g) => g.match?.outcome === "WIN").length * 100 / resolved.length : 0,
    }};
  }
}
export class GetSeasonHistory {
  constructor(private seasons: SeasonReader, private clock: Clock) {}
  execute(userId: string) { return this.seasons.history(userId, seasonAt(this.clock.now())); }
}
