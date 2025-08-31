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
