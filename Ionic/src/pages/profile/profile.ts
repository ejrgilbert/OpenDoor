import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { FriendsPage} from "../friends/friends";
import {AuthenticationProvider} from "../../providers/authentication/authentication";
import {UserProvider} from "../../providers/user/user_provider";
import {ProfilePicturesProvider} from "../../providers/profile-pictures/profile-pictures";

/**
 * Generated class for the ProfilePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html',
})
export class ProfilePage {

  //this should have info from the user provider
  user: { firstName: string, lastName: string, username: string } = {
    firstName: "",
    lastName: "",
    username: ""
  }

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public authenticator: AuthenticationProvider, public userProvider: UserProvider,
              public profilePic: ProfilePicturesProvider) {
    this.userProvider.findByUsername(authenticator.credentials.username).subscribe(
      (u) => {
        this.user.firstName = u.firstName;
        this.user.lastName = u.lastName;
        this.user.username = u.username;
      });
  }

  getProfilePicture(): string {
    return this.profilePic.getLinkByUsername(this.user.username);
  }

  manageFriends(){
    this.navCtrl.push(FriendsPage);
  }

  // logout() {
  //   this.authenticator.logout().subscribe((response) => {
  //       this.navCtrl.push(LoginPage);
  //     });
  // }

  ionViewDidLoad() {
    console.log('ionViewDidLoad ProfilePage');
  }

}
