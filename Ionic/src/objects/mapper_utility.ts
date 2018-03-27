import {Injectable} from "@angular/core";
import { Response } from '@angular/http';

import {User} from "./user";
import {Availability} from "./availability";
import {Friend} from "./friend";
import {Group} from "./group";
import {ConnectedUser} from "./user_connected";

@Injectable()
export class MapperUtility {

  constructor() {
    console.log('Hello Mapper Utility');
  }

  mapAvailabilities(response: Response): User[] {
    return response.json().results.map(this.toAvailability);
  }

  toAvailability(a: any): Availability {
    if (a == null || a == "" || a._body == "") return null;

    if (a._body != undefined) {
      a = a.json();
    }

    return <Availability>({
      location: a.location,
      activity: a.activity
    });
  }

  mapFriendsResponse(response: Response): Friend[] {
    return response.json().results.map(this.toFriend);
  }

  mapFriends(friends: any): Friend[] {
    return friends.map(this.toFriend, this);
  }

  toFriend(f: any): Friend {
    let toParse = f;
    if (f._body != undefined) {
      toParse = f.json();
    }

    let friend = <Friend>({
      id: toParse.id,
      guest: this.toUser(toParse.guest),
      host: this.toUser(toParse.host),
      name: toParse.name,
      available: toParse.available
    });

    return friend;
  }

  mapGroup(response: Response): Group {
    return this.toGroup(response.json());
  }

  toGroup(g: any): Group {
    console.log(g);

    let toParse = g;
    if (g._body != undefined) {
      toParse = g.json();
    }

    let group = <Group>({
      id: toParse.id,
      name: toParse.name,
      // TODO this will probably fail b/c we will probs need to iterate through an object, not 'Response'
      friends: this.mapFriends(toParse.friends)
    });

    return group;
  }

  mapUserConnected(response: Response): ConnectedUser {
    return this.toUserConnected(response.json());
  }

  mapUsersConnected(response: Response): ConnectedUser[] {
    return response.json().result.map(this.toUserConnected);
  }

  toUserConnected(u: any): ConnectedUser {
    if (u._body == "") return null;

    let toParse = u;
    if (u._body != undefined) {
      toParse = u.json();
    }

    let user = <ConnectedUser>({
      id: toParse.id,
      firstName: toParse.firstName,
      lastName: toParse.lastName,
      username: toParse.username,
      status: toParse.status
    })

    return user;
  }

  mapUsers(response: Response): User[] {
    return response.json().map(this.toUser, this);
  }

  toUser(u: any): User {
    if (u._body == "") return null;

    let toParse = u;
    if (u._body != undefined) {
      toParse = u.json();
    }

    let user = <User>({
      id: toParse.id,
      firstName: toParse.firstName,
      lastName: toParse.lastName,
      availability: this.toAvailability(toParse.availability)
    });

    return user;
  }
}
