package com.opendoor.persistence.service;

import com.opendoor.Application;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.AccountRepository;
import com.opendoor.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
 *
 * Interact with Users in the DB through this service
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Get the user that's logged into the application
   * @return The user object representation of who's logged in
   */
  public User getLoggedInUser() {
    // Get the user that's logged in
    String username = Application.getLoggedInUsername();
    return findByUsername(username);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User findByConfirmationToken(String confirmationToken) {
    return userRepository.findByConfirmationToken(confirmationToken);
  }

  public User findById(Long id) { return userRepository.findById(id); }

  public User findByUsername(String username) {
    Account account = accountRepository.findByUsername(username);
    return findByAccount(account);
  }

  public User findByAccount(Account account) { return userRepository.findByAccount(account); }

  private void enabledHelper(Long id, boolean enabled) {
    User user = findById(id);
    user.setEnabled(enabled);
    save(user);
  }

  public void disableUser(Long id) {
    enabledHelper(id, false);
  }

  public void enableUser(Long id) {
    enabledHelper(id, true);
  }

  public void save(User user) { userRepository.save(user); }

  public void deleteUser(Long id) {
    userRepository.delete(id);
  }
}
