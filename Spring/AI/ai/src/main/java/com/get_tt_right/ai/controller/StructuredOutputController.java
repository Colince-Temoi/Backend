package com.get_tt_right.ai.controller;

import com.get_tt_right.ai.model.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/** On top of this class, I'm going to mention a couple of annotations as can be visualized. Next I'll try to inject a bean of ChatClient.
 *
 */
@RestController
@RequestMapping("/api")
public class StructuredOutputController {
    private final ChatClient openAiChatClient;

    /** What is going to happen ? Whatever ChatClient bean that we have created inside the ChatClientConfig is going to be injected. While creating the ChatClient bean, we have set a default system message to act our LLM model as an internal HR assistant, but to test our scenario we should remove this restriction to make our LLM to respond to any kind of question.
     * That's why, what I'll do is, I will try to create another bean of openAiChatClient by using ChatClientBuilder object. Let me go to ChatClientConfig and copy this entire input parameter and the same I'm going to paste it here. So whatever openAiChatClient bean that I have created here, this is specific to this class.
     */
//    public StructuredOutputController(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
//        this.openAiChatClient = openAiChatClient;
//    }
    public StructuredOutputController(OpenAiChatModel openAiChatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(openAiChatModel);
        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8);
        chatClientBuilder.defaultOptions(options).defaultAdvisors(new SimpleLoggerAdvisor());
        this.openAiChatClient = chatClientBuilder.build();
    }

    /** Now let me create a new rest API with the path chat-bean. This is going to accept a request to param with the name message, and the return type of this method will be ResponseEntity of country cities.
     * When we're invoking the content() method, we're going to get the output in a string format, but instead I should invoke the entity() method and to this entity() method I can pass CountryCities.class as an input. With this we are telling to the framework I'm expecting the output in the form of CountryCities object.
     * I'm getting a compilation error because the method return type is response entity of CountryCities. Maybe what I can do is, I can try to catch the output with an object name as CountryCities. The same, I can try to return from this method by using ResponseEntity.ok() and by passing the CountryCities object in the payload.
     * Please make sure you are importing the ResponseEntity which belongs to the spring framework. There is another class with the same name inside the org.springframework.spring-ai-client-chat library. For our case, the ResponseEntity present inside the Spring Core library should be enough.
     */
    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCities> chatBean(@RequestParam("message") String message) {
        CountryCities countryCities = openAiChatClient
                .prompt()
                .user(message)
//                .call().content(CountryCities.class);
                .call().entity(CountryCities.class);
//                Save the changes and invoke this REST API. Like this I got a very similar o/p. Inside the response I got the country name along with the city details, which are matching with the format of my CountryCities.class. But I feel instead of using this lengthy or complicated approach, we can simply pass CountryCities.class as an input to the entity() method and behind the scenes spring AI is going to do the magic for you.
//                .call().entity(new BeanOutputConverter<>(CountryCities.class));
        return ResponseEntity.ok(countryCities);
    }

    /** I renamed the method name from chatBean to chatList. Also, the REST API path chat-bean to chat-list. From this REST API, I want to return only the list of cities in a given country. I don't want to return the country name, I only want to return the list of city names. That's why I'm going to update the return type to list of string.
     * With this expectation, we can't send the directly list of string class as an input to the entity() method. Instead of passing the class name details, we can invoke another entity() overloaded method which is going to accept the object of StructuredOutputConverter. StructuredOutputConverter is an interface inside the spring AI, which is going to convert the raw LLM output into a structured response of type <T>. You can Open it and see the docstring details.
     * We can look for the implementation classes for this interface. There is an implementation class with the name ListOutputConverter. By using this ListOutputConverter, we can request the LLM to send the response as a list of string objects. In its In these getFormat() method, they clearly highlighted the instructions that they are going to provide to the LLM i.e., "Respond with only a list of comma-separated values, without any leading or trailing text. Example format: foo, bar, baz"
     * Let's try to pass the object of this ListOutputConverter as an input to the entity() method. Save this, do a build and one the build is completed test to verify that everything is working as discussed. In the Postman, I am providing the same message i.e., "Provide me the city's these details in USA". Please make sure you are invoking the chat-list REST API. As soon as I click on the send button, I'm going to get only the list of city details under the output. This is proof of what we have just discussed. I didn't get any Java object or the country name details. In the similar lines we also have another converter with the name MapOutputConverter, this converter you need to use in the scenarios where you are expecting the output in the form of Map object.
     * Check the next REST API for more details.
     * */
    @GetMapping("/chat-list")
    public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
        List<String> countryCities = openAiChatClient
                .prompt()
                .user(message)
