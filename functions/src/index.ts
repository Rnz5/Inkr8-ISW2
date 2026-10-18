import {FirestoreProvider} from "./data/source/firestore";
import {PersistenceProvider} from "./data/source/persistenceProvider";
import {OpenAiWritingEvaluator} from "./data/source/openAiWritingEvaluator";
import {submissionIdentity} from "./domain/policy/submissionIdentity";
import {EvaluateGame, RetryEvaluation, StartGame, SubmitGame} from "./domain/usecase/gameUseCases";
import {GetDailyHome, GetSeasonRanking, GetWordExample, InitializeUser, UpdateUserName} from "./domain/usecase/contentUseCases";
import {GetProgress, GetSeasonHistory} from "./domain/usecase/progressUseCases";
import {ResetLeagues} from "./domain/usecase/resetLeagues";
import {gameHandlers, openAiKey} from "./presentation/handlers/gameHandlers";
import {contentHandlers} from "./presentation/handlers/contentHandlers";
import {seasonScheduler} from "./presentation/scheduler/seasonScheduler";
const clock = {now: () => Date.now()};
function persistenceProvider(name: string): PersistenceProvider {
  if (name === "firestore") return new FirestoreProvider();
  throw new Error(`Proveedor de base de datos no soportado: ${name}.`);
}
const {users, content, games, seasons, matches, progress} = persistenceProvider(process.env.DATABASE_PROVIDER ?? "firestore").create(clock);
const evaluator = new OpenAiWritingEvaluator(() => openAiKey.value());
const gameEndpoints = gameHandlers(users, new StartGame(games, content, clock), new EvaluateGame(games, evaluator, matches, clock),
  new SubmitGame(games), new RetryEvaluation(games), submissionIdentity);
export const startGame = gameEndpoints.startGame;
export const submitGame = gameEndpoints.submitGame;
export const retryEvaluation = gameEndpoints.retryEvaluation;
export const gameEvaluationWorker = gameEndpoints.submissionEvaluationEngine;
const contentEndpoints = contentHandlers(users, new InitializeUser(users, clock), new UpdateUserName(users),
  new GetDailyHome(content, clock), new GetSeasonRanking(seasons, clock), new GetWordExample(content, clock),
  new GetProgress(progress), new GetSeasonHistory(seasons, clock));
export const initializeUser = contentEndpoints.initializeUser;
export const updateProfile = contentEndpoints.updateProfile;
export const getHome = contentEndpoints.getHome;
export const getSeasonRanking = contentEndpoints.getSeasonRanking;
export const getWordExample = contentEndpoints.getWordExample;
export const getProfileProgress = contentEndpoints.getProfileProgress;
export const getSeasonHistory = contentEndpoints.getSeasonHistory;
export const leagueReset = seasonScheduler(new ResetLeagues(seasons, clock), matches, clock, games);
