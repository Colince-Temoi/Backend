package com.get_tt_right.gwserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/** Update as of 09/04/2025
 * As of now, we have created a circuit breaker pattern inside the gateway server, but it does not have any fallback mechanism. Since we don't have any fallback mechanism inside the response you can see some runtime exception details are being thrown like,"Upstream service is temporarily unavailable", or "Gateway Timeout", etc. In real business applications, throwing runtime exceptions to the client applications or to the UI applications is not a good idea and is NOT VALID APPROACH! That's why we need to have some fallback mechanisms inside which we can write some logic and send some message to the client applications or to the UI applications that will make sense for them. To create a fallback mechanism for circuit breaker pattern it is going to be super easy as discussed below.
 * Defining a fallback mechanism for circuit breaker pattern
 * ----------------------------------------------------------
 * Inside our gateway-server application, first we have to create a controller class. As of now, we have the filters package. Very similarly, I am going to create a new package inside my gatewayserver with the name controller and inside this package I am going to create a new controller class with the name FallbackController. Check the FallbackController class for more details and discussion.
 * The configuration, .setFallbackUri("forward:/contactSupport");, you can recal this 'forward:/....' syntax from Servlets discussions where we were saying that there are 2 ways Servlet to Servlet communication can happen, Dispatching(Within the same application) and Redirection (Outside the application). Dispatching can happen in 2 modes, forward and include, where we use forward mode to forward a request,response to another servlet. We went further and made it clear that in forward mode , the last response in the servlet chain is what will be registered and available to the client. So, if you are focusing on the final result, then go for forwarding mode. In this case, we are going to forward the request to the contactSupport method in the FallbackController class.
 *  - With this configuration, to set the fallback mechanism, I am nothing but telling my circuit breaker pattern that whenever an exception is thrown, please invoke the uri configured by forwarding the request to the contactSupport method in the FallbackController class. With this, we have some fallback mechanism. Now, you can test these changes to verify that everything is working as expected. So start Rabbit container, Accounts DB container, Config server application, EurekaServer, Accounts application, GatewayServer. Visit the Eureka Dashboard to verify that Accounts and GatewayServer applications are happily registered. Make the postman request gatewayserver/eazybank/accounts/api/contact-info and you should receive a successful response. If you visit the page, http://172.25.96.1:8072/actuator/circuitbreakers, you will see that the overall state of the circuit breaker pattern is "CLOSED" as everything is smooth so far, haha.
 *    If you visit the page http://172.25.96.1:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker, you will see that one event/request is recorded after the gatewayserver is started and the event/request is processed with the type of "SUCCESS". To mimic slow response issue from the upstream server/Accounts ms, Inside the Accounts controller, place a break point at the return statement of the getContactInfo REST API method. When you make the gatewayserver/eazybank/accounts/api/contact-info request, it will be stopped at the breakpoint and since we are not releasing the breakpoint, after sometime, this request will be failed/killed by the circuit breaker and based on the fallback mechanism, we will get the response from the contactSupport method in the FallbackController class i.e., 'An error occurred. Please try after some time or contact support team!!!' With this, my client applications will never know what is happening behind the scenes, they will never receive gateway timeout, upstream service is temporarily unavailable, etc. kind of errors.
 *    They will always simply receive the message from the FallbackController class i.e., 'An error occurred. Please try after some time or contact support team!!!' So, its solely up to you what kind of fallback mechanism you want to write inside your fallback REST API. And this fallback will never get invoked if your upstream server/Accounts ms is working fine and your requests are being processed successfully. If you remove the breakpoint and make the gatewayserver/eazybank/accounts/api/contact-info request, the request will be processed successfully without any issues which is a strong statement that in the success scenarios, the fallback mechanism is never going to come into picture. Up to now, you should be clear on how to implement the circuit breaker pattern inside the gateway server and how to implement the fallback mechanism.
 *  - Check slide for a summary of all the steps that we have in details gone through. This will help you when you want a quick refresher. Now, we are super clear on how to implement the circuit breaker pattern inside an Edge Server or inside a gateway server application. As a next step lets try to implement the same circuit breaker pattern inside individual ms's like Accounts ms. See discussion below.
 *
 * Implementing circuit breaker pattern inside non-reactive ms's like Accounts ms - With Feign Client
 * -------------------------------------------------------------------------------------------------------
 * We wil be implementing the circuit breaker pattern inside Accounts ms. Inside our Accounts ms, if you can recall,we have a REST API with the name fetchCustomerDetails. As part of this REST API, my accounts ms is going to invoke cards as well as loans ms's internally. Nothing but the internal ms communication. And this is made possible with the help of feign client. The question we need to ask ourselves is, what if one of the upstream/dependent/backing services like cards or loans is responding very slowly or is completely down/temporarily down or there are some network issues? Ins uch scenarios like we discussed previously, these transient faults will have a ripple effect on the downstream service(s)/Accounts ms and also from this Accounts ms the ripple effect will also be felt at the gatewayserver. That's why with the current code available inside the accounts ms, is going to create serious problems when one of the dependent/upstream services like loans and cards ms's is down or experiencing the transient issues. To overcome this problem, lets try to implement the circuit breaker pattern inside our Accounts ms as well.
 *  - We know that our Accounts ms is leveraging feign client to invoke loans and cards ms's internally. That's why we need to see if there is any integration between feign client and circuit breaker pattern which we can leverage with fewer efforts/fewer configurations inside Accounts ms. For the same inside the official website of spring.io >> Projects >> Spring Cloud . Under this Spring Cloud project, we can try search for the subproject 'Spring Cloud Open Feign'.Click on the Learn tab and open the current official documentation. Inside this official documentation, you will be able to see few LHS sections i.e., Introduction, Spring Cloud OpenFeign Features, Common Application Properties. Now if you click on the Spring Cloud OpenFeign Features you will see an RHS section with very many sub-topics/sub-docs. Here you will see something like 'Declarative REST Client: Feign' under which you can see something like 'Feign Spring Cloud CircuitBreaker Support'. If you click on this sub-doc, among the first statements you will see is, "If Spring Cloud CircuitBreaker dependency is on the classpath (pom.xml) and if we mention/configure the property spring.cloud.openfeign.circuitbreaker.enabled=true(Inside our application.yml file), Feign will wrap all methods with a circuit breaker. This is a really powerful and beautiful statement. haha!
 *   This implies that we don't have to make many changes whenever we are using feign client, so:
 *   1. Add the dependency related to Spring Cloud CircuitBreaker inside our pom.xml file
 *      Just after the dependency spring-cloud-starter-netflix-eureka-client, add the dependency spring-cloud-starter-circuitbreaker - The non-reactive version. Reason: Since we have not build Accounts ms based upon Spring Reactor.
 *      We just copied the netflix-eureka-client dependency and changed the artifact id to spring-cloud-starter-circuitbreaker-residency4j
 *      Finally load the maven changes.
 *   2. Add/Enable the property spring.cloud.openfeign.circuitbreaker.enabled=true inside our application.yml file
 *    Just copy that property as it is inside the documentation and paste it inside our Accounts application.yml file. Some magic will happen, as soon as I paste this property, it is automatically converted into yml format. So make the small modification like, remove the root element 'Spring' if it was added as we already have it. Remove the equal symbol '=' and replace it with a colon and suffix space ': '. Finally, make sure the properties are indented properly. Hurreey! we are good now.
 * With this, we have activated circuitbreaker for all the OpenFeign clients/calls inside our Accounts ms. Apart from the changes dicussed above, we should also mention other properties/configurations related to resilience4j.circuitbreaker inside our application.yml file just like we mentioned/configured and in detail discussed inside our gatewayserver application.yml file. i.e.,
 *resilience4j.circuitbreaker:
 *   configs:
 *     default:
 *       slidingWindowSize: 10
 *       permittedNumberOfCallsInHalfOpenState: 2
 *       failureRateThreshold: 50
 *       waitDurationInOpenState: 10000
 *
 * Just copy and paste the same values inside our accounts application.yml file towards the end.
 * Now, if you go back to the official documentation in the sub-doc i.e., Feign Spring Cloud CircuitBreaker support, there is a lot more information which if you are interested you can read. Now, my focus is information related to fallback mechanism setup, open link to the sub-doc i.e., Feign Spring Cloud CircuitBreaker Fallbacks.  We need to define the fallback mechanism for our cards and loans ms's. What should happen when loans ms is dowm? What should happen when cards ms is down? That's why to mention the fallback mechanism, there are some instructions given inside the sub-doc i.e., Feign Spring Cloud CircuitBreaker Fallbacks.
 *   1. We need to make sure on top of the feign client interfaces that we have created, we need to mention a fallback attribute as an input to the @FeignClient annotation i.e., @FeignClient(name = "loans", fallback = LoansFallback.class). To this fallback input parameter, we need to assign the class name of the fallback class i.e., LoansFallback.class.
 *   2. Implement the Fallback class i.e., LoansFallback.java. The logic that you need to write inside this class is, first you need to make sure you are implementing the same interface of your feign client i.e., LoansFeignClient na doverride the abstract methods that you have defined inside the interface i.e.,fetchLoanDetails. Now, inside this overriden methood, you can write your fallback logic, which means that feign client along with the circuit breaker pattern, whenever the upstream server has a transient issue or whenever there is an exception, the fallback logic is going to be executed instead of throwing the runtime exception.
 * Now, lets try to implement this inside our downstream server / Accounts ms.
 *  We have 2 feign client interfaces at the moment i.e., /service/client/CardsFeignClient.java and /service/client/LoansFeignClient.java. Now under the same services/client package, I am going to create 2 implementation classes to these interfaces i.e., CardsFallback.java and LoansFallback.java. On top of these feign client interface implementation classes I am going to mention @Component to declare it as a Spring bean. Post that, override the methods in the respective feign client interface inside the implementation classes.
 *  Check out these classes for more information on what fallback logic we are going to write inside them.
 * Now, under the feign client interfaces make sure to mention the input parameters to @FeignClient annotation properly i.e., @FeignClient(name = "cards", fallback = CardsFallback.class) and @FeignClient(name = "loans", fallback = LoansFallback.class).
 *  name - Application name of the upstream service.
 *  fallback - Class name of the fallback class.
 * With this, we should be good. We have implemented the circuit breaker pattern along with the fallback mechanism inside our downstream service i.e., Accounts ms for our upstream services i.e., Cards and Loans ms's.
 * Now, we are yet to complete, haha! As you know, any slight changes is likely to affect the existing working implementation. So, if you open CustomerServiceImpl, several things we need to adjust in order to accommodate whatever changes we have done regarding to feign client interfaces i.e.,
 *   1.Whenever we are trying to invoke the upstream services with the help of feign client interfaces, we are getting some response which we are storing inside ResponseEntity and thereafter from the entity we are trying to fetch the body of the response. But with the new changes that we have adjusted to, there are some scenarios where the response can come as null from the upstream services - especially with the fallback logic that we wrote. That's why we need to make sure we are putting some null checks before setting the body of the response inside the customerDetailsDto. Why? The reason is simple. Below is an explanation of why:
 *      I am getting a Loans response from Loans ms. This loans response I am setting into LoansDto object present inside CustomerDetailsDto before sending that to the client.In the scenario that the response from loans is null, we will face some issues because null cannot be set/mapped into a LoansDto object. So, we need to make sure that we are setting some null checks before setting the body of the response inside the customerDetailsDto. Otherwise, we will face null pointer exceptions.
 * With this, we should be good. Save the changes and do a build. You should be clear with all the changes we have done so far. Next we will see a visual demo of the changes we have done.
 *
 * To get started, start all the services in the order RabbitMQ, AccountsDb, LoansDb, CardsDb,configserver, eurekaserver, Accounts, Loans, Cards and at last start the GatewayServer application. Confirm that, the internal services + the Gateway server are registered in the Eureka Dashboard
 *  - As a next step, open the actuator endpoint url of Accounts ms because right now we are focusing on the circuit breaker that we have implemented inside the accounts ms. ls:8080/actuator. If you Ctrl + F the string 'circuitbreakers' in this Accounts ms page, you should be able to see some links related to circuit breakers. So open the link lh:8080/actuator/circuitbreakers, and you should be able to see the overall state of the circuit breaker pattern + more other information being used and tracked by the circuit breaker pattern that we have implemented Inside Accounts ms.
 *    Initially the information in the accounts ms actuator link lh:8080/actuator/circuitbreakers is empty i.e.,  circuitBreakers": { } - Means as of now, there are no circuit breakers because we have not made any calls to the upstream services yet. Its only when we make our very first request to the upstream services that the circuit breaker pattern starts working/get activated and will start monitoring all the requests coming towards the accounts ms to the upstream services i.e., Cards and Loans ms's.
 *  - Since we have just started our ms's, we won't be having any data in their respective DB's. First, lets create some data that we will work with by invoking the create API requests for Accounts, Loans and Cards ms's present under the folder gatewayserver inside our postman collection. Once the data is created, you should be able to see the data inside the respective DB's. And with this we have set up all the required data that we wil leverage to visualize our demo.
 *    Now, make a request to the FetchCustomerDetails endpoint.  As of now, you will get a successful response as this is expected in normal circumstances when non of our upstream services are experiencing any transient issues. Now, lets go to the Accounts actuator path lh:8080/actuator/circuitbreakers, and try to analyze whatever information is present there. If you refresh that page you should be able to see 2 circuit breakers activated/create behind the scenes. i.e.,
 *    {
 *   "circuitBreakers": {
 *     "CardsFeignClientfetchCardDetailsStringString": {
 *       "failureRate": "-1.0%",
 *       "slowCallRate": "-1.0%",
 *       "failureRateThreshold": "50.0%",
 *       "slowCallRateThreshold": "100.0%",
 *       "bufferedCalls": 2,
 *       "failedCalls": 0,
 *       "slowCalls": 0,
 *       "slowFailedCalls": 0,
 *       "notPermittedCalls": 0,
 *       "state": "CLOSED"
 *     },
 *     "LoansFeignClientfetchLoanDetailsStringString": {
 *       "failureRate": "-1.0%",
 *       "slowCallRate": "-1.0%",
 *       "failureRateThreshold": "50.0%",
 *       "slowCallRateThreshold": "100.0%",
 *       "bufferedCalls": 2,
 *       "failedCalls": 1,
 *       "slowCalls": 0,
 *       "slowFailedCalls": 0,
 *       "notPermittedCalls": 0,
 *       "state": "CLOSED"
 *     }
 *   }
 * }
 * These circuit breaker names are following some pattern! i.e., <FeignClientName><ApiName>StringString. You can see this pattern in the above circuit breaker names.
 * Here the <FeignClientName> is the name of the feign client interface that we have defined inside our downstream service i.e, Accounts ms's. And the <ApiName> is the Abstract method name that we have defined inside the feign client interface.
 * StringString represents the data types of your input parameters that the feign client interface Abstract method accepts.
 *
 * As of now, the overall status of the CardsFeignClientfetchCardDetailsStringString and LoansFeignClientfetchLoanDetailsStringString is CLOSED. Now, if you go and open the url lh:8080/actuator/circuitbreakerevents, you should be able to see the events related to these 2 circuit breakers.
 *  These events have information like the type i.e., SUCCESS or FAILURE or ERROR , ...etc. This answers the question like, Is the event a success/failure/error type? We also have more other information like, creationTime - when the event was created, errorMessage - if the event was a failure type otherwise null, durationInMs - how long it took to process the event, and stateTransition - will be populated with state transition details when the state of the circuit breaker changes from one state to another. For example, if it is changing from OPEN to CLOSED, the stateTransition will have a value like "from OPEN to CLOSED" - OPEN-to-CLOSED. Otherwise, it will have a null value.
 *  To see the filtered event information with respect to one circuit breaker, you can use the url lh:8080/actuator/circuitbreakerevents?name=CardsFeignClientfetchCardDetailsStringString - This did not work! In the Gateway server it worked when we had only one circuit breaker. - Find out if there is a way you can filter the events based on the circuit breaker name in scenarios where you have multiple circuit breakers in a single downstream service.
 * Now, let's introduce some simulated transient issues in order to visualize the negative scenarios. So, I am going to stop the Loans ms. With this, when my Accounts ms is trying to invoke loans ms with the help of feign client, the integrated circuit breaker pattern present inside feign client will monitor all such requests to the upstream server and based on the circuit breaker configurations it will know what to do. Now, since our loans ms is down, we expect not to get a successful loans information response and in this case, since we have configured a fallback mechanism, we will get a fallback response i.e., null. Now, lets try to hit the FetchCustomerDetails endpoint, and you should be able to see a fallback response. Nothing but, instead of getting a RTE response, we will get a null response for loans information and this will prevent the whole fetchCustomerDetails endpoint from breaking down as a result of the RTE if we hadn't configured the fallback mechanism.
 * Along with this, you should be able to see the state transition of the circuit breaker pattern when you send multiple requests to the same circuit breaker.
 *
 * If you make a request to fetchCustomerDetails endpoint in this scenario where your loans upstream service is down. In the response, you will get Accounts data and Cards data as expected. You will get Loans data as null because of the fallback mechanism. This way, we are making sure that we are serving to our clients a fallback response/ at least some information in case the upstream services are down and based upon whatever ms's are working instead of throwing a RTE and having the whole request break down as a result of one unwell ms. This is so serious btw! Think of a scenario where you are into an Amazon website. When you open the Amazon homepage, behind the scenes, a lot of ms's will be working together to display a lot of information to you. They will be showing you some discounts, what are the products available, and also down the page - they will be showing some recommendations to you based upon your past order history that you may like this product. What if the ms related to recommendations is not working? Do you think Amazon homepage will stop working?
 *  Of course NOT! Behind the scenes, developers will make sure that at least we have something being displayed on the page i.e, The banner, discount, and other products information is visible and only the recommendations information they will try to hide. And my end-user will be happy with that, at least he is getting some response and should be able to proceed with browsing of other products. Very similarly, here also, we are at least providing some fallback information to the end-user. Nothing but some graceful behavior in case the upstream services are down with the help of the circuit breaker pattern and fallback mechanism. Send the request to fetch customer details multiple times so that our circuitbreaker related to loans ms gets triggered and you should be able to see the state transition of the circuit breaker pattern from CLOSED to OPEN status.
 * If you refresh the page - http://172.25.96.1:8080/actuator/circuitbreakers - you should be able to see that the circuitbreaker related to loans ms is right now in the OPEN status. If I refresh the events related page - http://172.25.96.1:8080/actuator/circuitbreakerevents - you should be able to see the transition related information of the circuit breaker related to loans ms. You should see an event type of 'FAILURE_RATE_EXCEEDED' and the next event type will be 'STATE_TRANSITION' - Which implies that the state transition is happening for this circuit breaker from CLOSED to OPEN.
 * - Now, very similarly, I am going to stop the cards ms. With this, now both of the upstream/dependent services are down/have stopped working. My expectation is not to have a request that relies on these 2 upstream services to break down as a result of RTE's. With the help of circuit breaker pattern and fallback mechanism, we will be able to serve a fallback response to our end-user and the request will not break down. In addition, as a result of the 2 upstream services down, we won't experience any ripple effect at the downstream service i.e, Accounts ms >> Gatewayserver. This ripple effect would have been felt at the downstream services i.e., Accounts ms and GatewayServer and even the Client applications were it not for the beautiful circuit breaker pattern and fallback mechanism.The threads at these downstream services would have been held on a wait state and would have been waiting for the upstream services to come up and start working again. This means that with more requests incoming to our ms network the threads would be consumed/exhausted/depleted and the ms would start to break down. This is exactly what we don't want to happen in our ms network.
 *  Now if you make a request to fetch cusstomer details, this time in the response you should get Loans and cards information as null because of the fallback mechanism. You should at least get the accounts information. This is exactly what we want to happen in our ms network. Now you can go back to the page http://172.25.96.1:8080/actuator/circuitbreakers, and you should see the overall status of circuit breakers + related configured information + some some information that is being used by the circuit breaker pattern to monitor the events/request and make an informed decision on the state to transit to for the necessary corrective actions to be taken. You can also check with the events page - http://172.25.96.1:8080/actuator/circuitbreakerevents to have a visual of what is happening behind the scenes. All these we ahve discussed and visualized in details and you have all the required clarity to make sense of the circuit breaker pattern and fallback mechanism.
 * - If you now start your loans and cards ms's and start sendung some successful responses, some magic will happen behind the scenes as we have discussed in details, and you should be able to receive all the responses from all the 3 ms's as eventually, my configured circuit breaker patterns configured at the Accounts ms will go back to CLOSED status and you should be able to see that in the circuit breaker related page http://172.25.96.1:8080/actuator/circuitbreakers.
 *
 * For a summary of what steps we have followed whenever we are trying to implement the circuit breaker pattern with the help of Feign Client, please check the slide for a summary of all the steps that we have gone through.
 *
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
								.circuitBreaker(config -> {
									config.setName("accountsCircuitBreaker")
											.setFallbackUri("forward:/contactSupport");
								}))
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
