package com.opendoor.spring.rest;

import com.google.gson.Gson;
import com.opendoor.dto.user.NewUserDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {
    com.opendoor.Application.class
  },
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class GroupControllerTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  private User testUser;
  private String userPassword;

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

  private User toDelete;

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

    friend = generator.addToFriendList(groupService, testUser, otherUser);
  }

  @Test
  public void testAll() throws Exception {
    String nameChange = "Changed";

    // Perform the update
    mvc.perform(put("/group/changeFriendName/" + friend.getId())
      .param("groupId", allFriendsGroup.getId() + "")
      .param("name", nameChange)
      .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk());

    // Get the updated friends list and make sure the name of the user is "Changed"
    MvcResult result = mvc.perform(get("/group/allFriends")
      .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk())
      .andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    Group all = gson.fromJson(stringResult, Group.class);

    Friend updated = all.getFriend(friend.getId());
    assertTrue(updated.getName().equals(nameChange));
  }
}
