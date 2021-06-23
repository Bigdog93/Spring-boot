package com.koreait.facebook_clone.common.mailsender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sleepydog931025@gamil.com"); // 보내는 사람 이메일
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMimeMessage(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // boolean 은 파일을 보낼지 말지
            helper.setFrom("sleepydog931025@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // html 태그를 사용 할 수 있도록

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
