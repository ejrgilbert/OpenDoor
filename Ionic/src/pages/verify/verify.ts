import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the VerifyPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@Component({
  selector: 'page-verify',
  templateUrl: 'verify.html',
})
export class VerifyPage {

  account: Account;
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.account = this.navParams.get('account');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad VerifyPage');
  }

}
