package com.opendoor.spring.service;

import com.google.gson.Gson;
import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.FriendRepository;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.spring.service.util.DataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.crypto.Data;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = com.opendoor.Application.class,
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class GroupServiceTest {
  private DataGenerator generator;

  private User testUser;

  private Group testGroup;

  @Autowired
  private UserService service;

  @Autowired
  private AccountService acctService;

  @Autowired
  private GroupService groupService;

  @Autowired
  private FriendRepository friendRepository;

  private static Gson gson = new Gson();

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setUp() {
    MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
    generator = new DataGenerator(acctService, service);

    if (service.findByUsername(DataGenerator.USERNAME1) == null){
      testUser = generator.createUser();
      testGroup = testUser.getGroups().get(0);
    } else {
      testUser = service.findByUsername(DataGenerator.USERNAME1);
      testGroup = testUser.getGroups().get(0);
    }
  }

  @Test
  public void getAllFriendsGroupTest() {
    Group group = groupService.getAllFriendsGroup(testUser.getId());
    assertTrue(group.getName().equals(GroupService.ALL_FRIENDS_GROUP_NAME));
  }

  @Test
  public void getByIdTest() {
    Group group = groupService.getById(testUser.getId(), testGroup.getId());
    assertTrue(group.equals(testGroup));
  }

  @Test
  public void getByNameTest() {
    Group group = groupService.getByName(testUser.getId(), GroupService.ALL_FRIENDS_GROUP_NAME);
    assertTrue(group.getName().equals(GroupService.ALL_FRIENDS_GROUP_NAME));
  }

  private Friend addToGroup(User user) {
    Friend friend = new Friend(user);

    // Friend not already in the group
    groupService.addToGroup(testUser.getId(), testGroup.getId(), friend);

    Group updated = groupService.getById(testUser.getId(), testGroup.getId());
    Friend friendObj = friendRepository.findById(updated.getFriends().get(0).getId());
    assertTrue(updated.hasFriend(friendObj));

    return friendObj;
  }

  private void removeFromGroup(Group group, Friend friend) {
    groupService.removeFromGroup(testUser.getId(), group.getId(), friend);

    Group updated = groupService.getById(testUser.getId(), group.getId());
    assertFalse(updated.hasFriend(friend));
  }

  @Test
  public void addAndRemoveTest() {
    User user2 = generator.createUser2();
    Group otherUsersGroup = user2.getGroups().get(0);
    Friend friend = addToGroup(user2);

    // Friend already in the group
    try {
      groupService.addToGroup(testUser.getId(), testGroup.getId(), friend);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This friend is already in your group!"));
    }

    // Add to Group not "owned" by user
    try {
      groupService.addToGroup(testUser.getId(), otherUsersGroup.getId(), friend);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This group does not belong to you!"));
    }

    // Remove from group
    removeFromGroup(testGroup, friend);

    // Remove friend not in the group
    try {
      groupService.removeFromGroup(testUser.getId(), testGroup.getId(), friend);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This friend isn't in your group yet!"));
    }

    // Remove from Group not "owned" by user
    try {
      groupService.removeFromGroup(testUser.getId(), otherUsersGroup.getId(), friend);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This group does not belong to you!"));
    }

    // Tear down
    generator.deleteUser(user2.getId(), user2.getAccount().getUsername());
  }

  @Test
  public void editFriendNameTest() {
    User user2 = generator.createUser2();
    Group otherUsersGroup = user2.getGroups().get(0);
    Friend friend = addToGroup(user2);

    // Friend in the group
    groupService.editFriendName(testUser.getId(), testGroup.getId(), friend.getId(), "Changed Name");
    Group updated = groupService.getById(testUser.getId(), testGroup.getId());
    assertTrue(updated.getFriends().get(0).getName().equals("Changed Name"));

    // Edit friend in Group not "owned" by user
    try {
      groupService.editFriendName(testUser.getId(), otherUsersGroup.getId(), 1, "DNE");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This group does not belong to you!"));
    }

    // Friend not in the group
    try {
      groupService.editFriendName(testUser.getId(), testGroup.getId(), 100, "DNE");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("This friend doesn't exist!"));
    }

    // Tear down
    friend.setName("Changed Name");
    removeFromGroup(testGroup, friend);
    generator.deleteUser(user2.getId(), user2.getAccount().getUsername());
  }
}