//                .call().content(CountryCities.class);
//                .call().entity(CountryCities.class);
                .call().entity(new ListOutputConverter());
        return ResponseEntity.ok(countryCities);
    }

    /** The LLM is doing some default assumption, but you can give more clear instructions on how you want the value object to come based upon how clearly you are prompt to message and accordingly, you're going to get the structured response in the form of a Map object.
     * Just like how we have the ListOutputConverter and MapOutputConverter, we also have another implementation class of the StructuredOutputConverter, with the name BeanOutputConverter. This BeanOutputConverter we can use when we are expecting the response to come in the format of a given Java object. We already built such a scenario by using entity() method and passing the Java class name i.e., CountryCities.class.
     * Instead of using that approach, we can also send a object of BeanOutputConverter and to this BeanOutputConverter constructor we can pass the Pojo class name details like CountryCities.class. Check that out above in chat-bean REST API.
     * */
    @GetMapping("/chat-map")
    public ResponseEntity<Map<String,Object>> chatMap(@RequestParam("message") String message) {
        Map<String, Object> countryCities = openAiChatClient
                .prompt()
                .user(message)
                .call().entity(new MapOutputConverter());
        return ResponseEntity.ok(countryCities);
    }

    /** I changed the return type from List<String> to List<CountryCities>. And with that I'm going to get a compilation error because the ListOutputConverter is only capable of returning the structured response in a form of list of string, but this time the requirement is more complex. I want a list, but the list should have the objects of my own custom Pojo class which in this case is CountryCities.
     * Let's try passing the name of the class to the entity, just like how we did in chat-bean REST API. This is not going to work in Java. haha! Do you know the reason this is somewhat complex? Inside Java, generics are going to be erased during the runtime. So whatever CountryCities that I'm trying to provide here, this is going to be erased during the runtime. So that's why I'm getting a compilation error "Cannot access class object of a parameterized type"
     * Then how to resolve this compilation problem? To get a clue, we can try to look for if there is any overloaded entity() method is available. Yes, there is a overloaded method available which is accepting parameterized type reference as an input. Inside Spring lib There is a utility class with the name ParameterizedTypeReference. This is a spring utility class, which is going to help us to retain the generic information at runtime. Behind the scenes this utility class it is going to use a small trick of anonymous subclassing. By using this we should be able to solve our problem.
     * Now, as an input inside the entity() method, I need to create an object of new ParameterizedTypeReference. We don't have to override any of the method. Click on this cancel and mention semicolon towards the end. So if you try to understand the syntax to the constructor of this ParameterizedTypeReference, I'm passing the generic type, which is list of CountryCities. And since it is an abstract class, I'm trying to create an object of this class by using anonymous subclass syntax. As you can see, the body of the implementation class is empty. With this now our problem should get resolved.
     * Save, do a build and invoke the respective api from postman. This time the message being, "Provide me the country and their cities details in Europe ?" If I click on the send button, I'm going to get the list of my CountryCities objects. So here each object is having a country name and the cities. With what we have discussed now you should be crisp clear on how to get a structured output response from an LLM model. The key is to invoke this entity() method. All the concepts and the examples that we have discussed around the structured output converter, I have also mentioned them in our beautiful slides please refer them for more details and a refresher.
     * */
    @GetMapping("/chat-bean-list")
    public ResponseEntity<List<CountryCities>> chatBeanList(@RequestParam("message") String message) {
        List<CountryCities> countryCities = openAiChatClient
                .prompt()
                .user(message)
//                .call().entity(new ListOutputConverter()); // wont work for List<CountryCities>
//                .call().entity(CountryCities.class); // won't work as well.
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>() {
                });
        return ResponseEntity.ok(countryCities);
    }


}
