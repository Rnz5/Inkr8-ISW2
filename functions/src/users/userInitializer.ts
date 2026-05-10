import * as functions from "firebase-functions/v1";
import {db} from "../firebase/admin";

export const createUserProfile =
functions.auth.user().onCreate(async (user) => {
  const uid = user.uid;
  const userRef = db.collection("users").doc(uid);

  await userRef.set({
    id: uid,
    name: user.displayName ?? "",
    email: user.email ?? "",
    profileImageURL: user.photoURL ?? "",
    merit: 1000,
    rating: 0,
    reputation: 0,
    bestScore: 0.0,
    submissionsCount: 0,
    tournamentsPlayed: 0,
    tournamentsWon: 0,
    totalMeritEarned: 0,
    tipsReceived: 0,
    joinedDate: Date.now(),
    hasChosenUsername: false,
    isPhilosopher: false,
    isPlaced: false,
    placementMatchesPlayed: 0,
    totalPlacementScore: 0,
  });

  // League counts are now handled in the evaluation engine after placement matches
});
