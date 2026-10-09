import OpenAI from "openai";
import {defineSecret} from "firebase-functions/params";

export const OPENAI_API_KEY = defineSecret("OPENAI_API_KEY");

export type R8EvaluationResult = {
  finalScore: number;
  feedback: string;
  source?: "real" | "mock";
};

export type EvaluateParams = {
  apiKey: string;
  content: string;
  gamemode: string;
  requiredWords: string[];
  themeName: string | null;
  topicName: string | null;
  submissionsToday?: number;
  currentStreak?: number;
  recentScores?: number[];
};

const SYSTEM_PROMPT = `You are R8, evaluator of Inkr8. Metallic, confident, sarcastic, witty — never hateful or personal. Mock weakness intelligently. Praise sparingly and precisely. Judge HOW it's written, not WHAT it says.

Evaluation criteria (in order of weight):
grammar, coherence, vocabulary quality, required word usage, creativity, metaphor/idiom richness, depth, structure

Scoring:
- Score: 0.00–100.00. Passing: 60.00
- Most paragraphs: 50–75. Above 85: rare and earned. Above 95: exceptional. 100: near-mythical
- Penalize: spam, repetition, nonsense, missing or forced required words
- Reward: elegant, clear, precise, creative, well-structured writing
- Generic but grammatically correct = not high. Safe = not impressive

Feedback:
- 2–4 sentences. Sarcastic but useful
- High score: name what was strong, name what blocked perfection
- Low score: be severe, still constructive

Return ONLY valid JSON:
{"finalScore": 67.12, "feedback": "string"}

You will sometimes receive a CONTEXT block after the submission. If and only if the context contains something genuinely interesting (a long streak, a clear improvement pattern, a suspiciously high submission count for one day), append a single line at the end of your feedback wrapped in parentheses, written in R8's voice: cold, precise, occasionally sarcastic. This line must comment on behavior or pattern, never on writing quality. If there is nothing interesting in the context, omit this line entirely. Do not force it.`;

function buildEvaluationPrompt(params: Omit<EvaluateParams, "apiKey">) {
  const {content, gamemode, requiredWords, themeName, topicName} = params;
  const contextLines = [
    `Gamemode: ${gamemode}`,
    requiredWords.length > 0 ? `Required words: ${requiredWords.join(", ")}` : null,
    themeName ? `Theme: ${themeName}` : null,
    topicName ? `Topic: ${topicName}` : null,
  ].filter(Boolean).join("\n");

  let contextBlock = "";
  if (params.submissionsToday !== undefined || params.currentStreak !== undefined || params.recentScores !== undefined) {
    contextBlock += "\n\nCONTEXT:";
    if (params.submissionsToday !== undefined) {
      contextBlock += `\n- Submissions today: ${params.submissionsToday}`;
    }
    if (params.currentStreak !== undefined && params.currentStreak > 1) {
      contextBlock += `\n- Current streak: ${params.currentStreak} consecutive days`;
    }
    if (params.recentScores !== undefined && params.recentScores.length >= 3) {
      const last3 = params.recentScores.slice(-3);
      contextBlock += `\n- Last 3 scores: ${last3.map((s) => s.toFixed(1)).join(", ")}`;
    }
  }

  return `${contextLines}\n\nParagraph:\n"""${content}"""${contextBlock}`;
}

