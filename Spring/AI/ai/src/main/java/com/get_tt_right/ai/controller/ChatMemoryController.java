package com.get_tt_right.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/** On top of this class I'm going to mention a couple of annotations followed by I'm going to inject the ChatClientBean that we created inside the ChatMemoryChatClientConfig class.
 * As a next step, let me create a new RestAPI with the path /api/chat-memory.
 * Do a build and try to restart the application just to make sure that the new configurations are properly loaded. In the postman try to fire the request with message as "My name is Colince". Obviously the LLM model is going to respond something like "Nice to meet you, Colince! ... then "How can I assist you today?"
 * The next immediate question that I'm going to ask is what is my name? Because I provided my name in the previous conversation or in the previous chat, I'm expecting the LLM should at least guess my name and provide the information. But to our surprise we received this response saying that I don't have access to personal data about users unless it has been shared with me in the course of our conversation.Therefore, I don't know your name. If you would like, you can tell me your name This confirms to us that the LLM models they are not capable of remembering the things in between the conversations and this can be a serious problem.
 * But the same problem you're not going to face when you are using applications like ChatGPT. For example, to the ChatGPT I'm going to provide the same information as My name is JJ. As soon as I press enter it is going to give some information - "Nice to meet you, JJ! 😊" As a next question, I'm going to ask what is my name? To this the ChatGPT responded something like your name is JJ. With this you may have a question or confusion. How come the same OpenAI LLM model is working properly here? Whereas in our scenario it's not working. Let me clarify why this is working in ChatGPT but not in our scenario. ChatGPT is a LLM wrapper application. By using these wrapper application, the OpenAI developers they are doing all the magic of sharing the previous conversation history to the core LLM model. We need to build something very similar by taking the help from Spring AI framework. Let's try to understand how to solve this problem by using Spring AI from the next session.
 */
@RestController
@RequestMapping("/api")
public class ChatMemoryController {
    private final ChatClient chatClient;

//    I created a constructor and with the help of this constructor I'm trying to inject the ChatClientBean that we created inside the ChatMemoryChatClientConfig class.
//    Since we have multiple ChatClientBeans available inside our project I'm trying to be specific by providing the bean name by using these @Qualifier annotation.
    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /** This Rest API is going to accept a @RequestParam with the name message. Whatever message that is being sent by the end user the same will be forwarded to the OpenAI LLMmodel and the response String we are going to return from this rest API.
     * From Stateless to Stateful - Spring AI memory in action.
     * ----------------------------------------------------------
     * Here just before we try to send our prompt message to the LLM model, we have to configure the conversation key against to which all the conversation will get saved by the framework. Just after this user() method, I'm going to invoke the advisors() method. There are multiple overloaded advisors() method we have. We need to invoke the second method, which is going to accept the advisor specifications by using consumer lambda expression.
     * So let me invoke the same and to this, I need to pass a lambda expression with the input variable name as advisorsSpec and inside the lambda expression body by using the advisorsSpec, I need to invoke the param() method to this param() method we need to pass a constant that is provided by framework with the name CONVERSATION_ID. I need to invoke this constant by using ChatMemory inside this we have a constant with the name CONVERSATION_ID.
     * If I open this constant, you will be able to see it has a value with the name chat_memory_conversation_id. After mentioning this constant, we need to pass a value that needs to be treated as a key by the framework. I'm going to provide the value as "default". So this is a default value that I'm trying to provide for all the conversations that are going to happen inside my application. But this is a very basic way of providing the conversation ID, with this approach We are providing the same conversation ID, which is default for all the conversations that are going to happen inside our application.
     * And this has certain drawbacks, which we're going to discuss in a few minutes. And once we are clear with the drawback, we're going to fix the same in the next session. I hope you're clear with the changes that I have done. First I have configured the advisor to my ChatClient bean.
     *
     * Per-user memory in LLMs - Thanks to CONVERSATION_ID!
     * --------------------------------------------------------
     * Let's take a scenario, inside an enterprise application, this REST API is being invoked by a UI application. And this UI application can be used by thousands of end customers. With the changes that we have done so far, all these thousands of customers, all their conversation history will be saved against a common conversation ID with the value "default". And with that, what is going to happen? All the users' conversations will be mixed up into a single bucket. And with that, the LLM is also not going to behave individually for each customer.
     * To solve this problem, what I can do is, whatever client application who are trying to invoke my REST API, I can ask them to send an identifier which is unique for each end user. Inside real enterprise applications, they might be already having such unique identifiers like username, email, mobile number. All these identifiers, they are going to be unique for each individual. We are going to accept the same unique identifier as an input to our REST API. So just like how we are accepting message by using this RequestParam, this time I'm going to ask all my client applications to send a RequestHeader with the name username.
     * So here I have considered username as my unique identifier. When a client application sends this username inside the RequestHeader, the framework is going to assign that value to this username variable. So this username variable, I'm going to pass as a conversation ID. So previously, we always had a static value default, but right now for each user, we are providing different conversation ID. This is the only change that we need to do and behind the scenes, the framework is going to take care of storing and retrieving the messages based upon the given conversation ID, which is unique to an end user.
     * Let me save this. Do the build. Once the build is completed, I'll go to the InMemoryChatMemoryRepository. And here we know there is a ConcurrentHashMap, which is going to be used by the framework to store the chat messages based upon the given conversation ID. I'll try to keep a breakpoint inside findByConversationId() method  on the line " List<Message> messages = this.chatMemoryStore.get(conversationId);" so that we can see what is going to happen behind the scenes. From now onwards, in order to invoke this REST API, we need to make sure we are sending a header with the key username and the value will be the username of the end user.
     * For now, I'm going to provide the username as JJ01. With this header, I'll try to send a message with a value, "My name is JJ." Then fire the request and you should receive a response. Next, with a different username, maybe this time I'll give the username as JJ02. With this username, I'll send the name as John Doe. And for this message, I received a response as well. As a next step, what I'll do is I'll change this username to JJ01 and ask the question, "What is my name?" - ake sure you have your breakpoint ready. So with this, if I try to send the request and you should be able to visualize the breakpoint is stopped.
     * We can see what is present inside the HashMap, In the debugging conole you can just expand the {...} this and you should be able to see your HashMap name in our case "chatMemoryStore". Let me open the same and under the JJ01, we have four messages whereas for JJ02, there are only two messages. I hope you're visualizing how the framework is going to store the messages based upon the given conversation ID. If I release this breakpoint, I'm going to get the correct response. The response is your name is JJ. But if I change the username to JJ02, this time I'm going to get the response. Something like your name is John Doe. I hope you're crisp clear.
     * In enterprise applications. Please make sure you are configuring your own unique conversation ID. This can be anything, but please make sure this conversation ID is unique for each end user. ChatGPT also does the same. Whenever you are initiating a chat with the ChatGPT, it is going to create something like session ID, which is specific to an end user. Based upon this ID, it is going to remember the context of the previous chat messages.
     */
    @GetMapping("/chat-memory")
    public ResponseEntity<String> chatMemory(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
//        return ResponseEntity.ok(chatClient.prompt().user(message)
//                .call().content());
        return ResponseEntity.ok(chatClient.prompt().user(message).advisors(
//                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, "default")
                        advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                )
                .call().content());
    }


}
