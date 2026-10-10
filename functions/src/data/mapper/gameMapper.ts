import {Game} from "../../domain/model/models";
import {DocumentSnapshot} from "firebase-admin/firestore";
import {DomainError} from "../../domain/model/models";
export function mapGame(snapshot: DocumentSnapshot): Game {
  if (!snapshot.exists) throw new DomainError("not-found", "Partida no encontrada.");
  return {...snapshot.data(), id: snapshot.id} as Game;
}
