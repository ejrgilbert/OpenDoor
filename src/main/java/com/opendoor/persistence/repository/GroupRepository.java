package com.opendoor.persistence.repository;

import com.opendoor.persistence.model.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Really clean way to interact with Hibernate DB. Uses Spring Boot to
 * automatically write code for us. Better for simple interactions with DB
 * than having to implement it ourselves.
 */
@Repository("groupRepository")
public interface GroupRepository extends CrudRepository<Group, Long> {
  List<Group> findAll();
  Group findById(Long id);
}
