package com.get_tt_right.ai.controller;

import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptStuffingController {

    private final ChatClient openAiChatClient;

    public PromptStuffingController(@Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
    }

    /** In this file if you see, I am trying to give enough context details to the LLM model.
     * By using this details the LLM model can answer to the end-user's query accurately. If the end user is trying to as anything outside of the HR policy then the LLM should answer politely that it is unable to provide/perform the Job and is only an HR assistant.
     * Here inside the prompt template I have provided the entire content/details directly - everything is static here, there are no dynamic placeholders. If needed/if your use case is complex then you can load the dynamic values like notice period value, maternity leave value, Paid leave value, etc. All these details you can load from the DB and populate them into the prompt template dynamically during the runtime. For now, I will go with this static message.
     *Like this we are injecting the system prompt template and the same we will try to feed as an input into the method#system()
     *  */
    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return openAiChatClient
                .prompt()
//                By using this method#options we are trying to send a list of options that are specific to this REST API method. This time instead of hardcoding the model name directly we are trying set the same by using a constant. How? I have to type ChatModel and I have to import a Kotlin file from the package com.openai.models. This file has a list of all supported LLM models by OpenAI. This time for example we are using GPT-5.4 Nano model
//                Once all your options are set/configured you can do the build and once the build is completed you can try to invoke the same API and verify from the console logs that our fine-tuning options are being enforced.
//                .options(OpenAiChatOptions.builder().model(ChatModel.GPT_5_4_NANO.asString())
//                        .temperature(0.7))
                .system(systemPromptTemplate) // If you can open the method#system(), you will see it is accepting a Resource object as an input. So, whatever content available inside this Resource object will be sent as a System message to the LLM model.
                .user(message) // Passing whatever message I received from the end-user.
                .call().content();
    }

}