package com.get_tt_right.ai.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

/** Anytime when we want to create our own advisor, we need to make sure we are implementing one or both of CallAdvisor or StreamAdvisor interface(s). Since in our scenario we have not explored streaming style of communication yet, we will be implementing CallAdvisor interface. In the coming sessions we will explore on how to send the requests and receive the responses by using the stream option.
 * We are going to override the method#adviseCall, method#getName and method#getOrder.
 *
 * */
public class TokenUsageAuditAdvisor implements CallAdvisor {

//    Since we are looking to log the token details I am introducing a logger variable here.
    private static final Logger logger = LoggerFactory.getLogger(TokenUsageAuditAdvisor.class);

    /** When we are sending the request, inside the request we are not going to have any token details. Reason: The number of tokens/token details will be decided by the LLM once the request reaches to the LLM model - That's why I don't want to disturb anything around the request. We will have the request send directly to the LLM model.
     * To send the request to the LLM model, just like we visualized in the predefne advisors, we have to write the line of code i.e, ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest); Just copy the same and paste it here. Once we get the response form the LLM model, using the chatClientResponse object, I am going to get the actual chat response 1st and the same I will try to assign to the ChatResponse object.
     * Inside this chatResponse object I am going to have the details around the token - but before that I need to make sure I am returning the chatClientResponse object from this methid so that the next advisor can receive the response received from the LLM model.
     * Just before returning the response we can try to execute some logic. Inside the ChatResponse object we are going to have some metadata object and the same we are checking if it is not null. If it is not null, from the same metadat obbject we are going to get an object with the name getUsage. Inside this Usage object we are going to have all the token usage details. And btw whatever logic we are writing inside this if block is specific to OpenAI. Based upon my instructors experience he knows how OpenAI is going to send the tokens information inside the response and that's why he is using this logic. In case if you are using some different vendor LLM model then you need to do the research to understand where they are sending the token details.
     * In case if you are invoking an LLM model deployed inside your own network or in your local - there will be no tokens concept. Reason: Everthing is free when we set up our own LLM model inside our own network/local. Once I check if this Usage object is not null, then I will log the token usage details. If you navigate to the Usage object we have verious members/detail which will tell us what is the number of tokens that the prompt has taken, what is the completion tokens, what is the total tokens - represents the sum of prompt and completion tokens. Prompt tokens are nothing but your request tokens and completion tokens are nothing but the response tokens. If you want to know what the total tokens that are charged by the LLM to process a request end to end then you need to get this totalTokens value.
     * Now, you can put some break point here - Usage usage = chatResponse.getMetadata().getUsage();.
     * With this now our custom advisor is ready to use. As a next step we need to configure it. How? We have to options - either at the controller layer using the method#advisors or at the ChatClientCofig level using the method#defaultAdvisors. Next, you can do the build and test your changes and yes I saw a log i.e., Token usage details : DefaultUsage{promptTokens=89, completionTokens=1062, totalTokens=1151, cacheReadInputTokens=0}. Here we are simply logging the token details but in real enterprise applications you can save them inside the DB and charge the end-user accordingly based upon teh usage. Think of LLM vendors as wholeseller and you as retailer(That's how business works).
     * Since the TokenUsageAuditAdvisor has the highest order preference, it will be executed 1st followed by SimpleLoggerAdvisor. As of now we configured this TokenUsageAuditAdvisor under the Controller level but my instructor does not recommend me configuring the Advisor(s) individually at the REST API level - Reason: Usually most of the advisors logic that we write they will be common for all the REST APIs. That's why comment that line of code and configure the Advisor(s) at the ChatClientConfig level.
     * There we can repeat the line of code i.e., .defaultAdvisors(new SimpleLoggerAdvisor()) and this time pass the object of our custom advisor i.e., .defaultAdvisors(new TokenUsageAuditAdvisor()). Alternatively you can just use one line of code i.e., .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())). and send a list of advisor objects. How to do that, It is very easy, you just need to invoke the List.of method and to this pass the objects of the advisors. Now, you can do a build and test your changes. This time you can test using the section2>>PromptStuffing request becuase this time we are trying to provide the context data hence few tokens will be charged as can be seen in the logs i.e., Token usage details : DefaultUsage{promptTokens=197, completionTokens=289, totalTokens=486, cacheReadInputTokens=0} This is also a reaosn not to use the prmpt stuffing technique if you want to feed large amount of data as that is going to attract a good amount of bill.
     *
     * */
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if(chatResponse.getMetadata() != null) {
            Usage usage = chatResponse.getMetadata().getUsage();

            if(usage != null) {
                logger.info("Token usage details : {}",usage.toString());
            }
        }
        return chatClientResponse;
    }

    /** The name to this Advisor I will provide same as class name.
     * */
    @Override
    public String getName() {
        return "TokenUsageAuditAdvisor";
    }

    /**Coming to the order I am going to mention it as 1.
     * For the SimpleLoggerAdvisor we visualized that the default order is 0 whereas for this custom advisor I am trying to set the order as 1 which means that this Advisor is always going to get the highest preference.
     * */
    @Override
    public int getOrder() {
        return 1;
    }
}