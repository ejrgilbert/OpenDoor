import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import {GlobalsProvider} from "../globals/globals";
import {AuthenticationProvider} from "../authentication/authentication";

/*
  Generated class for the ManageConnectionsProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class ManageConnectionsProvider {

  constructor(public http: Http, public globals: GlobalsProvider, public authentication: AuthenticationProvider) {
    console.log('Hello ManageConnectionsProvider Provider');
  }

  // =============================================================
  // ================== Friend Request Methods ===================
  // =============================================================

  sendFriendRequest(id) {
    return this.http.put(`${this.globals.apiUrl}/friend/sendRequest/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  acceptFriendRequest(id) {
    return this.http.put(`${this.globals.apiUrl}/friend/acceptRequest/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  denyFriendRequest(id) {
    return this.http.put(`${this.globals.apiUrl}/friend/denyRequest/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  // =============================================================
  // ======================= Block Methods =======================
  // =============================================================

  blockUser(id) {
    return this.http.put(`${this.globals.apiUrl}/friend/block/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }

  unblockUser(id) {
    return this.http.put(`${this.globals.apiUrl}/friend/unblock/${id}`, {},
      {headers: this.authentication.getDefaultHeaders()});
  }
}
