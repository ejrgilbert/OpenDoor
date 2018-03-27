# Open Door - Test Plan #

## Unit Test Plan ##

### What We're Testing ###
We will be performing white-box testing on all back-end classes as well as some front end methods. We will be testing the POJO's and Services on the back-end as well as some methods written in Angular on the front-end.

### Tools We're Using ###

We will be using several technologies to achieve a robust Unit Test Suite. For the back-end, we will simply use JUnit for testing. On the front-end, we will use [Karma](https://karma-runner.github.io/1.0/index.html) and [Jasmine](https://jasmine.github.io/) to test the Angular JS code. Jasmine is the language we will actually write the tests in, whereas Karma is the runner for the Jasmine tests.

### How to Run Our Tests ###

#### JUnit - Eclipse ####
Unit tests will not depend on data already being in the database, so running the test will simply be Right Click > Run As > JUnit Test on the desired test. To run the entire test suite (keep in mind this will run the front-end tests as well), simply right click on the base folder in the Project Browser view do Right Click > Run As > JUnit Test.

#### Karma and Jasmine ####
You can run Karma tests through the command line tool Grunt (suggested [here](http://andyshora.com/unit-testing-best-practices-angularjs.html)). Simply execute `grunt test` and it performs all unit and integration tests in the project.

### Code Coverage Report ###

#### Eclipse ####
In order to run a coverage report Right Click > Coverage As > JUnit Test. Doing so will show you the various metrics of code coverage pertaining to the project.

#### Goals ####
We will keep line, branch, and complexity coverage above 70% for all back-end classes. This will help us to have a certain black-and-white number for us to aim for. However, the goal is not to meet some requirement and stop testing; the goal is to create a great baseline application to build off of. Therefore, we will make sure that our testing is thorough and complete rather than attempting to meet a coverage requirement.

### Test Result Discussion ###
The result of running our JUnit test suite should always have 100% passes in the `master` branch. If there is ever a failure, that will be documented as a bug and a new test will be written to ensure that bug is fully tested (if the existing test is inadequate). We will also make notes to ensure this issue does not happen in the future since `master` should have a clean application *at all times*.

---

## Acceptance Test Plan ##

### What We're Testing ###
We will have two portions of acceptance testing. Since we, in essence, have two projects (the back-end with the REST API at the top level as well as the front-end that builds on top of this API), we will need to have two acceptance, black-box test suites. So, we will need to have one for the Rest API and another for testing the Ionic framework front-end that depends on this API.

### How to Set Up for Testing ###
We will need to determine how we will actually populate the database. Will we have a script that we need to run? Or is there a way for Hibernate to create a "testing" and a "production" database?

### Tools We're Using ###
We will be using MockMVC, JUnit and Mockito to [test the Spring MVC Rest API](http://memorynotfound.com/unit-test-spring-mvc-rest-service-junit-mockito/). Creating these tests will ensure that our fully-functional back-end is functioning correctly through treating it as a black-box. We will be using [Protractor](http://www.protractortest.org/) to test the Ionic front-end as this enables us to interact with the front-end through a browser as a user would. We will be able to test that the Angular code interacts with the back-end and responds appropriately. This portion is extremely important because this is testing the application at the top-most part of the stack, where the user will be.

### Our Test Data ###

- What the test data is (are) like

In Database:
`notUnique` needs to be a user in the system
- Door is closed
`chicken` needs to be a user in the system
- Door is open
`enemy` needs to be a user in the system
- Door is open

`chicken` and `notUnique` need to be friends
- `notUnique` only has this one friend

In test data folder:
`testProfilePicture1.png` needs to be on the system somewhere
`testProfilePicture2.png` needs to be on the system somewhere

### Test Case Tables ###
A suite of multiple acceptance test cases
- Usually a few for each functional requirement.
- Maybe a few supporting non-functional requirements
- Performance/resource usage?
- Response Time?

The tests in the following tables will be relevant for both the Rest API and the Ionic testing since both of these are testing completed systems.

#### Preconditions ####

Below are the preconditions for every test:
- The system is loaded.

#### User Authentication ####

1. The user's username shall be a unique identifier.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>uniqueUsername</td>
    <td>
      Preconditions:<br />
        - `unique` is a unique username in the system.<br />
        - User is at the page to create a new account.<br /><br />
      Steps:<br />
      1. User types in `unique` to the username text box<br />
      2. User inputs `validPassword!` into both password text boxes <br />
      3. User inputs `validEmail@email.com` to the email text box<br />
      4. The user clicks the submit button to complete the registering process<br />
      5. The user clicks the log out button<br />
      6. The user logs in with `unique` username and `validPassword!` password<br />
    </td>
    <td>
      The system gives no error messages at any point in the registration process and is entered into the system when they click the registration submit button.<br />
      The user is able to log back in with the new account credentials.
    </td>
    <td></td>
  </tr>

  <tr>
    <td>notUniqueUsername</td>
    <td>
      Preconditions:<br />
        - `notUnique` is a username already in use in the system.<br />
        - User is at the page to create a new account.<br /><br />
      Steps:<br />
      1. User types in `notUnique` to the username text box<br />
      2. User inputs `validPassword!` into both password text boxes<br />
      3. User inputs `validEmail@email.com` to the email text box<br />
      4. The user clicks the submit button to complete the registering process<br />
      5. The user logs in with `notUnique` username and `validPassword!` password<br />
    </td>
    <td>
      The system gives an error messages saying that `notUnique` has already been used in the system. The user is not entered into the system when they click the registration submit button.<br />
      The user is not able to log in with the new account credentials.
    </td>
    <td></td>
  </tr>
</table>

2. The system's user passwords shall meet the following constraints: At least one capital letter, At least one symbol, At least 8 characters.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>validPassword</td>
    <td>
      This test will be the same as the uniqueUsername test above (see User Authentication section at table for number 1)
    </td>
    <td>
      Expected results will also be the same.
    </td>
    <td></td>
  </tr>

  <tr>
    <td>invalidPasswordCapital</td>
    <td>
      This test will be the same as notUniqueUsername test above (see User Authentication section at table for number 1). However, the inputs will be as follows:<br />
      - username: `unique`<br />
      - password: `invalidpassword!`<br />
      - email: `validEmail@email.com`<br />
    </td>
    <td>
      The system gives an error messages saying that the password needs to have at least one capital. The user is not entered into the system when they click the registration submit button.<br />
      The user is not able to log in with the new account credentials.
    </td>
    <td></td>
  </tr>

  <tr>
    <td>invalidPasswordSymbol</td>
    <td>
      This test will be the same as notUniqueUsername test above (see User Authentication section at table for number 1). However, the inputs will be as follows:<br />
      - username: `unique`<br />
      - password: `invalidPassword`<br />
      - email: `validEmail@email.com`<br />
    </td>
    <td>
      The system gives an error messages saying that the password needs to have at least one symbol. The user is not entered into the system when they click the registration submit button.<br />
      The user is not able to log in with the new account credentials.
    </td>
    <td></td>
  </tr>

  <tr>
    <td>invalidPasswordLength</td>
    <td>
      This test will be the same as notUniqueUsername test above (see User Authentication section at table for number 1). However, the inputs will be as follows:<br />
      - username: `unique`<br />
      - password: `invalid`<br />
      - email: `validEmail@email.com`<br />
    </td>
    <td>
      The system gives an error messages saying that the password needs to be at least 8 characters long. The user is not entered into the system when they click the registration submit button.<br />
      The user is not able to log in with the new account credentials.
    </td>
    <td></td>
  </tr>
</table>

3. The system shall require an email or phone number to verify user identity.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>validEmail</td>
    <td>
      This test will be the same as uniqueUsername test above (see User Authentication section at table for number 1). However, the inputs will be as follows:<br />
      - username: `unique`<br />
      - password: `validPassword!`<br />
      - email: `validEmail@email.com`<br />
    </td>
    <td>
      Expected results will also be the same.
    </td>
    <td></td>
  </tr>

  <tr>
    <td>invalidEmail</td>
    <td>
      This test will be the same as notUniqueUsername test above (see User Authentication section at table for number 1). However, the inputs will be as follows:<br />
      - username: `unique`<br />
      - password: `validPassword!`<br />
      - email: `invalidEmail`<br />
    </td>
    <td>
      The system gives an error messages saying that the email is not valid. The user is not entered into the system when they click the registration submit button.<br />
      The user is not able to log in with the new account credentials.
    </td>
    <td></td>
  </tr>
</table>

#### User Profiles ####

##### Preconditions #####
- User is authenticated into system as
  - username: `notUnique`
  - password: `validPassword!`
- User is at profile settings page

##### Tests #####

1. The system shall allow the user to set a profile picture.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>setFirstProfilePicture</td>
    <td>
      Preconditions:<br />
      - User has not set a profile picture yet<br /><br />
      Steps: <br />
      1. User clicks on current profile picture<br />
      2. Click "Upload" to change picture<br />
      3. User selects `testProfilePicture1.png` to set as profile picture
      4. User clicks "Save"
    </td>
    <td>
      User is redirected to profile page where profile is updated to now be
      `testProfilePicture1.png`
    </td>
    <td></td>
  </tr>

  <tr>
    <td>setNewProfilePicture</td>
    <td>
      Preconditions:<br />
      - User has already set their profile picture to be `testProfilePicture1.png`<br />
      Steps: <br />
      1. User clicks on current profile picture<br />
      2. Click "Upload" to change picture<br />
      3. User selects `testProfilePicture2.png` to set as profile picture
      4. User clicks "Save"
    </td>
    <td>
      User is redirected to profile page where profile is updated to now be
      `testProfilePicture2.png`
    </td>
    <td></td>
  </tr>

  <tr>
    <td>cancelSetProfilePicture</td>
    <td>
      Preconditions:<br />
      - User has already set their profile picture to be `testProfilePicture2.png` (see setNewProfilePicture above)<br />
      Steps: <br />
      1. User clicks on current profile picture<br />
      2. Click "Upload" to change picture<br />
      3. User selects `testProfilePicture1.png` to set as profile picture
      4. User clicks "Cancel"
    </td>
    <td>
      User is redirected to profile page where picture is not changed, it remains being `testProfilePicture2.png`
    </td>
    <td></td>
  </tr>
</table>

2. The system shall allow the user to set a semantic name (other than their username).
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>setName</td>
    <td>
      Preconditions: <br />
      - User's name is `Bob Marley`<br /><br />
      Steps: <br />
      1. User clicks on the "Name" field<br />
      2. User changes name to be `John Doe`<br />
      3. User clicks "Save"
    </td>
    <td>
      User is redirected to profile page where profile is updated to now have `John Doe` as their name. It is no longer `Bob Marley`
    </td>
    <td></td>
  </tr>

  <tr>
    <td>cancelSetName</td>
    <td>
      Preconditions: <br />
      - User's name is `Bob Marley`<br /><br />
      Steps: <br />
      1. User clicks on the "Name" field<br />
      2. User changes name to be `John Doe`<br />
      3. User clicks "Cancel"
    </td>
    <td>
      User is redirected to profile page where profile has not been changed, it remains as `Bob Marley`
    </td>
    <td></td>
  </tr>
</table>

#### Authorization of Access ####

##### Preconditions #####
- User is authenticated into system as
  - username: `notUnique`
  - password: `validPassword!`

##### Tests #####

1. The system shall not return user profiles on the Add Friends search pane if the unique username is not found.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>searchNonexistentUsername</td>
    <td>
      Preconditions: <br />
      - User is at profile settings page
      - `dne` is not a username of a profile in the system<br /><br />
      Steps: <br />
      1. User clicks on "Add Friends" button<br />
      2. User types `dne` into the search panel<br />
    </td>
    <td>
      No results pop up for the search `dne`.
    </td>
    <td></td>
  </tr>
</table>

2. Locations for user1 shall not be shown to user2 unless user1 and user2 are _mutual_ friends.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>locationPrivacyBetweenUsers</td>
    <td>
      Preconditions: <br />
      - `chicken` is the username of a profile in the system<br />
      - User only has one friend, `chicken`<br />
      - `enemy` is the username of a profile in the system<br />
      - User is not friends with `enemy`<br />
      - User is at maps<br /><br />
      Steps: <br />
      1. User looks at OpenDoor map view<br />
      2. User looks at OpenDoor list view<br />
    </td>
    <td>
      The user is able to see `chicken` on both the map and list with his Open Door. The user is not able to see `enemy` on the map or the list with his Open Door.
    </td>
    <td></td>
  </tr>
</table>

#### Friend Management ####

##### Preconditions #####
- User is authenticated into system as
  - username: `notUnique`
  - password: `validPassword!`
- User is at profile settings page

##### Tests #####

1. The user shall be able to view their friends list.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>viewFriendsList</td>
    <td>
      Preconditions: <br />
      - `chicken` is the username of a profile in the system<br />
      - User only has one friend, `chicken`<br /><br />
      Steps: <br />
      1. User clicks on "My Friends" button<br />
    </td>
    <td>
      A list of the user's friends is listed. `chicken` is the only one in this list.
    </td>
    <td></td>
  </tr>
</table>

2. The user shall be able to add a friend.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>addFriend</td>
    <td>
      Preconditions: <br />
      - `enemy` is the username of a profile in the system<br />
      - User is _not_ already friends with `enemy`<br /><br />
      Steps: <br />
      1. User clicks on "Add Friends" button<br />
      2. User types `enemy` into the search panel<br />
      3. User clicks the "+" button by the search result<br />
      4. User goes back to the friends list.
    </td>
    <td>
      A success message is displayed showing that the friend request has been sent. The users are _not_ friends yet in the system, `enemy` is not shown as a friend in the friends list.
    </td>
    <td></td>
  </tr>
</table>

3. The user shall be able to delete a friend.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>deleteFriend</td>
    <td>
      Preconditions: <br />
      - `chicken` is the username of a profile in the system<br />
      - User _is_ friends with `chicken`<br /><br />
      Steps: <br />
      1. User clicks on "My Friends" button<br />
      2. User holds finger down on `chicken` entry<br />
      3. User clicks "Remove Friend" button
    </td>
    <td>
      A success message is displayed showing that the friend has been removed. The users are no longer friends in the system, `chicken` is not shown as a friend in the friends list.
    </td>
    <td></td>
  </tr>
</table>

#### Location ####

1. The system shall have two ways to view open doors (Map View and List View).<br/>
Done through Authorization of Access test for number 2.
2. The system shall have the available friends of the user displayed on the open doors views.<br/>
Done through Authorization of Access test for number 2.
3. The system shall allow the user to open their door.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>openDoor</td>
    <td>
      Preconditions: <br />
      - User is authenticated into system as<br />
        - username: `notUnique`<br />
        - password: `validPassword!`<br />
      - User's door is not open<br />
      - User is at OpenDoor map view<br /><br />
      Steps: <br />
      1. User clicks on "Set Availability" button<br />
      2. User enters location as "Hunt Library"<br />
      3. User enters activity as "Studying"<br />
      4. User clicks "Open My Door"<br />
      5. User is redirected back to map view<br />
    </td>
    <td>
      At first the user's location on the map does not show that their door is open.
      After opening their door, the user's location should show that their door is open.
      When re-authenticating as `chicken`, you are able to see `notUnique`'s door as open.
    </td>
    <td></td>
  </tr>
</table>

4. The system shall allow the user to edit their availability.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>editAvailability</td>
    <td>
      Preconditions: <br />
      - User is authenticated into system as<br />
        - username: `chicken`<br />
        - password: `validPassword!`<br />
      - User's door is open<br />
      - User is at OpenDoor map view<br /><br />
      Steps: <br />
      1. User clicks on "Update Availability" button<br />
      2. User changes location as "Goodberry's"<br />
      3. User enters activity as "Chillling"<br />
      4. User clicks "Update My Availability"<br />
      5. User is redirected back to map view<br />
    </td>
    <td>
      At first the user's location on the map does show that their door is open.
      After updating their availability, the user's location _still_ shows that their door is open. When re-clicking the "Update Availability" button, the location is still set to "Goodberry's" and the activity is still "Chillling".<br />
      When re-authenticating as `notUnique`, you see that `chicken` is at "Goodberry's" doing the activity of "Chillling".
    </td>
    <td></td>
  </tr>
</table>

5. The system shall allow the user to close their door.
<table>
  <tr>
    <th>Test ID</th>
    <th>Description</th>
    <th>Expected Results</th>
    <th>Actual Results</th>
  </tr>

  <tr>
    <td>closeDoor</td>
    <td>
      Preconditions: <br />
      - User is authenticated into system as<br />
        - username: `chicken`<br />
        - password: `validPassword!`<br />
      - User's door is open<br />
      - User is at OpenDoor map view<br /><br />
      Steps: <br />
      1. User clicks on "Update Availability" button<br />
      2. User clicks "Close Door" button
    </td>
    <td>
      At first the user's location on the map shows that their door is open.
      After closing their door, the user's location should show that their door is closed.
      When re-authenticating as `chicken`, you are _not_ able to see `notUnique`'s door as open.
    </td>
    <td></td>
  </tr>
</table>

### Test Result Discussion ###
The result of running our Acceptance test suite should always have 100% passes in the `master` branch. If there is ever a failure, that will be documented as a bug and a new test will be written to ensure that bug is fully tested (if the existing test is inadequate). We will also make notes to ensure this issue does not happen in the future since `master` should have a clean application *at all times*.
