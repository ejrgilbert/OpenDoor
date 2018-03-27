package com.opendoor.persistence.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot/
 *
 * This model is to contain solely what is needed for authentication. Thus
 * we will only have username and password.
 * @author Elizabeth Gilbert
 */
@Entity
@Table( name = "account" )
public class Account {

  /**
   * The id of this account
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The username of this account
   */
  @Column(unique = true)
  @NotNull
  private String username;

  /**
   * This is validated via the AccountDto.password's @ValidPassword annotation.
   * (hashed)
   */
  @Length(max = 60)
  @NotNull
  private String password;

  // --------------------------------------------------
  // ------------- Getters and Setters ----------------
  // --------------------------------------------------

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  // --------------------------------------------------
  // ------------- Functional Methods -----------------
  // --------------------------------------------------

  public boolean equals(final Account other) {
    return this.id.equals(other.id)
      && this.username.equals(other.username)
      && this.password.equals(other.password);
  }

  // --------------------------------------------------
  // ---------------- Constructors --------------------
  // --------------------------------------------------

  public Account(){
    // Empty for Hibernate/JSON
  }

  public Account(String username, String password) {
    setId(0l); // ID auto-generated anyway...
    setUsername(username);
    setPassword(password);
  }
}
