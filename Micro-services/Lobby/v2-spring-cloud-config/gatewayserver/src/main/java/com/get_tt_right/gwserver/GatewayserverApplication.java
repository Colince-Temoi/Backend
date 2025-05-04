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

/** Update as of 29/04/2025
 * Introduction to Rate Limiter Pattern
 * ------------------------------------
 * Ever played a balloon pop-up game at the exhibition? The reason I brought this up is that it is very relevant to the Rate Limiter pattern that we will be discussing. During this game, whenever you play it, the person who owns this balloon shop will give only limited set of resources/darts, like you will have only 3 or 5 darts/chances to shoot based upon your payment. Beyond the chances given, you are not allowed to play the game. Asked yourself why the owner of this balloon shop is following this rule? It is very simple, if s/he gave you unlimited chances, then he is going to be at loss. That's why he is going to limit your chances/usage based upon your payment.
 * Rate Limiter Pattern -> Using this pattern inside ms's we can control and limit the rate of incoming requests to a specific API or to a ms. This pattern is majorly used to prevent abuse of the system and to protect the system resources and ensure that there is a fair usage of the services by everyone.
 * In ms's, multiple services will be deployed, and they may interact with each other to send a response to the client applications. However, sometimes if you don't put restrictions and controls on how many requests should be consumed by a specific client or person, then there can be a good chance performance degradation or resource exhaustion may happen. And in some scenario, there is also a chance DoS (Denial of Service) attacks may happen. A DoS attack happens when a single malicious user or a hacker is sending continuous requests to your servers - maybe million of requests - with the intention to make your ms network slow or even bring down your server/services/ms's. To avoid all these kind of scenarios and make sure that everyone has a fair usage of the services, we need to implement the Rate Limiter Pattern so that we can enforce limits on the rate of incoming requests.
 *  - If you are expecting 10k requests per second then accordingly you need to set up the infrastructure, and accordingly you can implement the rate limiter pattern as well. But allover sudden if you are getting 1 million requests per second then it is a flag that someone is trying to abuse your system or even trying to make its services slow or even worse - bring it down. To overcome such challenges and to protect your system resources, we need to implement the Rate Limiter Pattern.
 *
 *  Advantages when we implement Rate Limiter Pattern
 *  ---------------------------------------------------
 * . It will help your ms's from being overwhelmed by excessive or malicious requests.
 * . It ensures the stability, performance and availability of your services while providing controlled access to the resources inside the system.
 *   - Basically you are going to create a healthy environment where everyone can fairly use the services as long as they are within the rate limits set by you.
 * If we configure this rate limiter pattern inside our ms's , how is it going to stop the excess requests?  For excess requests, it is simply going to return a 429 Http status code with the message "Too Many Requests". This implies that it cannot accept any more requests, and this will indicate to clients that they are going to have to wait for some time i.e., after some few seconds or minutes, before you can make another request. We can easily enforce this rate limits based upon various strategies like: Maybe we can try to limit the requests based upon the:
 *  - Session
 *  - IP Address
 *  - Logged-In User
 *  - Tenant
 *  - server
 *  - etc
 * Like this there can be many strategies that you can use to limit the rate of incoming requests to your ms's. Additionally, you can use this rate limiter pattern to provide services to users based upon their payment plans or subscription plans/tiers. For instance, inside your system you may have users like, basic user, premium user, enterprise users, ...etc. So for different types of users you want to enforce different rate limits. This kind of requirement we can also implement using this pattern.
 * That was it about a very quick introduction about the rate Limiter pattern, and I'm assuming you are able to correlate with the balloon pop-up game. You may also experience a nostalgia of the balloon pop-up game, and that's the point where I wanted to share about this pattern.
 *
 * Introduction to Redis RateLimiter in Gateway Server
 * ------------------------------------------------------
 * We will try to understand how to implement Rate Limiter Pattern with the help of Spring Cloud Gateway. In the official documentation i.e., https://spring.io/ >> Projects >> Spring Cloud >> Spring Cloud Gateway >> Learn tab >> On clicking the Reference Doc. link, you will be taken to the official documentation >> Gateway Spring Cloud Gateway Reactive Server >> GatewayFilter Factories >> RequestRateLimiter GatewayFilter Factory. Here we have all the details like, What is RequestRateLimiter, How it is going to work. Actually you can see, using this Rate Limiter implementation we can determine if the current request is allowed to proceed or not. If not, a status of Http 429 - which is 'Too many requests' will be returned by default.
 * So, whenever we are trying to implement this Rate Limiter pattern, we need a KeyResolver whose purpose is, Using this keyResolver parameter - We need to tell to the rate limiter pattern implemented at the Gateway Server the criteria by which we will be enforcing this rate limit. Do we want to enforce this rate limit based upon a user or session or Ip address or server or ...etc.? So, based upon your requirements you need to provide the details with the help of KeyResolver interface. There is also a default implementation of KeyResolver interface which is PrincipalNameKeyResolver. If you are using Spring Security to secure your ms's then with the help of this PrincipalNameKeyResolver, it is going to fetch the current logged-in Username i.e.,Principal.getName() and accordingly it is going to enforce rate limit.
 *  - And by default, you can clearly see that it is documented if the KeyResolver does not find a key, the requests are going to be denied. If needed you can also adjust this behavior by setting the properties spring.cloud.gateway.filter.request-rate-limiter.deny-empty-key (true or false) and spring.cloud.gateway.filter.request-rate-limiter.empty-key-status-code properties.
 * So, that is all the concept or rather the rule/specification on how the things are going to work. To make our life easy, inside the GatewayServer we have an implementation with the help of Redis Server. What is Redis (Remote Dictionary Server) Server? It is a cache-based storage system. Using this Redis we can implement this RateLimiter.
 * On the RHS nav, you will see a hyperlink to the section "The Redis RateLimiter". So this, Redis implementation is based on work done at Stripe. The word 'Stripe' in the doc is a link to a blog from the Stripe team which if you can open, you will be even be more informed. Here, there is a good amount of information about what are the use-cases that Stripe team considered to implement the RateLimiter pattern. Under the section, ' Rate limiting can help make your API more reliable in the following scenarios:' you can see they have mentioned some scenarios i.e.,
 *   . One of your users is responsible for a spike in traffic, and you need to stay up for everyone else. Nothing but, a single user is trying to send a lot of traffic and with that, all other users are getting impacted.
 *   . Similarly, One of your users has a misbehaving script which is accidentally sending you a lot of requests. Or, even worse, one of your users is intentionally trying to overwhelm your servers. This is also one of the scenarios which we can avoid with the help of RateLimiter pattern.
 *   . Another scenario is, A user is sending you a lot of lower-priority requests, and you want to make sure that it doesn’t affect your high-priority traffic. For example, users sending a high volume of requests for analytics data could affect critical transactions for other users.
 *   . Another scenario is, Something in your system has gone wrong internally, and as a result you can’t serve all of your regular traffic and need to drop low-priority requests.
 * These are all scenarios where rate-limiting is a good option. If you are interested, please read that blog as it is fascinating - https://stripe.com/blog/rate-limiters
 * Now back to our Spring Cloud Gateway documentation under the section 'The Redis RateLimiter'. They are saying, the rate limiter pattern based upon the redis is inspired based upon the work done at Stripe. In order to implement this, we need to add the starter dependency i.e., spring-boot-starter-data-redis-reactive
 *  - This is going to use the Token Bucket Algorithm. To this algorithm, we need to pass 3 different type of properties i.e., replenishRate, burstCapacity, and requestedTokens . What are they?
 *    1. replenishRate - This property defines how many requests per second to allow. In other words this is the rate at which the token bucket is filled. For example, to this property if you assigned 100, means that for every one second behind the scenes 100 tokens will be added to your bucket. If you wait for 2 seconds and still have not consumed anything then, your bucket will have 200 requests and so on. It is also worth noting that every bucket is assigned to a user or any other criteria based on how you have defined your KeyResolver implementation.
 *    2. burstCapacity - With the help of replenishRate we will keep adding the number of requests per second. For example if my replenish rate is 100 requests/second. After 10 seconds, if my end-user is not using anything within this 10 seconds means that the bucket is going to have 1000 requests. And if you keep filling it, at some point in time, s/he will have millions of requests in his or her bucket. So to avoid this kind of overfilling the bucket with large number of values we are going to set the burst-capacity. Using this burst-capacity property we are going to set what is the number of tokens that a bucket can hold. So, if I set 200 as the burst capacity then in 2 seconds my replenish rate is going to fill 200 tokens. From the 3rd second if my end-user will have not used any of the tokens present inside his bucket, then since the bucket is already full, then the replenish rate cannot add more token.
 *    3. requestedTokens - Using this property we are going to define how many tokens a request costs. Usually by default, each request will cost a single token but if needed you can change this to 10 or 100 or anything and accordingly the number of tokens will be consumed in the bucket for each request.
 *  - In the doc, we also have some example i.e.,
 *     A steady rate is accomplished by setting the same value in replenishRate and burstCapacity - Means, if you assign the replenish rate and burst capacity as 100. This means that in every second, inside my user bucket, we are going to add 100 tokens and if s/he is not using them then they will get wasted and the tokens inside the bucket will be deleted and replaced with the new tokens every one second.
 *        . But in case, if you want to allow temporary bursts. Maybe if you want to give some flexibility to your end user i.e., since you have not used the 100 tokens in the previous second and in the current second you can use all the 100 tokens. In such scenario, you can mention the burst capacity higher than the replenish rate. For example, if you assign the burst capacity as 200, and the replenish rate as 100, then that means in the 1st second even though the end user is not able to use all his 100 tokens, in the 2nd second he should be able to use all his 200 tokens. So, that's how we need to handle the temporary bursts. A visual example is; Assume on one hand I have assigned 100 to both the burst capacity and replenish rate and on the second hand I have assigned 100 to the replenish rate and 200 as the burst capacity. In both scenarios, if the user used 50 tokens in the 1st second. In the 2nd second, scenario A user will only have 100 tokens and Scenario B user will have 150 tokens. In scenario A since the bucket was filled, and we are ready for the second replenishment, whatever was left will be deleted for new replenishment. Hope you are getting this crisp clear.
 *        In the doc you can also find an example i.e., If you want to allow 1 request per min, they have given an example i.e., For example, setting replenishRate=1, requestedTokens=60, and burstCapacity=60 results in a limit of 1 request/min. You can in lei-man's read this as 1 request costs 60 tokens and means in one minute you can only send one request else 429 error will shake your phallenges ceremonouisly.
 *          Here for every second you can only add one token into the bucket. Haha. Hope you are clearly seeing the sequence on how we are finally arriving to a limit of 1 request/min. So, by the end of 60 seconds or 1 minute, the replenish rate wil have added a total of 60 tokens into the bucket. By setting the burst capacity as 60 , means that the maximum number of tokens that we can add to the bucket is 60. At any point of time the bucket will have only a maximum of 60 tokens. By mentioning requestedTokens as 60 means whenever a user makes a single request 60 tokens will be consumed  with this we are making him to be able to send only one request/minute. If you needed him be able to make 2 requests/minute then accordingly you can adjust the value of requestedTokens to 30. 6 requests per minute the adjust the value of the requestedTokens to 10, and so on and so forth. So, accordingly, based upon your requirements you can play with these properties. In the same doc, you can see a sample yml configuration on how you can activate the Rate Limiter and configure the 3 properties that we have been discussing i.e.,
 *  spring:
 *   cloud:
 *     gateway:
 *       routes:
 *       - id: requestratelimiter_route
 *         uri: https://example.org
 *         filters:
 *         - name: RequestRateLimiter
 *           args:
 *             redis-rate-limiter.replenishRate: 1
 *             redis-rate-limiter.burstCapacity: 60
 *             redis-rate-limiter.requestedTokens: 60
 *  Here  they have given the example with the help of yml configurations, but next, we will see how to achieve this with the help of Java based configurations i.e., the Java DSL way.
 *  If you can scroll down in the doc, they also gave an example on how to create/configure a KeyResolver bean. i.e.,
 *  @Bean
 * KeyResolver userKeyResolver() {
 *     return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
 * }
 * You can see that from the bean, they are trying to return the user details. Inside the request there are some query params and inside the query params, there is a query param with the name/key 'user'. So they are trying to get the associated value to this query param. So, this is how they are trying to provide some KeyResolver implementation, but it is up to us based upon our business requirements, we can write any implementation logic for this KeyResolver which is a basic criteria/requirement to enforce all the properties about Rate Limiter that we just discussed earlier.
 * If you scroll up in the doc again under the section, 'The Redis RateLimiter', we have some interesting information around burstCapacity like: ' Setting this value to zero blocks all requests.' - Means if you mention/assign the burstCapacity value as zero, means it is going to block all the requests. So please make sure you are not mentioning the value as zero. Otherwise, your end-users will not be able to send any requests towards your ms's.
 * Up to now, you should have crisp clarity about this quick introduction around Redis RateLimiter in Gateway Server. Next we will be visually implementing and verifying this inside our GatewayServer.
 *
 * Implementing Redis RateLimiter in Gateway Server
 * ---------------------------------------------------
 * 1. Add the dependency related to redis i.e.,spring-boot-starter-data-redis-reactive. This artifactId is from the groupId org.springframework.boot
 * 2. Inside the GatewayServerApplication amin class. I am going to create 2 beans i.e.,
 *   . The very first bean is of return type KeyResolver. i.e,
 *   @Bean
 *   KeyResolver userKeyResolver() {
 * 		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
 * 				.defaultIfEmpty("anonymous");
 *    }
 *    - Inside the method userKeyResolver, I am trying to provide a key based upon which my RateLimiter pattern has to work. My instructor says he just copied this bean configuration code for KeyResolver from the official documentation.
 *      The logic inside this method is crisp simple, First we are trying to get the headers inside the request and getting the header with the key/name 'user'. So, based upon that header value, I am going to create a KeyResolver. If someone is not sending any header inside the request with the key/name 'user', then I am going to assign a default value i.e., 'anonymous'. This is the logic that we have maintained for the KeyResolver implementation but in real projects you can change this logic based upon your requirements.
 *  . The second bean that I am trying to create is of return type RedisRateLimiter i.e.,
 *  @Bean
 *  public RedisRateLimiter redisRateLimiter() {
 * 		return new RedisRateLimiter(1, 1, 1);
 *    }
 *  - Here I am trying to create a new object of RedisRateLimiter, and I am trying to pass 3 different values like int defaultReplenishRate,int defaultBurstCapacity,int defaultRequestedTokens. With these configurations we are going to add 1 token in each second and burst capacity is also going to be 1 and the cost of each request is also going to be 1. With this, it is pretty clear that for each second, my end-user can only make on request.
 * After defining these 2 beans, in the routing configurations that we have defined under the bean RouteLocator you can clearly see that as of now, we have implemented the circuit breaker patter for accounts route configurations. We have also implemented the retry pattern for loans route configurations. Maybe for cards, we can try to implement the RateLimiter pattern. So, just after the addResponseHeader filter configuration inside the cards route configurations, fluently invoke another filter with the name requestRateLimiter. Actually this requestRateLimiter filter is overloaded, nothing but we have 2 types of requestRateLimiter. One of them is a consumer that accepts some configurations and the other does not accept any input. So, I will invoke the one that consumes some configurations. Using lambda implementation, I am going to invoke setter methods like; setRateLimiter(-) which takes a RateLimiter object as input.
 *  - To this setRateLimiter, I am going to pass the redisRateLimiter method that we have defined that returns us a RateLimiter object/bean. After the setRateLimiter, I am going to fluently invoke one more setter method i.e., setKeyResolver(-) which take a bean of KeyResolver as input. So to this, I am going to pass the method invocation i.e., userKeyResolver which is a definition that will return us a KeyResolver bean/object. Up to now, we have added/enforced the requestRateLimiter filter/pattern for all the API's inside the cards ms.
 *  - As a next step, we need to start the redis DB or Redis container so that it can maintain creating the buckets with the 'user' / usernames associated values that we got from the headers and maintain all the configurations we defined inside the RedisRateLimiter object/bean. We are going to start our redis container with the help of docker. So please make sure your docker server is up and running and post that you can try open the terminal and run the docker command i.e., docker run -p 6379:6379 --name eazyredis -d redis. 6379 is the default port of redis. I am trying to give a name to the container using the flag --name and then the container name i.e., eazyredis. -d means starting the container in detached mode and at last the image name of redis i.e., 'redis' and by default the latest version will be pulled. Now, the redis container will be started behind the scenes. As a next step we need to provide the connection details of this redis container inside the application.yml file of GatewayServer.
 *  - The properties that we need to add to the application.yml file will have a parent key which is 'spring' followed by 'data' then under this data I am going to add few new properties i.e.,
 *      redis:
 *       connect-timeout: 2s  - Represents the connection timeout
 *       host: localhost  - What is the host
 *       port: 6379   - What is the port number
 *       timeout: 1s  - What is the timeout
 * So these are the redis related configurations. Now make sure that the 'data' element inside the application.yml file is present as a child under 'spring'. This looks good, next, do a build and once its completed start your GatewayServer application. But make sure that your. rabbitMQ, configserver, eurekaserver,and cards services are running. As of now, for this visual demo, we don't need accounts and loans services because we have implemented RateLimiter pattern only for cards service. Once everything is started successfully, in order to visually test and verify our RateLimiter pattern implementation we need to send a lot many requests within a single second so that we can visually see what is happening behind the scenes. So, to perform some load testing we can leverage Apache benchmark server. The official website of apache benchmark project is https://httpd.apache.org
 *  - On the LHS nav,if you can click on the download button you should be able to see some options on how we can download and set-up this server. You should be ale to see a link in the string 'Apache httpd for Microsoft Windows is available from a number of third party vendors.' which if you follow you can get more instructions on how you can set up this project for your Windows machine. Also, how to set up this project inside any other OS or even Windows system, you can always check with YouTube for videos that will help you set up this Apache benchmark. Actuality there are a good amount of videos that will help you with this regard. Alternatively,you can also google on how to set up apache benchmark, and you should see a good amount of blogs explaining this. My instructor showed this https://ubiq.co/tech-blog/how-to-use-apache-bench-for-load-testing/, It has instructions that can help you if you are using Unix based or Mac based OS's. Actually the installation is going to be very easy and straight forward for them. But for windows, the process is a bit lengthy. Like where you need to download this from the given links, and post that you need to set up the server.
 *    But don't worry, since we are seeing the demo with my instructor. After setting up your Apache benchmark - Verify by running the command ab -V to see the version information.
 *    Now run the command ab -n 10 -c 2 -v 3 http://localhost:8072/eazybank/cards/api/contact-info
 *     . ab - stands for Apache benchmark
 *     -n 10 means 10 requests. I am trying to send 10 requests and that's why I have mentioned -n 10
 *     -c 2 means 2 concurrent requests. I am trying to send 2 concurrent requests at a time whenever this ab is trying to send these 10 requests and that's why I have mentioned -c 2
 *       Nothing but instead of sending these requests one by one this ab will send 2 concurrent requests at a time.
 *     -v 3 . Here -v indicates verbose and when I give/assign the value 3 to this verbose flag that indicates to this AB server that I want to see a detailed report in the output.
 *     After this verbose flag related configurations, we need to mention the endpoint url that we are trying to invoke. In this case it is http://localhost:8072/eazybank/cards/api/contact-info - Here I am trying to invoke one of the cards service API which is contact-info. We are not invoking it directly, we are hitting it via GatewayServer as can be seen. On hitting this ab command i.e., ab -n 10 -c 2 -v 3 http://localhost:8072/eazybank/cards/api/contact-info
 *       . You should be able to see/visualize a detailed explanation on what has happened. Let's focus on the summary i.e.,
 *           Server Software:
 * Server Hostname:        localhost
 * Server Port:            8072
 *
 * Document Path:          /eazybank/cards/api/contact-info
 * Document Length:        209 bytes
 *
 * Concurrency Level:      2
 * Time taken for tests:   0.956 seconds  - Means all the requests are processed within 0.956 seconds. Which mean in this duration my ab send all the 10 requests to the backend server with a concurrency level as 2
 * Complete requests:      10
 * Failed requests:        9   - Out of the 10 requests 9 failed. Based on our rate Limiter configurations, for a user we have only assigned one token for each request/second
 * Total transferred:      4550 bytes
 * HTML transferred:       2090 bytes
 * Requests per second:    10.46 [#/sec] (mean)
 * Time per request:       191.292 [ms] (mean)
 * Time per request:       95.646 [ms] (mean, across all concurrent requests)
 * Transfer rate:          4.65 [Kbytes/sec] received
 *
 * For the very first request you can see we received a 200 i.e.,
 *   ---
 * LOG: header received:
 * HTTP/1.1 200 OK
 * transfer-encoding: chunked
 * X-RateLimit-Remaining: 0
 * X-RateLimit-Requested-Tokens: 1
 * X-RateLimit-Burst-Capacity: 1
 * X-RateLimit-Replenish-Rate: 1
 * Content-Type: application/json
 * Date: Sat, 03 May 2025 06:03:16 GMT
 * eazybank-correlation-id: 9b29f46d-57f2-47aa-ac2d-3e0542bdc8cd
 * X-Response-Time: 2025-05-03T09:02:50.254228400
 * connection: close
 *
 * Post that we started receiving a 429 - Too many requests
 * This clearly tell you that the rate Limiter pattern that we have implemented is working perfectly. To solidify your clarity, if you commit the requestRateLimiter filter configurations from the cards route configurations, you will notice all the requests are being processed no matter how may they are as we will have not enforced this rate limit. Hope you can visually understand with crisp clarity all this.
 * Up to now, you should have crisp clarity on the RedisRateLimiter pattern that we have implemented with the help of GatewayServer
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

	/**
	 * Returns a KeyResolver which returns the first value of the "user" header from the request.
	 * If the header is not present, it defaults to "anonymous".
	 * We have configured this logic for the KeyResolver and since we are not sending any headers inside the request with the key/name 'user' it is going to consider the KeyResolver value as 'anonymous'.
	 * If you want, you can test by sending the header inside the request, but that is not going to make any difference in the demo because inside the local system, we will always have a single user.
	 */
	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
