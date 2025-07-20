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

/** Update as of 13/07/2025
 * --------------------------
 * Implementing Authorization inside Gatewayserver using roles
 * --------------------------------------------------------------
 * As of now, inside our gatewayserver, we have just been checking if our client application is authenticated or not. That means, we are only performing the authentication but not the authorization. What if we have a scenario where, we only want to process the request(s) if the client application has certain role(s)/authorities/privileges. In such scenarios, instead of this 'authenticated()' method invocation like we saw in our previous session,on top of pathMatcher(....) behavior, you need to fluently invoke the hasRole(....)behavior.This method receives an input parameter like what role are you expecting which your client should have configured in the auth server. So, for all accounts related APIs, I will be expecting a client to have a role with the name 'ACCOUNTS'. Same drill, I can configure role-based access for cards and loans services as well as can be seen in the springSecurityFilterChain bean method of SecurityConfig. With this, I have now enforced the authorization inside the gatewayserver.
 * Now, as a next step, we need to make sure we are configuring these roles to our client application(s). Where? Inside the auth server. Navigate to Clients >> the client which we created i.e., get_tt_right-bank-callcenter-cc. Here you will see 2 tabs that look like they are similar but NOT i.e., 'Roles' and 'Service Account roles'. Since this client that we set up is a client which is following the Client Credentials Grant type flow, then it means it is also another application, nothing but it is not an end user/resource owner in other words. So, in such scenarios we should not configure under 'Roles' instead we should configure under 'Service Account roles'. If you navigate to that tab, you should see a button like 'Assign Roles' which happens to be a dropdown with options like 'Client roles' and 'Realm roles'. We are interested in 'Realm roles'. If you click on that, you should be able to see a list of roles, amongst them, we don't have any that we want i.e., we have nothing like 'ACCOUNTS', 'LOANS' or 'CARDS'
 * So, on the LHS nav, click on the 'Realm roles' button and post that click on the 'Create Role' button, then create 'ACCOUNTS' role with a description like 'Accounts role'. Same drill for 'CARDS' and 'LOANS' roles. But intentionally, I will not create the roles 'CARDS' and 'LOANS' so that we can do some negative tests after which on verifying that everything is working as expected, both positive and negative tests, we can create those roles as well later. Now, you can go back to our client and assign the 'ACCOUNTS' role that we have just created. You should see a message that, 'Role mapping updated' which implies that we have now assigned the 'ACCOUNTS' role to our client, and now we can verify the authorization inside the gatewayserver. Now, in our postman collection for microservices under the folder 'KeyCloak' and specifically the request like 'ClientCredentials_AccessToken', if you click on that to get a new access token then copy that and decode it in the website like jwt.io. You should be able to see under the 'Decoded Payload' a key like "realm_access" which actually has a Json object as its value. Inside that Json object value we have a key "roles" which has an array as its value. Amongst the elements in this array is the custom 'ACCOUNTS' role that we have just configured. All the remaining element like "default-roles-master", "offline_access" and "uma_authorization" are the default roles that are configured by default in the KeyCloak auth server.
 * For us, we are more interested inside this custom 'ACCOUNTS' role. Now, it is confirmed that the role(s) information is coming as part of the access token. Now, we need to write some logic inside our gatewayserver/resource server to extract this role(s) information and convey the same to the Spring security framework to use and validate my authorization rules that I have configured. For the same inside the same package like 'config' I am going to create a new class i.e., 'KeyCloakRoleConverter' because this class is going to help me to extract the roles that I am getting from the KeyCloak auth server into a format that Spring security framework can understand. Check out the class 'KeyCloakRoleConverter' for more details.
 * Now, we have successfully created a Role converter, but how to communicate this KeyCloakRoleConverter to the configurations that we have done inside our custom SecurityConfig class? For the same, inside the SecurityConfig class, we are going to create a new small method i.e., 'grantedAuthoritiesExtractor():Converter<Jwt, Mono<AbstractAuthenticationToken>>'. Check out the method 'grantedAuthoritiesExtractor()' for more details.
 *
 * Authorization demo inside our gateway-server
 * ---------------------------------------------
 * In our postman, I will try to create a new account from the request gatewayserver_security/Accounts_POST_ClientCredentials. Make sure from the Authorization tab, you get a fresh access token. So, behind the scenes, my KeyCloak auth server the roles information to the access token we will receive. Finally, you can make your request and the account should be created successfully. Now, I will also invoke the request gateway_server/CARDS_POST_ClientCredentials with the same mobile number that I used to create the accounts' information. But do you think we will get a successful response? haha. Let's see, get a fresh token for this request and invoke it. Boom! 403 Forbidden Access to the resource is prohibited. From postman you can see all this and they will even tease you and tell you that 'Try with other auth credentials?' haha This simply means yes, we are authorized - reason as to why we are not getting a 401 error, but we are getting a 403 because we don't have enough privileges haha. How to fix this issue? We need to provide enough privileges to the client application. For the same, in the KeyClaok administration console, on the LHS nav >> Realm roles >> Create Role >> with the name 'CARDS' and description 'Cards role' then >> Save. Post that on the LHS nav >> Clients  then open your specific client i.e, >> get_tt_right-bank-callcenter-cc >> then navigate to the tab 'Service Accounts Roles' >> Click on the Assign Role button making sure to select Realm roles from this button's dropdown >> Select the Realm role CARDS to assign to our client and finally you should see a success message like 'Role mapping updated'. With this now, my client application should be able to invoke the POST cards related APIs as well.
 * Same drill, test the request gateway_server/CARDS_POST_ClientCredentials again, and now you should be able to get a successful response as expected. If you copy the fresh generated token and decode it from the jwt.io website you should be able to verify that this time you have the 'CARDS' role in the 'realm_access' object. Same drill, you can do the tests for 'LOANS' roles as well. Then you can go ahead and invoke the request gateway_server/LOANS_POST_ClientCredentials and you should get a successful response as expected. And as anote make sure you are following the same spelling and case sensitivity for the roles you are creating and using inside your SecurityConfig class otherwise the authorization will fail.
 * With this, you should be super clear about how to enforce authentication and authorization inside a gatewayserver with the help of OAuth2 framework and an authorization server like KeyCloak. Inside the SecurityConfig.java class, in the bean method like springSecurityFilterChain, I have defined my own security requirements. In real-world, the requirements maybe more complex. So consider the security requirements we have configured in the bean method like springSecurityFilterChain as a stepping stone. Since the approach is going to be the same, you can go ahead and expound more and do awesome stuff around what we have just learnt.
 * With this, we can conclude the Client Credentials Grant type flow with KeyCloak as the authorization server.
 *
 * Deep Dive of Authorization Code Grant type flow
 * --------------------------------------------------
 * As of now, we saw a scenario inside our ms's, where an external backend server/API are trying to communicate with our ms's using backend APIs and there was no end-user involved. For such scenarios, we have to use Client Credentials grant type flow. But what if the client applications who are trying to consume your ms's are UI applications or Mobile applications? For such scenarios, definitely their will be an associated end-user who is trying to use that website or mobile application. For such scenarios, we have to use Authorization Code grant type flow which is available inside the OAuth2 framework.  Check slide for more theoretical details.
 * Authorization code Grant type flow demo as provided inside the website which is https://www.oauth.com/playground/ - Inside this website we can demo to test various OAUTH grant type flows
 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Check you Spring Security notes as we discussed this in crisp details.
 * There is no option for demo testing Client Credentials grant type in this website for some reason. For now, our interest is doing a demo test around Authorization Code grant type flow. Check with your notebook as we did this with crisp details. Be doing those test demos inside that website and try to correlate with what we have been discussing for all the grant type flows.
 *
 * Securing Gateway Server using Authorization Code Grant type flow inside OAuth2 framework
 * -----------------------------------------------------------------------------------------
 * Check slides for theoretical and visualized details on how this Authorization Code grant type flow will work inside the get_tt_right microservices' scenario. With that, we have from all dimensions(4 ways actually including even a demo test inside the oauth playground) discussed everything crisp clear round about how this Authorization Code grant type flow will work.
 * Next we will be implementing this Authorization Code grant type flow inside our ms's network.
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
