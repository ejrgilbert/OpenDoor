package com.opendoor.controller;

import com.google.gson.Gson;
import com.opendoor.Application;
import com.opendoor.dto.user.NewUserDto;
import com.opendoor.dto.user.PasswordDto;
import com.opendoor.persistence.model.Account;
import com.opendoor.persistence.model.User;
import com.opendoor.persistence.service.AccountService;
import com.opendoor.persistence.service.EmailService;
import com.opendoor.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for all account-related functionality
 * @author Elizabeth Gilbert
 */
@RestController
@CrossOrigin(origins = "http://localhost:8100")
public class AccountController {
  /**
   * Maps between java objects and JSON
   */
  private static Gson gson = new Gson();

  /**
   * Service for user-related persistence operations
   */
  @Autowired
  private UserService userService;

  /**
   * Service for accountx-related persistence operations
   */
  @Autowired
  private AccountService accountService;

  @Autowired
  private EmailService emailService;
  /**
   * Check whether email has been used by another user or not
   * @param email the email to check
   * @return true if has been used, false if not
   */
  private ResponseEntity<String> emailHasBeenUsed(String email) {

    // Lookup user in database by e-mail
    User userExists = userService.findByEmail(email);

    HashMap<String, String> response = new HashMap<>();
    if (userExists != null) {
      response.put("reject", "email");
      response.put("errorMessage", "Oops!  There is already a user registered with the email provided.");

      return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

  /**
   * Check whether username has been used by another user or not
   * @param username the username to check
   * @return true if has been used, false if not
   */
  private ResponseEntity<String> usernameHasBeenUsed(String username) {
    Account acct = accountService.findByUsername(username);

    HashMap<String, String> response = new HashMap<>();
    if (acct != null) {
      response.put("reject", "username");
      response.put("errorMessage", "Oops!  There is already a user registered with the username provided.");

      return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

  /**
   * Create the account in the database.
   * @param username the username of the user
   * @param passwordDto the password of the user (not hashed)
   */
  private void createAccount(String username, @Valid PasswordDto passwordDto) {
    // Need to first save the account object
    Account acct = new Account(username, passwordDto.getPassword());
    accountService.save(acct);
  }

  /**
   * Create the user in the database
   * @param userDto the NewUserDto representation of the user
   * @return the result of this operation
   */
  private ResponseEntity<String> createUser(NewUserDto userDto) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Set<ConstraintViolation<PasswordDto>> violations = factory.getValidator().validate(userDto.getPassword());

    StringBuilder violationsString = new StringBuilder();
    for (ConstraintViolation<PasswordDto> violation : violations) {
      violationsString.append(violation.getMessage());
    }

    HashMap<String, String> response = new HashMap<>();
    if (violations.size() > 0) {
      response.put("reject", "password");
      response.put("errorMessage", violationsString.toString());

      return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    createAccount(userDto.getUsername(), userDto.getPassword());

    // Now, save a new User object with the above account and information in the DTO
    User user = new User(userDto, accountService.findByUsername(userDto.getUsername()));

    // Disable user until they click on confirmation link in email
    user.setEnabled(false);

    // Generate random 36-character string token for confirmation link
    user.setConfirmationToken(UUID.randomUUID().toString());

    userService.save(user);
    return sendVerificationEmail(user);
  }

  /**
   * Send a verification email to the user
   * @param user the Data Transfer Object for the User
   * @return a response to be sent back to the user upon sending the email
   */
  private ResponseEntity sendVerificationEmail(User user) {
    HashMap<String, String> response = new HashMap<>();

    SimpleMailMessage registrationEmail = new SimpleMailMessage();
    registrationEmail.setTo(user.getEmail());
    registrationEmail.setSubject("Registration Confirmation");
    registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
      + Application.URL + "/confirm?token=" + user.getConfirmationToken());
    //registrationEmail.setFrom("noreply@domain.com");

    emailService.sendEmail(registrationEmail);
    response.put("successMessage", "A confirmation e-mail has been sent to " + user.getEmail());

    return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
  }

  /**
   * Process the new user request
   * @param userDto the user to register
   * @return a response based on the userDto data
   */
  @RequestMapping(
    method = POST,
    value = "/register",
    consumes = { "application/json"}
  )
  public ResponseEntity processRegistrationForm(@RequestBody @Valid NewUserDto userDto) {

    // Lookup user in database by e-mail
    ResponseEntity<String> response = emailHasBeenUsed(userDto.getEmail());
    if (response != null) {
      return response;
    }

    // Lookup account in database by username
    response = usernameHasBeenUsed(userDto.getUsername());
    if (response != null) {
      return response;
    }

    // Create a new user
    response = createUser(userDto);
    if (response != null) {
      return response;
    }

    return null;
  }

  /**
   * Verify that the user's email address is valid (this will
   * be accessed via a link in an email
   * @param token the confirmation token passed via URL parameter
   * @return The success/failure result in a ResponseEntity object
   */
  @RequestMapping(
    method = GET,
    value = "/confirm"
  )
  public ResponseEntity verifyEmailAddress(@RequestParam("token") String token) {

    // Find the user associated with the reset token
    User user = userService.findByConfirmationToken(token);

    HashMap<String, String> response = new HashMap<>();
    if (user == null) {
      response.put("reject", "confirmation token");
      response.put("errorMessage", "Oops!  This is an invalid confirmation link.");

      return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

    user.setEnabled(true);

    // save the user
    userService.save(user);

    response.put("successMessage", "Email successfully verified");
    return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
  }


  /**
   * Function to reset the user's password. Uses the user that's logged in.
   * @param passwordDto the user's password to reset to
   */
  @RequestMapping(
    method = POST,
    value = "/resetPassword",
    consumes = { "application/json"}
  )
  public void resetPassword(@RequestBody @Valid PasswordDto passwordDto) {
    String username = Application.getLoggedInUsername();

    Account account = accountService.findByUsername(username);
    account.setPassword(passwordDto.getPassword());

    accountService.save(account);
  }
}
