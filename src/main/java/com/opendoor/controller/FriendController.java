package com.opendoor.controller;

import com.google.gson.Gson;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.FriendService;
import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * TODO rename to make better ("Friend" doesn't really encapsulate the use of this controller)
 */
@RestController("friendController")
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/friend")
public class FriendController {
  @Autowired
  FriendService friendService;

  @Autowired
  UserService userService;

  private static Gson gson = new Gson();

  /**
   *
   */
  @RequestMapping(
    method = PUT,
    value = "/sendRequest/{id}"
  )
  public void sendFriendRequest(@PathVariable Long id) {
    User user = userService.getLoggedInUser();
    User other = userService.findById(id);

    friendService.sendFriendRequest(user, other);
  }

  @RequestMapping(
    method = PUT,
    value = "/acceptRequest/{id}"
  )
  public void acceptFriendRequest(@PathVariable Long id) {
    User user = userService.getLoggedInUser();
    User other = userService.findById(id);

    friendService.acceptFriendRequest(user, other);
  }

  @RequestMapping(
    method = PUT,
    value = "/denyRequest/{id}"
  )
  public void denyFriendRequest(@PathVariable Long id) {
    User user = userService.getLoggedInUser();
    User other = userService.findById(id);

    friendService.denyFriendRequest(user, other);
  }

  @RequestMapping(
    method = PUT,
    value = "/block/{id}"
  )
  public void blockUser(@PathVariable Long id) {
    User user = userService.getLoggedInUser();
    User other = userService.findById(id);

    friendService.blockUser(user, other);
  }

  @RequestMapping(
    method = PUT,
    value = "/unblock/{id}"
  )
  public void unblockUser(@PathVariable Long id) {
    User user = userService.getLoggedInUser();
    User other = userService.findById(id);

    friendService.unblockUser(user, other);
  }
}
