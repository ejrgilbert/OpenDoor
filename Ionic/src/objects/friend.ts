import {User} from "./user";

export interface Friend {
  id: number,
  guest: User,
  host: User,
  name: string,
  available: boolean
}
