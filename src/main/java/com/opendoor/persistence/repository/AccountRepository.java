package com.opendoor.persistence.repository;

import com.opendoor.persistence.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Really clean way to interact with Hibernate DB. Uses Spring Boot to
 * automatically write code for us. Better for simple interactions with DB
 * than having to implement it ourselves.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
  Account findByUsername(String username);
}
