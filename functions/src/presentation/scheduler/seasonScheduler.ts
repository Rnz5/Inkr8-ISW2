import {onSchedule} from "firebase-functions/v2/scheduler";
import {Clock, MatchRepository} from "../../domain/repository/contracts";
import {ResetLeagues} from "../../domain/usecase/resetLeagues";
import {seasonAt} from "../../domain/policy/seasonPolicy";
export function seasonScheduler(reset: ResetLeagues, matches: MatchRepository, clock: Clock) {
  return onSchedule({schedule: "*/5 * * * *", timeZone: "UTC", region: "us-central1", timeoutSeconds: 540, retryCount: 3}, async () => {
    await reset.execute(); await matches.recover(seasonAt(clock.now()));
  });
}
