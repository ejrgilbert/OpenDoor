package com.opendoor.spring.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opendoor.dto.user.UserDto;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.model.Availability;
import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.spring.service.util.DataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {
    com.opendoor.Application.class
  },
  properties = "logging.level.org.springframework.web=DEBUG"
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class AvailabilityControllerTest {
  MockMvc mvc;
  private static Gson gson = new Gson();

  private User testUser;
  private String userPassword;
  private String otherUserPassword;

  private Group allFriendsGroup;

  private User otherUser;
  private Friend friend;

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private GroupService groupService;

  private DataGenerator generator;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).addFilters(filterChainProxy).build();

    generator = new DataGenerator(accountService, userService);

    NewUserDto userDto = new NewUserDto("yes@yes.com", "burtmacklin", "@pr1lR0ck2","Andy", "Dwyer", "");
    testUser = generator.createUser(userDto);
    userPassword = "@pr1lR0ck2";
    allFriendsGroup = testUser.getGroups().get(0);

    otherUser = generator.createUser2();
    otherUserPassword = "@nn3R0ck2";

    friend = generator.addToFriendList(groupService, testUser, otherUser);
  }

  private void openDoor(Availability availability, List<String> groupNames) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("availability", availability);
    params.put("groupNames", groupNames);

    String toPass = gson.toJson(params);

    mvc.perform(put("/door/open")
        .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(params))
        .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk());
  }

  private void closeDoor() throws Exception {
    mvc.perform(put("/door/close")
        .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk());
  }

  private void updateDoor(Availability availability) throws Exception {
    mvc.perform(put("/door/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(gson.toJson(availability))
        .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk());
  }

  private List<UserDto> getAvailabilities(User user, String password) throws Exception {
    MvcResult result = mvc.perform(get("/door/allAvailable")
        .with(httpBasic(user.getAccount().getUsername(), password)))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    return gson.fromJson(stringResult, new TypeToken<List<UserDto>>(){}.getType());
  }

  @Test
  public void testAll() throws Exception {
    Availability availability = new Availability("Liquid State", "Chilling");
    List<String> groups = new ArrayList<>();
    groups.add(groupService.ALL_FRIENDS_GROUP_NAME);
    openDoor(availability, groups);

    List<UserDto> allAvailable = getAvailabilities(otherUser, otherUserPassword);
    assertTrue(allAvailable.size() == 1);
    assertTrue(allAvailable.get(0).getId() == testUser.getId());

    // Change the availability
    Availability availabilityUpdated = new Availability("D.H. Hill", "Studying");
    updateDoor(availabilityUpdated);

    allAvailable = getAvailabilities(otherUser, otherUserPassword);
    assertTrue(allAvailable.get(0).getAvailability().equals(availabilityUpdated));

    closeDoor();

    allAvailable = getAvailabilities(otherUser, otherUserPassword);
    assertTrue(allAvailable.size() == 0);
  }
}
