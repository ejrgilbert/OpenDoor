package com.opendoor.persistence.model;

import javax.persistence.*;
import java.util.List;

/**
 * A group of friends a user has in the system
 * @author Elizabeth Gilbert
 */
@Entity
@Table( name = "groups" )
public class Group {

  /**
   * The unique id of this group
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  /**
   * The name of this group
   */
  private String name;

  /**
   * List of all friends in this group
   *
   * One Group Has Many Friends, a Friend is not reused between Group(s) [OneToMany]
   * https://stackoverflow.com/questions/3113885/difference-between-one-to-many-many-to-one-and-many-to-many
   *
   * Cascade:
   * https://stackoverflow.com/questions/2302802/object-references-an-unsaved-transient-instance-save-the-transient-instance-be
   */
  // TODO: FetchType is a short term fix, would prefer LAZY
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<Friend> friends;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<Friend> getFriends() {
    return friends;
  }

  public void setFriends(List<Friend> friends) {
    this.friends = friends;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // -------------------------------------------------
  // ------------- Functional methods ----------------
  // -------------------------------------------------

  public void addFriend(Friend friend) {
    friends.add(friend);
  }

  public void removeFriend(Friend friend) {
    friends.remove(findFriend(friend));
  }

  /**
   * Determine if the friends list contains the passed friend
   * @param friend the friend to check
   * @return true if found, false if not
   */
  public boolean hasFriend(Friend friend) {
    return findFriend(friend) != -1;
  }

  /**
   * Find and return the index of the friend in the friends list
   * equaling the passed friend
   * @param friend the friend to find
   * @return the index of the found friend in the list, -1 if not found
   */
  private int findFriend(Friend friend) {
    for (int i = 0; i < this.friends.size(); i++) {
      if (this.friends.get(i).equals(friend)) return i;
    }

    return -1;
  }

  /**
   * Returns the representation of the passed friend in the group
   *    (so it has the DB ID in it)
   * @param friend the friend to find
   * @return the found friend, null if not found
   */
  public Friend getFriend(Friend friend) {
    int index = findFriend(friend);
    if (index != -1) return this.friends.get(index);

    return null;
  }

  /**
   * Find the friend associated with the specified id
   * @param friendId the id of the friend to find
   * @return the friend object associated with that id, null if not found
   */
  public Friend getFriend(long friendId) {
    for (Friend friend : this.friends ){
      if (friend.getId() == friendId) return friend;
    }

    return null;
  }

  /**
   * Determine if the list of friends in this Group
   * is equal to the list of friends in another group
   * @param otherFriends the group of friends to compare this object's with
   * @return true if equal, false if not
   */
  public boolean friendsEqual(List<Friend> otherFriends) {
    boolean result = true;
    for (Friend friend : this.friends) {
      for (Friend otherFriend : otherFriends) {
        result &= friend.equals(otherFriend);
        if (!result) return result; // If result has changed to false, don't need to check through rest of friends.
      }
    }

    return result;
  }

  /**
   * Determine if this Group is equal to the passed group
   * @param other the group to compare to
   * @return true if they are equal, false if not
   */
  public boolean equals(Group other) {
    return this.id == other.id &&
      this.name.equals(other.name) &&
      friendsEqual(other.friends);
  }

  // -------------------------------------------------
  // ---------------- Constructors -------------------
  // -------------------------------------------------

  public Group(String name, List<Friend> friends) {
    setName(name);
    setFriends(friends);
  }

  public Group() {
    // Empty for Hibernate/JSON
  }
}
