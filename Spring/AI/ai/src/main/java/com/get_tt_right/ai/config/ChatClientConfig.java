package com.get_tt_right.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Mentioning @Configuration because inside this class I am going to write some logic related to creation of beans.
 * Inside this class we want to explicitly create the beans of ChatClient for various ChatModels that we are trying to use. Once all the required ChatClient beans are created, we can use them inside our business logic as well.
 * Below we have now configured 2 beans of ChatClient representing 2 different ChatModels that we are trying to use inside our application
 * */
@Configuration
public class ChatClientConfig {
    /** Mentioning @Bean annotation because this method is going to return a bean of type ChatClient.
     * To this method I am going to inject a bean of type OpenAiChatModel. This bean is going to be created by the framework and the same we are trying to inject as a dependency to this method.
     * Next, we are going to use the ChatClient.create() method to create a ChatClient bean by passing the OpenAiChatModel bean as a parameter/input.
     * This is just one style of creating a ChatClient bean. In the method#ollamaChatClient we are going to use another style of creating a ChatClient bean.
     * */
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel); // With this single line of code, behind the scenes by using this OpenAiChatModel bean, a ChatClient bean is going to be created using which we can interact with OpenAI based LLM models.
    }
/** This time instead of using the ChatClient.create() method we are going to use the ChatClient.builder() method. To the builder we are going to pass the OllamaChatModel bean as a parameter/input. The output from this I will try to catch on the LHS by using ChatClient.Builder and the variable name as chatClientBuilder.
 * Previously, the framework used to create the bean of ChatClient.Builder but right now we are tyring to create the same object manually. Once we created this, using the object of the same, we are going to call/invoke the build() method which is going to return the object of ChatClient and the same is what we are trying to return from this method.
 * This 2nd approach/alternative is going to give more control to the developer by providing some methods. For example, there is some method from ChatClient.Builder with the name defaultSystem, etc. Like this there are so many methods which we can use to configure on how the LLM model should behave. We are going to discuss all these methods in the coming sessions. For now, by simply invoking the build() method we are going to create a ChatClient bean/object and that is what we are trying to return from this method.
 * */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel);
        return chatClientBuilder.build();
    }
}
