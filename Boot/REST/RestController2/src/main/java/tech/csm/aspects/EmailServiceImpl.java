package tech.csm.aspects;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        // Implementation of email sending logic
        System.out.println("Sending email to " + to);
    }
}