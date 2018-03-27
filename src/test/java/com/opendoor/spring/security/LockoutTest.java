package com.opendoor.spring.security;

import com.google.gson.Gson;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.spring.rest.util.DataGenerator;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test that after X failed login attempts, the user is blocked.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {
    com.opendoor.Application.class
  },
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class LockoutTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  private DataGenerator generator;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).addFilters(filterChainProxy).build();

    generator = new DataGenerator(mvc);
  }

  @Test
  public void verifyAuthenticationLockout() throws Exception {
    User testUser = generator.createNewUser();
    assertNotNull(testUser);

    // Try to fail logging in 11 times, "blocked" the 11 time
    int attempts = 0;
    while(attempts < 10) {
      MvcResult result = mvc.perform(get("/user/search/" + DataGenerator.USERNAME)
        .with(httpBasic(DataGenerator.USERNAME, "invalid")))
        .andExpect(status().isUnauthorized())
        .andReturn();

      assertTrue(result.getResponse().getErrorMessage().equalsIgnoreCase("Bad credentials"));
      attempts++;
    }

    // Should fail with "blocked" message
    MvcResult result = mvc.perform(get("/user/search/" + DataGenerator.USERNAME)
      .with(httpBasic(DataGenerator.USERNAME, "invalid")))
      .andExpect(status().isUnauthorized())
      .andReturn();

    assertTrue(result.getResponse().getErrorMessage().equalsIgnoreCase("blocked"));

    // Clean up
//    generator.deleteUser(accountService, userService, testUser.getId(), DataGenerator.USERNAME);
  }
}
