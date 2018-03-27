package com.opendoor.persistence.model;

import javax.persistence.*;

/**
 * A friend in a group
 * @author Elizabeth Gilbert
 */
@Entity
@Table( name = "friend" )
public class Friend {

  /**
   * Unique identifier in the DB
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  /**
   * Many groups can have the same guest (Many to One)
   * https://dzone.com/tutorials/java/hibernate/hibernate-example/hibernate-mapping-many-to-one-using-annotations-1.html
   */
  @ManyToOne(fetch = FetchType.EAGER)
  private User guest;

  /**
   * Many friends will have the same host (Many to One)
   * https://dzone.com/tutorials/java/hibernate/hibernate-example/hibernate-mapping-many-to-one-using-annotations-1.html
   */
  @ManyToOne(fetch = FetchType.EAGER)
  private User host;

  /**
   * The name that this group has associated with this user.
   */
  private String name;

  /**
   * Since friend not used between groups, maybe have `available` a boolean in the Friend object?
   * This would put this value in the correct spot in the DB.
   */
  private boolean available;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getGuest() {
    return guest;
  }

  public void setGuest(User user) {
    this.guest = user;
  }

  public User getHost() {
    return host;
  }

  public void setHost(User user) {
    this.host = user;
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

  // -------------------------------------------------
  // ------------- Functional methods ----------------
  // -------------------------------------------------

  /**
   * Not checking for id being equal since you could be adding a friend
   * not in the DB yet.
   * @param other the Friend to compare this one to
   * @return true if equal, false if not
   */
  public boolean equals(Friend other) {
    return this.available == other.available &&
      this.guest.equals(other.guest) &&
      this.name.equals(other.name);
  }

  // -------------------------------------------------
  // ---------------- Constructors -------------------
  // -------------------------------------------------

  public Friend(User user) {
    setGuest(user);
    setName(user.getFirstName() + " " + user.getLastName());
    setAvailable(false);
  }

  public Friend() {
    // Empty for Hibernate
  }
}
