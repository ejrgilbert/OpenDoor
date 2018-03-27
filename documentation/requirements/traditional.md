## Functional Requirements ##

#### User Profiles ####

1. The system shall allow the user to set a profile picture.
1. The system shall allow the user to set a semantic name (other than their username).
1. The system shall allow the user to manage their friends.

#### User Authentication ####

1. The user's username shall be a unique identifier.
1. The system's user passwords shall meet the following constraints: At least one capital letter, At least one symbol, At least 8 characters.
1. The system shall require an email or phone number to verify user identity.
1. The system can allow authorization through other types of accounts (Google, Facebook, etc.).
1. The system can have a "Forgot password?" option.

#### Authorization of Access ####

1. The system shall not return user profiles on the Add Friends search pane if the unique username is not found.
1. Locations for user1 shall not be shown to user2 unless user1 and user2 are _mutual_ friends.
1. Friend lists shall not be accessible by users other than the "owner" of the list.

#### Friend Management ####

1. The user shall be able to view their friends list.
1. The user shall be able to delete a friend.
1. The user shall be able to add a friend.
1. The system can allow a user to create and manage groups within friends.
1. The system can allow a user to delete groups within friends.
1. The system can allow a user to change the name of their friend.

#### Privacy Settings ####

1. The system can allow the user to set privacy to keep others from adding them as a friend (they must initiate the friendship).
1. The system can allow users to block other users.

#### Location ####

1. The system shall have two ways to view open doors (Map View and List View).
1. The system shall have the available friends of the user displayed on the open doors views.
1. The system shall allow the user to open their door.
1. The system shall allow the user to edit their availability.
1. The system shall allow the user to close their door.

---

## Non-Functional Requirements ##

1. The system shall be intuitive and clean looking.
1. The system shall respond to user input within .5 seconds.

---

## Constraints ##

1. The system shall be a Maven project.
1. The system's back-end shall be written in Java Spring MVC.
1. The system's back-end shall be tested using JUnit.
1. The system's back-end shall shall have a REST API at the top level.
1. The system shall have testing for the REST API using Mockito, Hamcrest matchers, JUnit and the Spring testing framework.
1. The system's front-end shall interact with the back-end via the REST API
1. The system's front-end shall be written in Ionic based on HTML, Bootstrap, and Angular.
1. The system's front-end shall have unit testing written in Karma (which will include using Jasmine).
1. The system's front-end shall have black-box testing written in Protractor (which is built on top of WebDriverJS and Selenium).
