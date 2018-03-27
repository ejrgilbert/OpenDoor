package com.opendoor.spring.service;

import com.google.gson.Gson;
import com.opendoor.persistence.service.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = com.opendoor.Application.class,
  properties = "logging.level.org.springframework.web=DEBUG"
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS) // Short-term fix until we can find a more efficient way to reset the database
public class EmailServiceTest {
  private MockMvc mvc;
  private static Gson gson = new Gson();

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private EmailService service;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void testAll() {
    // Keep in mind the username _must_ be unique
    // (there already exists by default a user named "user")
    // See Application.init method (that's where that user is made)
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setTo("sganesh3@ncsu.edu");
      msg.setText("Dear Spatika, I <3 U. \n\n\t OpenDoor *wink**wink* ");
      try {
          service.sendEmail(msg);
          assertNotNull(msg);

          msg = new SimpleMailMessage();
          msg.setTo("evgilber@ncsu.edu");
          msg.setSubject("OPENDOOR ROCKS");
          msg.setText("Dear Elizabeth, I <3 U. \n\n\t OpenDoor *wink**wink* ");

          service.sendEmail(msg);

          msg = new SimpleMailMessage();
          msg.setTo("calaw@ncsu.edu");
          msg.setSubject("OPENDOOR ROCKS");
          msg.setText("Dear Caroline, I <3 U. \n\n\t OpenDoor *wink**wink* ");

          service.sendEmail(msg);

          msg = new SimpleMailMessage();
          msg.setTo("mknovits@ncsu.edu");
          msg.setSubject("OPENDOOR ROCKS");
          msg.setText("Dear Melissa, I <3 U. \n\n\t OpenDoor *wink**wink* ");
          service.sendEmail(msg);
      } catch (Exception e) {
          e.printStackTrace();
          fail();
      }
  }
}
