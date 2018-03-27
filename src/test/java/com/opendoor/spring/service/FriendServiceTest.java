package com.opendoor.spring.service;

import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.*;
import com.opendoor.spring.service.util.DataGenerator;
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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = com.opendoor.Application.class,
        properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class FriendServiceTest {
  private DataGenerator generator;

  private User testUser;
  private User testUser2;

  private Group testGroup;

  @Autowired
  private UserService service;

  @Autowired
  private AccountService acctService;

  @Autowired
  private FriendService friendService;

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private WebApplicationContext context;

  @Before
  public void setUp() {
    MockMvc mvc = MockMvcBuilders.webAppContextSetup(context).build();
    generator = new DataGenerator(acctService, service);

    if (service.findByUsername(DataGenerator.USERNAME1) == null){
      testUser = generator.createUser();
      testUser2 = generator.createUser2();
    } else {
      testUser = service.findByUsername(DataGenerator.USERNAME1);
      testUser2 = service.findByUsername(DataGenerator.USERNAME2);
    }
  }

  public Connection getConnection() {
    Connection connection = connectionService.findByUsers(testUser.getId(), testUser2.getId());
    return connection;
  }

  public Connection sendRequest() {
    // Send friend request
    friendService.sendFriendRequest(testUser, testUser2);
    return getConnection();
  }

  public Connection acceptRequest() {
    friendService.acceptFriendRequest(testUser, testUser2);
    return getConnection();
  }

  public Connection denyRequest() {
    friendService.denyFriendRequest(testUser, testUser2);
    return getConnection();
  }

  public Connection blockUser() {
    friendService.blockUser(testUser, testUser2);
    return getConnection();
  }

  public Connection unblockUser() {
    friendService.unblockUser(testUser, testUser2);
    return getConnection();
  }

  @Test
  public void sendFriendRequestTest() {
    Connection connection = sendRequest();
    assertTrue(connection.getStatus() == Connection.ConnectionStatus.REQUESTED);
  }

  @Test
  public void acceptFriendRequestTest() {
    // Send request
    sendRequest();

    // Accept request
    Connection connection = acceptRequest();
    assertTrue(connection.getStatus() == Connection.ConnectionStatus.FRIENDS);
  }

  @Test
  public void denyFriendRequestTest() {
    sendRequest();

    // Deny request
    Connection connection = denyRequest();
    assertNull(connection);
  }

  @Test
  public void blockUserTest() {
    Connection connection = blockUser();
    assertTrue(connection.getStatus() == Connection.ConnectionStatus.BLOCKED);
  }

  @Test
  public void unblockUserTest() {
    Connection connection = unblockUser();
    assertNull(connection);
  }
}
