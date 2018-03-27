package com.opendoor.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * https://www.codebyamir.com/blog/user-account-registration-with-spring-boot
 *
 * Mails emails to Users
 */
@Service("emailService")
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Async
  public void sendEmail(SimpleMailMessage email) {
    mailSender.send(email);
  }

}
