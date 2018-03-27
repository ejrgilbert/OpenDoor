package com.opendoor.spring.service;

import com.google.gson.Gson;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.service.AccountService;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = com.opendoor.Application.class,
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class AccountServiceTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private AccountService service;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void testAll() {
    // Keep in mind the username _must_ be unique
    // (there already exists by default a user named "user")
    // See Application.init method (that's where that user is made)
    Account testAccount = new Account("user1", "password");
    service.save(testAccount);

    Account response = service.findByUsername("user1");
    assertNotNull(response);
    assertEquals("user1", response.getUsername());
    assertNotNull(response.getPassword());
    assertNotEquals("password", response.getPassword()); // Should be hashed, not plaintext

    service.deleteByUsername("user1");
    response = service.findByUsername("user1");
    assertNull(response);

    Account acct = null;
  }
}
