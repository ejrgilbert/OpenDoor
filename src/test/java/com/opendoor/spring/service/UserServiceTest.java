package com.opendoor.spring.service;

import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.security.GlobalAuthenticationConfiguration;
import com.opendoor.security.WebSecurityConfiguration;
import com.opendoor.spring.service.util.DataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = com.opendoor.Application.class,
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@Import({
  WebSecurityConfiguration.class,
  GlobalAuthenticationConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class UserServiceTest {
  private MockMvc mvc;

  private DataGenerator generator;

  private User testUser;

  @Autowired
  private UserService service;

  @Autowired
  private AccountService acctService;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Filter springSecurityFilterChain;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();

    generator = new DataGenerator(acctService, service);

    if (service.findByUsername("burtmacklin") == null){
      testUser = generator.createUser();
    } else {
      testUser = service.findByUsername("burtmacklin");
    }
  }

  @Test
  public void testFindByEmail() {
    User user = service.findByEmail(testUser.getEmail());

    assertNotNull(user);
    assertTrue(testUser.equals(user));
  }

  @Test
  public void testFindById() {
    User user = service.findById(testUser.getId());

    assertNotNull(user);
    assertTrue(testUser.equals(user));
  }

  @Test
  public void testFindByAccount() {
    User user = service.findByAccount(testUser.getAccount());

    assertNotNull(user);
    assertTrue(testUser.equals(user));
  }

  @Test
  public void testDisableUser() {
    service.disableUser(testUser.getId());
    User user = service.findById(testUser.getId());

    assertNotNull(user);
    assertEquals(false, user.getEnabled());
  }

  @Test
  public void testEnableUser() {
    service.enableUser(testUser.getId());
    User user = service.findById(testUser.getId());

    assertNotNull(user);
    assertEquals(true, user.getEnabled());
  }

  @Test
  public void testSaveUser() {
    String originalEmail = testUser.getEmail();

    testUser.setEmail("changed@not.edu");
    service.save(testUser);
    User user = service.findById(testUser.getId());

    assertNotNull(user);
    assertTrue(testUser.equals(user));

    // Fix testUser back
    testUser.setEmail(originalEmail);
  }
}
