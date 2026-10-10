import {User} from "../../domain/model/models";
export interface UserDto { name?: string; email?: string; merit?: number; rating?: number; seasonIndex?: number; accountClosed?: boolean }
export function mapUser(id: string, dto: UserDto): User {
  return {id, name: dto.name || "Writer", email: dto.email ?? null, merit: dto.merit ?? 0,
    rating: dto.rating ?? 0, seasonIndex: dto.seasonIndex ?? Number.MIN_SAFE_INTEGER};
}
