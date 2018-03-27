package com.opendoor.persistence.service;

import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

/**
 * http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot/
 *
 * Service to interact with the accounts in the DB through.
 */
@Service( "accountService" )
public class AccountService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AccountRepository accountRepository;

  /**
   * This must be called through the save(AccountDto) because otherwise the
   * Account.password will not be validated to meet constraints.
   */
  public void save(@Valid Account account) throws ConstraintViolationException {
    // Encrypt the password before saving to DB
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    accountRepository.save(account);
  }

  public Account findByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  public void deleteByUsername(String username) {
    accountRepository.delete(findByUsername(username));
  }
}
