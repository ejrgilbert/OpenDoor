import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the ProfilePicturesProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class ProfilePicturesProvider {

  constructor(public http: Http) {
    console.log('Hello ProfilePicturesProvider Provider');
  }

  getLinkByUsername(username: string): string {

    if (username == 'leslie') {
      return 'https://media1.popsugar-assets.com/files/thumbor/2ebfSQkPUKjxKfb5N3h2N-VIh7U/fit-in/2048xorig/filters:format_auto-!!-:strip_icc-!!-/2015/02/24/097/n/1922283/79e574c2_edit_img_image_845210_1424395524/i/Best-Leslie-Knope-GIFs.png';
    } else if (username == 'regalmeagle') {
      return 'https://upload.wikimedia.org/wikipedia/en/thumb/0/0a/Donna_Meagle.jpg/250px-Donna_Meagle.jpg';
    } else if (username == 'tommyfresh') {
      return 'http://i.imgur.com/YjlUYDr.jpg';
    } else if (username == 'lesliesbff') {
      return 'https://img.buzzfeed.com/buzzfeed-static/static/2014-09/18/12/campaign_images/webdr09/24-reasons-ann-perkins-needs-to-return-for-the-fi-2-20673-1411056640-1_dblbig.jpg';
    } else if (username == 'janet_snakehole') {
      return 'https://i.pinimg.com/236x/e8/07/45/e80745008ef7503d1a14e42316b39126--janet-snakehole-fictional-characters.jpg';
    } else if (username == 'burt_macklin') {
      return 'https://ih0.redbubble.net/image.412210358.3628/st%2Csmall%2C215x235-pad%2C210x230%2Cf8f8f8.lite-1u1.jpg';
    } else if (username == 'ben_wyatt') {
      return 'https://img.buzzfeed.com/buzzfeed-static/static/2014-08/13/17/enhanced/webdr09/anigif_enhanced-6409-1407965156-9.gif';
    } else return null;
  }

  getLinkByFirstName(firstName: string): string {

    if (firstName == 'Leslie') {
      return 'https://media1.popsugar-assets.com/files/thumbor/2ebfSQkPUKjxKfb5N3h2N-VIh7U/fit-in/2048xorig/filters:format_auto-!!-:strip_icc-!!-/2015/02/24/097/n/1922283/79e574c2_edit_img_image_845210_1424395524/i/Best-Leslie-Knope-GIFs.png';
    } else if (firstName == 'Donna') {
      return 'https://upload.wikimedia.org/wikipedia/en/thumb/0/0a/Donna_Meagle.jpg/250px-Donna_Meagle.jpg';
    } else if (firstName == 'Tom') {
      return 'http://i.imgur.com/YjlUYDr.jpg';
    } else if (firstName == 'Ann') {
      return 'https://img.buzzfeed.com/buzzfeed-static/static/2014-09/18/12/campaign_images/webdr09/24-reasons-ann-perkins-needs-to-return-for-the-fi-2-20673-1411056640-1_dblbig.jpg';
    } else if (firstName == 'April') {
      return 'https://i.pinimg.com/236x/e8/07/45/e80745008ef7503d1a14e42316b39126--janet-snakehole-fictional-characters.jpg';
    } else if (firstName == 'Andy') {
      return 'https://ih0.redbubble.net/image.412210358.3628/st%2Csmall%2C215x235-pad%2C210x230%2Cf8f8f8.lite-1u1.jpg';
    } else if (firstName == 'Ben') {
      return 'https://img.buzzfeed.com/buzzfeed-static/static/2014-08/13/17/enhanced/webdr09/anigif_enhanced-6409-1407965156-9.gif';
    } else return null;
  }

  getLinkByFullName(name: string): string {
    return this.getLinkByFirstName(name.split(" ")[0]);
  }

}
