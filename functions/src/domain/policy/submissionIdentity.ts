import {DomainError} from "../model/models";
import {SubmissionIdentity} from "../repository/contracts";

export const submissionIdentity: SubmissionIdentity = Object.freeze({
  resolve(value: unknown): string {
    if (typeof value !== "string" || !/^[a-zA-Z0-9-]{20,80}$/.test(value)) {
      throw new DomainError("invalid-argument", "ID de partida inválido.");
    }
    return value;
  },
});
