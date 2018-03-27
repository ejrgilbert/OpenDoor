package com.opendoor.spring.service;

import com.google.gson.Gson;
import com.opendoor.persistence.model.Availability;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.FriendRepository;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.AvailabilityService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = com.opendoor.Application.class,
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class AvailabilityServiceTest {
  private DataGenerator generator;
  private User testUser;
  private User friend;
  private Group testGroup;

  @Autowired
  private UserService userService;
  @Autowired
  private AccountService acctService;
  @Autowired
  private GroupService groupService;
  @Autowired
  private AvailabilityService availabilityService;
  @Autowired
  private FriendRepository friendRepository;

  private static Gson gson = new Gson();

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setUp() {
    MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
    generator = new DataGenerator(acctService, userService);

    testUser = generator.createUser();
    friend = generator.createUser2();

    // Make the two users friends
    generator.addToFriendList(groupService, testUser, friend);
    testUser = userService.findById(testUser.getId());

    testGroup = testUser.getGroups().get(0);
  }

  @Test
  public void allTest() {
    // Set testUser as available to their "All Friends" list
    Availability availability = new Availability();
    availability.setActivity("Chilling");
    availability.setLocation("PCJ");

    List<Group> groups = new ArrayList<>();
    groups.add(testGroup);
    availabilityService.openDoor(testUser.getId(), availability, groups);
//    testUser = userService.findById(testUser.getId());

    User updated = userService.findById(testUser.getId());
    assertTrue(availability.equals(updated.getAvailability()));

    // Check that `friend` can access `testUser`s availability
    List<User> allAvailable = availabilityService.getAvailabilities(friend.getId());
    assertEquals(1, allAvailable.size());
    assertEquals(testUser.getId(), allAvailable.get(0).getId());

    // Check that a user _not_ friends with testUser cannot access their availability
    User notFriend = userService.findByUsername("user");
    allAvailable = availabilityService.getAvailabilities(notFriend.getId());
    assertEquals(0, allAvailable.size());

    // Close testUser's door
    availabilityService.closeDoor(testUser.getId());

    updated = userService.findById(testUser.getId());
    assertNull(updated.getAvailability());

    // Check that testUser is not shown as available to `friend`
    allAvailable = availabilityService.getAvailabilities(friend.getId());
    assertEquals(0, allAvailable.size());
  }
}
