package com.opendoor.persistence.repository;

import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
 *
 * Interaction with the DB as coded by CrudRepository (we don't have to implement the logic).
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  User findByEmail(String email);
  User findByConfirmationToken(String confirmationToken);
  User findById(Long id);
  User findByAccount(Account account);
}
