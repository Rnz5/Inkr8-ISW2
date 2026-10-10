import {Clock, SeasonRepository} from "../repository/contracts";
import {seasonAt} from "../policy/seasonPolicy";
export class ResetLeagues {
  constructor(private repository: SeasonRepository, private clock: Clock) {}
  async execute(): Promise<void> { await this.repository.resetAll(seasonAt(this.clock.now())); }
}
