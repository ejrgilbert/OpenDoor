package com.opendoor.dto.user;

import com.opendoor.persistence.model.Availability;
import com.opendoor.persistence.model.User;

import javax.validation.Valid;

public class UserDto {
  private long id;
  private String firstName;
  private String lastName;
  @Valid
  private Availability availability;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Availability getAvailability() {
    return availability;
  }

  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  public UserDto(User user) {
    this(user.getId(), user.getFirstName(), user.getLastName(), user.getAvailability());
  }

  public UserDto(long id, String firstName, String lastName, Availability availability) {
    setId(id);
    setFirstName(firstName);
    setLastName(lastName);
    setAvailability(availability);
  }
}
