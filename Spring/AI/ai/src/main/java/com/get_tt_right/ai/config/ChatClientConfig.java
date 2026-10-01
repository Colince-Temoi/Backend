package com.get_tt_right.ai.config;

import com.get_tt_right.ai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
     *
     * Configuring default Advisors
     * --------------------------------
     * There are 3 overloaded methods of defaultAdvisor. I should be able to configure a single advisor or a list of advisors or by using a lambda expression of Consumer functional interface.
     * For now I will go with the 1st overloaded method of defaultAdvisor to configure a single advisor. Reason: For now I only have a single advisor that I want to configure. To defaultAdvisors method, I am going to pass an object of new SimpleLoggerAdvisor() - this advisor is available inside the framework. If you open this class, you should be able to see a documentation highlighting that it is " A simple logger advisor that logs the request and response messages." As discussed b4, whenever we want to implement our own Advisor, we need to make sure we are implementing the 2 interfaces i.e., CallAdvisor, StreamAdvisor as can also be visualized inside this predefined SimpleLoggerAdvisor class. We can get the response from the LLM model in 2 styles; 1. By using the normal style where we are going to wait for the response to come and all the responses is going to be displayed at once. 2. The other style is by using Streaming - As and when the LLM is generating the response we can stream that response to the end user or to the UI application.
     * Based on how you are trying to send the responses to the end user or to the UI application, you can try to implement any of these 2 interfaces; CallAdvisor or StreamAdvisor. When someone is implementing both of these interfaces, that means that a given Advisor is going to support normal call and streaming call to the LLM model. If you try to understand what they have written inside this SimpleLoggerAdvisor class, If you can scroll down, you should be able to see a method#adviseCall and inside this method, they are simply logging the request as the 1st thing with the help of the method#logRequest. Once the logging of the request is completed, they are forwarding the request to the next Advisor in the chain. If there is no Advisor available in the chain, the request will be sent to the LLM model. Once the LLM model sends the response, the same will be logged again by using the method#logResponse. If you open the method#logRequest you should be able to see that they are trying to log the request by using "..logger.debug...", similarly the logging of the response is also going to happen by using "..logger.debug...". In both these methods they are using the method#debug to log the request and response.
     * Since by default debug logging is not going to be enabled, in order to make this advisor work without any issues we need to make sure we are enableing the debug logging for this class. That we will do in a few. Rgerading other methods available inside the SimpleLoggerAdvisor class, we can see a method#getName - So, every Advisor is going to have a name. That name we can provide, or if we are not providing the name then the name of the Advisor will be derived from the class name - that is what you are visualizing inside the method#getName logic. Also, every Advisor is going to have some order - method#getOrder. If you try to check the order of this SimpleLoggerAdvisor bean, they are trying to set the order to 0. Reason: We invoked the new SimpleLoggerAdvisor which is a plain constructor, that's why they are considering the order as 0. If you want to set a different order then you can invoke the constructor SimpleLoggerAdvisor(int order) as can be seen in the SimpleLoggerAdvisor class by passing the order value/number. The higher the order number the more preference the Advisor is going to get. Which means that the Advisors with the higher order numbers they are going to be executed 1st followed by the lower order advisors.
     * If multiple Advisors have the same order then the order of execution is not going to be guaranteed by the framework. That's why always make sure you are handling this order as well if the order of execution is going to matter for your Spring AI application. For now, I am fine with this 0 default order. Just like how we have a method#adviceCall, there is also another method in the SimpleLoggerAdvisor class called method#adviseStream. When we are trying to invoke the LLM by using the Stream option, then the response will be streamed to the end user or to the UI application as and when the response is being generated. Since this Streaming is going to use the Spring Reactive library, you will be able to see the return type from this method is Flux of ChatClientResponse. In this method also they are trying to log the request by using invoking the method#logRequest but coming to the response logging, they are trying to aggragate all the ChatClientResponses and once the aggregation is completed at the end they are trying to log the response. Reason: When we are using the Stream, we are going to get the responses continously as and when they are generating. That's why what they have done is - instead of logging at multiple times, they are trying to aggregate the entire chat client responses and logging the same towards the end.
     * With what we have discussed, hope you have gotte some clarity on what the purpose of this SimpleLoggerAdvisor is. Now, if you go to the CallAdvisor interface, and if you try to look for the impl classes of this interface you should be able to see all the in-built advisors provided by the Spring AI framework. You can see advisors related to  ChatModel, MessageChat, PromptChat, ...etc. All these we are going to explore in the coming sessions. As of now, you are aware about the purpose of SimpleLoggerAdvisor. Now, let's see the purpose of SafeGuardAdvisor as well, you can open it to see what it has to offer. If you try to read the doc on top if this class, they are clearly highlight that it is an Advisor that blocks the call to the model provider if the user input contains any of the sensitive words. So, while we are creating an object of this Advisor, as a developer we need to provide a list of sensitive words. If the end user is using any of these sensitive words inside a request, the logic written inside this advisor what it is going to do is, it is going to have a check - if the if condition is satisfied then it is not going to allow the request to the LLM model instead it is simply going to generate a failure response of type AssistantMessage. What is the type of response it os going to generate, we have that information at the top of the SafeGuardAdvisor - it is going to generate this DEFAULT_FAILURE_RESPONSE. These kind of Advisors are very required inside enterprise environments. Since this is a very common scenario, Spring AI team provided this Advisor. As and when new versions of this Spring AI framework are going to be released, more and more advisors will be introduced. Feel free to explore all of them and use them based upon your use cases.
     * Now to see the demo of the SimpleLoggerAdvisor that we have configured, we need to enable the debug logging for this class. You can do that by adding the following property to the application.properties file - "logging.level.org.springframework.ai.chat.client.advisor=DEBUG". Basically, I have set the package where this SimpleLoggerAdvisor is available/present. So, for this package I have enabled the debug logging. You can save this test your changes. Like this everything that we are sending to the LLM and the response that we are receiving from the LLM will be logged inside the console. Now, if you invoke any of the APIs for example; chat, this time if you check the console you should have the complete details of the request and the response that we received from the OpenAI LLM model. Under the request if you see we are trying to send a System message as well as a user message - you should be able to see the default model that is being used by the Spring AI framework,the temperature, stream usage you can see it is false - all of which are the default chat options. In the coming sessions we will see how to change all these default chat options using which we can change the model type, temperature, stream usage etc. All these details we will see in the coming sessions. Similarly, we should also be able to see the response that we received from the LLM logged on the console - The message type as can be seen is AssistantMessage role, you should also be able to see the id which is specific to OpenAI. You should also be able to see the text response that we are visualizing in our PostMan. A part from that we also have many other metadata information as well. Hope you are seing how powerful these advisors are. This way, any cross-cutting concerns or housekeeping activities we should be able to easily implement by using the advisors concept.
     * As of now, I have configured the Advisors by using ChatClient.Builder method#defaultAdvisors. If you don't want to apply the advisors for all the REST APIs where the configured ChatClient bean is being used, you also have the option to configure the advisors inside your specific APIs as well. For example, if you go to any of the controller classes i.e., PromptStuffingController, just like how we have invoked the method#system and method#user you also have an option to chain the invokation of the method#advisors. To this advisors method, we can either pass a single advisor or List of advisors or advisors by using a Consumer lambda expression. You need to use this 3rd overloaded advisors method if you want to provide some dynamic placeholder data. If you open the advisors method which is present inside the DefaultChatClient class, you can see that if you are trying to provide a lambda expression by using this Consumer functional interface - from this lambda expression, it is going to take the Advisor specifications along with the parameters and advisor details. In case if your advisor requires some dynamic data the feel fee to use this 3rd overloaded advisors method. This is very similar as what we have previously seen as part of the prompt template demo. Next, we will try to build our own custom Advisor and configure the same inside our Spring AI application.
     * */
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
//        return ChatClient.create(openAiChatModel); // With this single line of code, behind the scenes by using this OpenAiChatModel bean, a ChatClient bean is going to be created using which we can interact with OpenAI based LLM models.
//        Here we are creating the object of OpenAiChatOptions by using the builder method and using this we can chain any number of options with each representing a chat option.
//        You can always check the official doc of OpenAI for the  latest name of the LLM model you want to use. Unless you have Strong reason always try to prefer the mini models because they consume less numb of credits compared to the premium models.
//        In the chain we are invoking other supporting methods i.e., temperature, maxCompletionTokens - to this we are setting some value like 100. Using the max completion tokens we are going to tell the openai LLM model on what are the total number of tokens that it can consume to generate a response for my chat. Sometimes especially during the lower environments or during the testing faces we don't want the LLM models to generate very lengthy responses and under such scenarios we can restrict the LLM model to generate some short response. How to convey that, by sending the maxCompletionTokens option.
//        This way you can invoke all the supporting methods of OpenAiChatOptions and set the corresponding chat options parameters/values that are supported by the OpenAI LLM chat model as per your requirements. In case if you are not clear about what is the purpose of these methods/options, you can always look up into the openai developer official doc to understand the purpose of each method.
        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8);
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);
        chatClientBuilder.defaultOptions(options) // Setting the default chat options. With this, what is going to happen is, inside my project wherever I am using this openAiChatClient to perform the interaction with the OpenAI LLM model, in all those places, these options will be send by the Spring AI framework during the Chat interaction. Of course there are many other mandator options which the Spring AI framework is going take care of by setting some default values.
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help\s
                        employees with questions related to HR policies, such as\s
                        leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics,\s
                        kindly inform them that you can only assist with queries related to\s
                        HR policies.
                        """)
//                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(List.of( new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
        .defaultUser("How can you help me ?");
        return chatClientBuilder.build();
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

    /** To this method we are trying to inject the ChatClient.Builder bean/object.
     * If you uncomment this you will get a CE "Could not autowire. No beans of 'Builder' type found." Reason: the property we configured i.e., spring.ai.chat.client.enabled=false
     * With this method we are trying to create an object of ChatClient and converting that as a bean. So, this ChatClient bean I can use in any of my controllers.
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
//        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8);
        return chatClientBuilder
//                .defaultOptions(options)
//                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),
//                        new TokenUsageAuditAdvisor()))
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help\s
                        employees with questions related to HR policies, such as\s
                        leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics,\s
                        kindly inform them that you can only assist with queries related to\s
                        HR policies.
                        """)
                .defaultUser("How can you help me ?")
                .build();
    }
     */
}
