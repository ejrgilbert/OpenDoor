import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { HttpModule } from '@angular/http';

import { MyApp } from './app.component';
import { DoorPage } from '../pages/door/door';
import { FriendsPage} from "../pages/friends/friends";
import { HomePage } from '../pages/home/home';
import { ListPage } from '../pages/list/list';
import { LoginPage } from '../pages/login/login';
import { ProfilePage } from '../pages/profile/profile';
import { RegisterPage } from '../pages/register/register';
import { VerifyPage} from "../pages/verify/verify";

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

import { GlobalsProvider } from '../providers/globals/globals';
import {AddfriendPage} from "../pages/addfriend/addfriend";
import { AccountProvider } from '../providers/account/account_provider';
import { AvailabilityProvider } from '../providers/availability/availability_provider';
import { GroupProvider } from '../providers/group/group_provider';
import { ManageConnectionsProvider } from '../providers/manage-connections/manage-connections';
import { UserProvider } from '../providers/user/user_provider';
import { AuthenticationProvider } from '../providers/authentication/authentication';
import { MapperUtility } from "../objects/mapper_utility";
import { ProfilePicturesProvider } from '../providers/profile-pictures/profile-pictures';

@NgModule({
  declarations: [
    MyApp,
    AddfriendPage,
    DoorPage,
    FriendsPage,
    HomePage,
    ListPage,
    LoginPage,
    ProfilePage,
    RegisterPage,
    VerifyPage
  ],
  imports: [
    BrowserModule,
    HttpModule,
    IonicModule.forRoot(MyApp),
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    AddfriendPage,
    DoorPage,
    FriendsPage,
    HomePage,
    ListPage,
    LoginPage,
    ProfilePage,
    RegisterPage,
    VerifyPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    GlobalsProvider,
    AccountProvider,
    AvailabilityProvider,
    GroupProvider,
    ManageConnectionsProvider,
    UserProvider,
    AuthenticationProvider,
    MapperUtility,
    ProfilePicturesProvider
  ]
})
export class AppModule {}
