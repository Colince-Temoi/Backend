package tech.csm.aspects;

import org.springframework.stereotype.Service;

@Service
public class JmsServiceImpl implements JmsService {
    @Override
    public void sendMessage(String destination, String message) {
        // Implementation of JMS sending logic
        System.out.println("Sending message to " + destination);
    }
}