package com.opendoor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * http://www.baeldung.com/spring-security-block-brute-force-authentication-attempts
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
  @Autowired
  private LoginAttemptService loginAttemptService;

  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    WebAuthenticationDetails auth = (WebAuthenticationDetails)
      e.getAuthentication().getDetails();

    loginAttemptService.loginFailed(auth.getRemoteAddress());
  }
}
