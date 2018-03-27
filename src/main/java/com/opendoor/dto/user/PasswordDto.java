package com.opendoor.dto.user;

import com.opendoor.security.ValidPassword;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object for updating a user's password
 * @author Elizabeth Gilbert
 */
public class PasswordDto {

  /**
   * The password to update to
   */
  @ValidPassword
  @NotNull
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public PasswordDto(String password) {
    setPassword(password);
  }

  public PasswordDto() {
    // Empty for JSON construction
  }
}
