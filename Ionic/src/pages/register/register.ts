import { Component } from '@angular/core';
import {VerifyPage} from "../verify/verify";
import {IonicPage, NavController, NavParams, ToastController} from 'ionic-angular';
import {AccountProvider} from "../../providers/account/account_provider";
import {Account} from "../../objects/account";
import {isUndefined} from "ionic-angular/util/util";

/**
 * Generated class for the RegisterPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register',
  templateUrl: 'register.html',
})
export class RegisterPage {
  account: Account;

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public accountProvider: AccountProvider, public toastCtrl: ToastController, ) {
    this.account = <Account>({
      email: '',
      username: '',
      password: '',
      firstName: '',
      lastName: ''
    });
  }

  //Go to page telling user to verify their email
  verifyEmail() {
    this.accountProvider.register(this.account).subscribe( () => {
      this.navCtrl.push(VerifyPage, {account: this.account});
    }, (err) => {
      err = err.json();
      console.log(err);
      let msg: string = '';
      if (!isUndefined(err.errors)) {
        msg = err.errors[0].defaultMessage;
      } else if (!isUndefined(err.errorMessage)) {
        msg = err.errorMessage;

        // Only get the first error (comma-delimited)
        if (msg.includes(',')) {
          msg = msg.split(',')[0];
        }
      } else {
        msg = 'An error occurred :/'
      }

      //pop up that lets user know
      let toast = this.toastCtrl.create({
        message: msg,
        duration: 3000,
        position: "bottom"
      });
      toast.present();
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');
  }

}
