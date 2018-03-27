import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import {GlobalsProvider} from "../globals/globals";

import {Account} from "../../objects/account";
import {AuthenticationProvider} from "../authentication/authentication";

/*
  Generated class for the AccountProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class AccountProvider {

  constructor(public http: Http, public globals: GlobalsProvider, public authentication: AuthenticationProvider) {
    console.log('Hello AccountProvider Provider');
  }

  /**
   * @param account
   */
  register(account: Account) {
    return this.http.post(`${this.globals.apiUrl}/register`, account);
  }

  /**
   * @param newPassword
   */
  resetPassword(newPassword: string) {
    return this.http.post(`${this.globals.apiUrl}/resetPassword`, { password: newPassword });
  }

}
