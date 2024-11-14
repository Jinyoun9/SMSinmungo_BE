package com.smsinmungo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;


  public void sendEmail(String toEmail, String title, String content) throws MessagingException {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(toEmail);
    helper.setSubject(title);
    helper.setText(content, true);
    helper.setReplyTo("tommys915@gmail.com");

    try {
      emailSender.send(message);
    } catch (RuntimeException e) {
      log.error("Unable to send email: {}", e.getMessage());
      throw new RuntimeException("Unable to send email", e);
    }
  }
}
