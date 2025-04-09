package com.get_tt_right.gwserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/** Update as of 31/3/2025
 * Generating and pushing docker images with Spring Cloud Gateway changes
 * -----------------------------------------------------------------------------
 * As of now, we developed an edge server for our ms's network and we tested everything inside the local system. As a  next step, lets try to build the docker images for all the projects that we have as a result of the edge server changes. Post that, we will update the docker compose file and test all the edge server or gateway server related changes using docker containers.
 * Before we try to generate the docker images, we need to enable the health related readiness and liveness url's inside my accounts, loans and cards ms's. The reason is, when we are trying to update the docker compose file for the gateway server related changes, we need to define the dependencies for our gateway server saying that, "Only once my internal m's like loans, cards and accounts are started successfully and their respective health is good, then only I should start my gateway server".
 * For that reason we have to add the properties related to the health probes inside the application.yml file of the ms's like accounts, loans and cards. These kind of properties we already defined inside the config server, so just copy and paste them to the application.yml file of the ms's. In the configserver under the management parent property, you can see a child property with the name health, copy it and its subsequent child properties to the application.yml file of the ms's.
 * Similarly, don't forget to enable the health related probes under the child property to management called 'endpoint'. Copy this also from the configserver application.yml file to the ms's application.yml file. With this we should be good. Do a build and stop all the running local instances. And before you try to generate the docker images, check your pom.xml file for each of your application that we have the jib plugin and the tag name is V5 for cards, loans and accounts ms's. Also, inside your docker desktop, make sure there are no images and containers related to your ms's.
 * Now, inside your terminal for each of your ms's navigate to the parent folder where we have the pom.xml and run the command: mvn compile jib:build. Try to always have these maven commands on your finger tips! They are going to be very helpful for you. Your instructor has also mentioned these commands in the GitHub repo. You can always check them incase you have any doubts.
 *
 * Updating docker-compose file to adapt Spring cloud gateway changes - Post that we can validate that everything is working as expected.
 * --------------------------------------------------------------------
 * In the docker-compose folder open the default folder and then the docker-compose.yml file. Just like the other services, we need to define the gatewayserver related service. Just copy one of the services and edit its details to reflect those of the gatewayserver.
 * We copied the cards service definition and changed the service name to 'gatewayserver', image name + tag name, container name, port mapping, the environment variable to define the application name. With this we should be good. Remove any other thing and retain the extends definition.
 *  Under the common-config.yml file, we have a service with the name 'microservice-eureka-config' and under this service, we have passed the eureka related details along with the configserver details because the service 'microservice-eureka-config' is an extention of the service 'microservice-configserver-config'
 * Ctrl + F for 'v4' and then on the LHS of the search bar, there will be a dropdown icon, click on it and then type 'v5' and then click the replace all. In this simple step we will have updated the tag names of our cards, accounts and loans services.
 * Now as a next step, we need to define the health checks for all our accounts, cards and loans ms's. For the same, you can copy the health check related information from the defined eurekaserver service and then paste that for loans, cards and accounts ms's. What you need to change in the value to the test url is the port number of the accounts/cards/loans services.
 * As a next step, to the gatewayserver service definition, mention the depends-on after the ports, then mention all the dependent services with the condition service-healthy. With these changes, we should be good. Now run the docker compose up -d command and see if everything goes up properly. This will start all our containers and the very first service that will start is the configserver, then eurekaserver, accounts, loans and cards services. Once all my individual services health is good then my gatewayserver is going to start towards the end. You can also validate the same under the docker desktop as all the containers are in running status.
 * If you click on the containers, you should be able to see the logs related to each. You can see logs like, "started xxx application in xyz seconds" to indicate that a particular service started successfully. Now, try to clean all the logs inside the containers of gatewayserver , cards, loans and accounts ms's so that we can see our trace/correlationid logs very clearly. So, to test these edge server related changes, we need to follow the below steps:
 *  - Create data inside our ms's. In postman client under configserver folder, execute the below create requests available inside the respective ms's with the help of gatewayserver:
 *   . eazybank/accounts/api/create
 *   . eazybank/cards/api/create
 *   . eazybank/loans/api/create
 *  - As a next step, invoke the FetchCustomerDetails request. You should expect a successful response in the body as expected. You can also validate the custom response headers are coming as expected. For example, we have a header with the name 'eazybank-correlation-id' alongside a value to it that my gatewayserver initially generated when the first request to fetch the customer details made its way through the gatewayserver. If you copy this value to the trace/correlationid and search the same inside my accounts ms, you should be able to see a log inside my accounts ms with this trace id. The same you can also verify inside cards as well as loans ms's.
 *    In the gatewayserver service, you should see 2 logs with the same correlation id. Once that comes from the RequestTraceFilter and another that comes from the ResponseTraceFilter. With this we have successfully validate the changes inside the docker environment by executing the docker compose file. Now run the docker compose down command to stop all the running containers. While they stop, one last update we need to do, copy the same content present inside the /default/docker-compose.yml into /qa/docker-compose.yml and /prod/docker-compose.yml files. Since we din't make any changes inside the common-config.yml file no need to update anything in /qa and /prod common-config.yml files.
 *    Also please make sure in the respective folders we have the correct profiles specified under the environment variable definition 'SPRING_PROFILES_ACTIVE'
 * With this we are done about all the discussion about the edge server, in the next session we will again come back to it to beef it up more with lot many other business logic related to security, fault tolerance, resilience, ..etc. But upto now, the train should be ready to go! in terms building an API gateway or an edge server for our ms network. Now, all my external clients don't have to invoke different different individual ms's API urls, instead they can simply rely on one edge server/ gateway server/ entry point.
 *
 * Update as of 06/4/2025
 * -------------------------
 * Hands-on Implementing the circuit breaker pattern inside the GatewayServer
 * -------------------------------------------------------------------------
 * Check your notebook for a headstart of what we discussed before coming here.
 * 1. Open the pom.xml file of gatewayserver and just after the dependency 'spring-cloud-starter-netflix-eureka-client' which is related to eureka client, add the dependency 'spring-cloud-starter-circuitbreaker-reactor-resilience4j' to the pom.xml file. This will allow us to use the circuit breaker pattern. Since the edge server or gateway server is build based upon Spring Reactive model, we need to make sure we are mentioning the reactive circuit breaker artifact id for the dependency.
 * 2. Inside the Spring boot main class i.e., GatewayserverApplication class. Inside this class we have done all routing related configurations and as of now we are trying to use filters like rewritePath and addResponseHeader. Very similarly, we are going to leverage one of the inbuilt provided filter by the Spring cloud gateway called circuitBreaker. If you try to invoke the circuitBreaker filter, it is going to accept some lambda based configurations.
 *    - Inside this lambda expression, I am using 'config' as a reference variable. On top of it, I am going to invoke the below methods:
 *     . setName - to set the name of the circuit breaker. Nothing but using this, we are going to assign a name to our circuit breaker because inside you application you may have multiple/any number of circuit breaker configurations related to various REST APIs or related to various ms's.
 *       Here you can clearly see we are trying to define this circuit breaker configurations whenever someone is trying to invoke the specific path, "/eazybank/accounts/**", which is related to Accounts ms. So the name that we can give here is "accountsCircuitBreaker". This makes it very easy to identify which circuit breaker is related to which ms.
 *       Since we are trying to make the changes inside the accounts ms related routing configurations, the name we have given for the circuit breaker is going to be apt but there is no restriction that we need to use this naming convention, you can give and use any name you like.
 * With this, we have successfully created a circuit breaker for accounts ms related paths.
 * As a next step, we need to provide some circuit breaker related properties/configurations inside our application.yml file. Below is the detailed explanation of what each property doesL:
 * The root element/property is resiliency4j.circuitbreaker. Under it are going to mention a child element i.e., 'configs' which has a child element i.e., 'default' which has the below children elements:
 * slidingWindowSize: 10 - Here we have mentioned the sliding window size as 10 . The purpose of this property is - Using this property we are communicating to the circuit breaker pattern on how many requests it has to initially monitor/track before it tries to change its status from CLOSED to OPEN.
 *                         In other words, I am telling to my circuit breaker pattern that, "Please! at least monitor 10 requests coming towards my Accounts ms. After monitoring 10 requests you can take the decision on whether to continue with the CLOSED state/status or move to the OPEN state/status."
 * permittedNumberOfCallsInHalfOpenState: 2 - The property permitted number of calls in half open status we are giving it a value of 2. Like we were discussing previously,once my circuit breaker moved into OPEN status, it will never stay here forever. Periodically, it is going to move to the HALF-OPEN state, and it is going to allow certain number of requests/traffic to the accounts ms. Since circuit breaker cannot decide how many requests it has to pass/allow, we need to provide such information using the property permittedNumberOfCallsInHalfOpenState.
 *                                            I have given it a value of 2. This means, I want my circuit breaker pattern to allow 2 requests/traffic in the HALF-OPEN status and based upon how these 2 requests are processed, the circuit breaker pattern will decide whether to move to the CLOSED status or OPEN status.
 * failureRateThreshold: 50 - Here I have given a value of 50 which means nothing but 50%. With this, I am trying to communicate to the circuit breaker pattern that, " If at least 50% of the requests the circuit breaker is monitoring are failed, then it is going to move from the CLOSED state to the OPEN state."
 * waitDurationInOpenState: 10000 - I have given the value 10000, which indicates 10000 milliseconds. This is the time that the circuit breaker pattern is going to wait in the OPEN state (To allow the upstream service to recover) before it moves to the HALF-OPEN state to allow the partial traffic.
 *
 * - This should be clear to you now. You can also notice that under the 'configs' configuration, we have mentioned a child property called 'default' which means, All the properties we have defined as child elements under this 'default' property will be applicable for all kind of circuit breakers that you are going to create inside your dowstream service.  But if you want to go with different configurations for different circuit breakers, then you need to use the circuit breaker name as a child property under the 'configs' property. Nothing but, mention it in the place of 'default'.
 *   This way, we can also mention/configure different configurations for specific circuit breakers.
 * - After the changes we have made, we should be good and ready to see the circuit breaker pattern in action. Start your ms's in the order, configserver - Because all our ms's are dependent on the config server. Then start the eureka server - Since all our individual ms's and gateway server will need to register with the eureka server and so we need to start the eureka server first. Then start the accounts ms. I will not start the cards and loans ms's because, we want to see the demo of circuit breaker pattern with accounts ms alone. Even inside the GatewayServerApplication routing configurations, we have configured the circuit breaker pattern only for requests towards the accounts ms.
 *   If needed you can also make these changes for traffic towards other ms's like cards and loans ms's. After accounts ms started successfully, I need to start my gateway server so that we can start invoking our accounts ms related requests/APIs. Now, visit the browser and validate the Eureka dashboard is up and running properly at the port 8070 and that both the Accounts and GatewayServer ms's are registered with the eureka server. Yes! Under the 'Instances currently registered with eureka' section, you can see both the ms's are registered with the eureka server.
 *   . As a next step, open the actuator path of your gateway server. i.e., lh:8072/actuator. Here, you should be able to see all the URL's supported by my actuator. If you ctrl + F and search for the string 'circuitbreakers', you will see a lot many URL's related to circuit breakers. So you can open the URL , "http://172.25.96.1:8072/actuator/circuitbreakers". As of now, you can see that the circuit breakers are empty i.e., circuitBreakers": { }
 *     When we start making request towards the accounts ms with the help of Gateway server, all the circuit brekaer related information is going to be populated inside this page i.e., "http://172.25.96.1:8072/actuator/circuitbreakers". To test these changes, lets go inside the Postman.
 *      Under the gatewayserver folder, I have a request 'eazybank/accounts/api/contact-info' - Whenever we try to invoke this request, we know that it should return the contact details to whom we should reach out when there is an issue with accounts ms. Inside the response we should see a message, contactDetails as well as the onCallSupport details. Now, if go back to the browser and try to refresh the page http://172.25.96.1:8072/actuator/circuitbreakers, you should be able to see the details related to the accounts circuit breaker pattern. i.e.,
 *      circuitBreakers": {
 *     "accountsCircuitBreaker": {     - This is the name that we have given to our circuit breaker. In this case, it is accountsCircuitBreaker.
 *       "failureRate": "-1.0%",  - Here the value -1.0% indicates that there are no requests that have failed.
 *       "slowCallRate": "-1.0%", - Here the value -1.0% indicates that there are no requests that have been processed with a slow call rate.
 *       "failureRateThreshold": "50.0%",
 *       "slowCallRateThreshold": "100.0%",
 *       "bufferedCalls": 2,
 *       "failedCalls": 0, -  The property failedCalls indicates the total number of calls that have failed.
 *       "slowCalls": 0, - The property slowCalls indicates the total number of calls that have been processed with a slow call rate.
 *       "slowFailedCalls": 0,
 *       "notPermittedCalls": 0,
 *       "state": "CLOSED"  - As of now, the circuit breaker is in CLOSED state.
 *     }
 *   }
 *✅ failureRateThreshold: "50.0%"
 * Think of this as the tolerance level for failures.
 * If more than 50% of recent calls fail, the circuit breaker will open — just like a safety switch.
 * Imagine a restaurant: If more than half of your orders go wrong, you might stop taking new ones until you figure things out.
 *
 * ✅ slowCallRateThreshold: "100.0%"
 * This is the maximum allowed percentage of slow responses before the circuit breaker reacts.
 * "100%" here means you're okay even if every call is slow. You won’t open the breaker just because things are sluggish.
 * Like saying: “We’ll tolerate slowness, just don’t crash.”
 *
 * ✅ bufferedCalls: 2
 * This is how many calls are being considered for calculating stats (failure rate, slow rate).
 * It’s like a small scoreboard: If only 2 people have entered the restaurant recently, their experience determines if the system is healthy.
 * Very low number = very sensitive to issues.
 *
 * ✅ slowFailedCalls: 0
 * This is a combo stat.
 * It counts how many requests were both slow AND failed.
 * Think of it like: not only did the waiter take forever, but they also brought the wrong dish.
 *
 * ✅ notPermittedCalls: 0
 * This is key.
 * It tells you how many calls were rejected outright because the circuit was OPEN.
 * Picture a restaurant with the shutters down — people come knocking, but nobody is allowed in.
 * If this goes up, it means your system is actively blocking requests to protect itself.
 *
 * ✅ state: "CLOSED"
 * This is the current status of the breaker. There are 3 main states:
 * CLOSED: All good — requests are flowing.
 * OPEN: Trouble detected — requests are blocked.
 * HALF_OPEN: Testing the waters with a few calls to see if things are back to normal.
 * In your case, it’s CLOSED, so the service is operating normally.
 *
 * - So yea, like this we have/can see some information about the circuit breaker called 'accountsCircuitBreaker'. If you have multiple circuit breakers defined inside your application you can obviously see all of them inside the page i.e., "http://172.25.96.1:8072/actuator/circuitbreakers".
 * - Now, we can also try t understand the events that are happening behind the scenes under a given circuit breaker. For this you can open the URL "http://172.25.96.1:8072/actuator/circuitbreakerevents/{name}" where name is the name of the circuit breaker. This will give you the events that have happened under a given circuit breaker. In our case the name is "accountsCircuitBreaker".
 *   So the URL will be "http://172.25.96.1:8072/actuator/circuitbreakerevents/accountsCircuitBreaker". As of now it is showing empty i.e., circuitBreakerEvents": [ ] because the URL is wrong, haha. So after circuitbreakerevents you need to give a question mark and then the name of the circuit breaker. i.e., circuitbreakerevents?name=accountsCircuitBreaker. The query param is 'name' and value is 'accountsCircuitBreaker'.
 *   Now, as soon as I open this correct URL, you should see an output like below:
 *   circuitBreakerEvents": [
 *     {
 *       "circuitBreakerName": "accountsCircuitBreaker",
 *       "type": "SUCCESS",
 *       "creationTime": "2025-04-07T06:43:55.870178400+03:00[Africa/Nairobi]",
 *       "errorMessage": null,
 *       "durationInMs": 848,
 *       "stateTransition": null
 *     }
 *   ]
 * As of now, there is only one event that happened because we only invoked one request inside the Postman and that request is 'SUCCESS' type. If I try to make the request eazybank/accounts/api/contact-info, 2 more times then I should see 2 more events inside the circuit breaker events page with the type as 'SUCCESS'.
 * This means you circuit breaker patterns is continuously monitoring all the invocations happening towards your upstream service i.e., Accounts ms. Now if you go to the other URL i.e., "http://172.25.96.1:8072/actuator/circuitbreakers", you can see that the bufferedCalls became 3 and the overall state now is still 'CLOSED' because there is no request that has failed or been processed with a slow call rate.
 * Now, in order to see the demo of the circuit breaker pattern in case of a slow call rate. I will go into accounts controller where we have written the implementation logic related to the contact-info API. Inside this method we are simply sending the object of accountsContactInfoDto. To mimic the scenario of slow response, I am going to put a break point at the return statement of this API and I will never release this breakpoint.
 *  With that, always the request will come to this REST API, but it is not going to respond back to the gateway server or to the client application. With this breakpoint, lets now try to understand how the circuit breaker pattern is going to work. So inside my postman I am making a request to eazybank/accounts/api/contact-info . You will see that the breakpoint stopped the execution, but I am not going to release it. If you go back to the postman, after some time my Gateway throws a 504 error like, Gateway Timeout.
 *  Now if you try to refresh the page for the accountsCircuitBreaker events i.e., "http://172.25.96.1:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker", you will see the 4th request that we sent indicating it failed with the type 'ERROR'. You can also see the error message here i.e.,      "errorMessage": "java.util.concurrent.TimeoutException: Did not observe any item or terminal signal within 1000ms in 'circuitBreaker' (and no fallback has been configured)". If you go to the other page i.e., "http://172.25.96.1:8072/actuator/circuitbreakers", you can see that the bufferedCalls now is 4 and the overall state is still 'CLOSED'.
 *  Why it is still in CLOSED state? Because 50% of the calls have not failed , as of now you can see that the failed call is only 1 out of 4. To change my Circuit breaker pattern from CLOSED state to OPEN state I need to send many more request to my contact-info API and of course all of them should fail. Eventually, my circuit breaker pattern will realise that many of the request are failing, and it should move from CLOSED to OPEN state. Now, if you make the request multiple times, If you observe on the Postman client console, for some time you will be getting a 504 error like , "Gateway Timeout". At some point in due course of sending these requests
 *  the response will change with status of 503 , "error": "Service Unavailable", "message": "Upstream service is temporarily unavailable", ..etc. This error is being thrown by the circuit breaker pattern. If you visit the page "http://172.25.96.1:8072/actuator/circuitbreakers", you can see that the state is now OPEN.  You can also refresh the other page for circuitbreakerevents  i.e., "http://172.25.96.1:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker" and you can see that as you scroll down we have many requests with the type as 'ERROR'. If you kee scrolling you will see that at some point of time when my circuitbreaker realises the failure rate exceeded the threshold value of 50%, you will see the type changed to 'FAILURE_RATE_EXCEEDED'.
 *  And that why from the next event after this one with the type as 'FAILURE_RATE_EXCEEDED' , my circuit breaker is going to state the transition as can be seen that the type of that event is 'STATE_TRANSITION' and the stateTransition property will have a value of 'CLOSED_TO_OPEN' for this event. For the preceding events it was null, I mean, when the type was 'ERROR' and also 'FAILURE_RATE_EXCEEDED', the stateTransition property was null.
 *  Now my circuit breaker pattern will move to the OPEN state and the next events/all the calls after this state transition are not going to be permitted to reach the upstream service/accounts ms as can be seen from the type value i.e., 'NOT_PERMITTED'.
 *
 *  Rewired Knowledge
 *  -------------------
 *  Upstream: An upstream service is one that provides data or functionality that other services depend on. It is typically the source or origin of the data flow.
 *  Downstream: A downstream service is one that consumes data or functionality provided by other services. It is typically the receiver in the data flow.
 *
 *  What advantage are we getting with this 'NOT_PERMITTED'? Now, my gateway server is not going to waste its resources by invoking the accounts ms. My circuit breaker pattern which is sitting in the middle (At the downstream service i.e, GatewayServer) will always throw an immediate error to the gateway server that the Upstream service i.e, Accounts ms is not available. With that, my gateway server resources and threads will not be blocked for a long time. Now, since we have waited for more than 10 seconds which is the wait duration in OPEN state. If you go to the postman and try to invoke new requests. From our last request the response had a status 503, "error": "Service Unavailable", and "message": "Upstream service is temporarily unavailable". Now,
 *  if you make another request, this time you can verify that the actual invocation to the upstream service i.e, Accounts ms happened and that's why the response status will change to something else i.e., 504 , "error": "Gateway Timeout", ..etc. Now, if you go back to the actuator URL's and try to refresh the page http://172.25.96.1:8072/actuator/circuitbreakers, you will see that the state is now HALF_OPEN and all the other properties have been reset for monitoring based on the configurations we have for the circuit breaker pattern when in HALF_OPEN state so that an appropriate action can be taken. If you open the circuit breaker events page i.e., http://172.25.96.1:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker, you can see events where the state transition happened from OPEN to HALF_OPEN.
 *  And during the HALF_OPEN state (Where partial traffic or requests are allowed), we have sent only one request so far, and it has also failed. In this state also, I will also try to send the request multiple times and eventually the circuit breaker pattern will realise that many of the requests are failing above the threshold value, and it should move from HALF_OPEN to OPEN state. If you refresh the circuit breaker events page, you can see that the state transition happened from HALF_OPEN to OPEN and any more further request will not get permitted to reach the upstream service/accounts ms for the wait duration of 10 seconds. Like this we have seen the demo of CLOSED to OPEN, OPEN to HALF-OPEN and HALF-OPEN to OPEN state transitions. Now, to see the demo that the circuit breaker pattern is going to switch to the CLOSED status,
 *  if the majority of the requests are being processed successfully, I can remove the breakpoint in the upstream service/accounts ms and in my postman client I can send the request multiple times and eventually my circuit breaker pattern will realise that most of the requests are processed successfully and it should move from OPEN to CLOSED state. Now, if you go to your actuator URLs, you can see that the state is now CLOSED and all the other properties have been reset for monitoring based on the configurations we have for the circuit breaker pattern when in CLOSED state. For the circuit breaker events page i.e., http://172.25.96.1:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker, you can see events where the state transition happened from HALF-OPEN to CLOSED because majority if not all the requests were processed successfully.
 *  This way, the circuit breaker pattern is doing a lot of work behind the scenes to make our ms's fault-tolerant and resilient. We have seen a live demo where I am saving my gateway server(Nothing but the breakpoint we added in the upstream server/Accounts ms) and opening too many threads (Nothing but the many requests I am sending to the gateway server from my postman client) and waiting for the response from accounts ms/upstream server with the help of this circuit breaker pattern. Up to now, you should have crisp clarity on whatever visual discussion we have had till now.
 *
 * As of now, we have created a circuit breaker pattern inside the gateway server but it does not have any fallback mechanism. Since we don't have any fallback mechanism inside the response you can see some runtime exception details are being thrown like,"Upstream service is temporarily unavailable", or "Gateway Timeout", etc. In real business applications, throwing runtime exceptions to the client applications or to the UI applications is not a good idea and is NOT VALID APPROACH! That's why we need to have some fallback mechanisms inside which we can write some logic and send some message to the client applications or to the UI applications that will make sense for them. To create a fallback mechanism for circuit breaker pattern it is going to be super easy as discussed below.
 *
 * Defining a fallback mechanism for circuit breaker pattern
 * ----------------------------------------------------------
 * Before this, make sure to review whatever we have discussed. Both visual hands on practice and also the docstrinng you have generated and then commit those changes with confidence before you can resume the discussion.
 * */
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
									config.setName("accountsCircuitBreaker");
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
