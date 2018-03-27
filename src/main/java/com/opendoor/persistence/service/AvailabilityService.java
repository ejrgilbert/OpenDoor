package com.opendoor.persistence.service;

import com.opendoor.persistence.model.Availability;
import com.opendoor.persistence.model.Friend;
import com.opendoor.persistence.model.Group;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.repository.FriendRepository;
import com.opendoor.utils.HibernateUtil;
import org.hibernate.type.Type;
import org.hibernate.*;
import org.hibernate.boot.model.source.spi.HibernateTypeSource;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.AVA;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to interact with the accounts in the DB through.
 */
@Service( "availabilityService" )
public class AvailabilityService {

  @Autowired
  private GroupService groupService;

  @Autowired
  private UserService userService;

  @Autowired
  private FriendRepository friendRepository;

  /**
   * Get all users the passed user can see the availabilities of
   *
   * Create a sql statement that gets all rows in friends table with userId as the guest_id and available as true
   * Takes the host_ids in these selected rows and returns that list of users (the hosts)
   *
   * @param guestId The id of the user to find the availabilities they have access to
   * @return the list of hosts the passed in guest can see the availability of
   */
  public List<User> getAvailabilities(long guestId) {
    // TODO make more efficient
    Iterable<Friend> allFriends = friendRepository.findAll();

    List<Long> hostIds = new ArrayList<>();
    for (Friend friend : allFriends)
      if (friend.getGuest().getId() == guestId && friend.isAvailable())
        hostIds.add(friend.getHost().getId());

    List<User> hosts = new ArrayList<>();
    for (Long id : hostIds)
      hosts.add(userService.findById(id));
    return hosts;
  }

  /**
   * Perform actual logic to open/close a user's door to the specified group
   * @param group set user's door as opened/closed to friends in this group
   * @param isAvailable true if open door, false if close door
   */
  private void toggleAvailability(Group group, boolean isAvailable) {
    for (Friend friend : group.getFriends()) {
      friend.setAvailable(isAvailable);
      friendRepository.save(friend);
    }
  }

  /**
   * Open the person's door to the specified Groups
   * @param userId
   * @param availability
   */
  public void openDoor(long userId, Availability availability, List<Group> groups) throws IllegalArgumentException {
    User user = userService.findById(userId);

    // Verify that the selected group is actually owned by the user
    for (Group group : groups) {
      if (!user.hasGroup(group.getId())) throw new IllegalArgumentException("One of these groups doesn't belong to you!");
    }

    // Set the user's availability
    user.setAvailability(availability);
    userService.save(user);

    // Now all groups _are_ owned by the user
    for (Group group: groups) {
      // TODO Make constants class for defining error messages
      toggleAvailability(group, true);
    }
  }

  /**
   * Close the person's door to all of their groups
   * @param userId
   */
  public void closeDoor(long userId) {
    User user = userService.findById(userId);
    List<Group> groups = user.getGroups();

    // Set the user's availability object as null
    user.setAvailability(null);
    userService.save(user);

    for (Group group: groups) {
      toggleAvailability(group, false);
    }
  }

  public void updateDoor(Long userId, Availability availability) {
    User user = userService.findById(userId);

    // Set the user's availability
    user.setAvailability(availability);
    userService.save(user);
  }

}
