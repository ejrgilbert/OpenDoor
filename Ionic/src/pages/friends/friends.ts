import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, AlertController } from 'ionic-angular';
import {AddfriendPage} from "../addfriend/addfriend";
import {GroupProvider} from "../../providers/group/group_provider";
import {Friend} from "../../objects/friend";
import {ProfilePicturesProvider} from "../../providers/profile-pictures/profile-pictures";

/**
 * Generated class for the FriendsPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-friends',
  templateUrl: 'friends.html',
})
export class FriendsPage {

  searchTerm: string = "";
  masterFriends: Friend[];
  filteredFriends: Friend[];

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public groupProvider: GroupProvider, public alertCtrl: AlertController,
              public profilePic: ProfilePicturesProvider) {
    groupProvider.getAllFriends().subscribe(
      (g) => {
        this.masterFriends = g.friends;
        this.filteredFriends = g.friends;
      });
  }

  getProfilePicture(friend: Friend): string {
    return this.profilePic.getLinkByFullName(friend.name);
  }

  // search bar input
  onInput(){
    this.filteredFriends = this.masterFriends.filter((friend) => {
      return friend.name.toLowerCase().indexOf(this.searchTerm.toLowerCase()) != -1;
    });
  }

  //search bar cancelled
  onCancel(){
    this.searchTerm = "";
    this.filteredFriends = this.masterFriends;
  }

  hasFriends(): boolean {
    return this.masterFriends == undefined || this.masterFriends.length != 0;
  }

  addFriend(){
    this.navCtrl.push(AddfriendPage);
  }


  removeFriend(friend){
    let alert = this.alertCtrl.create({
      title: 'Confirm Remove',
      message:`Are you sure you want to remove ${friend.name}? <br><br> <b>Under development, not functional!</b>`,
      buttons: [
        {
          text: 'Cancel'
        },
        {
          text: 'Remove',
          handler: () => {
            console.log('remove confirmed');
            //TODO http call
          }
        }
      ]
    });
    alert.present();
  }

  itemTapped(friend) {
    let alert = this.alertCtrl.create({
      title: `${friend.name}`,
      // message: `Do you want to remove ${friend.firstName} ${friend.lastName} as a friend?`,
      buttons: [
        {
          text: 'Remove Friend',
          handler: () => {
            console.log('remove clicked');
            this.removeFriend(friend);
          }
        }
      ]
    });
    alert.present();
  }


  ionViewDidLoad() {
    console.log('ionViewDidLoad FriendsPage');
  }

}
