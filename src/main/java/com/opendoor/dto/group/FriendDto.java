package com.opendoor.dto.group;

import com.opendoor.dto.user.UserDto;
import com.opendoor.persistence.model.Friend;

import javax.validation.Valid;

public class FriendDto {
  private long id;
  @Valid
  private UserDto guest;
  @Valid
  private UserDto host;
  private String name;
  private boolean available;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UserDto getGuest() {
    return guest;
  }

  public void setGuest(UserDto guest) {
    this.guest = guest;
  }

  public UserDto getHost() {
    return host;
  }

  public void setHost(UserDto host) {
    this.host = host;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public FriendDto(Friend friend) {
    setId(friend.getId());
    setGuest(new UserDto(friend.getGuest()));
    setHost(new UserDto(friend.getHost()));
    setName(friend.getName());
    setAvailable(friend.isAvailable());
  }

  public FriendDto() {
    // Constructor for json
  }
}
