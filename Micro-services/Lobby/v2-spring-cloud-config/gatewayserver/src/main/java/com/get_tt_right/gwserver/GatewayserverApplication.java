package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/** Update as of 09/08/2025
 * --------------------------
 * Introduction to event driven microservices
 * -------------------------------------------
 * What are they? why should we build them? To understand this, we are going to answer the below questions:
 * 1. How can we avoid temporal coupling whenever possible inside our ms network? You might have already heard of loose coupling but temporal coupling might be a new word for you. First lets try to understand what is loose coupling and how it is different from temporal coupling. As part of loose coupling, we will try to build our appn's business logic in separate ms's so that they can be developed, deployed and scaled independently. This is what we have been trying to achieve since we began our sessions. We separated all the logic related to accounts into accounts ms and so on. With that we achieved a loose coupling.
 *    Temporal coupling occurs whenever a caller service expects an immediate response from a callee service before continuing its processing. Think like, you have ms1 and ms2, ms1 is depended on ms2. So, whenever the ms1 is trying to invoke ms2, ms1 is going to continuously wait till it gets a response from the ms2. In this kind of scenario, ms1 has a temporal coupling with ms2. So, any slow behavior of ms2 will directly affect/have an impact on ms1. That's why wnenever possible, we need to avoid temporal coupling between our ms's. And this temporal coupling happens whenever we are using synchronous communication between the services. As of now, we have been following the synchronous communication with the help of REST APIs.
 *    So, in order to avoid the temporal coupling, we have to use asynchronous communication whenever possible inside our ms's network. Synchronous communication between the services is not always necessary. In many real-world scenarios, asynchronous communication can fulfill the business requirements very effectively. So, wherever possible, we need to establish the asynchronous communication between the services. You're maybe having questions about what is asynchronous communication? We will get to know this. Before that, we will see/clarify why we should not consider synchronous communication in all type of scenarios. Inside the synchronous communication, there are 2 approaches majorly used by everyone. The 1st one is, imperative approach and the second one is, reactive approach. Inside the imperative approach, whenever the ms1 is trying to invoke ms2, in such scenarios, there will be a thread dedicatedly assigned to this communication and a thread which is blocked for this operation is going to wait continuously for the response to come from ms2. So, this us a very plain synchronous communication approach.
 *    In the reactive approach, there won't be any threads blocked in the ms1 to wait for the response from ms2. Instead, what is going to happen is - there will be a thread while invoking the ms2, after the invocation, the thread will go back to the thread pool, and it will try to pick up the next request which are coming to ms1. When the response from the ms2 is received by the ms1 then only a thread from the thread pool is going to be assigned. Here in the reactive approach, we are trying to use the threads more efficiently compared to the imperative approach. Regardless of whether you are using imperative approach or reactive approach, the communication between the 2 ms's is going to be synchronous - which means until unless your ms1 is going to receive the response from the ms2, it is not going to process the next business logic. This kind of synchronous communication you may need for critical business scenarios inside your ms's where you want to show the immediate response to the end-users. Think like, inside your bank website, the end-user is trying to understand what is his current balance - he will click on a button which will reveal his current balance. In such scenarios, the communication has to be synchronous because the end user is waiting to see the response immediately on the screen.
 *    But we should not use synchronous communication in all type scenarios. Wherever possible, we need to leverage asynchronous communication. So, how to build this asynchronous communication between the ms's? In order to build asynchronous communication between the ms's, we need to build event driven ms's. An event is an incident, that happens inside your ms's which signifies a state transition or an update inside your system. Whenever an event takes place, we need to alert the concerned parties. For example, in an e-commerce application, whenever you place an order inside for example amazon website, the order ms has to notify the delivery ms which is deployed inside the amazon network. The communication between the order and delivery ms doesn't have to be a synchronous communication, Instead the order ms - as soon as the end-user made a payment and the order is confirmed, it is going to trigger an event which is going to act as a notification to the delivery ms. In this scenario, the order ms is just only going to generate an event or trigger a notification. Apart from that, the order ms is not going to wait for the delivery process to complete and the delivery ms to give a successful response. The order ms responsibility is to only notify/send a notification to the delivery ms. So, this is a classic example of event-driven ms.
 *    If keen enough, here the communication is not going to happen synchronously, instead the communication is going to happen with the help of asynchronous communication. Reason - order ms is not waiting for the successful response from the delivery ms. Instead, it send a notification or alert to tell that I have already done my part and my main job is completed. With all this we have discussed, you should be clear.
 * Now, you may have a question like - how do we go about building these event driven ms's. In order to build the event-driven ms's, we need to follow event driven architecture which is producing and consuming events with the help of asynchronous communication, event brokers and in the same process we can leverage 2 fascinating projects available inside the Spring cloud ecosystem which are Spring cloud function and Spring cloud stream. So, far you should be clear with the challange and are super excited with the event driven ms's. So, lets explore the world of event driven ms's.
 *
 * Introduction to event driven models
 * --------------------------------------
 * We have decided to build event driven ms's wherever possible inside our ms's network. In order t build event driven ms's, inside the industry, there are 2 majorly used event driven models: 1. Publisher/Subscriber model aka Pub/Sub model - This model revolves around subscriptions. Producers will generate the events and that are distributed to all the interested subscribers for consumption. Inside this model, once an event is received and consumed by the consumers, it cannot be replayed again and again which means any new subscribers joining later will not have access to the past events. 2ndly, we have Event Streaming model - Inside this model also, the producers will produce the events and the consumers will consume the events. But here, there is a clear difference between these 2 models. Inside event streaming model, events will be written into a log in a sequential manner. Producers publish the events as they occur, and these events are stored in a well-ordered fashion. Coming to the consumers, instead of subscribing to the events, they will have the ability to read from any part of the event stream which means - the events can be replayed allowing the clients to join at any time and receive all the past events as well. Yes, that was one of the major difference between these 2 models, and you need to choose one of them based upon your requirements/business scenario.
 * If your business scenario is not expecting your consumers to read the past events then you should go for the Publisher/Subscriber as the ideal model. Whereas if you want to give flexibility to your subscribers/consumers to read/replay the past events then you should go for the Event Streaming model. If needed, inside event streaming model we can disable the replaying of the events feature. So, there is no good or bad approach between these 2 approaches in terms of event driven ms's. You can choose whichever based upon your requirements/business scenario. Now, how can we follow/implement these models inside our ms's? is a question you're maybe asking yourself. For the same, both of these models have various products available. The pub/sub model is frequently implemented with the rabbit MQ. The event streaming model is frequently implemented with Apache kafka which is a robust platform that is widely utilized for event streaming processing. All these we are going to leverage in our sessions. Since these 2 products are completely different from each other providing various capabilities, we are going to discuss each of them separately. First, we are going to focus on building event driven ms's with the help of rabbit MQ. Next, we are going to focus on building event driven ms's with the help of Apache kafka. So far, you should be crisp clear about these 2 event driven models that we have discussed. Next, we are going to discuss in details the pub/sub model with the help of rabbit MQ.
 *
 * What we are going to build using a pub/sub model
 * ---------------------------------------------------
 *  Check slides for more theoretical details.
 *
 * Spring Cloud Function
 * ---------------------------
 * Under the Spring Cloud project, we have a subproject called - Spring Cloud Function. If you visit its page, you will see a high level overview tab and what are the goals why we need to use this Spring Cloud Function. You will see that it: 1. Promotes the implementation of business logic via functions. 2. Decouples the development lifecycle of business logic from any specific runtime target so that the same code can run as a web endpoint - which means Rest APIs, a stream processor, or a task. 3. It also supports running your business logic as a stand-alone either in your local or a PaaS environment.
 * Apart from these, if you scroll down on this Overview tab, under the section "Spring Cloud Function features:", you will be able to see, that there are many adapters available to integrate your Spring Cloud Function(s) with Aws Lambda, Microsoft Azure, Google Cloud Functions, Apache Open Whisk and more. So, all the majority of the Serverless providers have adapters that we can use to integrate with the Spring Cloud functions. Next, you can click on the learn tab to get the official documentation. You will be able to see multiple sections available if you want to learn more about Spring Cloud Functions i.e., If you want to deploy your Spring Cloud function as a stand-alone web applications you can refer to the section " Stand-alone Web Applications", very similarly you can deploy them as a stand-alone streaming application the refer to "Stand-alone Streaming Applications", you can deploy them as a packaged function -" Deploying a Packaged Function". We also have a section explaining about "Serverless Platform Adapters". You will see official documentation on how to deploy your business logic with the help of Spring Cloud Functions inside the serverless environments like AWS Lambda, Microsoft Azure function, Google Cloud Functions,etc. Majority of the cloud providers and the serverless platforms are supported for Spring Cloud Function.
 * With all we have discussed starting from the pdf to here, you should have understood on why we should also learn Spring Cloud Function whenever we are trying to build ms's. Using these Spring Cloud function, we always build event driven ms's. In our scenario we are going to build a new ms with the name message. And since this new ms is going to completely use event driven model, it will be a good decision to build this message ms with the help of Spring Cloud Function.
 *
 * Develop message ms with the help of Spring Cloud Function
 * ----------------------------------------------------------
 *
 * *  * */
@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/* This method is going to create a bean of type RouteLocator and return it.
	* Inside this RouteLocator only, we are going to send all our custom routing related configurations based on our requirements.
	**/
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/eazybank/accounts/**")
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))

				.route(p -> p.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("lb://LOANS"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())))
						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 1, 1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
