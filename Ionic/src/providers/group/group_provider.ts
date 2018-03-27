import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import {GlobalsProvider} from "../globals/globals";
import {MapperUtility} from "../../objects/mapper_utility";
import {AuthenticationProvider} from "../authentication/authentication";

/*
  Generated class for the GroupProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class GroupProvider {

  constructor(public http: Http, public globals: GlobalsProvider,
              public mapper: MapperUtility, public authentication: AuthenticationProvider) {
    console.log('Hello GroupProvider Provider');
  }

  editFriendName(friendId, groupId, name) {
    return this.http.put(`${this.globals.apiUrl}/group/changeFriendName/${friendId}?groupId=${groupId}&name=${name}`, {},
      {headers: this.authentication.getDefaultHeaders()})
  }

  getAllFriends() {
    return this.http.get(`${this.globals.apiUrl}/group/allFriends`,
      {headers: this.authentication.getDefaultHeaders()}).map(this.mapper.toGroup, this.mapper);
  }
}
