package inyro.spring.Service;

public interface EmailService {
    void sendNotification(String to, String subject, String content);
}
