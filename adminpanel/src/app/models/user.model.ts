import {Role} from "../enums/role.enum";

export interface User {
  username: string;
  password: string;
  role: Role;
  token: string;
}
