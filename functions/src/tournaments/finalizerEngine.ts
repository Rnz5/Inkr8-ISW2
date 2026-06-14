import {onDocumentUpdated} from "firebase-functions/v2/firestore";

export const tournamentFinalizerEngine = onDocumentUpdated(
  {
    document: "tournaments/{tournamentId}",
    region: "us-central1",
  },
  async (event) => {
    const before = event.data?.before.data();
    const after = event.data?.after.data();

    if (!before || !after) return;

    if (before.status !== "COMPLETED" && after.status === "COMPLETED") {
      console.log(`[finalizerEngine] Tournament ${event.params.tournamentId} completed. Stats already updated by evaluationEngine.`);
    }
  }
);
