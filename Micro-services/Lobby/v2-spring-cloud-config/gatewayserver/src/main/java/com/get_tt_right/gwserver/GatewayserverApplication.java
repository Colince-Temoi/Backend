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

/** Update as of 04/05/2025
 * Implementing Rate Limiter Pattern in Accounts ms
 * ----------------------------------------------------
 * We will be discussing on how to implement Rate Limiter pattern in normal Spring boot ms's. Previously, we explored on how to implement Rate Limiter pattern inside a Gateway server. Now we will try implementing the same inside Accounts ms. For the same, I am choosing to implement this Rate Limiter pattern on /java-version API of AccountsController, that is on the behavior named getJavaVersion. I want this API to be invoked based upon the Rate Limitations that I am going to define. We need to follow the following steps:
 * 1. Mention a method level annotation i.e., @RateLimiter(<->,[-]). This annotation takes 2 input parameters, i.e., name which is mandatory and fallBackMethod which is optional.  The name will hold what is the name of this RateLimiter configuration. So, for that I am specifying it same as the REST API method name i.e., getJavaVersion. For now, I will not give any fallback mechanism/behavior. First lets have a look on how the behavior is going to be without fallback and later with fallback.
 * 2. Next, we need to mention the RateLimiter properties inside the application.yml. As of now we have circuit breaker and retry related configurations. Very similarly, I am going to mention RateLimiter related configuration/properties. All these you can find inside the Resiliency4J official documentation i.e.,
 * resilience4j.ratelimiter:
 *     instances:
 *         backendA:
 *             limitForPeriod: 10
 *             limitRefreshPeriod: 1s
 *             timeoutDuration: 0
 *             registerHealthIndicator: true
 *             eventConsumerBufferSize: 100
 *         backendB:
 *             limitForPeriod: 6
 *             limitRefreshPeriod: 500ms
 *             timeoutDuration: 3s
 *
 * My instructor pasted/utilized the below properties:
 * resilience4j.ratelimiter:
 *   configs:
 *     default:  // Implies that I am trying to configure these properties/configs for all rate limiter instances/definitions inside my Accounts ms. That's why I have mentioned 'default'
 *       timeoutDuration: 1000
 *       limitRefreshPeriod: 5000
 *       limitForPeriod: 1
 *
 * As child properties to default, we have mentioned 3 important properties i.e., timeoutDuration, limitRefreshPeriod and limitForPeriod.
 * limitRefreshPeriod - We have assigned it 5000 ms. This means, for every 5 seconds, I want to renew the quarter. What is the quarter that I have set for each refresh period? I have set it to 1. You can identify this with the help of the value assigned to limitForPeriod. That means, I want to renew the quarter for every 5 seconds. This means for every 5 seconds, I have configured that only one request is allowed. But in real production applications, the number will be in thousands.
 * limitForPeriod - We have assigned it 1. This means, for every 5 seconds, I have configured that only one request is allowed.
 * timeoutDuration - We have assigned it 1000 ms. Think like, one of the threads came and is trying to invoke a specific API but the rate limiter did not allow it because the number of requests allowed during a particular period is exhausted. So, in such scenarios, with the help of timeoutDuration, we are telling what is the maximum time that a particular thread can wait for the new refresh period to arrive with a new quarter. With this configuration my thread is going to wait for a maximum of 1 second. Within 1 second, if my rate limiter is not allowing the requests then my thread is simply not going to wait further, and it is going to return back with an error message.
 *
 * If keen, you may have observed that this is something different from what we have discussed in the gateway server rate limiter definition and configurations. Yes, that true as both approaches are different. With the Gateway server and Redis Rate Limiter  and KeyResolver, we enforce the quarter limitations based upon  our defined criteria i.e., user/username, IP address, server, etc. But here in individual ms's (Without Spring Cloud GatewayServer), whenever we are trying to implement rate limiter pattern then the approach is going to be different as discussed in this session. Whatever properties/configurations that you provided inside the application.yml, is going to only apply for all type of requests coming towards your specific REST API method. But it is important to not that, they could also apply to all your REST API methods inside your specific ms given that you used @RateLimiter annotation on all your REST API methods - nothing but as a class level annotation.
 *  - This type of RateLimiter implementation, you can utilize where you have infrastructure that can only handle few requests i.e., 10,000 requests per second. Beyond 10,000 requests per second, if you don't want to accept the requests, then in such scenarios, then this approach is going to be super helpful.
 *  - You may also have different requirement(s) i.e., where you want a particular low priority API to process less number of requests than the other high priority API's (So that they can process more number of requests without any issues). All such type of custom requirements you can achieve with this approach.
 *
 *  Once the above changes that we have discussed and implemented are saved and a build is completed, you can go ahead and start the accounts ms. But make sure your Rabbit, Config server and eureka server are all up and running. Finally, after the accounts service is started, you can start your gateway server. Reason: My accounts ms is registered with the eureka server and so, the same details from the eureka server I want my gateway server to fetch during the start-up.
 *  And btw , in this approach we don't need any redis container. To demo these changes, In our postman collection under gateway server, we have a request i.e., eazybank/accounts/api/java-version. If you invoke this API, you will get an output indicating everything is happy! haha. Now, when you invoke this send button multiple times, nothing but send multiple requests to this API, at some point you will be greeted by an error code "INTERNAL_SERVER_ERROR" with the message "RateLimiter 'getJavaVersion' does not permit further calls". Since inside the Accounts ms we have a global exception handler, it is throwing this "INTERNAL_SERVER_ERROR" 500 but behind the scenes the reason as to why this is issue came up is due to the rate limiter implementation that we have defined inside the Accounts ms on top of getJavaVersion method. Since this request, 'http://localhost:8072/eazybank/accounts/api/java-version' is a GET request you can also validate the same from the browser. If you refresh this page multiple times, you will get the same error message.
 *  As a next step, lets define a fallback mechanism for this RateLimiter pattern. As a second parameter to @RateLimiter(<RateLimiterName>,[fallbackMethod]), using the attribute fallbackMethod, we can define a fallback method that will be invoked if the rate limiter is not allowing the requests. To this I am assigning a method 'getJavaVersionFallback'. Copy the getJavaVersion method as it and replicate it just changing the method name to getJavaVersionFallback. Reason, whenever we are trying to create a fallback method, we need to make sure that the method signature is same as the original method signature.  In addition, we need to make sure we are passing one extra input parameter to this fallback method of type Throwable. Once this is done, as a next step you need to write the fallback logic. Here the fallback logic I want to write is: Inside the ResponseEntity body, I am going to simply return an output like "Java 17". So in cases where my original method is not able to process its business logic, I have defined a fallback method that will return this output. If you think like in the real projects; You have an original method which is going to perform a lot of heavy operations and this REST method supports a low or is a low priority API. So, based upon your Rate Limiter configurations on such methods, If you are not allowing any further requests to this REST method within a timeframe then you can simply return a fallback response - usually some simple logic you have to write inside your fallback. Could be something like saying, "Please try again after some time" or you could send some information from the cache. I mean, haha, it's up to you what kind of logic you want to write inside your fallback method.
 * - Now, once the build to these changes is completed. As usual, stop the Accounts service as well as the Gateway service and the restart them respectively. Now if you go to the browser and try to refresh the same page multiple times, at some point in time you will get the fallback method response i.e., "Java 17".
 * Up to now, you should be super clear on how to implement the rate limiter pattern inside your normal ms's. Like this, for each of the Resilience4j patterns, we are learning and implementing them in 2 different ways/places/approaches - With the help of Gateway pattern, nothing but at the Gateway server. And with the help of the resilience4j library + annotations on top of the normal ms's. Using these 2 approaches, now you have flexibility to implement these patterns either in the Gateway Server or in the normal ms's based upon your business requirements.
 *
 *  * */
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
