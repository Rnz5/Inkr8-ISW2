import {Clock, ContentRepository, GameRepository, MatchRepository, SeasonRepository, UserRepository} from "../../domain/repository/contracts";

export interface Persistence {
  readonly users: UserRepository;
  readonly content: ContentRepository;
  readonly games: GameRepository;
  readonly seasons: SeasonRepository;
  readonly matches: MatchRepository;
}

export abstract class PersistenceProvider {
  create(clock: Clock): Persistence {
    return this.createPersistence(clock);
  }

  protected abstract createPersistence(clock: Clock): Persistence;
}
