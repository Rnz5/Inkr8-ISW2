import {DomainError} from "../../domain/model/models";
import {UserRepository} from "../../domain/repository/contracts";
import {GetDailyHome, GetSeasonRanking, GetWordExample, InitializeUser, UpdateUserName} from "../../domain/usecase/contentUseCases";
import {GetProgress, GetSeasonHistory} from "../../domain/usecase/progressUseCases";
import {authenticated} from "./callables";
export function contentHandlers(users: UserRepository, initialize: InitializeUser, updateName: UpdateUserName, home: GetDailyHome, ranking: GetSeasonRanking,
  example?: GetWordExample, progress?: GetProgress, history?: GetSeasonHistory) {
  return {
    initializeUser: authenticated(users, async (uid, _data, request) => {
      return initialize.execute(uid, String(request.auth?.token.name ?? "Writer"), typeof request.auth?.token.email === "string" ? request.auth.token.email : null);
    }),
    updateProfile: authenticated(users, async (uid, data) => {
      if (typeof data.name !== "string") throw new DomainError("invalid-argument", "Nombre inválido.");
      await updateName.execute(uid, data.name); return {updated: true};
    }),
    getHome: authenticated(users, async () => home.execute()),
    getSeasonRanking: authenticated(users, async (uid, _data, request) => {
      await initialize.execute(uid, String(request.auth?.token.name ?? "Writer"), typeof request.auth?.token.email === "string" ? request.auth.token.email : null);
      return ranking.execute(uid);
    }),
    getWordExample: authenticated(users, async (_uid, data) => {
      if (!example) throw new DomainError("failed-precondition", "Contenido no configurado.");
      if (!Number.isSafeInteger(data.day)) throw new DomainError("invalid-argument", "Día inválido.");
      return example.execute(data.day as number);
    }),
    getProfileProgress: authenticated(users, async (uid) => {
      if (!progress) throw new DomainError("failed-precondition", "Progreso no configurado.");
      return progress.execute(uid);
    }),
    getSeasonHistory: authenticated(users, async (uid) => {
      if (!history) throw new DomainError("failed-precondition", "Temporadas no configuradas.");
      return {seasons: await history.execute(uid)};
    }),
  };
}
