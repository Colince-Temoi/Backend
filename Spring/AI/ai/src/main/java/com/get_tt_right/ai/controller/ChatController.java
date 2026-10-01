package com.get_tt_right.ai.controller;

import com.get_tt_right.ai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  */
@RestController
@RequestMapping("/api") // Prefix API path.
public class ChatController {

//    private final ChatClient chatClient; // Like this I am trying to perform the constructor dependency injection and that's why I have defined "final" here.
    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClient;

    /** As discussed before, to create the bean of ChatClient we just have to use the logic specified in the docstring present inside the DefaultChatClient class i.e., ChatClient.Builder.build().
     * What I will do is, 1st, I will try to inject the ChatClient.Builder as a dependency into this constructor. So, ChatClient.Builder bean will be created by the framework during the startup.
     * So using the same chatClientBuilder object/bean, I need to invoke the build() method which is going to give me the bean of ChatClient and the same I am trying to assign to the chatClient Secondary variable/field which I have created/defined inside this class.
     * */
//    public ChatController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }

    /** To the constructor, I am trying to pass the beans that I have created inside the ChatClientConfig class by using the @Qualifier annotation. We know that by default that the method name will be considered as the bean name and that's why the same method names I am trying to pass as an input to the @Qualifier annotation.
     * Now, both these injected beans I am trying to assign to the openAiChatClient and ollamaChatClient secondary variables/fields that we have defined/created inside this class.
     * */
    public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                          @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClient = ollamaChatClient;
    }

    /** Like this to the method#prompt we are going to pass the user provided input message chaining the call and content method.
     * Here we are using the openAiChatClient.
     * */
    @GetMapping("/openai/chat")
    public String openAIChat(@RequestParam("message") String message) {
        return openAiChatClient.prompt()
//                .system(""System message"") // Like this no need to use this unless you want to override the default system message as shown/uncommented in the next line.
                .system("""
                        You are an internal IT helpdesk assistant. Your role is to assist 
                        employees with IT-related issues such as resetting passwords, 
                        unlocking accounts, and answering questions related to IT policies.
                        If a user requests help with anything outside of these 
                        responsibilities, respond politely and inform them that you are 
                        only able to assist with IT support tasks within your defined scope.
                        """)
//                .advisors(new TokenUsageAuditAdvisor())
//                .user(message) // Overrides the default user message as uncommented in the next line.
                .user(message)
                .call().content();
//        return openAiChatClient.prompt(message).call().content();
    }

    /**Here we are using the ollamaChatClient.
     * */
    @GetMapping("/ollama/chat")
    public String ollamaChat(@RequestParam("message") String message) {
        return ollamaChatClient.prompt(message).call().content();
    }


    /** The chat method is a GET endpoint that accepts a message parameter.
     * The same message we are going to send it as a prompt to the LLM model. Whatever LLM model that is going to provide us with a response we are going to feed that back to the end-user as a response from this REST API method.
     * Now, to integrate this Spring AI application with an LLM model we need to know few interface details. The most important interfaces from the Spring AI framework are ChatClient and ChatModel. Check slides for more details.
     * Ctrl + N to look for the interface with the name ChatModel. The interface is present in the package org.springframework.ai.chat.model. In this interface we have some default methods and abstarct methods as well. If you look for the impl classes of this interface you will visualize that as of now we have a single imple class with the name OpenAiChatModel. Why we have a single impl class? Reason: We only added OpenAI dependency inside our project. In case we added other dependencies relates to Anthropic, Azure AI, etc, then those impl classes will be available under my prroject. Inside this impl class, all the logic related to integrating with the OpenAI LLM chat model is developed by the Spring AI framework. If you look for a method with the name call, anyone who wants to interact with the OpenAI LLM model, they basically need to invoke this method#call(Prompt prompt) by using the ChatModel interface. Using this Prompt, a request prompt will be constructed, the same will be verified and towards the end this internalCall method will be invoked - check method#call for what we are just discussing here. You can also navigate to the method#internalCall from the method#call body and you will see that under that method we have all the logic specific to the OpenAI LLM models.
     * As we have visualized this OpenAiChatModel impl class is going to do a lot of heavy lifting around populating the required request fields based upon the API expectations of OpenAI. And once the response is received, the logic as can be seen in the method#internalCall we have more logic that will take care of reading and extracting the response so that inside our business logic we can process that response. So, during te start up of our application, a bean of OpenAiChatModel will be created by the framework and hence we don't have to worry as a developer. We can either inject this ChatModel bean into our controller and invoke the method#call(Prompt prompt) or we can directly use the ChatClient bean and invoke the method#prompt(String prompt) and then invoke the method#call(Prompt prompt) - but as we had discussed before, if you want to interact with the ChatModel logic directly, you will need to understand a lot of things and you will need to perform a lot of heavy lifting because you will be trying to write your code by using a low level API. To avoid al this complexity for the developers, the Spring team, they also have a ChatClient interface API. For this interface, there is an impl class i.e.,DefaultChatClient. So by default, the framework is not going to create the bean of ChatClient, we as developers we need to create this bean and inject the same into our Controller class or inside our business logic. Again to create the bean of this class, we don't have to write a lot of code  - as can be visualized in it's docstring, by simply invoking ChatClient.Builder.build() a default impl of ChatClient which is this DefaultChatClient object will be created behind the scenes. You can click on that part of the docstring i.e., "ChatClient.Builder.build()" which is a link, actually. Once you landed there, on the LHS you can go to the impl logic and there you can easily visualize that they are trying to create a bean of DefaultChatClient by using the default request. Like this they are assuming alot of default values and the same default values will be passedon to the ChatModel, otherwise if you want to interact with the ChatModel directly means you will have to explicitly take care of populating the default values by yourself which my instructor does not recommend.
     * That's why anytime if you want to interact with an LLM model, always leverage the ChatClient API. So, what I have to do is - Inside my Controller, I will try to create a secondary field of type ChatClient. Now, by using the constructor of this ChatController class I will try to do the dependecy injection. To this constructor I have to inject the bean of type ChatClient so that we can assign the same to this "chatClient" field. Now using this chatClient variable, we can interact with the LLM model of OpenAI. So, inside my REST API by using the chatClient reference, I need to invoke the prompt method and to this I will pass whatever message that I received fom my end-user followed by I need to invoke the method#call. Like this, this method#call will take care of calling the LLM model behind the scenes. But this call method(If you navigate to it) you will visualize that it is going to give us a response in the form of CallResponseSpec which is going to have a lot of details about the response including the metadata and the actual response provided by the LLM model along with other important details. So, to get my actual response, I will need to d a lot of digging inside this CallResponseSpec object. But instead of doing all that donkey work, what I can do is - I can simply chain the method#content invokation which is going to return me the actual LLM response content as a String object and the same is what we are trying to return from our REST API. Like this you should be visualizing the power of ChatClient fluent API - I mean, it has a lot of developer friendly methods using which we can interact with the LLM models by writing very less code. If this ChatCient fluent API is not present inside the Sring AI framework, then that means we as a developer we need to write a lot of lines of code. So, haaha! that was the ony logic that I need to write to interact with the LLM model.
     * If you open the properties file, you will see we have some default created/defined property. Check out that fiele for more details and discussion on the properties defined or that we will be defining there. Now, you can do a build and try starting your application. Now during the start-up we are going to run into a suprise - In the logs if you try to understand the reason for the error being faced. It is very simple, basically we as a developer we are trying to track with the OpenAI by using this OpenAI ChatModel but for this OpenAiChatModel to interact with the LLM model it requires an apikey. Reason: OpenAPI LLM models they are not free to use. So, as a developer we need to provide the apikey as well so that our Spring AI application can interact with the remote LLM models provided by the OpenAI. To create this apikey, we need to go to the OpenAI platform. You may have been using ChatGPT for many days but this is not the website where you have to create the apikey. We need to go the website https://platform.openai.com. Here you may have a question which is - what is the difference between OpenAI and ChatGPT. The answer is very simple - let's take the apple scenario. Apple is an organization and it produces a lot of products iPhone, MacBooks, iPads, AirPods and so on. Very similarly, OpenAI is an organization that has various LLM models and LLM based applications. One such application is ChatGPT. So think of this OpenAI as the parent of this ChatGPT application. Inside this website you need to log in with the same account of your ChatGPT. If you have not used ChatGPT before then you will need to sign up with a new account which is very simple. Once logged in, on the LHS nav click on the settings icon which is going to take you to some page. For the 1st time users the platform is going to ask you to enter some basic details like organization and so on - fill in those details. Next, on the LHS nav, navigate to Billing. To create an API key you need to have some balance inside your account. The platform is goin to allow you to recharge a minimum amount of $5. This will be more than enough to complete the entire sessions we will be discussing. In fact my instructor used just $1 or $2 to test all the examples iniside all the sessions. Later, we will also learn how to run local LLM models - of course they are not going to be so powerful like OpenAI provided models.
     * Once the amount is loaded, you need to navigate to the API keys LHS nav and on that page you need to click on "Create new secret key". Now I inputed the name of my key as "SpringAI" and selected the project as "Default project" from the dropdown. In case if you want, you can create a separate project as well. Click on create secret key and copy the key generated for later use. Click done. Now before I try to configure this Key inside my Spring AI application, lets see the pricing details of OpenAI. If you go to the website - https://developers.openai.com/api/docs/pricing here you will be able to see the pricing details of various LLM models of OpenAI. For example, if you tr to use the model gpt-5.5 for every 1 million tokens of input messages it is going to cost you $5 where as for the output 1M tokens, it is going to cost you $30. For cheaper models like gpt-5.4-mini, gpt-5.4-nano, etc it is going to cost very less becuase obviously they require very less computation power. Inside LLMs, everything is going to be tracked by using tokens. So, every text that we send as a request and every response that we get from the LLM models, they are going to be charged based upon the tokens. We are going to discuss these tokens in detail in the comming sessions. To configure the OpenAI apikey inside my Spring AI application we have to use the property spring.ai.openai.api-key. To this, I can assign my apikey directly but that is not recommended as it will expose my secret key when I check in my code inside the GitHub repo. As an alternative, we will leverage the environment variables option to assign/inject this apikey. This time if you do the build and restart your application you should not face any suprises/issues and your application should now start successfully at the default port 8080. Now, I can try to send some input to my REST API. In you collection, we have a request with the name Chat - using the message request param or query param we are providing the prompt "What is your name and which model you are using?" When we fire this request we now what is going to happen - whatever prompt the end-user is providing, it will go to the OpenAI LLM model and whatever response will be given will come back as a response. I am going to fire the "Joke" request to the same REST API with the message "Tell me a joke about Spring Boot?" and like this you should get a proper response.
     * With what we have discussed you should have now seen the power of Spring AI application. We as a Java or Spring Boot developer the learning curve is very less - the heavy lifting is being done by the Spring AI framework. Of course there is more to Spring AI, a lot of beautiful concepts around LLM models which we are going to discuss throughout our sessions. With this, you should be in a pole postion to appreciate this framework which makes integarting with LLM models cheap/easy. As an enterprise organization, no one has to develop their own LLM models, they can simply integarte their applications with the LLM models provided by various vendors. And by investing few dollors, they can convert their applications into intelligent apps. Of course there are multiple falvours of LLM models - we can integarte with LLM models present inside the Cloud environment or inside your own infrastrycture or inside your local environment - we are going to dicuss all those options/alternatives in the coming sessions. Nw you should be crisp clear with this insitial demo and the basic concepts that we have discussed so far.
     * * */
//    @GetMapping("/chat")
//    public String chat(@RequestParam("message") String message) {
//        return chatClient.prompt(message).call().content();
//    }

}