import {Friend} from "./friend";

export interface Group {
  id: number,
  name: string,
  friends: Friend[]
}
