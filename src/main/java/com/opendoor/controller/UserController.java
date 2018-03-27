package com.opendoor.controller;

import com.opendoor.dto.user.ConnectedUserDto;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.dto.user.UserDto;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.ConnectionService;
import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Controller for all user-related operations
 * @author Elizabeth Gilbert
 */
@RestController
@CrossOrigin(origins = "http://localhost:8100")
@RequestMapping("/user")
public class UserController {
  /**
   * Service for user-related persistence operations
   */
  @Autowired
  UserService userService;

  /**
   * Service for user-related persistence operations
   */
  @Autowired
  AccountService accountService;

  @Autowired
  ConnectionService connectionService;

  // TODO create a login method that returns success
  @RequestMapping(
    method = GET,
    value="/login"
  )
  public Principal login(Principal user){
    return user;
  }

  /**
   * Find the user in the database by username, does not returned if has been blocked
   * @param username the name of the user to find
   * @return nothing if not found in the database or the User if found
   */
  @RequestMapping(
    method = GET,
    value = "/search/{username}",
    produces = { "application/json"}
  )
  @ResponseBody
  public ConnectedUserDto findByUsername(@PathVariable String username) {
    User other = userService.findByUsername(username);
    // What if the username is not found? return null
    if (other == null) return null;

    User user = userService.getLoggedInUser();
    Connection connection = connectionService.findByUsers(user.getId(), other.getId());

    if ((connection != null) && connection.getStatus() == Connection.ConnectionStatus.BLOCKED)
      return null;

    return (other == null) ? null : new ConnectedUserDto(other, connection);
  }

  /**
   * Find the user in the database by id
   * @param id the id of the user to find
   * @return nothing if not found in the database or the User if found
   */
  @RequestMapping(
    method = GET,
    value = "/{id}",
    produces = { "application/json"}
  )
  @ResponseBody
  public NewUserDto findById(@PathVariable Long id) {
    User user = userService.findById(id);
    return user == null ? null : new NewUserDto(user);
  }

  /**
   * Update the user in the database
   * @param id the id of the user to update
   * @param userDto the NewUserDto containing the updates
   */
  @RequestMapping(
    method = PUT,
    value = "/{id}",
    consumes = { "application/json" }
  )
  @ResponseStatus(HttpStatus.OK)
  public void updateUser(@PathVariable long id, @RequestBody @Valid NewUserDto userDto) {
    // TODO this should probably function by logged-in user
    User oldUser = userService.findById(id);
    oldUser.update(userDto);
    
    userService.save(oldUser);
  }

  /**
   * Disable the specified user
   * @param id the id of the user to disable
   */
  @RequestMapping(
    method = PUT,
    value = "/disable/{id}"
  )
  @ResponseStatus(HttpStatus.OK)
  public void disableUser(@PathVariable long id) { userService.disableUser(id);}

  /**
   * Enable the specified user
   * @param id the id of the user to enable
   */
  @RequestMapping(
    method = PUT,
    value = "/enable/{id}"
  )
  @ResponseStatus(HttpStatus.OK)
  public void enableUser(@PathVariable long id) { userService.enableUser(id);}

}
