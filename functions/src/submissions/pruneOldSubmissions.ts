import {db} from "../firebase/admin";

/**
 * Prunes old unsaved submissions for a specific author. Keeps only the 10 most recent unsaved submissions
 *
 * @param authorId The ID of the author whose submissions should be pruned.
 */
export async function pruneOldSubmissions(authorId: string) {
  const MAX_ARCHIVE_SIZE = 10;
  const dbRef = db;

  try {
    const snapshot = await dbRef.collection("submissions")
      .where("authorId", "==", authorId)
      .where("isSaved", "==", false)
      .orderBy("timestamp", "desc")
      .limit(100)
      .get();

    if (snapshot.size <= MAX_ARCHIVE_SIZE) {
      return;
    }

    const docsToDelete = snapshot.docs.slice(MAX_ARCHIVE_SIZE);

    const batch = dbRef.batch();
    docsToDelete.forEach((doc) => {
      batch.delete(doc.ref);
    });

    await batch.commit();
    console.log(`[Maintenance] Successfully pruned ${docsToDelete.length} old archive entries for user ${authorId}`);
  } catch (error: unknown) {
    const err = error as { code?: number; message?: string };

    if (err.code === 9 || (err.message && err.message.includes("index"))) {
      console.error(`[INDEX_REQUIRED] pruneOldSubmissions failed for ${authorId}. ` +
        "Ensure composite index (authorId ASC, isSaved ASC, timestamp DESC) exists in firestore.indexes.json.");
    } else {
      console.error(`[Maintenance Error] Failed to prune submissions for ${authorId}:`, error);
    }
  }
}
