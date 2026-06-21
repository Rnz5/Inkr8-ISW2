import {onSchedule} from "firebase-functions/v2/scheduler";
import {db} from "../firebase/admin";

export const monthlyStatsSnapshot = onSchedule(
  {
    schedule: "0 2 1 * *",
    region: "us-central1",
    timeoutSeconds: 540,
    memory: "512MiB",
  },
  async () => {
    const now = new Date();
    const firstOfThisMonth = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth(), 1));
    const firstOfLastMonth = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth() - 1, 1));

    const monthId = `${firstOfLastMonth.getUTCFullYear()}-${String(firstOfLastMonth.getUTCMonth() + 1).padStart(2, "0")}`;

    const dateIds: string[] = [];
    const cursor = new Date(firstOfLastMonth);
    while (cursor.getTime() < firstOfThisMonth.getTime()) {
      dateIds.push(cursor.toISOString().split("T")[0]);
      cursor.setUTCDate(cursor.getUTCDate() + 1);
    }

    try {
      const recordsSnap = await Promise.all(
        dateIds.map((id) =>
          db.collection("stats").doc("daily").collection("records").doc(id).get()
        )
      );

      let totalSubmissions = 0;
      let rankedSubmissions = 0;
      let practiceSubmissions = 0;
      let totalScoreWeighted = 0;
      let totalMeritEarned = 0;
      let totalMeritSpent = 0;
      let tournamentsCompleted = 0;
      const themeDayCounts: Record<string, number> = {};

      recordsSnap.forEach((doc) => {
        if (!doc.exists) return;
        const data = doc.data();
        if (!data) return;

        totalSubmissions += data.totalSubmissions ?? 0;
        rankedSubmissions += data.rankedSubmissions ?? 0;
        practiceSubmissions += data.practiceSubmissions ?? 0;
        totalMeritEarned += data.totalMeritEarned ?? 0;
        totalMeritSpent += data.totalMeritSpent ?? 0;
        tournamentsCompleted += data.tournamentsCompleted ?? 0;

        if (typeof data.averageScore === "number" && data.totalSubmissions > 0) {
          totalScoreWeighted += data.averageScore * data.totalSubmissions;
        }

        if (data.hardestTheme && data.hardestTheme !== "N/A") {
          themeDayCounts[data.hardestTheme] = (themeDayCounts[data.hardestTheme] ?? 0) + 1;
        }
      });

      const averageScore = totalSubmissions > 0 ? Number((totalScoreWeighted / totalSubmissions).toFixed(2)) : 0;
      const mostPlayedGamemode = rankedSubmissions >= practiceSubmissions ? "RANKED" : "PRACTICE";

      let hardestTheme = "N/A";
      let maxDays = 0;
      for (const theme in themeDayCounts) {
        if (Object.prototype.hasOwnProperty.call(themeDayCounts, theme)) {
          if (themeDayCounts[theme] > maxDays) {
            maxDays = themeDayCounts[theme];
            hardestTheme = theme;
          }
        }
      }

      const snapshotData = {
        monthId,
        totalSubmissions,
        rankedSubmissions,
        practiceSubmissions,
        averageScore,
        totalMeritEarned,
        totalMeritSpent,
        tournamentsCompleted,
        mostPlayedGamemode,
        hardestTheme,
        createdAt: Date.now(),
      };

      await db.collection("stats").doc("monthly").collection("records").doc(monthId).set(snapshotData);

      console.log(`Monthly snapshot completed for ${monthId}:`, snapshotData);
    } catch (error) {
      console.error(`Failed to generate monthly snapshot for ${monthId}:`, error);
    }
  }
);
