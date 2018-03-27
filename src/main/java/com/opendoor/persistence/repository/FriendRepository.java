package com.opendoor.persistence.repository;

import com.opendoor.persistence.model.Friend;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Really clean way to interact with Hibernate DB. Uses Spring Boot to
 * automatically write code for us. Better for simple interactions with DB
 * than having to implement it ourselves.
 */
@Repository("friendRepository")
public interface FriendRepository extends CrudRepository<Friend, Long> {
  Friend findById(Long id);
}
