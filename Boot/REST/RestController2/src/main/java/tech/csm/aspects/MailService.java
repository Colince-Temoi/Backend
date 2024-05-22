package tech.csm.aspects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    
	@Autowired
//    private JavaMailSender mailSender;

    public void sendMail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo("colincetemoi190@gmail.com");
//        message.setSubject("Test Subject");
//        message.setText("This is a test email.");
//
//        mailSender.send(message);
        System.out.println("Email sent successfully.");
    }
}