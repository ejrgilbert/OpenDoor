import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { DoorPage } from './door';

@NgModule({
  declarations: [
    DoorPage,
  ],
  imports: [
    IonicPageModule.forChild(DoorPage),
  ],
})
export class DoorPageModule {}
