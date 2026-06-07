import {onDocumentUpdated, onDocumentDeleted} from "firebase-functions/v2/firestore";
import {db, FieldValue} from "../firebase/admin";

/**
 * Trigger to decrement savedSubmissionsCount when a submission is "un-saved"
 */
export const onSubmissionUnsaved = onDocumentUpdated("submissions/{submissionId}", async (event) => {
  const before = event.data?.before.data();
  const after = event.data?.after.data();

  if (!before || !after) return;

  // If isSaved changed from true to false
  if (before.isSaved === true && after.isSaved === false) {
    const authorId = after.authorId;
    if (authorId) {
      await db.collection("users").doc(authorId).update({
        savedSubmissionsCount: FieldValue.increment(-1),
      });
    }
  }
});

/**
 * Trigger to decrement savedSubmissionsCount if a saved submission is deleted entirely
 */
export const onSubmissionDeleted = onDocumentDeleted("submissions/{submissionId}", async (event) => {
  const data = event.data?.data();
  if (!data) return;

  if (data.isSaved === true) {
    const authorId = data.authorId;
    if (authorId) {
      await db.collection("users").doc(authorId).update({
        savedSubmissionsCount: FieldValue.increment(-1),
      });
    }
  }
});
