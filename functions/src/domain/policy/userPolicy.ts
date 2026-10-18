import {DomainError} from "../model/models";

export function validateUserName(name: string): string {
  if (name.trim().length < 2 || name.length > 20 || /[\p{C}]/u.test(name)) {
    throw new DomainError("invalid-argument", "El nombre debe tener entre 2 y 20 caracteres visibles.");
  }
  return name;
}
