import {HttpsError, onCall, CallableRequest} from "firebase-functions/v2/https";
import {DomainError} from "../../domain/model/models";
import {UserRepository} from "../../domain/repository/contracts";
import {logger} from "firebase-functions";
export function authenticated(users: UserRepository, handler: (uid: string, data: Record<string, unknown>, request: CallableRequest) => Promise<unknown>) {
  return onCall({region: "us-central1"}, async (request) => {
    if (!request.auth) throw new HttpsError("unauthenticated", "Inicia sesión para continuar.");
    try {
      await users.assertOpen(request.auth.uid);
      return await handler(request.auth.uid, request.data && typeof request.data === "object" ? request.data : {}, request);
    } catch (error) {
      if (error instanceof DomainError) throw new HttpsError(error.code, error.message);
      if (error instanceof HttpsError) throw error;
      logger.error("Callable failed", error instanceof Error ? error.message : "Unknown error");
      throw new HttpsError("internal", "No se pudo completar la operación. Reintenta.");
    }
  });
}
export function gameId(value: unknown): string {
  if (typeof value !== "string" || !/^[a-zA-Z0-9-]{20,80}$/.test(value)) throw new DomainError("invalid-argument", "ID de partida inválido.");
  return value;
}
