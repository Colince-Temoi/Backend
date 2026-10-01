package com.get_tt_right.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {

    private final ChatClient openAiChatClient;

    public PromptTemplateController(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
    }
/** Here, we only want the LLM model to only provide the email body response. That's why I have given the below clear instructions.
 * The customerMessage can be the customer support ticket that my end user may have raised.
 *
    String promptTemplate = """
            A customer named {customerName} sent the following message:
                           "{customerMessage}"
            
            Write a polite and helpful email response addressing the issue.
            Maintain a professional tone and provide reassurance.
            
            Respond as if you're writing the email body only. Don't include subject,
            signature
            """; */

    @Value("classpath:/promptTemplates/userPromptTemplate.st")
    Resource userPromptTemplate;

    /**Here we are going to build a scenario which is going to help our customer support team to get the potential email responses that they can send to the customers based upon the complaints that they raise.
     * This REST API instead of accepting a single request param, it is going to accept a couple of request params with the names customerName and customerMessage. This time my user message should accept some dynamic values as an input like what is the customer name and what is the customer message. That's why we are not directly hardcoding the user message here which is going to make my code/business logic clumsy.
     * What we can do instead is we can use prompt templates. But before that let's visualize a basic approach that we could also follow (Not recommended). In this class you can create a String constant/attribute with the name promptTemplate - commented above. Inside that prompt template we have defined a prompt which is going to accept some dynamic values as an input like what is the customer name and what is the customer message.
     * Now using the prompt template above, we can now invoke the method#user which is going to accept a lambda expression of Consumer functional interface and this Functional interface it is going to accept PromptUserSpec. So, the input to the lambda expression I am going to mention with the variable name as promptTemplateSpec and inside the lambda body by using this promptTemplateSpec we are going to invoke the method#text() and pass the prompt template followed by we are going to chain our invocations of param() method(s) to pass the dynamic values.
     * This should be good enough. Now you can test your changes using the postman request section2>>UserPromptTemplate. Like this we got a proper response with a professional email body. With this the customer support executive you don't have to type all this. All you can do is review the content and update as required then towards the end  you can put your signature and click on the send button. This REST API is a classic example on how we can improve the productivity of Customer Support team members.
     * This a basic approach that we have discussed but as said don't leverage this basic approach as it makes you code/business logic clumsy. A better and recommended approach - Under the resources folder create a new folder with the name promptTemplates or just templates. Inside that create a new file with the name userPromptTemplate.st - Like this the file nae can be anything but please make sure you are mentioning the file extension as .st. Into this file move the prompt template contents that we have defined above and comment out the prompt template that we have defined above.
     * Next, define the @Value annotation to load the prompt template from the resources folder. Just below that annotation, create a new attribute with the name userPromptTemplate of type Resource. Please make sure you are importing Resource from org.springframework.core.io package. Now, this userPromptTemplate attribute I can pass to the method#text of the PromptUserSpec. After making these changes you can test your REST API one more time and yes, the behavior should be same.
     * With all we have discussed you should now be clear on how to use prompt templates. You may have a question like; why can't I build this prompt template by using String appending concept? I mean, I should be able to append all these dynamic values at runtime and prepare any complex prompt as well. This may be your itch haha! Yes you can do that but is a very clumsy way of doing it. Using Prompt templates is going to provide you better maintainability and separation of the logic. The next common itch you may have is; Instead o the curly braces for the dynamic values, can I use other special characters like less than symbol and greater than symbol? Yes you can use them as well but for that you need to make some configurations. You can check the official doc around this - https://docs.spring.io/spring-ai/reference/api/prompt.html -. I mean the official doc is a place where ypu can refer any/all concepts in case you have any doubts including ones that we have not discussed in our sessions.
     * The information that I want you see in the link https://docs.spring.io/spring-ai/reference/api/prompt.html - Is something to do with TemplateRenderer(I) - This interface you can override if you want to use different syntax inside your prompt template other than the default curly braces. There, they have given an example where they are trying to use the < and > symbols. You can check that out.
     * */
    @GetMapping("/email")
    public String emailResponse(@RequestParam("customerName") String customerName,
            @RequestParam("customerMessage") String customerMessage) {
        return openAiChatClient
                .prompt()
                .system("""
                        You are a professional customer service assistant which helps drafting email
                        responses to improve the productivity of the customer support team
                        """)
                .user(promptTemplateSpec ->
                        promptTemplateSpec.text(userPromptTemplate)
                                .param("customerName", customerName)
                                .param("customerMessage", customerMessage))
                .call().content();
    }

}