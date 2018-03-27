package com.opendoor.persistence.service;

import com.opendoor.persistence.model.Connection;
import com.opendoor.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class will use both ConnectionService and GroupService
 * to actually connect users as friends on the back-end
 */
@Service("friendService")
public class FriendService {
  @Autowired
  public GroupService groupService;

  @Autowired
  public ConnectionService connectionService;

  @Autowired
  public UserService userService;

  public void sendFriendRequest(User user, User other) {
    connectionService.save(new Connection(user, other, Connection.ConnectionStatus.REQUESTED));
  }

  /**
   * Accept the friend request between the logged-in user
   * and the passed in user
   * @param other the user to accept the friend request of
   */
  public void acceptFriendRequest(User user, User other) {
    // Add to all friends group
    groupService.addToAllFriendsGroup(user.getId(), other.getId());

    // Update the connection
    connectionService.save(new Connection(user, other, Connection.ConnectionStatus.FRIENDS));
  }

  public void denyFriendRequest(User user, User other) {
    connectionService.delete(user.getId(), other.getId());
  }

  public void blockUser(User user, User other) {
    connectionService.save(new Connection(user, other, Connection.ConnectionStatus.BLOCKED));
  }

  public void unblockUser(User user, User other) {
    connectionService.delete(user.getId(), other.getId());
  }
}
