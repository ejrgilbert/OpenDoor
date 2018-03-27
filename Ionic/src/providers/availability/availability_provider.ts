import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import {Door} from "../../objects/door";
import {Availability} from "../../objects/availability";
import {GlobalsProvider} from "../globals/globals";
import {Observable} from "rxjs/Observable";
import {User} from "../../objects/user";
import {MapperUtility} from "../../objects/mapper_utility";
import {AuthenticationProvider} from "../authentication/authentication";

/*
  Generated class for the AvailabilityProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class AvailabilityProvider {

  constructor(public http: Http, public globals: GlobalsProvider,
              public mapper: MapperUtility, public authentication: AuthenticationProvider) {
    console.log('Hello AvailabilityProvider Provider');
  }

  closeDoor() {
    return this.http.put(`${this.globals.apiUrl}/door/close`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  /**
   * Expected format:
   * {
   *   'availability' : {
   *     'location' : <location>,
   *     'activity' : <activity>
   *   }
   *   'groupNames' : [
   *     <name1>,
   *     <name2>,
   *     ...
   *   ]
   * }
   * @param door
   */
  openDoor(door: Door) {
    return this.http.put(`${this.globals.apiUrl}/door/open`, door,
      {headers: this.authentication.getDefaultHeaders()});
  }

  /**
   * Expected format:
   * {
   *     'location' : <location>,
   *     'activity' : <activity>
   * }
   * @param availability
   */
  updateDoor(availability: Availability) {
    return this.http.put(`${this.globals.apiUrl}/door/update`, availability,
      {headers: this.authentication.getDefaultHeaders()});
  }

  getDoor() {
    return this.http.get(`${this.globals.apiUrl}/door`,
      {headers: this.authentication.getDefaultHeaders()})
      .map(this.mapper.toAvailability)
  }

  getAllAvailableFriends(): Observable<User[]> {
    return this.http.get(`${this.globals.apiUrl}/door/allAvailable`,
      {headers: this.authentication.getDefaultHeaders()})
      .map(this.mapper.mapUsers, this.mapper);
  }


}
