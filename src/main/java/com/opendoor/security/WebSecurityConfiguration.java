package com.opendoor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * User Registration:
 * https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
 *
 * User Authentication:
 * http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot/
 *
 * Password Hashing:
 * http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
 *
 * Configure which endpoints on the REST API are restricted via security.
 * The /register and /confirm are open open for anyone to access.
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder(11);
  }

  /**
   * We are injecting our implementation of the users details service
   * We are defining an authentication provider that references our details service
   * We are also enabling the password encoder
   * @return the system's DaoAuthenticationProvider
   */
  @Bean
  public DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/register", "/confirm", "/logout").permitAll()
//      .antMatchers(HttpMethod.OPTIONS, "/user/login").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .anyRequest().authenticated().and()
      .httpBasic().and()
      .csrf().disable();
  }


}
