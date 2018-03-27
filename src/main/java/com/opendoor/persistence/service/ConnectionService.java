package com.opendoor.persistence.service;

import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@Service( "connectionService" )
public class ConnectionService {

  @Autowired
  private ConnectionRepository connectionRepository;

  private Connection findByUserWithOrder(Long user1, Long user2) {
    // Find status of connection for these users
    // TODO make more efficient
    Iterable<Connection> connections = connectionRepository.findAll();

    for (Connection connection : connections) {
      if ((connection.getUser1().getId() == user1 && connection.getUser2().getId() == user2) ||
              (connection.getUser1().getId() == user2 && connection.getUser2().getId() == user1))
        return connection;
    }

    return null;
  }

  public void save(@Valid Connection connection) throws ConstraintViolationException {
    // First need to find by user and if connection does not exist then add new connection
    // One connection is a bidirectional relationship
    if( connection.getId() == null) {
      Connection other = findByUserWithOrder(connection.getUser2().getId(), connection.getUser1().getId());
      if (other != null) {
        connection.setId(other.getId());
      }
    }

    connectionRepository.save(connection);
  }

  public Connection findByUsers(Long user1, Long user2) {
    Connection connection = findByUserWithOrder(user1, user2);
    if(connection == null) {
        connection = findByUserWithOrder(user2, user1);
    }

    return connection;
  }

  public void delete(Long user1, Long user2) {
    connectionRepository.delete(findByUsers(user1, user2).getId());
  }
}
