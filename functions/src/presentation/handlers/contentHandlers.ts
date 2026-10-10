import {DomainError} from "../../domain/model/models";
import {Clock, ContentRepository, SeasonRepository, UserRepository} from "../../domain/repository/contracts";
import {league, RANKED_COST} from "../../domain/policy/gamePolicy";
import {seasonAt} from "../../domain/policy/seasonPolicy";
import {authenticated} from "./callables";
export function contentHandlers(users: UserRepository, content: ContentRepository, seasons: SeasonRepository, clock: Clock) {
  return {
    initializeUser: authenticated(users, async (uid, _data, request) => {
      const user = await users.initialize(uid, String(request.auth?.token.name ?? "Writer"), typeof request.auth?.token.email === "string" ? request.auth.token.email : null, seasonAt(clock.now()));
      return {...user, league: league(user.rating)};
    }),
    updateProfile: authenticated(users, async (uid, data) => {
      if (typeof data.name !== "string") throw new DomainError("invalid-argument", "Nombre inválido.");
      await users.updateName(uid, data.name.trim()); return {updated: true};
    }),
    getHome: authenticated(users, async () => ({word: await content.dailyWord(Math.floor(clock.now() / 86400000)), season: seasonAt(clock.now()), rankedCost: RANKED_COST})),
    getSeasonRanking: authenticated(users, async () => {
      const season = seasonAt(clock.now()); return {season, members: await seasons.ranking(season)};
    }),
  };
}
