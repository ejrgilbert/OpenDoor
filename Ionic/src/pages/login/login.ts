import { Component } from '@angular/core';
import {NavController, NavParams, ToastController} from 'ionic-angular';
import { HomePage } from '../home/home';
import { RegisterPage} from "../register/register";
import { Http } from "@angular/http";

import { AuthenticationProvider } from "../../providers/authentication/authentication";
import {AvailabilityProvider} from "../../providers/availability/availability_provider";
import {GlobalsProvider} from "../../providers/globals/globals";

/**
 * Generated class for the LoginPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {
  credentials: {username: string, password: string} = {
    username: 'leslie',
    password: '@nnR0cks'
  };

  constructor(public navCtrl: NavController, public navParams: NavParams, public http: Http,
              public toastCtrl: ToastController, public authentication: AuthenticationProvider,
              public availabilityController: AvailabilityProvider, public globals: GlobalsProvider) {
  }

  //go to register page
  register() {
    this.navCtrl.push(RegisterPage);
  }

  //Login
  doLogin(){
    this.authentication.login(this.credentials).subscribe(() => {
      // Determine if door is open or closed
      this.availabilityController.getDoor().subscribe((a) => {
        this.globals.doorIsOpen = a != null;
      });

      this.navCtrl.push(HomePage);
    }, (err) => {
      console.log(err);
      //pop up that lets user know
      let toast = this.toastCtrl.create({
        message: 'Could not Login',
        duration: 3000,
        position: "bottom"
      });
      toast.present();
    });

    // let headers = new Headers();
    // headers.append("Content-Type", "application/json");
    // headers.append("Authorization", "Basic " + btoa("user:password"));
    //
    // let requestoptions = new RequestOptions({
    //   method: RequestMethod.Get,
    //   url: 'http://localhost:8080/user/login',
    //   headers: headers
    // });
    //
    // console.log(requestoptions);
    //
    // new Promise(resolve => {
    //   this.http.request(new Request(requestoptions))
    //     .map((res: Response) => {
    //       let jsonObj: any;
    //       if (res.status === 204) {
    //         jsonObj = null;
    //       }
    //       else if (res.status === 500) {
    //         jsonObj = null;
    //       }
    //       else if (res.status === 200) {
    //         jsonObj = res.json()
    //       }
    //       console.log({status: res.status, json: jsonObj});
    //     })
    //     .subscribe(res => {
    //       console.log(res);
    //       this.navCtrl.push(HomePage);
    //     }, (err) => {
    //       console.log(err);
    //         //pop up that lets user know
    //         let toast = this.toastCtrl.create({
    //           message: 'Could not Login',
    //           duration: 3000,
    //           position: "bottom"
    //         });
    //         toast.present();
    //     })
    // });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

}
