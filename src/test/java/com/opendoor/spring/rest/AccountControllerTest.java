package com.opendoor.spring.rest;

import com.google.gson.Gson;
import com.opendoor.dto.user.ConnectedUserDto;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.dto.user.PasswordDto;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.UserService;
import com.opendoor.spring.rest.util.DataGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {
    com.opendoor.Application.class
  },
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Short-term fix until we can find a more efficient way to reset the database
public class AccountControllerTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

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

    generator = new DataGenerator(mvc);
  }

  @Test
  public void testAll() throws Exception {
    NewUserDto userDto = new NewUserDto("yes@yes.com", "burtmacklin", "@pr1lR0ck2","Andy", "Dwyer", "");

    // Create a new user
    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isOk());

    // Test that we can get the user now
    MvcResult result = mvc.perform(get("/user/search/" + userDto.getUsername())
      .with(httpBasic("user", "password")))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    assertTrue(stringResult.contains(userDto.getUsername()));
    toDelete = gson.fromJson(stringResult, User.class);

    // Now we need to get the user's confirmation token now that we have their DB id
    result = mvc.perform(get("/user/" + toDelete.getId())
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk()).andReturn();
    stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    assertTrue(stringResult.contains(userDto.getUsername()));
    User withToken = gson.fromJson(stringResult, User.class);
    toDelete.setConfirmationToken(withToken.getConfirmationToken());

    // Verify with incorrect confirmation token
    HashMap<String, String> request = new HashMap<>();
    request.put("token", "invalidToken");

    result = mvc.perform(get("/confirm")
      .param("token", "invalidToken"))
        .andExpect(status().isBadRequest())
        .andReturn();

    stringResult = result.getResponse().getContentAsString();
    assertTrue(stringResult.contains("Oops!  This is an invalid confirmation link."));

    // Verify with correct confirmation token
    result = mvc.perform(get("/confirm")
      .param("token", toDelete.getConfirmationToken()))
        .andExpect(status().isOk())
        .andReturn();
    stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.contains("Email successfully verified"));
  }

  @Test
  public void invalidTest() throws Exception {
    toDelete = generator.createNewUser();

    // Try to add another person with the same email
    NewUserDto userDto = new NewUserDto("yes@yes.com", "unique", "@pr1lR0ck2","Andy", "Dwyer", "");

    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isBadRequest());

    MvcResult result = mvc.perform(get("/user/search/" + userDto.getUsername())
      .with(httpBasic("user", "password")))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.equals(""));

    // Try to add another person with the same username
    userDto = new NewUserDto("unique@yes.com", "burtmacklin", "@pr1lR0ck2","Andy", "Dwyer", "");

    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testPasswordConstraint() throws Exception {
    NewUserDto userDto = new NewUserDto("yes@yes.com", "burtmacklin", "invalid","Andy", "Dwyer", "");

    // Try to create a new user with an invalid password
    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isBadRequest())
      .andReturn();

    // Should not be able to get the user
    MvcResult result = mvc.perform(get("/user/search/" + userDto.getUsername())
      .with(httpBasic("user", "password")))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.equals(""));
  }

  @Test
  public void passwordConstraintTest() throws Exception {
    // Add in user with password that's too short
    NewUserDto testUser = new NewUserDto("yes@yes.com", "burtmacklin", "@pr1l","Andy", "Dwyer", "");

    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(testUser)))
      .andExpect(status().isBadRequest());

    // Test that the user was not added.
    MvcResult result = mvc.perform(get("/user/search/" + testUser.getUsername())
      .with(httpBasic("user","password")))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.equals(""));
  }

  @Test
  public void resetPasswordTest() throws Exception {
    toDelete = generator.createNewUser();

    // Try to reset that user's password with an invalid password
    String oldPassword = generator.getNewUsersPassword();
    PasswordDto passwordDto = new PasswordDto("invalid");

    mvc.perform(post("/resetPassword")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(passwordDto))
      .with(httpBasic(DataGenerator.USERNAME, DataGenerator.PASSWORD)))
      .andExpect(status().isBadRequest());

    // Reset that user's password
    String newPassword = "n3wP@55w0rd";
    passwordDto = new PasswordDto(newPassword);

    mvc.perform(post("/resetPassword")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(passwordDto))
      .with(httpBasic(DataGenerator.USERNAME, DataGenerator.PASSWORD)))
      .andExpect(status().isOk());

    // Should be able to login with that new password
    // (first we'll have to 'log out' by calling REST API as someone else)
    mvc.perform(get("/user/search/user")
      .with(httpBasic("user","password")))
      .andExpect(status().isOk());

    // With old password
    mvc.perform(get("/user/search/user")
      .with(httpBasic(DataGenerator.USERNAME, DataGenerator.PASSWORD)))
      .andExpect(status().isUnauthorized());

    // With new password
    mvc.perform(get("/user/search/user")
      .with(httpBasic(DataGenerator.USERNAME, newPassword)))
      .andExpect(status().isOk());
  }
}