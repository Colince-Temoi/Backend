package com.get_tt_right.gwserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/** Update as of 13/04/2025
 * Http Timeout Configurations
 * ------------------------------
 * As of now, we have discussed the circuit breaker pattern. In this session, we will have a look at one of the most common scenarios that we may face inside our real projects and how we can handle the same with the help of Spring Cloud gateway. To visualize this issue, we are going to invoke the Contact Info API present inside the Loans ms i.e., /api/contact-info
 *  In postman, under the gatewayserver folder, we have a request i.e.,eazybank/loans/api/contact-info. As soon as you click on the send button, happily you should be able to get a proper response as expected. This means that my service is working perfectly without any performance issues. But sometimes, your services may respond very slowly, sometimes they may take more than 30 seconds or more than 1 minute as we never know what is happening at the server side. Maybe the ms is performing very slow.
 *  In such scenarios, you may not get an immediate response. For a quick demo, in order to simulate this, I am keeping a breakpoint at the return statement of the contact-info API inside LoansController. Intentionally, I will not release this break-point and will make my client application which is Postman to wait for this response. Now if you invoke the gatewayserver/eazybank/loans/api/contact-info request, you will notice the execution is stopping at our breakpoint and the postman client will keep waiting for the response. If you don't release the breakpoint, even for 10 minutes, your client application will keep waiting for the response. This is a very common scenario that we may face in our real projects.
 *   Behind the scenes, there is a thread waiting on the gatewayserver for the response of the contact-info API of Loans ms. At the same time, at the loans ms, there will be a thread which will be waiting for the response to be received and dispatched back to the gatewayserver. Like this, unnecessarily, you are making your threads, memory and other server resources to wait for this response to come, and we don't know when this response is going to come. To overcome these challenges, we should define some timeout configurations at the gatewayserver level. Using these timeout configurations we can control the time at which we want to wait for a response to come back from the server. Nothing but, we can only wait for a maximum period of time. Beyond this configured time, we are not going to wait for the response.
 *   Instead, we will simply invoke the request one more time, or we will take some fallback mechanism to handle this based upon our requirements and the business logic. Up to now, you should have clarity on what the problem here is and the impact of it!We are unnecessarily making our resources to wait for this response which we never know when this response is going to come. So if you release the breakpoint and resume execution, if you go to our postman client you will see we are now getting our expected response.  You can clearly see that it took around 20m 45.72s to get the response - Just near the response status, you will see this response time and if you hover on it you will be able to see more details. This is a very big performance hit, and we should avoid this as much as possible. In ideal situations, we should not wait for such long times in real projects.
 * Now, I will invoke the same contact-info API for accounts ms and see what is going to happen. gatewayserver/eazybank/accounts/api/contact-info. Inside the AccountsController we have a similar REST API method i.e., getContactInfo. At the return statement of this method, I am going to put a breakpoint and from the postman, I am going to invoke the request i.e., gatewayserver/eazybank/accounts/api/contact-info. You should be able to see the execution halts at the breakpoint. Without resuming the execution, if you go back to the postman client, you should ba able to see a response i.e., ""An error occurred. Please try after some time or contact support team!!!". Here if you may have noticed my client application is not waiting for long unlike when we were invoking the same endpoint related to loans ms. Why? haha. If you see the route configurations inside the gateway server application, as of now, for the route ""/eazybank/accounts/**"  we configured a circuitBreaker filter . Internally, this circuit breaker is going to have some default configurations related to timeout. By default, circuit breaker timeout will wait maximum for 1 second to get a response. If it doesn't get a response within 1 second, then it will trigger the fallback mechanism that you have defined.
 *  But, we may not be using this circuit breaker pattern throughout/everywhere in our ms's. So, to handle this problem in other places like loans and cards ms's on whose routing configurations we have not defined any circuitBreaker pattern filter, we need to define/configure the timeout configurations so that my gateway server or any other ms's can wait for a maximum period of time for a response to come. Nothing but so they won't wait for a response indefinitely/long time. To understand how to achieve this, you can visit the official documentation of Spring Cloud Gateway i.e., https://docs.spring.io/spring-cloud-gateway/reference/ >> On the LHS nav - Spring Cloud Gateway Reactive Server >> Http timeouts configuration. In this section, we have all the complete details related to timeout configurations. As a developer, its a thumb rule that you should always configure these timeouts, otherwise you are going to introduce some performance issues/bottlenecks in your applications/ms network. And btw, configuring these timeouts is very easy.  In this doc you should see a heading - Global timeouts
 *   You can copy whatever configurations present under this heading and paste them inside the application.yml file of your gatewayserver application. When we mention these properties/configurations, they are going to be global for all your ms's that will receive the external traffic through your gatewayserver application. We have 2 types of timeout configurations as can be seen in our pasted global timeout configurations i.e., connect-timeout and response-timeout. What is the difference between them? connect-timeout is the time that your gatewayserver is going to take to get a connection thread from the other ms. Suppose, if gateway server is trying to send a request to the loans ms, first it will try to get a connection of loans ms. Sometimes, due to network problems or other transient issues, the time to get the connection thread also may take longer. That's why we are trying to configure 1000 milliseconds, nothing but 1 second. Within 1 second, if my gateway server is not able to get a connection thread from the loans ms, it is not going to wait any further, and it is going to instantly kill the request. The next property that we have here is response-timeout which specifies the maximum time in seconds that your gateway server is going to wait to receive the response from the respective other ms like loans or cards ms.
 * Instead of 5s, I have assigned response-timeout with a value of 2s. I don't want to wait for 5s, as this is too long for me. But based upon your own business requirements, you can always change these values to whatever you want for the properties connect-timeout and response-timeout.
 * Now, lets invoke the request to contact-info of loans ms i.e., gatewayserver/eazybank/loans/api/contact-info and see what is going to happen this time round. This time again, the execution stopped at our breakpoint inside the LoansController at the return statement of /contact-info REST API method. Without releasing this breakpoint of you go back to postman client, you should be able to see a 504 Gateway Timeout response with the message i.e.,"message": "Response took longer than timeout: PT2S". On the postman client you should also see that the response took more than 2 seconds - the set maximum timeout value. Once these 2 seconds elapse, the gateway server is going to kill the request and return the 504 Gateway Timeout response to the client application. Since the timeout configurations we have configured inside the gatewayserver are global, they are going to be applicable for all kind of ms's/paths that will receive the external traffic through your gatewayserver application. But for Accounts ms, it may not be the scenario because of the circuit breaker pattern filter that we have defined as part of its route configurations inside the eazyBankRouteConfig method. We pretty well have the clarity that this circuit breaker pattern filter has its own internal timout configurations which are different from the global timeout configurations that we have configured inside the gatewayserver application.
 *   Whenever we have these kind of scenarios, then the global timeout configurations defined inside the gatewayserver will be ignored for the ms whose routing configurations we have defined circuit breaker pattern filter and applicable to all other ms's without circuit breaker pattern filter definition inside their routing configurations that will receive the external traffic through your gatewayserver application. This implies that whatever implicit or explicit timeout configurations specific to circuit breaker pattern filter will take the highest precedence over the global timeout configurations defined inside the gatewayserver application. Later we will see how to override the default/implicit timeout configurations provided by the circuit breaker. In the application.yml file of gatewayserver we have configured the global timeout configurations which will be applicable for all kind of ms's and REST API paths that will receive the external traffic through your gatewayserver application and have no circuit breaker pattern filter defined inside their routing configurations. Sometimes, you may want to provide timeout configurations based upon a specific url path or ms. For such scenarios, if you visit the official documentation just after the Global configurations section, you can see a section 'Per-route timeouts' which has all the details on how to define timeout configurations based upon a specific url path/route or ms.
 *   Inside our GatewayServer we are already defining some routing configurations by following Java Domain-Specific Language (DSL) approach inside a method eazyBankRouteConfig.  If you are configuration your routing configurations with the help of application.yml then you can mention the metadata related configurations under your routing configurations in order to achieve the Per-route specific timeout configurations i.e.,
 *         - id: per_route_timeouts
 *         uri: https://example.org
 *         predicates:
 *           - name: Path
 *             args:
 *               pattern: /delay/{timeout}
 *         metadata:
 *           response-timeout: 200
 *           connect-timeout: 200
 * - But since we are using Java DSL style of configurations, you can see from the documentations that we can fluently configure the per-route timeout configurations by invoking metatdata related behaviors and passing the 1st input parameter as key and second input parameter as value of our configurations. i.e.,
 * import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
 * import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;
 *
 *       @Bean
 *       public RouteLocator customRouteLocator(RouteLocatorBuilder routeBuilder){
 *          return routeBuilder.routes()
 *                .route("test1", r -> {
 *                   return r.host("*.somehost.org").and().path("/somepath")
 *                         .filters(f -> f.addRequestHeader("header1", "header-value-1"))
 *                         .uri("http://someuri")
 *                         .metadata(RESPONSE_TIMEOUT_ATTR, 200)
 *                         .metadata(CONNECT_TIMEOUT_ATTR, 200);
 *                })
 *                .build();
 *       }
 * Above you can clearly see to the methods i.e., metadata(-,-) where we are passing some key constants and values for our  per-route timeout configurations. So, this is how you can configure the per-route timeout configurations for each route inside your ms's. On the same page if you can scroll down, we have more interesting information i.e.,
 *       - id: per_route_timeouts
 *         uri: https://example.org
 *         predicates:
 *           - name: Path
 *             args:
 *               pattern: /delay/{timeout}
 *         metadata:
 *           response-timeout: -1
 *
 *  If you configure the response-timeout with a negative value, that will disable the global response-timeout configurations for a particular route. In case if you don't want to follow global timeout configurations for a particular route, you can configure the response-timeout with a negative value. With this, for the above sample particular route, there won't be any timeout configurations. It is going to wait for the response indefinitely until you kill that thread manually by yourself.
 *  Up to now, you should be crisp clear about these timeout configurations.
 *
 *  Summery of what we have learnt
 *  ---------------------------------
 *  Spring Cloud gateway is a very powerful Edge server inside which there are very many configurations and filters, some of which we have seen and others we are yet to even explore. It was not possible for our instructor to discuss all of them but he left us empowered with the official documentation information and how we can use it to make/define some configurations and filters to our gatewayserver application by visually and step by step showing us some examples as provided in the official documentation. With this knowledge, in future if you have different requirements for your gatewayserver application, you can easily figure out how to configure them by coming to this official documentation and doing some basic research. Like this, accordingly, you can implement the same inside your gatewayserver application/projects.
 *  The main intention of our instructor/mentor is " To empower us by teaching the foundational/ basic concepts, and with that, he is confident, and I am confident that I can easily understand the official documentation" Maybe prior to having being empowered by my mentor/instructor if you had tried to read the docs by yourself, you may have found it a bit challenging to understand the things. But with all the discussions and empowerment we have had it is going to be super easy to understand.
 *
 * * */
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
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
						.uri("lb://LOANS"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

}
