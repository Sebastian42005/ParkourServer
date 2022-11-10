import {Role} from "../enums/role.enum";

export interface User {
  username: string;
  role: Role;
  token: string;
  tokenExpiresAt: string;
}

export class AdminUser {
  static token: string;
}
