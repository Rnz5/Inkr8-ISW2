import {onSchedule} from "firebase-functions/v2/scheduler";
import {db} from "../firebase/admin";

function getISOWeekId(date: Date): string {
  const target = new Date(date.valueOf());
  const dayNumber = (date.getUTCDay() + 6) % 7;
  target.setUTCDate(target.getUTCDate() - dayNumber + 3);
  const firstThursday = new Date(target.getUTCFullYear(), 0, 4);
  const weekNumber = 1 + Math.round(
    ((target.getTime() - firstThursday.getTime()) / 86400000 - 3 + ((firstThursday.getUTCDay() + 6) % 7)) / 7
  );
  return `${target.getUTCFullYear()}-W${String(weekNumber).padStart(2, "0")}`;
}

export const weeklyStatsSnapshot = onSchedule(
  {
    schedule: "15 1 * * 1",
    region: "us-central1",
    timeoutSeconds: 540,
    memory: "512MiB",
  },
  async () => {
    const now = new Date();
    const startOfToday = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth(), now.getUTCDate()));

    const dateIds: string[] = [];
    for (let i = 1; i <= 7; i++) {
      const d = new Date(startOfToday.getTime() - i * 24 * 60 * 60 * 1000);
      dateIds.push(d.toISOString().split("T")[0]);
    }

    const weekId = getISOWeekId(new Date(startOfToday.getTime() - 24 * 60 * 60 * 1000));

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
      const themeAverages: Record<string, { totalAvg: number; days: number }> = {};

      recordsSnap.forEach((doc) => {
        if (!doc.exists) return;
        const data = doc.data();
        if (!data) return;

        totalSubmissions += data.totalSubmissions ?? 0;
        rankedSubmissions += data.rankedSubmissions ?? 0;
        practiceSubmissions += data.practiceSubmissions ?? 0;
        totalMeritEarned += data.totalMeritEarned ?? 0;
        totalMeritSpent += data.totalMeritSpent ?? 0;

        if (typeof data.averageScore === "number" && data.totalSubmissions > 0) {
          totalScoreWeighted += data.averageScore * data.totalSubmissions;
        }

        if (data.hardestTheme && data.hardestTheme !== "N/A") {
          if (!themeAverages[data.hardestTheme]) {
            themeAverages[data.hardestTheme] = {totalAvg: 0, days: 0};
          }
          themeAverages[data.hardestTheme].days += 1;
        }
      });

      const averageScore = totalSubmissions > 0 ? Number((totalScoreWeighted / totalSubmissions).toFixed(2)) : 0;
      const netMeritChange = totalMeritEarned - totalMeritSpent;

      let hardestTheme = "N/A";
      let maxDays = 0;
      for (const theme in themeAverages) {
        if (Object.prototype.hasOwnProperty.call(themeAverages, theme)) {
          if (themeAverages[theme].days > maxDays) {
            maxDays = themeAverages[theme].days;
            hardestTheme = theme;
          }
        }
      }

      const snapshotData = {
        weekId,
        totalSubmissions,
        rankedSubmissions,
        practiceSubmissions,
        averageScore,
        totalMeritEarned,
        totalMeritSpent,
        netMeritChange,
        hardestTheme,
        createdAt: Date.now(),
      };

      await db.collection("stats").doc("weekly").collection("records").doc(weekId).set(snapshotData);

      console.log(`Weekly snapshot completed for ${weekId}:`, snapshotData);
    } catch (error) {
      console.error(`Failed to generate weekly snapshot for ${weekId}:`, error);
    }
  }
);
