import {Availability} from "./availability";

export interface Door {
  availability: Availability,

  // This part is optional
  groupNames?: string[];
}
