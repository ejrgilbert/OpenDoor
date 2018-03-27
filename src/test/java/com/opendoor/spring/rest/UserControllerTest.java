package com.opendoor.spring.rest;


import com.google.gson.Gson;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.dto.user.UserDto;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
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
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
public class UserControllerTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  private DataGenerator generator;

  private User testUser;
  private String userPassword;

  @Autowired
  private UserService service;

  @Autowired
  private AccountService acctService;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).addFilters(filterChainProxy).build();

    generator = new DataGenerator(acctService, service);

    NewUserDto userDto = new NewUserDto("yes@yes.com", "burtmacklin", "@pr1lR0ck2","Andy", "Dwyer", "");

    if (service.findByUsername("burtmacklin") == null){
      testUser = generator.createUser();
    } else {
      testUser = service.findByUsername("burtmacklin");
    }
    userPassword = "@pr1lR0ck2";
  }

  private String getById(long id) throws Exception {
    MvcResult result = mvc.perform(get("/user/" + id)
      .with(httpBasic("user", "password")))
      .andExpect(status().isOk())
      .andReturn();
    return result.getResponse().getContentAsString();
  }

  @Test
  public void securityTest() throws Exception {
    // Try to call without credentials, should fail
    mvc.perform(get("/user/" + testUser.getAccount().getUsername()))
      .andExpect(status().isUnauthorized());

    // Try to call with credentials of user that DNE
    mvc.perform(get("/user/" + testUser.getAccount().getUsername())
      .with(httpBasic("DNE", "password")))
      .andExpect(status().isUnauthorized());

    // Call with credentials, should pass
    mvc.perform(get("/user/search/" + testUser.getAccount().getUsername())
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword)))
      .andExpect(status().isOk());
  }

  @Test
  public void testFindByUsername() throws Exception {
    MvcResult result = mvc.perform(get("/user/search/" + testUser.getAccount().getUsername())
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword)))
      .andExpect(status().isOk())
      .andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.contains(testUser.getAccount().getUsername()));

    // User DNE
    result = mvc.perform(get("/user/search/" + "dne")
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword)))
      .andExpect(status().isOk())
      .andReturn();
    stringResult = result.getResponse().getContentAsString();

    assertTrue(stringResult.equals(""));
  }

  @Test
  public void testFindById() throws Exception {
    String stringResult = getById(testUser.getId());

    // There should be no greetings in the system.
    assertTrue(stringResult.contains(gson.toJson(new NewUserDto(testUser))));

    // User DNE
    stringResult = getById(10);

    assertTrue(stringResult.equals(""));
  }

  @Test
  public void testUpdateUser() throws Exception {
    String original = testUser.getEmail();
    testUser.setEmail("change@change.com");
    NewUserDto userDto = new NewUserDto(testUser);

    mvc.perform(put("/user/" + testUser.getId())
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword))
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isOk());

    String stringResult = getById(testUser.getId());
    NewUserDto result = gson.fromJson(stringResult, NewUserDto.class);

    assertTrue(result.getEmail().equals(testUser.getEmail()));

    // Invalid user
    testUser.setEmail("invalidemail");
    userDto = new NewUserDto(testUser);
    mvc.perform(put("/user/" + testUser.getId())
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword))
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isBadRequest());

    testUser.setEmail(original);
  }

  @Test
  public void testDisableUser() throws Exception {
    testUser.setEnabled(false);
    mvc.perform(put("/user/disable/" + testUser.getId())
      .with(httpBasic(testUser.getAccount().getUsername(),userPassword)))
      .andExpect(status().isOk());

    String stringResult = getById(testUser.getId());
    assertTrue(stringResult.contains(gson.toJson(new NewUserDto(testUser))));

    // Re-enable the user
    testUser.setEnabled(true);
    mvc.perform(put("/user/enable/" + testUser.getId())
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk());
  }

  @Test
  public void testEnableUser() throws Exception {
    testUser.setEnabled(true);
    mvc.perform(put("/user/enable/" + testUser.getId())
      .with(httpBasic(testUser.getAccount().getUsername(), userPassword)))
      .andExpect(status().isOk());

    String stringResult = getById(testUser.getId());

    // There should be no greetings in the system.
    assertTrue(stringResult.contains(gson.toJson(new NewUserDto(testUser))));
  }
}