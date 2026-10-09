export function calculateDynamicRatingChange(
  myRating: number,
  opponentRating: number,
  outcome: "WIN" | "LOSS" | "DRAW"
): number {
  const ratingGap = opponentRating - myRating;

  const gapAdjustment = Math.max(-5, Math.min(5, ratingGap * 0.05));

  if (outcome === "WIN") {
    let winChange = Math.round(4 + gapAdjustment);

    if (myRating >= 180) {
      winChange = Math.min(winChange, 2);
    } else if (myRating >= 150) {
      winChange = Math.min(winChange, 3);
    }

    return Math.max(1, winChange);
  } else if (outcome === "LOSS") {
    return Math.min(-1, Math.round(-6 + gapAdjustment));
  } else {
    return 1;
  }
}
