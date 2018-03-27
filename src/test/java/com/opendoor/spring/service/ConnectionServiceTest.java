package com.opendoor.spring.service;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.ConnectionRepository;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.ConnectionService;
import com.opendoor.persistence.service.UserService;
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

import javax.xml.crypto.Data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = com.opendoor.Application.class,
        properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class ConnectionServiceTest {
  private DataGenerator generator;

  private User testUser;
  private User testUser2;

  private Group testGroup;

  @Autowired
  private UserService service;

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private ConnectionRepository connectionRepository;

  @Autowired
  private AccountService acctService;

  private static Gson gson = new Gson();

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

  @Test
  public void saveTest() {
    Iterable<Connection> allConnections = connectionRepository.findAll();
    assertTrue(Iterables.size(allConnections) == 0);

    Connection connection = new Connection();
    connection.setUser1(testUser);
    connection.setUser2(testUser2);
    connection.setStatus(Connection.ConnectionStatus.FRIENDS);
    connectionService.save(connection);

    allConnections = connectionRepository.findAll();
    assertTrue(Iterables.size(allConnections) == 1);

    connection.setUser1(testUser2);
    connection.setUser2(testUser);
    connection.setStatus(Connection.ConnectionStatus.FRIENDS);
    connectionService.save(connection);

    allConnections = connectionRepository.findAll();
    assertTrue(Iterables.size(allConnections) == 1);
  }

  @Test
  public void findByUsersTest() {
    // Create a new connection
    Connection connection = new Connection();
    connection.setUser1(testUser);
    connection.setUser2(testUser2);
    connection.setStatus(Connection.ConnectionStatus.FRIENDS);
    connectionService.save(connection);

    // Verify we can find it with order user1, user2
    Connection fromDatabase = connectionService.findByUsers(testUser.getId(), testUser2.getId());
    assertNotNull(fromDatabase);

    // Verify we can find it with order user2, user1
    fromDatabase = connectionService.findByUsers(testUser2.getId(), testUser.getId());
    assertNotNull(fromDatabase);

  }
}
