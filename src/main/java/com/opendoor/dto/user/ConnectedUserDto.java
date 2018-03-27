package com.opendoor.dto.user;

import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.User;

public class ConnectedUserDto {

  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private Connection.ConnectionStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) { this.id = id; }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Connection.ConnectionStatus getStatus() {
    return status;
  }

  public void setStatus(Connection.ConnectionStatus status) {
    this.status = status;
  }

  public ConnectedUserDto() {

  }

  public ConnectedUserDto(User user, Connection connection) {
    setId(user.getId());
    setUsername(user.getAccount().getUsername());
    setFirstName(user.getFirstName());
    setLastName(user.getLastName());
    if (connection == null) {
      setStatus(null);
    } else setStatus(connection.getStatus());
  }
}
