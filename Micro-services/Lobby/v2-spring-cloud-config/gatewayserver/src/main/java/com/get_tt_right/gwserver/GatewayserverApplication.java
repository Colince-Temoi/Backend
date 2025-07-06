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

/** Update as of 29/06/2025
 * Microservices Security
 * ------------------------
 *  Check the slides for the theoretical discussion around Microservices Security. And more details around Spring Security, you can check that with your notebook. Here, we will be discussing the actual implementation of Microservices Security.
 * Problems that OAUTH2 tries to solve
 * -------------------------------------
 * 1. Separation of authorization and authentication logic from the application logic.- This we discussed in details in the slides.
 * 2. For this second problem, we will see a small demo. For the same we will visit stack overflow website. As you know it's an all developers favourite website. Inside this website assuming you don't have any account as of now and want to create an account with them. As soon as you click on the sign-up button, you will see there are multiple approaches on how I can create an account inside this website. The most basic approach is; I can input my 'Display name', 'Email' and 'Password' and then click on the 'Sign up' button and with that an account will be created for me behind the scenes. Apart from this, I can also sign up with social login like Google, Facebook, GitHub etc. Here you may have a question like, how will the stack-overflow know what are the credentials of my google or facebook or even GitHub account? The answer is, Stack overflow does not need to know your actual credentials to these socials. With the help of OAUTH2 framework, they can get your basic details what is your name and email. For example, lets sign-up with GitHub, as soon as I do this, I will be redirected to the GitHub login page - You can verify this from the domain url in you tab. Here, I need to enter my GitHub credentials and it is pretty clear that I am not sharing my own credentials of GitHub to the Stack-overflow because I am entering these credentials inside the GitHub login page/website. Which means, I am not sharing the credentials to any 3rd party applications like Stack-overflow.
 *    As soon as I click on the sign-in button and my credentials are correct, I will be landed back to the stack-overflow page. And you can clearly see that now, stack-overflow has my basic details like can be seen i.e., email, Display name which I have stored inside the GitHub. This means that, the basic resources i.e., email and name that I stored inside the GitHub server are now shared to the stack-overflow with the help of OAUTH2 framework. And in the process, I didn't anywhere share my credentials with the stack-overflow. I entered my own credentials in the GitHub login page, and it has given my basic details i.e., email and name to the stack-overflow. Behind the scenes it also issued an access token to the stack-overflow and using this access token, in future maybe after 2 or 3 days, if I came back into the stack-overflow website behind the scenes, stack-overflow can try to send there request to GitHub server and try to log me in to the GitHUb automatically with the help of the access tokens.
 *    What is happening in all these story here is, GitHub is trying to give a temporary access token to the stack-overflow with limited access. I didn't share my master credentials to the stack-overflow and at the same time, GitHub didn't give an access token to stack-overflow using which the stack-overflow can do anything on my GitHub. The access token it has given, has very limited privileges like, stack overflow can read what is my name and email and that's it. Apart from reading these details, the stack-overflow cannot perform any other operation inside my resources on my GitHub. It cannot for example create repositories or delete repositories or perform any other operations on my GitHub as those are advanced privileges that only require me to personally log in to the GitHub and be given an access token that has these privileges. So, GitHub only has given very basic privileges to the stack-overflow which are reading profile details like email and name. So, this is one of the problems that OAUTH2 framework tries to solve. I mean, without sharing our credentials to the 3rd party applications, we can still provide temporary access to them to access resources that I stored inside a resource server like GitHub, Google, Facebook, or any other application which has implemented OAUTH2 framework inside there system.
 * Check slides for continuation on these theoretical discussions.
 *
 * Scopes demo - Scope is an OUTH2 jargon. It defines privileges in an access token. So, with this access token, my client application can interact with the resource server and can only perform the actions based upon the privileges defined by the scope.
 * --------------
 * For example, taking the stack-overflow scenario, click on the sign-up button >> Click sign up with GitHub >> You will notice, if you had already signed up earlier with GitHub, GitHub won't ask for your credentials again. But the important point to note is, "It is asking the consent from me the resource owner" whether I am okay to share the information from GitHub to the stack-overflow. Specifically you will see something like,
 * "Stack Overflow by Stack Overflow Open Source
 * wants to access your Colince-Temoi account
 * Personal user data
 * Email addresses (read-only)
 * This application will be able to read your private email addresses."
 * - The kind of information my GitHub is going to share in this scenario is, "Personal user data" like email address. You should also be able to see an explanation like, "This application will be able to read your private email addresses." So, this is the scope that my stack overflow is requesting to the GitHub server. And btw, GitHub server cannot decide by itself, that's why since I own these details as a resource owner, I am being asked for permission/consent whether I am okay to 'Authorize StackExchange' or StackOverflow to access/read these details which is 'Email addresses (read-only)'. Suppose, if my stack-overflow is trying to get access to check-in/commit into the GitHub repository on my behalf, then the same will be displayed to you the resource owner with crisp clarity so that you can decide whether you want to allow or not. Of course if I saw something like that, I would straight away deny that, because I can't risk to give such a higher privilege/scope to the stack-overflow. I only want to share my email details to the stack-overflow so that behind the scenes, stack-overflow can create an account for me in their system. So, if okay, click on 'Authorize StackExchange' and behind the scenes, the GitHub auth-server is going to issue an access token and with that access token, stack-overflow behind the scenes is going to send a request to the resource server of GitHub and will be able to read/fetch my email address/details Apart from email it also fetched my display name.
 *  In simple words, every authorization server will define scopes and based upon these scopes it is going to control the privileges of client applications. If you see the official documentation of GitHub - https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/scopes-for-oauth-apps, where they have mentioned "Scopes for OAuth apps" - here they have mentioned what are the scopes that are supported by the GitHub auth server. On this page, you will be able to see many scopes which you can read through. You can see for example, " If someone is trying to get a scope of 'repo' that means " You are going to be Granting full access to public and private repositories including read and write access to code, commit statuses, repository invitations, collaborators, deployment statuses, and repository webhooks." This is a very powerful scope. You can also be able to see another one like, "user:email" which Grants read access to a user's email addresses. and very similarly another one like "read:user" which Grants access to read a user's profile data. Up to now, you should have crisp clarity that using these scopes only inside the OAUTH2 framework, the privileges wil be controlled.
 *
 *  Introduction to IAM products and why KeyCloak
 *  -----------------------------------------------
 *  - As of now, we have discussed very briefly about OAUTH2 and OIDC which are just but specifications. Using these specifications, we can only understand what are the standarnds that we need to follow when trying to implement security inside our applications. By taking these specifications alone, we can't implement security inside any applications. Since security is the most common requirement for many organizations, many companies saw an opportunity to build products around the specifications provided by OAUTH2 and OIDC. Let's try to explore some of these products. The 1st one will be KeyCloak. If you visit its website - https://www.keycloak.org/ - you will see that it is an Open Source IAM product. With the help of this KeyCloak, we can easily set-up an authorization server for an organization. Very big organizations like Google, GitHub, Facebook, Twitter, e.t.c., can afford to build their authorization server based upon the standards defined inside the OAUTH2 and OIDC. But, how about the smaller organizations or organizations who are not interested to build this Auth server from scratch by correctly following the standards defined inside OAUTH2 and OIDC? For all such organizations, these products which are build on top of OAUTH2 and OIDC specifications are going to come in handy for them. One such product is KeyCloak which is completely open-source. You can always search on Google the sponsors of this project. You can see at the bottom of its page that KeyCloak supports Standard Protocols like OIDC, OAuth 2.0 and SAML 2.0. It aslo supports Social Login, Single-Sign On, ... etc. Throughout our sessions we are going to leverage this Keycloak product because it is open-source. We won't be introducing any commercial products. To practice OAUTH2 framework based upon what we have been discussing, opensource products are a perfect choice. Just like we have KeyClaok, we have other products (Commercial) which are build based upon OIDC and OAUTH2 specifications/standards i.e., OKTA - Very famous for enterprise applications. People who can afford it, can easily set up an auth server with the help of OKTA. Very similarly AWS has a product with the name Cognito - which is going to provide IAM that can scale to any amount of traffic. We also have FusionAuth, ForgeRock, Auth0 and many more. For some reason if your organization is looking to build their own authorization server, to make their journey easy , recently Spring team developed a new project with the name Spring Authorization Server. Using this Spring Authorization server, anyone can build their own authorization server. But please note that it is a very new project in the Spring ecosystem and could maybe still have some journey to get to maturity levels of the other products like Cognito, Okta, Keycloak, etc. - My Instructors opinion.
 *  - In our sessions we are going to leverage Keycloak, but for some reason if you are looking to build your own authorization server, then you can always use Spring Authorization Server - Just refer to their docs to get rolling. So, far you should have clarity on why we choose Keycloak to implement Authorization server inside our ms network.
 *  - For more details - check your notebook.
 *  Deep-dive of Client Credentials Grant type flow
 *  ------------------------------------------------
 *  - Check your notebook for details and slides for a summary.
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
