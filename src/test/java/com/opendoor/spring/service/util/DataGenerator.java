package com.opendoor.spring.service.util;

import com.google.gson.Gson;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Valid;
import java.util.UUID;

import static org.junit.Assert.*;

public class DataGenerator {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  private AccountService accountService;
  private UserService userService;

  public static final String USERNAME1 = "burtmacklin";
  public static final String PASSWORD1 = "@pr1lR0ck2";

  public static final String USERNAME2 = "leslie";
  public static final String PASSWORD2 = "@nn3R0ck2";

  public DataGenerator(AccountService accountService, UserService userService) {
    this.accountService = accountService;
    this.userService = userService;
  }

  public User createUser() {
    NewUserDto userDto = new NewUserDto("yes@yes.com", USERNAME1, PASSWORD1,"Andy", "Dwyer", "");
    return createUser(userDto);
  }

  public User createUser2() {
    NewUserDto userDto = new NewUserDto("unique@yes.com", USERNAME2, PASSWORD2,"Leslie", "Knope", "");
    return createUser(userDto);
  }

  public User createUser(@Valid NewUserDto userDto) {

    // Need to first save the account object
    Account acct = new Account(userDto.getUsername(), userDto.getPassword().getPassword());
    accountService.save(acct);

    // Now, save a new User object with the above account and information in the DTO
    User user = new User(userDto, accountService.findByUsername(userDto.getUsername()));

    // So we don't have to worry about enabling the user
    user.setEnabled(true);

    // Generate random 36-character string token for confirmation link
    user.setConfirmationToken(UUID.randomUUID().toString());

    userService.save(user);

    Account response = accountService.findByUsername(userDto.getUsername());
    assertNotNull(response);
    assertEquals(userDto.getUsername(), response.getUsername());
    assertNotNull(response.getPassword());
    assertNotEquals(userDto.getPassword(), response.getPassword()); // Should be hashed, not plaintext

    User testUser = new User(userDto, response);
    testUser.setEnabled(true);

    // Need to get the user from the DB in order to set the ID of testUser,
    // otherwise testUser would not have an ID at all.
    user = userService.findByEmail(userDto.getEmail());

    assertNotNull(user);
    testUser.setId(user.getId());
    testUser.getGroups().get(0).setId(user.getGroups().get(0).getId()); // Set the id of the All Friends group
    assertTrue(testUser.equals(user));

    return user;
  }

  public Friend addToFriendList(GroupService groupService, User host, User guest){
    Group allFriends = groupService.getAllFriendsGroup(host.getId());

    Friend friend = new Friend(guest);
    friend.setHost(host);
    Group allFriends2 = groupService.getAllFriendsGroup(guest.getId());
    Friend friend2 =  new Friend(host);
    friend2.setHost(guest);
    groupService.addToGroup(guest.getId(), allFriends2.getId(), friend2);
    return groupService.addToGroup(host.getId(), allFriends.getId(), friend);
  }

//  public void deleteAllFriends(FriendRepository friendRepository) {
//    Iterable<Friend> friends = friendRepository.findAll();
//
//    for(Friend friend : friends)
//      friendRepository.delete(friend.getId());
//  }

  public void deleteUser(long id, String username) {
    userService.deleteUser(id);
    accountService.deleteByUsername(username);
  }
}