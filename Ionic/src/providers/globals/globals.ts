import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the GlobalsProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class GlobalsProvider {

  apiUrl = 'http://localhost:8080';
  doorIsOpen: boolean = false;

  constructor(public http: Http) {
    console.log('Hello GlobalsProvider Provider');
  }

}
