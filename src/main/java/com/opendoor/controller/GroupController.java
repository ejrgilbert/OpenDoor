package com.opendoor.controller;

import com.opendoor.Application;
import com.opendoor.dto.group.GroupDto;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Controller containing all Group-related operations.
 * @author Elizabeth Gilbert
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/group")
public class GroupController {

  /**
   * Service for group-related persistence operations
   */
  @Autowired
  private GroupService groupService;

  /**
   * Service for user-related persistence operations
   */
  @Autowired
  private UserService userService;

  /**
   * Edit the name of a friend in a group
   * @param friendId The id of the friend to edit
   * @param groupId The id of the group with the given friend
   * @param name The name to set the friend to
   */
  @RequestMapping(
    method = PUT,
    value = "/changeFriendName/{friendId}"
  )
  public void editFriendName(@PathVariable Long friendId, @RequestParam("groupId") Long groupId, @RequestParam("name") String name) {
    User user = userService.getLoggedInUser();
    groupService.editFriendName(user.getId(), groupId, friendId, name);
  }

  /**
   * Get the list of all friends for the logged-in user
   * @return the list of all friends for the logged-in user
   */
  @RequestMapping(
    method = GET,
    value = "/allFriends",
    produces = { "application/json"}
  )
  @ResponseBody
  public GroupDto getFriendsList() {
    User user = userService.getLoggedInUser();

    // Find the all friends list for the logged in user
    return new GroupDto(groupService.getAllFriendsGroup(user.getId()));
  }
}
