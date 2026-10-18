import {Clock, ContentRepository, SeasonReader, UserRepository} from "../repository/contracts";
import {league, RANKED_COST} from "../policy/gamePolicy";
import {seasonAt} from "../policy/seasonPolicy";
import {validateUserName} from "../policy/userPolicy";
import {DomainError} from "../model/models";
import {containsWord} from "../policy/evaluationPolicy";

export class GetDailyHome {
  constructor(private content: ContentRepository, private clock: Clock) {}
  async execute() {
    const now = this.clock.now(), wordDay = Math.floor(now / 86400000);
    return {
      word: await this.content.dailyWord(wordDay), wordDay,
      season: seasonAt(now),
      rankedCost: RANKED_COST,
    };
  }
}

export class UpdateUserName {
  constructor(private users: UserRepository) {}
  async execute(userId: string, name: string): Promise<void> {
    await this.users.updateName(userId, validateUserName(name.trim()));
  }
}

export class InitializeUser {
  constructor(private users: UserRepository, private clock: Clock) {}
  async execute(userId: string, name: string, email: string | null) {
    const user = await this.users.initialize(userId, name, email, seasonAt(this.clock.now()));
    return {...user, league: league(user.rating)};
  }
}

export class GetSeasonRanking {
  constructor(private seasons: SeasonReader, private clock: Clock) {}
  async execute(userId?: string) {
    const season = seasonAt(this.clock.now());
    const members = await this.seasons.ranking(season);
    return {season, members, ...(userId ? {own: await this.seasons.position(userId, season)} : {})};
  }
}
export class GetWordExample {
  constructor(private content: ContentRepository, private clock: Clock) {}
  async execute(day?: number) {
    const word = await this.content.dailyWord(day ?? Math.floor(this.clock.now() / 86400000));
    if (!word.sentence.trim() || !containsWord(word.sentence, word.text)) {
      throw new DomainError("failed-precondition", "No se pudo cargar el ejemplo. Por favor, intenta de nuevo");
    }
    return {sentence: word.sentence};
  }
}
