package com.opendoor.persistence.model;

import com.opendoor.dto.user.NewUserDto;
import com.opendoor.persistence.service.GroupService;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
 *
 * Contains an Account for every user. The Account contains the username and password
 * whereas, the purpose of the User is for extraneous information.
 * @author Elizabeth Gilbert
 */
@Entity
@Table(name = "user")
public class User {

  /**
   * The database id of this user
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  /**
   * The email of this user
   */
  @Column(nullable = false, unique = true)
  @Email(message = "Please provide a valid e-mail")
  @NotEmpty(message = "Please provide an e-mail")
  private String email;

  /**
   * The first name of this user
   */
  @Column(name = "first_name")
  @NotEmpty(message = "Please provide your first name")
  private String firstName;

  /**
   * The last name of this user
   */
  @Column(name = "last_name")
  @NotEmpty(message = "Please provide your last name")
  private String lastName;

  /**
   * Whether or not this user is enabled (if they are not enabled they cannot
   * use their account to authenticate with the system)
   *
   * In order to enable a user, they must confirm their email
   */
  private boolean enabled;

  @Column(name = "confirmation_token")
  private String confirmationToken;

  /**
   * https://stackoverflow.com/questions/2302802/object-references-an-unsaved-transient-instance-save-the-transient-instance-be
   */
  @OneToOne
  private Account account;

  /**
   * List of all this user's groups
   *
   * One User Has Many Groups, a Group is not reused between User(s) [OneToMany]
   * https://stackoverflow.com/questions/3113885/difference-between-one-to-many-many-to-one-and-many-to-many
   */
  // TODO: FetchType is a short term fix, would prefer LAZY
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<Group> groups;

  /**
   * The availability of this user
   */
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Availability availability;

  // --------------------------------------------------
  // ------------- Getters and Setters ----------------
  // --------------------------------------------------

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean value) {
    this.enabled = value;
  }

  public List<Group> getGroups() { return groups; }

  public void setGroups(List<Group> groups) { this.groups = groups; }

  public Availability getAvailability() {
    return availability;
  }

  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

  // --------------------------------------------------
  // ------------- Functional Methods -----------------
  // --------------------------------------------------

  public boolean groupsEqual(List<Group> other) {
    boolean result = true;
    for (Group group : this.groups) {
      for (Group otherGroup : other) {
        result &= group.equals(otherGroup);
        if (!result) return result; // If result has changed to false, don't need to check through rest of groups.
      }
    }

    return result;
  }

  public void update(NewUserDto userDto) {
    setEmail(userDto.getEmail());
    account.setUsername(userDto.getUsername());
    // Will not set password this way
    setFirstName(userDto.getFirstName());
    setLastName(userDto.getLastName());
  }

  public boolean equals(User other) {
    Hibernate.initialize(this.getGroups());

    return this.id == other.id &&
      this.account.equals(other.account) &&
      this.firstName.equals(other.firstName) &&
      this.lastName.equals(other.lastName) &&
      this.email.equals(other.email) &&
      groupsEqual(other.groups) &&
      this.enabled == other.enabled;
  }

  /**
   * Find and return the group with the specified name
   * @param name the name to find
   * @return null if can't find, group if can find
   */
  public Group getGroup(String name) {
    for (Group group : this.groups) {
      if (group.getName().equals(name))
        return group;
    }

    return null;
  }

  /**
   * Find and return the group with the specified id
   * @param id the name to find
   * @return null if can't find, group if can find
   */
  public Group getGroup(long id) {
    for (Group group : this.groups) {
      if (group.getId() == id)
        return group;
    }

    return null;
  }

  /**
   * Check whether or not the user has a group with
   * the specified name
   * @param name the name of the group to check
   * @return true if user has a group with this name, false if not
   */
  public boolean hasGroup(String name) {
    return getGroup(name) != null;
  }

  /**
   * Check whether or not the user has a group with
   * the specified id
   * @param id the name of the group to check
   * @return true if user has a group with this name, false if not
   */
  public boolean hasGroup(long id) {
    return getGroup(id) != null;
  }

  // --------------------------------------------------
  // ---------------- Constructors --------------------
  // --------------------------------------------------

  public User(NewUserDto dto, Account account) {
    setEmail(dto.getEmail());
    setFirstName(dto.getFirstName());
    setLastName(dto.getLastName());
    setAccount(account);

    // All users have at least one group (for all friends)
    // TODO: Is this the most efficient data type?
    ArrayList<Group> groups = new ArrayList<>();
    Group group = new Group(GroupService.ALL_FRIENDS_GROUP_NAME, new ArrayList<Friend>());
    groups.add(group);
    setGroups(groups);

    // Confirmation token and enabled are handled in the RegisterController
  }

  // For Hibernate
  public User() {

  }
}
