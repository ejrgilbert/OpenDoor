import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import 'rxjs/add/operator/map';

import {GlobalsProvider} from "../globals/globals";

/*
  Generated class for the AuthenticationProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class AuthenticationProvider {

  // Flag for whether or not the user is authenticated
  authenticated: boolean = false;

  // Currently logged in user's username TODO THIS IS BAD!!!
  credentials: {
    username: string,
    password: string
  }

  constructor( public http: Http, public globals: GlobalsProvider ) {
    console.log('Hello AuthenticationProvider Provider');
    this.credentials = {
      username: "",
      password: ""
    }
  }

  isAuthenticated() {
    return this.authenticated;
  }

  login(creds) {
    let headers = new Headers();
    creds ?
      headers.append("Authorization", "Basic " + btoa(creds.username + ":" + creds.password))
      : {};
    headers.set('X-Requested-With', 'XMLHttpRequest');

    return this.http.get(`${this.globals.apiUrl}/user/login`, {headers: headers, withCredentials: true})
      .map((response: Response) => {
        console.log("Logged in!");
        console.log(response);
        this.credentials.username = response.json().principal.username;
        this.credentials.password = creds.password;

        this.authenticated = !!response.json().name; // https://stackoverflow.com/questions/7452720/what-does-the-double-exclamation-operator-mean
      });
  }

  logout() {
    return this.http.post(`${this.globals.apiUrl}/logout`, {})
      .map(() => {
          this.authenticated = false;
      });
  }

  getDefaultHeaders(): Headers {
    let headers = new Headers();
    headers.append("Authorization", "Basic " +
      btoa(this.credentials.username + ":" + this.credentials.password));

    return headers;
  }

}
