package inyro.spring.Service.impl;

import org.springframework.stereotype.Service;

import inyro.spring.Service.EmailService;

//테스트용 임시 구현
@Service
public class TempEmailService implements EmailService {
    @Override
    public void sendNotification(String to, String subject, String content) {
        System.out.println("Sending email to: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Content: " + content);
    }
}
