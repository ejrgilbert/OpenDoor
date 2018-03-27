import {Availability} from "./availability";

export interface User {
  id: number,
  firstName: string,
  lastName: string,
  availability: Availability
}
