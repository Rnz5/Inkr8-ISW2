import {onCall, HttpsError} from "firebase-functions/v2/https";
import {db, FieldValue} from "../firebase/admin";
import {calculateRankedEntryCost} from "../utils/meritCalculator";

type MeritAction =
  | "PURCHASE_EXAMPLE_SENTENCE"
  | "ENTER_RANKED"
  | "ABANDON_RANKED"
  | "REWARD_PRACTICE"
  | "REWARD_RANKED"
  | "CHANGE_USERNAME"
  | "SAVE_SUBMISSION"
  | "EXPAND_MERIT_CAP";

function validateUsername(username: string): string | null {
  if (username.length < 2) return "Username must be at least 2 characters.";
  if (username.length > 20) return "Username must be at most 20 characters.";

  const allowedRegex = /^[A-Za-z0-9_.,*{}[\]()√]+$/;
  if (!allowedRegex.test(username)) {
    return "Username contains invalid characters.";
  }

  const hasLetterOrDigit = /[A-Za-z0-9]/.test(username);
  if (!hasLetterOrDigit) {
    return "Username must contain at least one letter or number.";
  }

  return null;
}

export const applyMeritAction = onCall(
  {
    region: "us-central1",
  },
  async (request) => {
    const uid = request.auth?.uid;
    if (!uid) {
      throw new HttpsError("unauthenticated", "You must be signed in.");
    }

    const action = request.data?.action as MeritAction | undefined;
    if (!action) {
      throw new HttpsError("invalid-argument", "Missing action.");
    }

    const userRef = db.collection("users").doc(uid);
    let updatedFields: Record<string, unknown> = {};

    await db.runTransaction(async (tx) => {
      const userSnap = await tx.get(userRef);

      if (!userSnap.exists) {
        throw new Error("User not found."); // Caught by transaction and converted to HttpsError if needed
      }

      const user = userSnap.data();
      const currentMerit = user?.merit ?? 0;
      const meritCap = user?.meritCap ?? 50000;
      const rankedWinStreak = user?.rankedWinStreak ?? 0;
      const rankedLossStreak = user?.rankedLossStreak ?? 0;
      const reputation = user?.reputation ?? 0;

      let meritDelta = 0;
      let reason = "";

      switch (action) {
      case "EXPAND_MERIT_CAP": {
        const cost = Math.floor(meritCap * 0.25);
        const expansionAmount = 10000;

        if (currentMerit < cost) {
          throw new Error("Insufficient Merit for expansion.");
        }

        meritDelta = -cost;
        reason = "EXPAND_MERIT_CAP";
        updatedFields = {
          merit: currentMerit - cost,
          meritCap: meritCap + expansionAmount,
        };
        tx.update(userRef, updatedFields);
        break;
      }

      case "PURCHASE_EXAMPLE_SENTENCE": {
        const cost = 25;

        if (currentMerit < cost) {
          throw new Error("Not enough Merit.");
        }

        meritDelta = -cost;
        reason = "PURCHASE_EXAMPLE_SENTENCE";
        updatedFields = {
          merit: currentMerit - cost,
        };
        tx.update(userRef, updatedFields);
        break;
      }

      case "SAVE_SUBMISSION": {
        const submissionId = request.data?.submissionId;
        if (!submissionId) {
          throw new Error("Missing submissionId.");
        }

        // Optimization: Use cached count instead of transactional query to avoid timeouts
        const savedCount = user?.savedSubmissionsCount ?? 0;
        const cost = 2000 + Math.floor(savedCount / 3) * 200;

        if (currentMerit < cost) {
          throw new Error("Not enough Merit.");
        }

        const subRef = db.collection("submissions").doc(submissionId);
        const subSnap = await tx.get(subRef);

        if (!subSnap.exists) {
          throw new Error("Submission not found.");
        }

        const subData = subSnap.data();
        if (subData?.authorId !== uid) {
          throw new Error("You are not the author.");
        }

        if (subData?.isSaved === true) {
          throw new Error("Submission is already saved.");
        }

        meritDelta = -cost;
        reason = "SAVE_SUBMISSION";
        updatedFields = {
          merit: currentMerit - cost,
          savedSubmissionsCount: FieldValue.increment(1),
        };
        tx.update(userRef, updatedFields);

        tx.update(subRef, {
          isSaved: true,
        });
        break;
      }

      case "ENTER_RANKED": {
        const startOfTodayMs = new Date();
        startOfTodayMs.setUTCHours(0, 0, 0, 0);

        // Transactional queries are expensive. ENTER_RANKED limit check
        // could be further optimized by moving it to a daily counter on the user doc,
        // but for now we keep it and ensure it's the only query.
        let submissionsToday = 0;
        try {
          const todayRankedSnap = await tx.get(db.collection("submissions")
            .where("authorId", "==", uid)
            .where("playmode", "==", "RANKED")
            .where("timestamp", ">=", startOfTodayMs.getTime()));
          submissionsToday = todayRankedSnap.size;
        } catch (indexErr) {
          console.warn("applyMeritAction: daily ranked count query failed", indexErr);
        }

        if (submissionsToday >= 5) {
          throw new Error("Daily ranked limit reached (5). Return tomorrow.");
        }

        const cost = calculateRankedEntryCost(
          rankedWinStreak,
          rankedLossStreak,
          reputation
        );

        if (currentMerit < cost) {
          throw new Error("Not enough Merit.");
        }

        const rankedSessionStartedAt = Date.now();
        meritDelta = -cost;
        reason = "ENTER_RANKED";
        updatedFields = {
          merit: currentMerit - cost,
          currentlyInRanked: true,
          rankedSessionStartedAt: rankedSessionStartedAt,
          rankedLossStreak: rankedLossStreak,
          rankedWinStreak: rankedWinStreak,
        };
        tx.update(userRef, updatedFields);
        break;
      }

      case "ABANDON_RANKED": {
        if (!user?.currentlyInRanked) {
          throw new Error("No active ranked session to abandon.");
        }

        updatedFields = {
          currentlyInRanked: false,
          rankedSessionStartedAt: null,
        };
        tx.update(userRef, updatedFields);
        break;
      }

      case "CHANGE_USERNAME": {
        const cost = 1000;
        const rawNewUsername = String(request.data?.newUsername ?? "").trim();
        const normalizedNewUsername = rawNewUsername.toLowerCase();

        const validationError = validateUsername(rawNewUsername);
        if (validationError) {
          throw new Error(validationError);
        }

        const oldUsername = String(user?.name ?? "").trim();
        const normalizedOldUsername = oldUsername.toLowerCase();

        if (!oldUsername) {
          throw new Error("Current username not found.");
        }

        if (normalizedOldUsername === normalizedNewUsername) {
          throw new Error("That is already your username.");
        }

        if (currentMerit < cost) {
          throw new Error("Not enough Merit.");
        }

        const oldUsernameRef = db.collection("usernames")
          .doc(normalizedOldUsername);
        const newUsernameRef = db.collection("usernames")
          .doc(normalizedNewUsername);
        const newUsernameSnap = await tx.get(newUsernameRef);

        if (newUsernameSnap.exists) {
          throw new Error("That username is already taken.");
        }

        tx.delete(oldUsernameRef);

        tx.set(newUsernameRef, {
          userId: uid,
          username: rawNewUsername,
          normalized: normalizedNewUsername,
          createdAt: Date.now(),
        });

        meritDelta = -cost;
        reason = "CHANGE_USERNAME";
        updatedFields = {
          name: rawNewUsername,
          merit: currentMerit - cost,
        };
        tx.update(userRef, updatedFields);
        break;
      }

      case "REWARD_PRACTICE":
      case "REWARD_RANKED": {
        throw new Error("Action must be applied by backend flow.");
      }

      default:
        throw new Error("Unsupported action.");
      }

      if (meritDelta !== 0) {
        const txRef = userRef.collection("meritTransactions").doc();
        tx.set(txRef, {
          amount: meritDelta,
          reason: reason,
          timestamp: Date.now(),
          balanceAfter: currentMerit + meritDelta,
        });
      }
    }).catch((err) => {
      throw new HttpsError("failed-precondition", err.message);
    });

    return {
      success: true,
      updatedFields,
    };
  }
);
