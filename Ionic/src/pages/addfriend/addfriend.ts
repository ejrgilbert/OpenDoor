import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import {UserProvider} from "../../providers/user/user_provider";
import {ConnectedUser} from "../../objects/user_connected";
import {ManageConnectionsProvider} from "../../providers/manage-connections/manage-connections";
import {ProfilePicturesProvider} from "../../providers/profile-pictures/profile-pictures";

/**
 * Generated class for the AddfriendPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-addfriend',
  templateUrl: 'addfriend.html',
})
export class AddfriendPage {

  searchTerm: string;
  result: ConnectedUser;

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public userProvider: UserProvider, public connectionProvider: ManageConnectionsProvider,
              public profilePic: ProfilePicturesProvider) {
    this.result == null;
  }

  //search bar input
  onInput(){
    this.findUser();
    if(this.result != null){
      console.log(this.result.status);
    }
  }

  //search bar cancelled
  onCancel(){
    this.searchTerm = "";
    this.result = null;
  }

  findUser(){
    if (this.searchTerm != null && this.searchTerm != "") {
      this.userProvider.findByUsername(this.searchTerm).subscribe(
        (r) => {
          this.result = r;
        }
      );
    } else {
      this.result = null;
    }
  }

  getProfilePicture(): string {
    return this.profilePic.getLinkByUsername(this.result.username);
  }

  addFriend(){
    this.connectionProvider.sendFriendRequest(this.result.id).subscribe(()=>{
      this.findUser();
    });
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AddfriendPage');
  }

}
