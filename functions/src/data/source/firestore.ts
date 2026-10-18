import {getApps, initializeApp} from "firebase-admin/app";
import {getFirestore} from "firebase-admin/firestore";
import {Clock} from "../../domain/repository/contracts";
import {FirebaseUserRepository} from "../repository/firebaseUserRepository";
import {FirebaseContentRepository} from "../repository/firebaseContentRepository";
import {FirebaseGameRepository} from "../repository/firebaseGameRepository";
import {FirebaseSeasonRepository} from "../repository/firebaseSeasonRepository";
import {FirebaseMatchRepository} from "../repository/firebaseMatchRepository";
import {Persistence, PersistenceProvider} from "./persistenceProvider";

export class FirestoreProvider extends PersistenceProvider {
  protected createPersistence(clock: Clock): Persistence {
    const app = getApps().find((candidate) => candidate.name === "[DEFAULT]") ?? initializeApp();
    const db = getFirestore(app);
    return {
      users: new FirebaseUserRepository(db, clock),
      content: new FirebaseContentRepository(db),
      games: new FirebaseGameRepository(db, clock),
      seasons: new FirebaseSeasonRepository(db),
      matches: new FirebaseMatchRepository(db, clock),
    };
  }
}
