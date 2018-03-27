package com.opendoor.dto.user;

import com.opendoor.persistence.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * The Data Transfer Object (dto) that will enable us to
 * pass information to and from the backend via Rest API.
 *
 * Need to have this to enable us to verify password strength.
 * @author Elizabeth Gilbert
 */
public class NewUserDto {

  /**
   * The email of the user
   */
  @Email(message = "Please provide a valid e-mail")
  @NotEmpty(message = "Please provide an e-mail")
  private String email;

  /**
   * The username of the user
   */
  @NotNull
  private String username;

  /**
   * The password of the user (unhashed)
   */
  @NotNull
  private PasswordDto password;

  /**
   * The firstName of the user
   */
  @NotEmpty(message = "Please provide your first name")
  private String firstName;

  /**
   * The lastName of the user
   */
  @NotEmpty(message = "Please provide your last name")
  private String lastName;

  private String confirmationToken;

  // --------------------------------------------------
  // ------------- Getters and Setters ----------------
  // --------------------------------------------------

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public PasswordDto getPassword() {
    return password;
  }

  public void setPassword(PasswordDto password) {
    this.password = password;
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

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

  // --------------------------------------------------
  // ----------------- Constructors -------------------
  // --------------------------------------------------

  public NewUserDto(String email, String username, String password, String firstName, String lastName, String confirmationToken) {
    setEmail(email);
    setUsername(username);
    setPassword(new PasswordDto(password));
    setFirstName(firstName);
    setLastName(lastName);
    setConfirmationToken(confirmationToken);
  }

  public NewUserDto(User user) {
    this(user.getEmail(), user.getAccount().getUsername(),
      user.getAccount().getPassword(), user.getFirstName(), user.getLastName(), user.getConfirmationToken());
  }

  public NewUserDto() {
  // Need default constructor for JSON mapping
  }
}
