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

/** Update as of 17/08/2025
 * --------------------------
 * Develop message ms with the help of Spring Cloud Function
 * ----------------------------------------------------------
 * We will generate a base skeleton project for or message ms by adding dependencies related to Spring Cloud Function. For the same, navigate to the site - https://start.spring.io/. Make sure you are selecting the project as 'maven' Language as Java Spring Boot version - whatever stable default version selected for you. Right now it is '3.5.4'. Under the project metadata the 'Group' is going to be - 'com.get_tt_right' The 'Artifact' is going to be 'message'. The 'Name' also is going to be 'message'. 'Description' is going to be 'Microservice to support messaging in get_tt_right-bank'. The 'Package name' will be automatically be populated for you based on the 'Group' and the 'Artifact' values. Packaging make sure to select 'jar' and Java version is going to be '17'. Now, you can click on 'Add dependencies' tab and add the following dependencies:
 * 1. Search for 'function' - You can be able to read the brief description attached to this dependency. I.e., " Promotes implementation of business logic via functions and supports a uniform programming model across serverless providers, as well as the ability to run stand-alone(locally or in a PaaS). So what is this 'Uniform programming model'? - It is the business logic that you are going to write with the help of functions. So, the same business logic you can deploy across various serverless providers, or you can also use/deploy it as a stand-alone web application or streaming application." Like this, there are many possibilities if you write your business logic with the help of Spring Cloud Function. The same is what they are trying to convey with the help of the brief description attached to the dependency. So, select that dependency. And that's the only dependency we only need for now.
 *  If you click on the Explore button,you can see that under the dependencies we have: 'spring-boot-starter', 'spring-boot-starter-test' - Added for us behind the scenes and 'spring-cloud-function-context' - which we just added manually. So, 'spring-cloud-function-context' is the very important dependency that we need to add whenever we are trying to use Spring Cloud Function. Now, since I am good with these dependencies, I can go ahead and click on the download button to download our maven project into your desired location. Finally, extract it and import it in your IDE. Check the main class of the message service for continuation of this discussion.
 *
 *  Apache Kafka Vs RabbitMQ
 *  ---------------------------
 *  Previously, we have discussed on how to build ms's that can communicate asynchronously with the help of RabbitMQ message broker. Now, we are going to focus on how to build asynchronous communication or event driven communication between ms's with the help of Apache Kafka. Before we try to understand what is Apache Kafka ni details, first we will learn on an introduction about what are the differences RabbitMQ and Apache Kafka and under which scenarios we need to choose Apache Kafka or RabbitMQ. Like we discussed previously, both are popular messaging systems. But, they have some fundamental differences in terms of how they are designed, architecture, and use cases. If you are asking what are these differences, see next line;
 *  1. In terms of design - Kafka is a distributed event streaming platform whereas RabbitMQ is a message broker. This means, Kafka is designed to handle large volumes of data while RabbitMQ is designed to handle smaller volumes of data with more complex routing requirements. So, if you are looking for complex routing requirements inside your application then you need to go with the rabbitMQ. Whereas if you are tyring to handle large amounts of data, then Apache Kafka is the best product for you.
 *     These days rabbitMQ is also trying to become an event streaming platform in its recent versions, but there is a long way for them to achieve the capabilities provided by the apache Kafka.
 *  2. The next important difference is around the data retention - How the data is going to be stored inside the Apache Kafka and the RabbitMQ. So, whenever you send the messages or the event details to these products, Kafka is going to store all the data on the disk whereas RabbitMQ is going to store the data inside memory. Since Kafka is using disk capacity to store the data, it can store or retain any amount of data for longer periods of time whereas since RabbitMQ is trying to store the data inside the memory, then it is more suitable for the applications that require very low latency.
 *  3. There is also a difference in terms of performance - Kafka is generally considered faster than RabbitMQ, especially whenever you are trying to deal with large volumes of data. However, RabbitMQ can have better performance in the scenarios where your application need complex routing requirements.
 *  4. Scalability - Kafka is highly scalable whereas rabbitMQ is more limited in its scalability. Kafka is highly scalable because, whenever we are using Kafka, we can horizontally add any number of kafka brokers to the kafka cluster which simply means that there is no limitation to you on how much data you want to you want to process or how much kafka brokers you want to set up inside the cluster. Whereas, when you are dealing with rabbitMQ, definitely there will be some limitations on how much you want to scale rabbitMQ.
 *
 * Overall, both these 2 products are good, they support event streaming or asynchronous communication between your microservices. So, whenever you are in a dilemma on which product to use, it completely depends upon your requirements. If you are looking for a high performance messaging system that can handle any large volume of data, then kafka is a good choice. If you are not going to handle large volume of data and if you are looking for a message system with complex routing requirements, then rabbitMQ is a good choice. Moreover, RabbitMQ is very easy to maintain compared to the apache kafka. In simple words, if your organization is going to handle a small amount of data on day-to-day basis, then RabbitMQ is a good choice. Otherwise, you can always leverage Apache Kafka.
 * With all we have discussed you should be having crisp clarity on what are the differences between Apache Kafka and RabbitMQ. From the next sessions, we will try to understand more details about Apache Kafka.
 *
 * Introduction to Apache Kafka
 * -------------------------------
 * Before sharing more details about apache kafka, we will visualize a real life example that is very close to apache kafka. Check slides for this.
 *
 * Apache Kafka Installation
 * --------------------------
 * To get started with Apache Kafka inside our ms's, first we need to make sure we set up the Apache Kafka inside the local system. For the same you can come to this official website of Kafka i.e., https://kafka.apache.org/
 * Below are my own steps that I researched using ChatGPT after having dificulties to install Apache Kafka inside my local system by following the steps provided in the official documentation under the Get Started/Quickstart section.
 *  1.  Navigate to the Kafka directory from PowerShell i.e., cd C:\Kafka\kafka_2.13-4.1.0
 *  2.  Remove-Item -Recurse -Force .\tmp\kraft-combined-logs
 *  3.  .\bin\windows\kafka-storage.bat random-uuid
 *  4.  .\bin\windows\kafka-storage.bat format --standalone -t k-ANpMsUTKK68g4DCshGew -c .\config\server.properties
 *  5.  .\bin\windows\kafka-server-start.bat .\config\server.properties
 *
 * Updates to Accounts and Message ms's code to connect to this Kafka and leverage it for event streaming and asynchronous communication
 * --------------------------------------------------------------------------------------------------------------------------------------
 * In the pom.xml of accounts and message ms, the only changes that I need to make are; 1. Okay right now, we are using the dependency related to RabbitMQ i.e., spring-cloud-stream-binder-rabbitmq. Now, we need to change this dependency to the dependency related to Apache Kafka i.e., spring-cloud-stream-binder-kafka. 2. Change the tag name to the next higher version under the image element that is present in the jib related plugin configurations.
 * Next, in the application.yml file present inside message ms, here we have previously mentioned the rabbitMQ related connection details. Delete them as we are no more going to use rabbitMQ inside this section. Under the element 'stream', I am mentioning a child property i.e., 'kafka.binder.brokers' under the brokers we need to mention what are the endpoint url(s) of Kafka. Nothing but where is Kafka available. The Kafka inside our local system is available at the port 9092 and with the host name as 'localhost'. If you have multiple kafka brokers running inside a cluster you can try to mention all of them as a list of elements under the 'brokers' element. Since right now we only have a single kafka broker running inside our system, we need to mention a single value under the 'broker' property. This same changes, I am making inside the application.yml file of accounts ms. Reason: Inside accounts ms also we have previously mentioned properties related to the rabbitMQ. So same drill, delete the properties/configurations related to the rabbitMQ connection details.
 *  - Next I will move my cursor under the 'stream' property, under the same position where 'bindings' is present and mention a new property i.e., kafka.binder.brokers and a value i.e., 'where your kafka has started. Btw, I just remembered, I won't delete the Kafka related connection details configurations inside accounts ms because we are using it to load some configurations from GitHub.
 * So, those are the only changes that we have to do, we don't have to make any other changes to make our ms's work asynchronously with the help of event streaming model and the apache kafka. I am not joking here, haha, these are the only changes. I just replaced the dependencies and connection details related to rabbitMQ with the kafka related dependencies and connection details as discussed. THOSE ARE THE ONLY,haha, changes that I have done. With this, you should be super clear about the power of SPRING CLOUD STREAM', all the infrastructure concerns are going to be taken care of behind the scenes hence giving a nice developer experience because the transition from one product to another is going to be super quick by just adding the respective dependencies and properties as discussed that are needed for a particular product. Actually, I even smiled personally! As a next step, I can start all my ms's in th recommended order as we have been doing since day one. The only ms's you will not start are Cards and loans related services as we are not going to use them in any demo here for now.
 *  - You have to also make sure that you started kafka using the steps discussed previously above before starting your services. With this, my accounts and message ms 's will be connected to the local kafka running behind the scenes. We can also validate the same, i.e., Are the required topics created inside the apache kafka? For this, we can leverage a plugin available inside the InteliJ IDEA. The plugin is, 'kafkalytic'. Install the same from the marketplace inside your IntelliJ IDEA. Once done, on the RHS nav of you IDE, you will be able to see a 'K' symbol. Once you have installed this plugin, you can click on the '+'/ add button and mention the kafka broker connection details. Inside our local system, we only have one broker which is running at the port 9092, so I mentioned 'localhost:9092'. If you have multiple brokers then you can mention them as comma separated i.e., host1:port, host2:port. Next, you can click on the Test connection button to verify that the connection is successful or not. After that, you can click on the 'Ok' button. With this, you can clearly see that we have a new folder created under this Kafkalytic i.e., localhost:9092. Under this local kafka you should be able to see sub-folders i.e., 'Brokers', 'Consumers', 'Topics'.
 *  - Under the 'Brokers' folder, you can see that there is one broker. Similarly, if you try to expand the 'Consumers' folder, you can see we have 3 consumers, most importantly focus on 'accounts' and 'message'. The 3rd consumer you are seeing i.e., 'anonymous....' is a result of the rabbitMQ and configs loading related stuff. And btw, I noticed that, I can use both Kafka and RabbitMQ simultaneously in a single Spring Cloud Stream (Spring Boot) application. Reason: Each messaging system (Kafka, RabbitMQ, etc.) has its own binder implementation (spring-cloud-stream-binder-kafka, spring-cloud-stream-binder-rabbit). And hence, You can declare multiple binders in your application and bind different channels (destinations) to different messaging systems. So, Depending on configuration, some streams can go to Kafka topics, others to RabbitMQ queues. This is what is happening in the Accounts service because I am using RabbitMQ to load GitHub configs and Kafka for event streaming as discussed in details above. Btw, as a disclaimer, you may run into 'Caused by: java.lang.IllegalStateException: A default binder has been requested, but there is more than one binder available for 'org.springframework.cloud.stream.messaging.DirectWithAttributesChannel' : rabbit,kafka, and no default binder has been set.' issues. Reason: when you have more than one binder (Kafka + RabbitMQ) on the classpath, but you have to tell Spring Cloud Stream which one to use as the default. Spring Cloud Stream tries to pick a binder automatically if you don’t specify one, but since you have two binders (rabbit, kafka), it doesn’t know which one to choose.
 *    To fix this, Set a default binder i.e., spring.cloud.stream.default-binder: kafka   # or rabbit. Especially If most of your bindings should use Kafka (or Rabbit), set it as the default. This way, any binding that doesn’t explicitly specify a binder will use kafka. Option 2: Specify binder per binding i.e.,
 *    spring:
 *   cloud:
 *     stream:
 *       binders:
 *         kafka-binder:
 *           type: kafka
 *           environment:
 *             spring.kafka.bootstrap-servers: localhost:9092
 *         rabbit-binder:
 *           type: rabbit
 *           environment:
 *             spring.rabbitmq.host: localhost
 *             spring.rabbitmq.port: 5672
 *             spring.rabbitmq.username: guest
 *             spring.rabbitmq.password: guest
 *
 *       bindings:
 *         kafkaOutput:
 *           destination: kafka-topic
 *           binder: kafka-binder   # 👈 explicitly choose binder
 *         rabbitOutput:
 *           destination: rabbit-queue
 *           binder: rabbit-binder  # 👈 explicitly choose binder
 *   - If you want some bindings to go to Kafka and others to RabbitMQ, configure each one explicitly as shown above.
 * ✅ Rule of thumb:
 * If most channels use one binder → set default-binder.
 * If you’re truly mixing Kafka and Rabbit in one app → explicitly set binder on each binding.
 *
 * This was my use case: I am using RabbitMQ to load configs from GitHub and Kafka for event streaming. Hence, didn't want to delete the RabbitMQ related connection details from the application.yml file of accounts ms. Below is what is happening in the case of this setup:
 * You have two binders on the classpath:
 *  . RabbitMQ → because you’re using Spring Config Server with RabbitMQ for config refresh.
 *  . Kafka → because you’ve configured spring.cloud.stream.kafka.binder.
 * Spring Cloud Stream doesn’t know which binder to use for your updateCommunication-in-0 and sendCommunication-out-0 channels, since you didn’t explicitly tell it.
 * ✅ How to fix
 * Since you’re using RabbitMQ for config refresh only (via Spring Cloud Bus/Config), but Kafka for event streaming in your microservice:
 * Keep RabbitMQ configured under spring.rabbitmq → that’s fine, Spring Cloud Bus will use it automatically for config refresh events.
 * You don’t need to define a Rabbit binder for event streaming unless you also want to consume from Rabbit queues inside this app.
 * Tell Spring Cloud Stream to use Kafka as the default binder → so your function bindings (updateCommunication etc.) will always go through Kafka.
 *  spring:
 *   cloud:
 *     stream:
 *       default-binder: kafka
 *       bindings:
 *         updateCommunication-in-0:
 *           destination: communication-sent
 *           group: ${spring.application.name}
 *         sendCommunication-out-0:
 *           destination: send-communication
 *       kafka:
 *         binder:
 *           brokers:
 *             - localhost:9092
 *  ✨ Key Points
 * spring.rabbitmq config stays → used by Spring Cloud Bus for config refresh.
 * spring.cloud.stream.default-binder: kafka → ensures your business event streaming uses Kafka.
 * You don’t need to create Rabbit bindings in spring.cloud.stream.bindings unless you want to directly consume/produce Rabbit messages from this app.
 * 👉 That way:
 * RabbitMQ = used behind the scenes for Config/Bus refresh.
 * Kafka = used explicitly for your microservice event streaming.
 *
 * I thought it wise to mention the above discussion and how I solved the problem I encountered around, java.lang.IllegalStateException 'A default binder being requested, but there is more than one binder available ...'
 * Under the consumers sub-folder, you can see 3 consumer i.e., accounts, message and anonymous....(If you click this you will notice it is related to 'springCloudBus' stuff' Ignore it. Our focus should be on the 2 consumers is.e., accounts and message.
 * We are seeing the 2 consumers i.e., accounts and message because communication between accounts and message ms is going to be 2 way using asynchronous communication. First, my accounts ms is going to produce a message/event for my message ms. In such scenario, my message ms is going to act as a consumer. In the vice varsa scenario where my message ms is going to produce a message/event, in such scenario the accounts ms is going to act as a consumer. That's why under the consumers subfolder we are able to see the 2.
 * Just to re-iterate, we only have one broker in our local system and that's why under the brokers subfolder you can only see one. If you have multiple brokers inside real production cluster you should be able to see all of them.
 * Now, if you open the 'Topics' sub folder, here you should be able to see the topics information which are: 'communication-sent' and 'send-communication'. These are based upon the destination details that we have defined inside the application.yml file. You can also see an extra topic, '__consumer_offsets', which is going to be used by the Apache Kafka internally. You can also see another topic,'springCloudBus' which is related to Spring Cloud bus stuff.
 * With what we have discussed so far, it is pretty clear that our local ms's are able to connect with the Kafka server that we have started in our local system. As a next step, we can try to test the scenario and verify by visualizing if the asynchronous is happening between two ms's. For the same, first I need to start my KeyCloak Server because right now my GatewayServer is secured with the help of OAuth2 standards. In your Docker desktop, start the existing keycloak container. You can log into the KeyCloak console and validate if the client details are available inside the KeyCloak server that we had earlier on configured.
 *  - Yes, everything is okay as far as KeyCloak is concerned. As a next step, keep a breakpoint inside your email function at the log statement. Now, get a token (Make sure to mention the correct client details as configured in your Keycloak) and invoke the endpoint 'Accounts_POST_ClientCredentials' which is available under gateway_security in our postman. You should receive a successful response from the accounts ms and behind the scenes it may have also pushed a message into our Kafka cluster, that's why you will see the breakpoint stopped inside our email function. Before releasing the breakpoint, if you check the accounts table in the DB, you can see that the communication switch right now is 'null'. As soon as I release the breakpoint, immediately, the column is going to be updated to true value.
 *    This confirms that our end to end scenario is working fine with the help of Apache Kafka, did you see how easy it is to switch from RabbitMQ to Kafka, haha. If you are not using Spring cloud functions and Spring Cloud Stream, you will be needed to do  hell lot of work. That's why my humble request is , BEFORE DOING SOMETHING THAT SWEATS YOUR ASS OF, THINK OF LEVERAGING THESE LATEST PROJECTS OR LATEST TECHNIQUES!!! that are available inside the Spring Ecosystem. Even today, I see many developers are using old approaches to connect with the RabbitMQ and Kafka. I feel very sad for them because they can instead easily achieve this job with the help of Spring Cloud Functions and Spring Cloud Stream. Atleast you are educated now, spread the knowledge to the other devs as well.
 * Now, we tested everything inside the local system, as a next step we need to test the things inside the docker environment.
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
