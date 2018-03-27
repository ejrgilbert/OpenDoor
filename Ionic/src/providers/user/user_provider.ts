import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import {GlobalsProvider} from "../globals/globals";
import {MapperUtility} from "../../objects/mapper_utility";
import {AuthenticationProvider} from "../authentication/authentication";

/*
  Generated class for the UserProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class UserProvider {

  constructor(public http: Http, public mapper: MapperUtility, public globals: GlobalsProvider, public authentication: AuthenticationProvider) {
    console.log('Hello UserProvider Provider');
  }

  findByUsername(username) {
    return this.http.get(`${this.globals.apiUrl}/user/search/${username}`,
      {headers: this.authentication.getDefaultHeaders()})
      .map(this.mapper.toUserConnected);
  }

  disableUser(id) {
    return this.http.put(`${this.globals.apiUrl}/user/disable/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  enableUser(id) {
    return this.http.put(`${this.globals.apiUrl}/user/enable/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

}
