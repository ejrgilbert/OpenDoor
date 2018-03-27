package com.opendoor.persistence.service;

import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.FriendRepository;
import com.opendoor.persistence.repository.GroupRepository;
import com.opendoor.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interact with Groups in the DB through this service
 */
@Service( "groupService" )
public class GroupService {
  public static final String ALL_FRIENDS_GROUP_NAME = "All Friends";

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private FriendRepository friendRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Get the All Friends group associated with the specified user
   * @param userId the user to get the All Friends group for
   * @return The all friends group of the user
   */
  public Group getAllFriendsGroup(long userId) {
    return getByName(userId, ALL_FRIENDS_GROUP_NAME);
  }

  /**
   * Get the group for the specified user by its id
   * @param userId the user to get the group for
   * @param groupId the id of the group to get
   * @return null if the group was not found under the user, the group if it was
   */
  public Group getById(long userId, long groupId) {
    User user = userRepository.findById(userId);
    return user.getGroup(groupId);
  }

  /**
   * Get the group for the specified user by its id
   * @param userId the user to get the group for
   * @param groupName the name of the group to get
   * @return null if the group was not found under the user, the group if it was
   */
  public Group getByName(long userId, String groupName) {
    User user = userRepository.findById(userId);
    return user.getGroup(groupName);
  }

  public Friend addToAllFriendsGroup(long hostId, long guestId) {
    User user = userRepository.findById(guestId);
    Friend friend = new Friend(user);

    Group allFriends = getAllFriendsGroup(hostId);
    return addToGroup(hostId, allFriends.getId(), friend);
  }

  /**
   * Add a user to this group
   * @param hostId the user that's logged into the system, should be the "host" of the friend
   * @param groupId the id of the group to add to
   * @param friend the friend to add to this group
   * @throws IllegalArgumentException if the friend is already in the group
   *    or the group is not "owned" by the specified user
   */
  public Friend addToGroup(long hostId, long groupId, Friend friend) throws IllegalArgumentException {
    Group group = getById(hostId, groupId);

    // Checks that the given host has the given group
    if (group == null)
      throw new IllegalArgumentException("This group does not belong to you!");

    // and that the given group does not already have the given friend
    if (group.hasFriend(friend))
      throw new IllegalArgumentException("This friend is already in your group!");

    group.addFriend(friend);

    groupRepository.save(group);

    // Now let's get the friend we just saved and return the object with the DB id now
    group = getById(hostId, groupId);
    return group.getFriend(friend);
  }

  /**
   *
   * @param hostId the user that's logged into the system, should be the "host" of the friend
   * @param groupId the id of the group to remove from
   * @param friend the friend to remove from the group
   * @throws IllegalArgumentException if the friend is not in the group
   *    or the group is not "owned" by the specified user
   */
  public void removeFromGroup(long hostId, long groupId, Friend friend) throws IllegalArgumentException {
    Group group = getById(hostId, groupId);

    // Checks that the given host has the given group
    if (group == null)
      throw new IllegalArgumentException("This group does not belong to you!");

    // and that the given group has the given friend
    if (!group.hasFriend(friend))
      throw new IllegalArgumentException("This friend isn't in your group yet!");

    group.removeFriend(friend);

    groupRepository.save(group);
  }

  /**
   * Since friends not shared by groups, we can just edit the friend associated with friendId
   * @param hostId the user that's logged into the system, should be the "host" of the friend
   * @param groupId the id of the group that the friend is supposed to be in
   * @param friendId the friend to edit
   * @param name the name to change the friend's name to
   * @throws IllegalArgumentException if the friend does not exist in the DB
   *    or the group is not "owned" by the specified user
   */
  public void editFriendName(long hostId, long groupId, long friendId, String name) throws IllegalArgumentException {
    Group group = getById(hostId, groupId);

    // Checks that the given host has the given group
    if (group == null)
      throw new IllegalArgumentException("This group does not belong to you!");
    Friend friend = group.getFriend(friendId);

    // and that the given group has the given friend
    if (friend == null)
      throw new IllegalArgumentException("This friend doesn't exist!");

    friend.setName(name);
    friendRepository.save(friend);
  }
}