export async function evaluateWithR8(params: EvaluateParams): Promise<R8EvaluationResult> {
  try {
    const client = new OpenAI({
      apiKey: params.apiKey,
    });

    const response = await client.chat.completions.create({
      model: "gpt-4o-mini",
      temperature: 0.3,
      response_format: {type: "json_object"},
      messages: [
        {role: "system", content: SYSTEM_PROMPT},
        {
          role: "user",
          content: buildEvaluationPrompt({
            content: params.content,
            gamemode: params.gamemode,
            requiredWords: params.requiredWords,
            themeName: params.themeName,
            topicName: params.topicName,
            submissionsToday: params.submissionsToday,
            currentStreak: params.currentStreak,
            recentScores: params.recentScores,
          }),
        },
      ],
    });

    const raw = response.choices[0]?.message?.content ?? "{}";

    let parsed: Partial<R8EvaluationResult> = {};

    try {
      parsed = JSON.parse(raw);
    } catch (error) {
      console.error("JSON parse failed → fallback to mock", error);

      return {
        ...evaluateWithMockR8({
          content: params.content,
          requiredWords: params.requiredWords,
        }),
        source: "mock",
      };
    }

    const finalScoreRaw = Number(parsed.finalScore ?? 0);
    const finalScore = Math.min(100, Math.max(0,
      Number(finalScoreRaw.toFixed(2))));

    const feedback =
      typeof parsed.feedback === "string" && parsed.feedback.trim().length > 0 ?
        parsed.feedback.trim() :
        "R8 stared at this paragraph in disappointment and returned nothing.";

    return {
      finalScore,
      feedback,
      source: "real",
    };
  } catch (error) {
    console.error("OpenAI failed → fallback to mock", error);

    return {
      ...evaluateWithMockR8({
        content: params.content,
        requiredWords: params.requiredWords,
      }),
      source: "mock",
    };
  }
}

function buildMockFeedback(params: {
  finalScore: number;
  missingWordsCount: number;
  content: string;
}): string {
  const {finalScore, missingWordsCount, content} = params;

  const nonsenseSignals = ["asd", "hshs", "idkdk", "uhs", "jdjd"];
  const lowered = content.toLowerCase();
  const looksMessy = nonsenseSignals.some((s) => lowered.includes(s));

  if (looksMessy || finalScore < 35) {
    return `R8 read this, regretted it, and still
    decided to be generous.
    The structure collapses, the phrasing wanders,
    and whatever idea tried
    to exist here never fully arrived. Try again
    with intention this time.`;
  }

  if (finalScore < 60) {
    return `
    There is a paragraph here, technically, but it
    leans more on survival
     than precision. Some sentences hold together,
     yet the writing lacks control,
     and the required words did not all feel earned.
     R8 expects more discipline than this.`;
  }

  if (finalScore < 80) {
    return `This was serviceable, which is not the
    same as impressive. The structure
     is mostly clear and the language works, but
     the paragraph still lacks the sharpness
      and elegance that would make R8 pause in
      actual respect.`;
  }

  if (missingWordsCount > 0) {
    return `You wrote with more control than most,
    which is refreshing, but leaving
     required words behind cost you. Strong writing
     can survive many things, but
      ignoring part of the challenge is still a flaw
      R8 will happily remember.`;
  }

  return `At last, something that resembles intention.
  The paragraph is
   controlled, coherent, and written with enough
   confidence to keep R8 interested.
    It still is not perfection, but unlike most entries,
    this one was worth finishing.`;
}

function evaluateWithMockR8(params: {
  content: string;
  requiredWords: string[];
}): R8EvaluationResult {
  const text = params.content.trim();
  const wordCount = text.length === 0 ? 0 : text.split(/\s+/).length;

  const lowered = text.toLowerCase();
  const usedCount = params.requiredWords.filter((w) =>
    lowered.split(/\W+/).includes(w.toLowerCase())
  ).length;

  const missingWordsCount = Math.max(0,
    params.requiredWords.length - usedCount);

  let score = 52;

  if (wordCount >= 50) score += 8;
  if (wordCount >= 90) score += 6;
  if (wordCount >= 120) score += 4;

  score -= missingWordsCount * 7;

  if (text.length > 0) {
    const sentenceCount = text.split(/[.!?]+/).filter((s) =>
      s.trim().length > 0).length;
    if (sentenceCount >= 3) score += 4;
  }

  const hasComma = text.includes(",");
  const hasSemicolon = text.includes(";");
  const hasMetaphorishSignal =
    lowered.includes("like a ") ||
    lowered.includes("as if") ||
    lowered.includes("as though");

  if (hasComma) score += 2;
  if (hasSemicolon) score += 2;
  if (hasMetaphorishSignal) score += 4;

  const nonsenseSignals = ["asd", "hshs", "idkdk", "uhs", "jdjd"];
  if (nonsenseSignals.some((s) => lowered.includes(s))) {
    score -= 30;
  }

  score = Math.max(0, Math.min(100, Number(score.toFixed(2))));

  return {
    finalScore: score,
    feedback: buildMockFeedback({
      finalScore: score,
      missingWordsCount,
      content: text,
    }),
  };
}
