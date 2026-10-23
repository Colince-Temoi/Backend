package com.get_tt_right.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/** Update as of 2026-10-01
 * --------------------------
 * Foundations of Generative AI and LLMs
 * ---------------------------------------
 * Check slides for more details.
 *
 * Funny Memes of AI
 * ------------------
 * Check AImemes folder. You will visualize some images/memes which conveys a message around Artificial Intelligence in a fun style.
 * With what we have visualized you should have gotten some good break around the funny AI memes. Also you must have gotten the message, "Always use the gen AI tools as your assistants but don't try to completely rely on them and don't let them to take over you."
 *
 * Types of Generative AI models
 * ------------------------------
 * Check our beautiful slides for more details.
 *
 * Introduction To LLMs - The Text Experts
 * -----------------------------------------
 * Check our beautiful slides for more details.
 *
 * Core Job of an LLM - Guessing Next Word.
 * -------------------------------------------
 * Check our beautiful slides for more details.
 *
 * Attention is all you need - white paper.
 * ------------------------------------------
 * So, this is a white paper which is initially published around 2017 by this list of scholars or scientists working at Google research. In this white paper, they proposed a new simple network architecture with the name Transformer.
 * By using this Transformer architecture they initially experimented on two machine translation tasks which shows these models to be superior in quality while being more parallelizable and requiring significantly less time to train.
 * This is the initial step that led in building the LLM models. Though Google Scholars published this white paper it is the OpenAI which published the first LLM model by using ChatGPT. That's why you might have observed a discussion as well in the web. In the initial days of ChatGPT Google also rushed to release an LLM wrapper with the name Google Bird, but that is not equally good as ChatGPT.
 * And that's where everyone pulled the leg of Google saying that you guys published a white paper based upon your research but by using that research you didn't able to build a perfect LLM wrapper like ChatGPT. At a later point of time many vendors like Microsoft, Twitter, everyone started building the LLM wrappers and right now Google is doing a pretty good job under the name of Google Gemini.
 * We can read this white paper by clicking on this view PDF - https://arxiv.org/pdf/1706.03762. If you click on this view pdf it is going to show you the white paper of 15 pages. Here they clearly highlighted how this model with the name transformer is going to work. It is going to use various concepts like Embedding, Positional Encoding, attention layers. We don't have to learn all these in detail but on a high level throughout this session. I will try to explain most important topics that you need to understand to know the internal working of an LLM model.
 * As a developer, you don't have to know everything around these LLM models. But you should have a basic knowledge and that is what we are trying to achieve throughout this session.
 *
 * The concept of Tokens and Token IDs in LLM.
 * -------------------------------------------
 * Check our beuatiful slides for more details
 *
 * Inside an LLM dictionary - Understanding Model Vocabulary.
 * -----------------------------------------------------------
 * Check our beautiful slides for more details.
 *
 * Embeddings and Vectors - A way to represent meaning.
 * -----------------------------------------------------
 * Check our beautiful slides for more details.
 *
 * How embedding Vectors are calculated.
 * --------------------------------------
 * Check our beautiful slides for more details.
 *
 * What are Static Embeddings.
 * ----------------------------
 * Check our beautiful slides for more details.
 *
 * Positional Embeddings - How AI understands word order.
 * ------------------------------------------------------
 * Check our beautiful slides for more details.
 *
 * The magic of attention - How AI understand context.
 * ---------------------------------------------------
 * Check our beautiful slides for more details.
 *
 * Summery Discussion
 * -------------------
 * Initially, a end user will provide an input to an LLM wrapper or LLM model. But before this LLM model released to public this model will go through the training. But before this LLM model released to public this model will go through the training. During the training we know what is going to happen. The LLM model will be trained on billions of words and trillions of tokens. Based upon these billions of words the LLM model is going to build its own model vocabulary. As of today, the vocabulary size is around 100K but this is expected to increase a lot in the coming future. Inside this model vocabulary we are going to have that tokens, token IDs along with the static embedding value. The static embedding values they are represented using vectors.
 * For example, for the word king there'll be a token ID something like 361 will be assigned and this token ID internally refers to an static embedding. So this is what is going to happen during the training phase. Now let's assume the model is deployed into production and made available to the public. When an end user provided a input to the ChatGPT, it is going to first convert that input to tokens by using that Tokenization concept. As part of this Tokenization entire input sentences they are going to be split into multiple tokens along with their token IDs. These tokens along with their static embedding values they are going to be updated with the positional embedding. Once the positional embedding is added the same will be provided as an input to the Transformer architecture.
 * Inside the Transformer architecture we are going to have multiple attention layers. Inside these attention layers the attention score is going to be calculated by the LLM model and that's how they are going to understand the entire context and the entire meaning of each of the word. and after multiple other calculations, we are going to get the successful response in a token format. So this token is again converted into a normal text by using the same model vocabulary and these normal text will be shown to the end user on the UI page. So this is a high level information on how the LLMs they are going to work internally.
 * I put my best efforts to explain you the concepts of generative AI, LLM models in a simple language. You don't have to know everything about the internal functioning of the LLM models. But you need to be aware about all the jargons like embedding tokens, token IDs, vectors, attention layers so on. Whatever is required and important for you those details I covered throughout this section. These jargons I'm going to use to a great extent in the coming sections when we are going to handle the advanced scenarios by using Spring AI framework. In case if you want to explore more details about this fancy LLM models or gen AI models or in general about artificial intelligence you can refer to this YouTube channel with the name 3Blue1Brown. Inside this channel they explained all the complex topics like LLM models using good graphic representations.
 * For example, if you go to these videos you'll be able to see there is a video with the name Large Language Models explained briefly. In the similar lines you also have another video with the name How might LLMs store facts, Deep learning chapter7.There is another video explaining about the Transformers, the tech behind the LLMss Deep Learning chapter5. There is a video around the attention layer that is being followed inside the Transformers. There is tons of information but please don't expect that the explanation will be in a very simple language - haha! The explanation will be somewhat fast and they are going to use a lot of mathematical jargons.They assume that you are an AI engineer and you know most of the jargons and concepts. But you can try to watch these videos and grasp as much as possible and whatever required for you. You should now be crisp clear with all the discussions that we have done around the gen AI and LLM models. From the next session, let's shift our focus onto the Spring AI.
 *
 *
 **/
@SpringBootApplication
public class AiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

}
