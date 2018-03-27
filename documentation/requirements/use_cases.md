# OpenDoor Use Cases #

## UC 0: User logs into the system ##
### Preconditions: ###

- User has an account in the system.
- User is at login page.

### Mainflow: ###

- User types user name and password [E1] [E2]
- They click "Login" and are entered into the system.

### Subflows: ###

None

### Alternative flows: ###

- [E1] User inputs incorrect username. System responds with "Username not found".
- [E2] User inputs incorrect password, but correct username. System responds with "Incorrect password".

---

## UC 1: User creates an account ##

### Preconditions: ###

- User does not have an account in the system yet.
- User is at login page.

### Mainflow: ###

- User clicks "Register"
- User inputs their desired username [E1].
- User inputs their desired password 2x to verify password is typed correctly [E2] [E3].
- User inputs either email or phone number, user must authorize their email and phone number
- User types user name and password [E4] [E5].
- They click "Login" and are entered into the system.

### Subflows: ###

- None

### Alternative flows: ###

- [E1] Username is already taken, system alerts user. User is not able to submit with this username until it is changed to be unique.
- [E2] Password is not the same in both input boxes, system alerts user. User is not able to submit with this password until both boxes have same password.
- [E3] Error message is displayed if password does not meet constraints (defined in traditional requirement number 1).
- [E4] User inputs incorrect username. System responds with "Username not found".
- [E5] User inputs incorrect password, but correct username. System responds with "Incorrect password".

---

## UC 2: User adds a new friend ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks on profile at top of page
- User clicks on "Add Friends" button
- User can use search panel to find their friend with the friend's unique tag [S1]
- User can click "+" to add the individual to their friends list

### Subflows: ###

- [S1] System searches for user with this tag. If it is not found, no record is returned.

### Alternate flows: ###

None

---

## UC 3: User deletes friend ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks on profile at top of page
- User clicks on "My Friends" button
- User searches for friend name (or scrolls to friend) in the search bar
- User holds finger down on friend result
- User clicks "Remove Friend" button [S1]

### Subflows: ###

- [S1] Success message is displayed.

### Alternate flows: ###
None

---

## UC 4: User opens their door ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks on "Set Availability" button
- User is redirected to a page that prompts for location and activity type
- User inputs location
- User inputs activity type
- User clicks "Open My Door" [S1]
- User is redirected to main page
- Shows that door is now open

### Subflows: ###

- [S1] User clicks back button and door is not opened

### Alternate flows: ###
None

---

## UC 5: User closes their door ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks on "Change Availability" button
- User is redirected to a page that displays current location and activity and clicks "Close My Door" button [S1] [S2]
- User is redirected to main page
- Shows that door is now closed

### Subflows: ###
- [S1] User changes location and/or activity and clicks "Update Availability"
- [S2] User clicks back button and door is not closed

### Alternate flows: ###
None

---

## UC 6: User wants to meet friend using map view ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User sees the icon of a friend whose door is open [E1]
- User clicks on the available friend's icon
- System displays the available friend's location and activity and clicks "Ask to Join" [S1]
- System prompts the user for an estimate on when they will meet the available friend
- User inputs the length of time it will take the user to get to the available friend and clicks "Ok" [S2]
- User is redirected to main page

### Subflows: ###

- [S1] User clicks "Cancel" and does not ask to join available friend
- [S2] A message is sent to the available friend "May I join you in about [duration] minutes?"

### Alternative flows: ###
- [E1] User is looking at map and scrolls away from location
  - User can find their own location by clicking on the "Re-center" icon in the corner of the screen

---

## UC 7: User wants to meet friend using list view ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks "List" button toward the top of page
- User is redirected to a page that displays available friends' names and their activity [E1]
- User clicks on an available friend's icon
- System displays the available friend's location and activity and clicks "Ask to Join" [S1]
- System prompts the user for an estimate on when they will meet the available friend
- User inputs the length of time it will take the user to get to the available friend and clicks "Ok" [S2]
- User is redirected to main page

### Subflows: ###

- [S1] User clicks "Cancel" and does not ask to join available friend
- [S2] A message is sent to the available friend "May I join you in about [duration] minutes?"

### Alternative flows: ###
- [E1] User clicks "Map" button torward top of the page and is redirected to the main page

---

## UC 8: User receives notification for request to meet up ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User sees a change in notification icon in the top right corner of the main page and presses it
- User is redirected to a page which shows the user a friend's request to meet up with the user
- User reads message "May I join you in about [duration] minutes?"
- User is prompted to respond to the request with preset options and the user chooses "Yes!" [S1]
- User is redirected to main page

### Subflows: ###

- [S1] User does not want to meet friend and clicks "No".
  - System displays dialog box with default message (that is editable) to send reason why "No"
    - User clicks "Send" to send that message
    - User edits message and clicks "Send"
    - User clicks "Send and Close My Door"
    - User edits message and clicks "Send and Close My Door"

### Alternate flows: ###
None

---

## UC 9: User edits profile ##
### Preconditions: ###

- UC 0 is completed.
- User is on main page.

### Mainflow: ###

- User clicks on profile at top of page
- User clicks on gear/settings icon

#### Change Name ####
- User clicks on "Name" field
- User changes name to be displayed on their profile

#### Change profile picture ####
- User clicks on picture
- Click "Upload" to change picture [S1] [S2]

#### Change email ####
- User clicks on email
- User changes email [S1] [S2]
- User prompted to reauthorize their email [E1]

### Subflows: ###

- [S1] User clicks "Cancel" and the profile is not changed
  - User is redirected to profile page where change is not made
- [S2] User clicks "Save"
  - User is redirected to profile page where profile is updated to reflect the change

### Alternate flows: ###
- [E1] System shows error message if invalid email is entered

---
