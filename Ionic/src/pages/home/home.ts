import { Component } from '@angular/core';
import { NavController, NavParams, AlertController } from 'ionic-angular';
import { ProfilePage } from '../profile/profile';
import { DoorPage } from '../door/door';
import { GlobalsProvider} from "../../providers/globals/globals";
import {AvailabilityProvider} from "../../providers/availability/availability_provider";
import {User} from "../../objects/user";
import {ProfilePicturesProvider} from "../../providers/profile-pictures/profile-pictures";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  selectedItem: any;
  //icons: string[];
  activities: string[];
  items: Array<{title: string, username: string, activity: string, icon: string}>;
  currentItems: any = [];

  searchTerm: string;

  masterFriends: User[];
  filteredFriends: User[];

  constructor(public navCtrl: NavController, public navParams: NavParams, public alertCtrl: AlertController,
              public globals: GlobalsProvider, public availabilityProvider: AvailabilityProvider,
              public profilePic: ProfilePicturesProvider) {
    this.availabilityProvider.getAllAvailableFriends().subscribe(
      (a) => {
        this.masterFriends = a;
        this.filteredFriends = a;
      }
    );

    // If we navigated to this page, we will have an item available as a nav param
    this.selectedItem = navParams.get('item');
    this.activities = ['Chilling', 'Studying', 'Watching TV', 'Eating Lunch'];


    this.items = [];
    for (let i = 1; i < 5; i++) {
      this.items.push({
        title: 'Friend ' + i,
        username: 'username' + i,
        activity: this.activities[Math.floor(Math.random() * this.activities.length)],
        icon: 'person' //this should be the user's avatar
      });
    }
  }

  hasFriends(){
    return this.filteredFriends == undefined ||this.filteredFriends.length != 0;
  }

  itemTapped(friend) {
    let alert = this.alertCtrl.create({
      title: `${friend.firstName} ${friend.lastName}`,
      message: `Activity: ${friend.availability.activity} <br> Location: ${friend.availability.location}`,
      buttons: ['OK']
    });
    alert.present();
  }

  getProfilePicture(user: User): string {
    return this.profilePic.getLinkByFirstName(user.firstName);
  }

  // search bar input
  onInput(){
    this.filteredFriends = this.masterFriends.filter((friend) => {
      return `${friend.firstName} ${friend.lastName}`.toLowerCase().indexOf(this.searchTerm.toLowerCase()) != -1;
    });
  }

  //search bar cancelled
  onCancel(){
    this.searchTerm = "";
    this.filteredFriends = this.masterFriends;
  }

  //Navigate to the profile page
  openProfile(){
    this.navCtrl.push(ProfilePage);
  }

  //Navigate to manage door page
  manageDoor(){
    this.navCtrl.push(DoorPage);
  }

  isOpen(){
    return this.globals.doorIsOpen;
  }

}
