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

/** Update as of 10/05/2025
 * Introduction to Bulkhead Pattern
 * -----------------------------------
 * With the help of this bulkhead pattern, we can improve the resilience and isolation of components or services within a ms network/ a system. You may be wondering why on the RHS of this slide, there is an image of a boat or Ship.  The reason we are having it is because, this bulkhead pattern is inspired from the concept of bulkheads in the ships. So what are these bulkheads? If you can see the image of the ship, using these bulkheads, we are physically partitioning the entire ship so that even if one of the compartments is flooded with water, the other compartments are safe and secure.Nothing but it is not going to affect the other compartments/partitions inside the ship. This will thus enhance the overall stability and safety of the ship or vessel. If you have watched the Titanic movie, when the Ship collides with a mountain in the middle of the sea, it didn't immediately submerge into the ocean. Actually a lot of stories happened in between the collisions and climax. The water didn't enter into the entire ship at a time because behind the scenes there are bulkheads in the ship. When you lock a compartment, the water will not enter into the other compartments very easily.
 * Using the same bulkheads inside the ships,the bulkhead pattern in the software perspective is inspired. So, with the help of this pattern we can isolate and limit the impact of failures or high loads in one component from spreading to the other components. It also helps to ensure that a heavy load in one part of the system does not bring down the entire system and this enables other components to work and continue functioning independently of each other. How bulkhead pattern is going to achieve the stability is, with the help of this pattern we can allocate or assign resources to a specific REST API or MS so that teh excessive usage of resources we can avoid. On a high level, the summery is: With the help of bulkhead pattern we can define the resource boundaries for all the components inside a ms and this will enhance the resilience and stability of the system. Check slides in order to understand this bulkhead pattern with the help of a diagram.
 *
 * How to implement the Bulkhead Pattern
 * -------------------------------------
 * Inside the Spring Cloud Gateway, there is no support for bulkhead pattern as of now. This being said, we can only implement bulkhead pattern by using the Resilience4J library. On the page - https://resilience4j.readme.io/docs/getting-started-3#configuration if you can scroll down in the Annotations section, you will notice there is an annotation with which we can leverage to implement the bulkhead pattern. You can see that with the help of @Bulkhead we can configure the bulkhead related configurations i.e.,
 * @Bulkhead(name = BACKEND, type = Bulkhead.Type.THREADPOOL)
 * public CompletableFuture<String> doSomethingAsync() throws InterruptedException {
 *         Thread.sleep(500);
 *         return CompletableFuture.completedFuture("Test");
 *  }
 *  - The above code you can find it on that page, I copied it from there.
 *  name = BACKEND
 *  type = Bulkhead.Type.THREADPOOL
 *
 *  Means, with these bulkhead configurations, we are trying to assign the thread pool to the operation/behavior 'doSomethingAsync'.
 *  The properties related to bulkhead you can identify if you scroll up in the page under the configurations section. i.e.,
 *  resilience4j.bulkhead:
 *     instances:
 *         backendA:
 *             maxConcurrentCalls: 10  // Here you can mention what is the maximum number of concurrent calls that a particular bulkhead pattern can support on top of a REST API.
 *         backendB:
 *             maxWaitDuration: 10ms
 *             maxConcurrentCalls: 20
 *
 * // The above is normal are normal bulkhead configurations where you can only control the concurrent calls. But if you want to control the threads also, with the help of the below thread pool configurations you can assign maximum thread pool size, what is the core thread pool size, what is the queue capacity. So, these are all the properties that you can use to define the bulkhead configurations. I am not going to implement any of these inside our ms's  and we are not going to see the demo of bulkhead pattern in our ms's. The reason is, to see the demo of this bulkhead pattern we need some commercial tools or some performance tools like loadrunner or Jmeter where we can see the thread usage which is a complicated process.
 *
 * resilience4j.thread-pool-bulkhead:
 *   instances:
 *     backendC:
 *       maxThreadPoolSize: 1
 *       coreThreadPoolSize: 1
 *       queueCapacity: 1
 *       writableStackTraceEnabled: true
 *
 * - Up to now, I am assuming you have the clarity of what is bulkhead pattern and how to implement it.
 * In future whenever you have these kind of scenarios, where you want to define the boundaries for your API's inside the ms, you can leverage what we have just learnt in regard to the bulkhead pattern and with the help of your performance testing team you can always try to validate and verify your changes.
 *
 * Aspect order of Resiliency patterns.
 * -----------------------------------
 * As of now, we have discussed various resilience patterns supported by this resilience4j library. We have also gone ahead and implemented these patterns inside our ms's individually. Sometimes you may have complex business logic where you may end-up combining various resilience patterns. In such scenarios, you may have a question like; " What is the order that my resilience library is going to follow if I have multiple resilience patterns defined for a single REST API method or for a single service/routing path? "
 * To understand this, inside the official doc of resilience4j library, https://resilience4j.readme.io/docs/getting-started-3#aspect-order, under the section "Aspect order" you can see various information regarding this. You can clearly see  the order of the patterns that resilience4j is going to follow, i.e, Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) ) with the innermost - Bulkhead - taking 1st priority and the outermost - retry - taking the last priority. Which means retry pattern will be applied at the end. Here 'Function' represents the actual REST API method in your individual ms or the routing path that you have defined inside your gateway server.
 * Sometimes you maybe fine with this default order but if you have some complex scenario where you want this default order then its is going to be super easy with the help of the properties like:
 * - resilience4j.retry.retryAspectOrder
 * - resilience4j.circuitbreaker.circuitBreakerAspectOrder
 * - resilience4j.ratelimiter.rateLimiterAspectOrder
 * - resilience4j.timelimiter.timeLimiterAspectOrder
 * - resilience4j.bulkhead.bulkheadAspectOrder
 * All those I have copied from the official doc. No magic here. In the yml configuration like shown below, using these properties you can define the order of execution i.e.,
 * resilience4j:
 *   circuitbreaker:
 *     circuitBreakerAspectOrder: 1
 *   retry:
 *     retryAspectOrder: 2
 * Also these yml configurations I have gotten from the official doc. The higher the number the higher the priority. So, with the above yml configurations, we are giving higher priority to the retry pattern. Higher priority means higher value which means that it is going to be applied/executed first. Post that only then the circuit breaker pattern will be applied/executed and so on.
 * In the doc you can see that - Circuit Breaker starts after Retry finish its work
 * This was some quick information that I need to give and for more other details including even stuff we have not yet discussed, you can refer to the official doc.
 * In regard to this Aspect order of Resiliency patterns, my instructors humble advice is, "Please don't try to use all these patterns simultaneously in a single ms or in a single routing path, it will be like doing over-engineering and without proper testing, you may get some surprises inside the production." Please do some due diligence and post that only, go with the required patterns inside your individual ms or routing path configurations.
 *
 * Demo of Resiliency patterns using docker containers
 * -----------------------------------------------------
 * As of now we implemented and tested various resiliency patterns inside our local system. To test the same changes using docker containers we have to create docker containers for all the services that we are leveraging in our system with an updated tags to what we had previously. For example, prior to making a change to a service, if its tag name was v5, then the new tag should be v6 to accommodate/capture these new changes. Since you are familiar on how to go about generating docker images and updating docker compose file(s) information, you can go ahead and do it by following the below simple guide:
 * 1. Inside your pom.xml change the tag name inside your jib plugin configuration.
 * 2. Inside your docker compose file change the tag name for the respective docker service container.
 * And btw, inside the GitHub repo of your instructor, you can find maven as well as docker commands that we have been using throughout this course. Make sure in future to replicate this readme file in your GitHub repo as well.
 *
 * Since we introduced a redis service, means we are doing some changes inside our docker compose file i.e.,
 * - Create a new redis service configuration inside your docker compose file i.e.,
 *   redis:
 *     image: redis
 *     ports:
 *       - "6379:6379"
 *     healthcheck:
 *       test: [ "CMD-SHELL", "redis-cli ping | grep PONG" ]  // To perform the health check this is the command that we need to use. You can always get this command from the official documentation of redis as well.
 *       timeout: 10s   // This is the timeout configuration
 *       retries: 10    // This is the retry configuration
 *     extends:
 *       file: common-config.yml   // Post that, I am trying to extend the network-deploy-service service present inside the common-config file so that my redis service will also start under the network of 'eazybank'.
 *       service: network-deploy-service
 * - Reason: To implement the Rate Limiter pattern with the help of Spring Cloud gateway, we need a redis service to store the rate limiter configurations - that is the key(s) i.e., KeyResolver and the value(s) - RedisRateLimiterConfiguration.
 * - After mentioning/defining the redis service related configurations inside the docker compose file, inside the gatewayserver service defined in this same docker compose file, under the dependencies, nothing but the depends-on section, we need to add this new dependency which is related to redis service.
 *   You may have noticed that the poor guy, "gatewayserver" has a lot of dependencies, and he has to start towards the end haha.
 *   So after defining the dependencies information, under the environment variables, nothing but the 'environment' section of this 'gatewayserver' service, we need to provide environment properties/configurations related to redis service i.e.,
 *       SPRING_DATA_REDIS_CONNECT-TIMEOUT: 2s   // This is the connection timeout of redis
 *       SPRING_DATA_REDIS_HOST: redis           // This is the host name. We are using the name of the service that we have created inside our docker compose file. We should not mention 'localhost' here.
 *       SPRING_DATA_REDIS_PORT: 6379            // This is the port number
 *       SPRING_DATA_REDIS_TIMEOUT: 1s           // This is the timeout configuration
 *
 * - At last, make sure to make change related to the tags. I.e.,
 * Accounts/Cards/Loans services previously had the tag name as v5 and now it has the tag name as v6.
 * Gatewayserver service previously had the tag name as v1 and now it has the tag name as v2.
 * Configserver/Eureka services previously had the tag name as v1 and will retain the same as we have not made any changes to it.
 * - So after making all these changes, I have copied the same docker-compose.yml file into the qa and prod profiles. We didn't make any changes inside the common-config.yml file. As a next step we can try to execute the docker compose up command for the production profile and see if everything is working as expected or not.
 *
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
