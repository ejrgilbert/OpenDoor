package com.opendoor.spring.rest.util;

import com.google.gson.Gson;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataGenerator {
  private static Gson gson = new Gson();
  MockMvc mvc;
  NewUserDto userDto;

  public static final String USERNAME = "burtmacklin";
  public static final String PASSWORD = "@pr1lR0ck2";

  public DataGenerator(MockMvc mvc) {
    this.mvc = mvc;
    this.userDto = new NewUserDto("yes@yes.com", USERNAME, PASSWORD,"Andy", "Dwyer", "");
  }

  public String getNewUsersPassword() {
    return this.userDto.getPassword().getPassword();
  }

  public User createNewUser() throws Exception {
    // Create a new user
    mvc.perform(post("/register")
      .contentType(MediaType.APPLICATION_JSON)
      .content(gson.toJson(userDto)))
      .andExpect(status().isOk());

    User testUser = confirmEmail(userDto);

    return testUser;
  }

  public User confirmEmail(NewUserDto userDto) throws Exception {
    // Test that we can get the user now (need to do this to get the user's DB id
    MvcResult result = mvc.perform(get("/user/search/" + userDto.getUsername())
      .with(httpBasic("user", "password")))
      .andExpect(status().isOk()).andReturn();
    String stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    assertTrue(stringResult.contains(userDto.getUsername()));
    User testUser = gson.fromJson(stringResult, User.class);

    // Now we need to get the user's confirmation token now that we have their DB id
    result = mvc.perform(get("/user/" + testUser.getId())
            .with(httpBasic("user", "password")))
            .andExpect(status().isOk()).andReturn();
    stringResult = result.getResponse().getContentAsString();

    assertNotNull(stringResult);
    assertTrue(stringResult.contains(userDto.getUsername()));
    User withToken = gson.fromJson(stringResult, User.class);
    testUser.setConfirmationToken(withToken.getConfirmationToken());

    // Confirm email address
    mvc.perform(get("/confirm")
      .param("token", testUser.getConfirmationToken()))
        .andExpect(status().isOk());

    // Test that user is now enabled and can use their credentials
    mvc.perform(get("/user/search/" + userDto.getUsername())
            .with(httpBasic(userDto.getUsername(), "@pr1lR0ck2")))
            .andExpect(status().isOk());

    return testUser;
  }

  public void deleteUser(AccountService accountService, UserService userService, long id, String username) {
    userService.deleteUser(id);
    accountService.deleteByUsername(username);
  }


}
