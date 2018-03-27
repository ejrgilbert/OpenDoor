package com.opendoor.dto.group;

import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupDto {
  private long id;
  private String name;
  private List<FriendDto> friends;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<FriendDto> getFriends() {
    return friends;
  }

  public void setFriends(List<FriendDto> friends) {
    this.friends = friends;
  }

  public GroupDto(Group group) {
    setId(group.getId());
    setName(group.getName());

    friends = new ArrayList<>();
    for (Friend friend : group.getFriends()) {
      friends.add(new FriendDto(friend));
    }
  }

  public GroupDto() {
    // For json mappings
  }
}
