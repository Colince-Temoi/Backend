package tech.csm.aspects;
public interface EmailService {
    void sendEmail(String to, String subject, String body);
}