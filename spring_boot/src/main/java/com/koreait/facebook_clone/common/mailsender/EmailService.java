package com.koreait.facebook_clone.common.mailsender;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendMimeMessage(String to, String subject, String text);
}
