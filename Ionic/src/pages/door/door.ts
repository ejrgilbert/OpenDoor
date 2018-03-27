import { Component } from '@angular/core';
import {AlertController, IonicPage, NavController, NavParams, ToastController} from 'ionic-angular';
import {HomePage} from "../home/home";
import { GlobalsProvider } from '../../providers/globals/globals';
import {AvailabilityProvider} from "../../providers/availability/availability_provider";
import {Availability} from "../../objects/availability";
import {Door} from "../../objects/door";

/**
 * Generated class for the DoorPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-door',
  templateUrl: 'door.html',
})
export class DoorPage {
  activities: string[];

  door = <Door>({
    availability: <Availability> ({
      location: '',
      activity: ''
    })
  });

  constructor(public navCtrl: NavController, public navParams: NavParams,
              public alertCtrl: AlertController, public globals: GlobalsProvider,
              public availabilityController: AvailabilityProvider, public toastCtrl: ToastController) {
    this.activities = ['Chilling', 'Studying', 'Watching TV', 'Eating Lunch', 'Attending Posters & Pies'];

    this.availabilityController.getDoor().subscribe((a) => {
      if (a == null) {
        // Door is not open
        this.door.availability.location = '';
        this.door.availability.activity = '';
      } else {
        // Door is already open
        this.door.availability.location = a.location;
        this.door.availability.activity = a.activity;
      }
    });
  }

  manageDoor(){
    if(this.doorIsOpen()){
      this.updateDoor();
    }else {
      this.openDoor();
    }
  }

  //set location and activity
  openDoor(){

    this.availabilityController.openDoor(this.door).subscribe(() => {
      this.globals.doorIsOpen = true;

      console.log(`Activity: ${this.door.availability.activity} Location: ${this.door.availability.location}`);
      let alert = this.alertCtrl.create({
        title: 'Your door is open!',
        message: `Your friends will see that you are ${this.door.availability.activity} at ${this.door.availability.location}`,
        buttons: ['OK']
      });
      alert.present().then( () => {
          this.navCtrl.push(HomePage);
        }
      );
    }, (err) => {
      err = err.json();

      //pop up that lets user know
      let toast = this.toastCtrl.create({
        message: err.errorMessage,
        duration: 3000,
        position: "bottom"
      });
      toast.present();
    });
  }

  updateDoor() {

    this.availabilityController.updateDoor(this.door.availability).subscribe(() => {
      this.globals.doorIsOpen = true;

      console.log(`Activity: ${this.door.availability.activity} Location: ${this.door.availability.location}`);
      let alert = this.alertCtrl.create({
        title: 'Your door has been updated!',
        message: `Your friends will see that you are ${this.door.availability.activity} at ${this.door.availability.location}`,
        buttons: ['OK']
      });
      alert.present().then(() => {
        this.navCtrl.push(HomePage);
      });

    }, (err) => {
      err = err.json();

      //pop up that lets user know
      let toast = this.toastCtrl.create({
        message: err.errorMessage,
        duration: 3000,
        position: "bottom"
      });
      toast.present();
    });
  }

  closeDoor(){
    let alert = this.alertCtrl.create({
      title: 'Close Door',
      message: 'Are you sure you want to close your door?',
      buttons: [{
        text: 'Disagree',
        handler: () => {
          console.log('Disagree clicked');
        }
      },
        {
          text: 'Agree',
          handler: () => {
            this.availabilityController.closeDoor().subscribe(() => {
              this.globals.doorIsOpen = false;
              console.log('Agree clicked');

              this.navCtrl.push(HomePage);
            });
          }
        }]
    });
    alert.present();
  }

  doorIsOpen(): boolean {
    return this.globals.doorIsOpen;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad DoorPage');
  }

}
