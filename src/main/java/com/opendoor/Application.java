package com.opendoor;

import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
  public static final String URL = "http://localhost:8080/";

  public static String getLoggedInUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName(); //get logged in username

    return username;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

//  @Bean
//  CommandLineRunner init(final AccountService accountService, final UserService userService) {
//
//    return new CommandLineRunner() {
//
//      @Override
//      public void run(String... arg0) throws Exception {
//        accountService.save(new Account("user", "password"));
//        Account account = accountService.findByUsername("user");
//
//        User user = new User(new NewUserDto("email@email.com", "", "",
//          "bob", "bob", ""), account);
//        userService.save(user);
//        User saved = userService.findByUsername(user.getAccount().getUsername());
//        userService.enableUser(saved.getId()); // So we don't have to confirm before use
//      }
//    };
//  }
}

