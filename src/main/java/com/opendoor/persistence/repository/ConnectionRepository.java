package com.opendoor.persistence.repository;

import com.opendoor.persistence.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("connectionRepository")
public interface ConnectionRepository extends CrudRepository<Connection, Long> {
    List<Connection> findByUser1(Long userId);
}
