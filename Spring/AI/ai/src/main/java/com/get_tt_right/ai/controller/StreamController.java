package com.get_tt_right.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {

    private final ChatClient openAiChatClient;

    public StreamController(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
    }

    /** When we are looking to get a response as a stream we need to make sure we are invoking the Stream method then chain that with any of the methods we discussed previously like content, chatResponse and chatClientResponse. This time the return type of these methods Flux of String or Flux of an object i.e., ChatResponse or ChatClientResponse. Reason: We are using the stream method and LLM is going to send the response as and when it is being generated. This streaming process is going to continously emitt the message from the LLM to our Spring AI application and that why we are going to get Flux of an object i.e., String, ChatResponse or ChatClientResponse as an output.
     * Flux is a concept in the Spring Reactive ecosystem. The main advantage of using the Flux is - it is not going to block the thread until the entire response is received. As and when a response is received the same will be processed and then the thread will be free until the next response is received from the LLM model. You can think of Flux as Conveyor belt where you are going to get the producs asynchronously - as and when a response is being received it is going to process the response and release the thread. Once the entire response is received - the Flux is going to receive a "complete" signal and with that it is going to complete the entire processing of the response. If new to Flux - you may have some challenge understanding.
     * Anyway - let's visualize a demo on how this stream and Flux are going to work so that you can have some idea and in future if you have these kind of scenarios you can try to explore around Flux and Spring reactive programming. Now, do a build and spin up your Spring AI application. Now we will invoke our API from the browser - we don't want to invoke it from Postman - reason: Postman does not support streaming of the response. http://localhost:8080/api/stream?message=Tell%20me%20about%20all%20the%20HR%20policy%20details? - this is what I am invoking from my browser. Since we are using a default system message, to act the LLM model as a HR assistant, I am trying to ask a question related to HR policy only. As soon as I press enter you can be able to visualize that this time I am getting the response as and when it is being generated - like a stream. The browser is not waiting for a complete response to arrive.
     * Whereas if you try to invoke the other REST API http://localhost:8080/api/openai/chat?message=Tell%20me%20about%20all%20the%20HR%20policy%20details?- you will be able to see that the response is going to be received only after the entire response is received. You can subtly visualize what I am talking about the difference between these 2 APIs by refreshing the loader in the browser and seeing what is happening. For the stream API, as and when the loader is loading, the response is going to be received as and when it is being generated - like a stream. For the chat API, the response is going to be received only after the entire response is received - I mean after the loader is done. So, if you have scenario where you want to stream the response to the client applications then please make sure you are using the method#stream instead of the method#call. Please note that when you are using this ,method#stream you need to make sure you are sarrounding/wrapping your return object with the help of Flux.
     * */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message) {
        return openAiChatClient.prompt().user(message).stream().content();
    }
}