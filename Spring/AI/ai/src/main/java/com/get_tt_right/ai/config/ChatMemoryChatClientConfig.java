package com.get_tt_right.ai.config;

import com.get_tt_right.ai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * * */
@Configuration
public class ChatMemoryChatClientConfig {

    /** Inside this ChatMemoryChatClientConfig class, I'm going to create a @Bean() method, which is going to create the bean of type ChatMemory. Check out its docstring for more details. To this method I'm going to inject the bean of type JdbcChatMemoryRepository with the variable name jdbcChatMemoryRepository. This bean is going to be created automatically by the Spring AI framework.
     * The customization that I want to make is related to the bean of type MessageWindowChatMemory. That's why what I'll do is, by using the MessageWindowChatMemory class I'm going to invoke the builder() method followed by there are methods like maxMessages using which you can control the maximum messages that framework needs to consider while populating the ChatMemory. Let me invoke the same, to this method I'm going to pass a int value 10. Using this 10 value I'm telling to the framework to consider a maximum of last 10 messages.
     * Next, I need to invoke the chatMemoryRepository method and pass the bean of type JdbcChatMemoryRepository followed by towards the end I need to invoke the build() method. Like this whatever bean that we are going to get from this code/logi, I'm going to return from this method by using the return keyword. And that is going to create a bean of type ChatMemory. The same is what we are trying to inject to chatMemoryChatClient bean method below where the ChatClient bean is getting created. Save changes, do build and then restart your application.
     * I'm going to send the message "What is your name ?" with the username JJ02 and LLM is going to respond your name is John Doe. Let's try to test some negative scenarios. I'll try to mention some new username JJ03, if I send this request since LLM does not have any clue it is going to send the I'm sorry response. This gives a confirmation to us everything is working even with these new changes that we have developed so far.
     * */
    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    /** I'm going to get rid of all these chatOptions because I just wanted to create a simple ChatClientBean
     * I am also getting rid of defaultAdvisors, defaultSystem and defaultUser related configurations. So this is a simple ChatClientBean that I'm trying to create with the help of this class.
     * But since we already have another ChatClient Bean with the same name inside another class, ChatClientConfig, the Spring Boot is going to throw an error while we are trying to start the application. To resolve the problem what I'll do is, I'll try to provide a name to this @Bean.
     * The name is "chatMemoryChatClient". As a next step, I can try to inject this ChatClient into one of the controller class. Let me go to the controller package and create a new controller class with the name "ChatMemoryController". Check it out for more details.
     *
     * From Stateless to Stateful - Spring AI memory in action.
     * --------------------------------------------------------
     * To this constructor, first I'm going to inject a Bean of type ChatMemory, the same I'm trying to add as an input parameter to the constructor.
     * By default, Spring AI framework behind the scenes, it is going to create a Bean of ChatMemory implementation. As of now, inside the framework, we have a single Bean of type MessageWindowChatMemory. This you can visualize if you go to the ChatMemory interface and on it's RHS click on the (fx) icon. - We aleady visualized this.
     * The same Bean is what will be injected into our constructor. This class/bean(MessageWindowChatMemory) we also visualized that it has a dependency on the Bean of type ChatMemoryRepository. As of now, inside our classpath, we have a single implementation class to this interface i.e., InMemoryChatMemoryRepository. and this is what will be injected into the MessageWindowChatMemory bean/class.
     * So by the time we are injecting the ChatMemory Bean into our constructor, this ChatMemory Bean internally, it is also going to have the required ChatMemoryRepository Bean configured. The job of the developer is very simple. First, the developer has to create the object of advisor. In total, we have three kinds of advisors available in the framework - this we discussed and visualized. If you try to search (Ctl + N) for *MemoryAdvisor, there is an advisor with the name MessageChatMemoryAdvisor and PromptChatMemoryAdvisor.
     * The framework is not showing me the advisor related to the vector store because I have not added any dependency related to the vector database inside my pom.xml. The BaseChatMemoryAdvisor that yiu are seeing is just a simple interface which is being extended by all the memory advisors. Let me go for the implementation class with the name MessageChatMemoryAdvisor. So I have to create an object of this class. For the same, what I'll do is, I'll try to invoke the builder() method and to this builder() method, we need to pass the Bean of ChatMemory.
     * We already have that as an input to the constructor. The same, I'm trying to pass to this builder() method and towards the end, I'm going to invoke the build() method, which is going to give me an object of type Advisor. Let me give a name to the variable of the object. The name is memoryAdvisor. In the similar lines, I also want to configure one more advisor, which is going to help us in logging the request and response. Let me create a variable of type Advisor with the variable name as loggerAdvisor. To this, I'm assigning the object of SimpleLoggerAdvisor.
     * So these two advisors, I want to configure into this ChatClient. It's very simple. We just have to invoke the defaultAdvisors() method against the ChatClientBuilder. Since I want to provide multiple advisors, I'll try to pass all of them as a list by using the of() method in the List. And to this method, I'm passing the LoggerAdvisor and MemoryAdvisor as an input. With these changes, now my chatMemoryChatClientBean, it is going to have the capability to memorize all the conversations that are going to happen inside my application. But configuring this MemoryAdvisor alone is not going to work because when this advisor is trying to memorize all the conversations, it needs an input from our side against which key it has to save all the conversations.
     * That key details we can provide from our controller layer. We know this Bean is being used right now inside the ChatMemoryController. Check out it's docstring for a contnuation on this.
     *
     * Using maxMessages to limit chat history in Spring AI.
     * ------------------------------------------------------
     * Here this ChatMemory input represents the bean of MessageWindowChatMemory. If you open this ChatMemory interface, you will see that it has an implementation class MessageWindowChatMemory. Into this bean, if you open it, a bean of type ChatMemoryRepository will be injected by the framework and that will be the bean of JdbcChatMemoryRepository. If you open the ChatMemoryRepository interface, you will see that it has an implementation class InMemoryChatMemoryRepository and JdbcChatMemoryRepository - this we have visualized.
     * So this is all working fine. But what if you have a requirement if you want to change something around the default behavior of the Spring AI framework. For example, this MessageWindowChatMemory implementation by default it is going to use a MAX_MESSAGES of 20 to populate the chat messages into the request that will be sent to the LLM model. You may want to increase this or decrease this number of messages. In these kind of scenarios, it is advisable to create your own beans and don't depend on the default beans created by the framework.
     * How to do the same? let's visualize this. Inside this ChatMemoryChatClientConfig class, I'm going to create a @Bean() method, which is going to create the bean of type ChatMemory. Check out its docstring for more details. To this method I'm going to inject the bean of type JdbcChatMemoryRepository with the variable name jdbcChatMemoryRepository. This bean is going to be created automatically by the Spring AI framework.
     *
     * */
    @Bean("chatMemoryChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
//        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8);
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);
//        chatClientBuilder
//                .defaultOptions(options) // Setting the default chat options. With this, what is going to happen is, inside my project wherever I am using this openAiChatClient to perform the interaction with the OpenAI LLM model, in all those places, these options will be send by the Spring AI framework during the Chat interaction. Of course there are many other mandator options which the Spring AI framework is going take care of by setting some default values.
//                .defaultSystem("""
//                        You are an internal HR assistant. Your role is to help\s
//                        employees with questions related to HR policies, such as\s
//                        leave policies, working hours, benefits, and code of conduct.
//                        If a user asks for help with anything outside of these topics,\s
//                        kindly inform them that you can only assist with queries related to\s
//                        HR policies.
//                        """)
//                .defaultAdvisors(new SimpleLoggerAdvisor())
//                .defaultAdvisors(List.of( new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
//        .defaultUser("How can you help me ?");
//        return chatClientBuilder.build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor))
                .build();
    }
/**
 *  */
    @Bean("ollamaChatMemoryChatClient")
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);
        return chatClientBuilder.build();
    }
}
