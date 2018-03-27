package com.opendoor.security;

import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class GlobalAuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
  @Autowired
  private UserService userService;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Autowired
  private HttpServletRequest request;

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService());
  }

  /**
   * http://www.baeldung.com/spring-security-block-brute-force-authentication-attempts
   * @return
   */
  @Bean
  UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip;
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
          ip = request.getRemoteAddr();
        } else {
          ip = xfHeader.split(",")[0];
        }

        if (loginAttemptService.isBlocked(ip)) {
          throw new RuntimeException("blocked");
        }

        try {
          final com.opendoor.persistence.model.User user = userService.findByUsername(username);
          if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
          }

          return new org.springframework.security.core.userdetails.User(
            user.getAccount().getUsername(), user.getAccount().getPassword(), user.getEnabled(),
            true, true, true,
            AuthorityUtils.createAuthorityList("USER"));
        } catch (final Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
  }
}
