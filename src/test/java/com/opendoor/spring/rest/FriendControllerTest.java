package com.opendoor.spring.rest;

import com.google.gson.Gson;
import com.opendoor.dto.user.ConnectedUserDto;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.GroupService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.spring.service.util.DataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database

public class FriendControllerTest {
  MockMvc mvc;
  private static Gson gson = new Gson();

  private User testUser;
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

    NewUserDto userDto = new NewUserDto("yes@yes.com", "burtmacklin",
            "@pr1lR0ck2","Andy", "Dwyer", "");
    if (userService.findByUsername(DataGenerator.USERNAME1) == null ) {
      testUser = generator.createUser(userDto);
      otherUser = generator.createUser2();
    } else {
      testUser = userService.findByUsername(DataGenerator.USERNAME1);
      otherUser = userService.findByUsername(DataGenerator.USERNAME2);
    }
    allFriendsGroup = testUser.getGroups().get(0);
  }

  private ConnectedUserDto getOther() throws Exception {
    MvcResult result = mvc.perform(get("/user/search/" + DataGenerator.USERNAME2)
            .with(httpBasic(DataGenerator.USERNAME1, DataGenerator.PASSWORD1)))
            .andExpect(status().isOk())
            .andReturn();
    String stringResult = result.getResponse().getContentAsString();

    return gson.fromJson(stringResult, ConnectedUserDto.class);
  }

  private void performHttp(String mapping) throws Exception {
    mvc.perform(put(mapping + otherUser.getId())
      .with(httpBasic(DataGenerator.USERNAME1, DataGenerator.PASSWORD1)))
        .andExpect(status().isOk());
  }

  private void sendRequest() throws Exception {
    performHttp("/friend/sendRequest/");
  }

  private void acceptRequest() throws Exception {
    performHttp("/friend/acceptRequest/");
  }

  private void denyRequest() throws Exception {
    performHttp("/friend/denyRequest/");
  }

  private void blockUser() throws Exception {
    performHttp("/friend/block/");
  }

  private void unblockUser() throws Exception {
    performHttp("/friend/unblock/");
  }

  @Test
  public void sendFriendRequestTest() throws Exception {
    sendRequest();

    // Connection should be REQUESTED
    ConnectedUserDto connection = getOther();
    assertEquals(Connection.ConnectionStatus.REQUESTED, connection.getStatus());
  }

  @Test
  public void acceptFriendRequestTest() throws Exception {
    sendRequest();
    acceptRequest();

    // Connection should be FRIENDS
    ConnectedUserDto connection = getOther();
    assertEquals(Connection.ConnectionStatus.FRIENDS, connection.getStatus());
  }

  @Test
  public void denyFriendRequestTest() throws Exception {
    sendRequest();
    denyRequest();

    // Connection should be null
    ConnectedUserDto connection = getOther();
    assertNull(connection.getStatus());
  }

  @Test
  public void blockUserTest() throws Exception {
    blockUser();

    // User shouldn't even be returned
    ConnectedUserDto connection = getOther();
    assertNull(connection);
  }

  @Test
  public void unblockUserTest() throws Exception {
    blockUser();
    unblockUser();

    // Connection should be null (since there wouldn't be one in the DB)
    ConnectedUserDto connection = getOther();
    assertNull(connection.getStatus());
  }
}
