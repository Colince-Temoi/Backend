package tech.csm.aspects;
public interface JmsService {
    void sendMessage(String destination, String message);
}