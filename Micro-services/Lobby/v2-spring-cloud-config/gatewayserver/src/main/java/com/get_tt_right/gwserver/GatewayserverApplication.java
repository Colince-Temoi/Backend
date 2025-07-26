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

/** Update as of 20/07/2025
 * --------------------------
 Implementing Authorization Code grant type flow inside our ms's network.
 --------------------------------------------------------------------------
 * 1. Register client and end-user inside KeyCloak Auth server.
 * ----------------------------------------------------------------
 *  Check with your notebook for details. But we are going to create a new client with the details like:
 *  Client Type : OpenID Connect, Client ID : get_tt_right-bank-callcenter-ac (ac indicates authorization code), Name : Get_tt_right Bank Call Center UI App, Description : Get_tt_right Bank Call Center UI App. Post that, you can click on the next button.
 *  Check your notes for what to do post clicking the Next button.
 *  Next, you will be asked to configure what is your root URL, Home URL, Valid redirect URIs, Valid post logout redirect URIs, and Web origins. You can ignore Root and Home URL. But under the Valid Redirect URIs, in ideal scenario, we should mention what is the URL that my auth server needs to redirect the end-user post successful authentication. Maybe you can redirect the end-user to the dashboard webpage, or to some profile webpage once the authentication and the access token processing is completed.
 *   So, whatever valid redirect URI(s) that your client application is going to send in that request, the same has to be configured here. This will save you from some hacking scenarios. If you don't have this check, then hackers will redirect this access token to their own website which is very dangerous and that's why we need to make sure we mention the valid redirect URI(s). But for now since we don't have any proper UI application, we can mention asterisk, which means I am okay with any kind of redirect URI. Also, since we don't have any valid/Proper UI application inside our set-up, we can also ignore this Valid post logout redirect URIs field as well. Under the Web Origins I can mention asterisk. The purpose of this field is; in real world, your client application may be deployed in a different domain and a different port number whereas the auth server or ms's may also be deployed in a different domain and port number. In such scenarios, the browsers will stop communication between them by throwing a CORS related error.
 *   If you want to understand more details about what is CORS, check with your Spring Security notebook. Like we discussed, the CORS security feature inside browsers, by default it will stop the communication between 2 different domains and port numbers until unless the backend server is allowing. So here, I am configuring inside my Auth server saying that, please accept the requests from cross domains as well. By configuring the asterisk here I am fine to receive the traffic from any kind of domain and port number. But in your real projects, you can mention what is the actual domain name where your client(s) application is deployed. Or domain to any cross application that you are aware of that will try to be interacting with this auth server. You can save these changes and you should be able to see the credentials of your client application under the 'Credentials' tab.
 *   Check with your notebook for more details.
 *  Now, as a next step, we should create end-user details inside the KeyCloak server. Check your notebook for more details. But the end-user that we are going to create has the details like; username : Colince, email : colince@get_tt_right.com (Make sure to use a valid email - I used colincetemoi190@gmail.com), first name : Colince, last name : Temoi. At last, you can click on the create button. Check more details in your notebooks as we have ever done this.
 *  You may have a question like, in real world, who is going to create the end-user details? Is the admin going to manually create the end-users just like we have done every time an end-user wants to register? Of course not. KeyCloak exposes a lot of REST APIs using which any properly authenticated application, they can connect with the KeyCloak auth server, and they can create the end-users by invoking these REST APIs. Maybe I can have some login page or some sign-up page inside my web application. When an end-user trying to create an account very first time I can have a sign-up button which will take him to the sign-up page and in the sign-up page he can enter all his credentials and I can send a request to the KeyClaok server using REST APIs and Keyclaok can create the users behind the scenes. This we also discussed, check your notebook for more details.
 *  All the actions that an admin can perform from the UI, all those are supported REST APIs approach as well. Yo should be super clear with all that we have discussed. Now, we have the client and the end user details registered with the auth server. As a next step we will try to leverage Authorization code grant type flow inside our ms's network.
 * 2. Demo for Authorization Code grant type flow
 * -----------------------------------------------
 * We will try to use Authorization code grant type flow and try to access the secured resources available behind the gateway-server. To test this grant type flow, since we don't have any UI or mobile application, we need not worry as with the help of postman, we should be able to easily mimic a UI/Mobile application scenario. All theoretical details you can find in your notebook as we have previously done a demo on this grant type flow during our spring security sessions.
 *In postman collection i.e., gateway_server, we have several POST requests suffixed '_AuthCode'. We have for Accounts, Cards as well as loans related requests. For the accounts related request, minus any configurations under the authorization tab, if you try to invoke it, by default definitely you will encounter a 401 Unauthorized error. Now, we need to add the authorization code grant type flow configurations. Check with your notebook for the details on how we were approaching this, then you will relate with what is configured under the authorization tab for our respective requests with respect to Authorization Code Grant type flow.
 * Finally, we did also test the GET request like Accounts_GET_PermitAll, which we know does not require any Authentications and it also worked. This confirms that all the Account, Cards and Loans details are created behind the scenes. Regardless of whether we are using Client credentials or authorization code grant type flows, we don't need to make any changes inside the gateway server. In both the flows, my gateway server is going to act as a Resource server. So, these are the mostly used grant type flows inside the industry whenever we are trying to implement security inside the ms's network. That's why we have discussed only about these 2 grant type flows. As a next step, we are going to generate the docker images of of what we have been discussing and update the docker compose file.
 *
 *  Demo of Ms's security using Docker containers
 *  -----------------------------------------------
 * Here, we will change inside the pom.xml file the tag name of gatewayserver to v5 and generate a docker image for it, because that's where we have made changes in regard to security. The rest of the services will remain the same as we have not made any changes inside them. Make sure also to make the necessary adjustments inside the docker-compose.yml file.
 * This discussion is going to be interesting as we are going to learn how to secure our internal services so that no one can access them directly. Always the client applications have to go through the edge server only. No shortcuts. Below are the changes we have done inside the docker-compose.yml file:
 * 1. Changed the gatewayserver service tag name from v4 to v5.
 *    I have created a new environment variable which is SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI: "http://keycloak:8080/realms/master/protocol/openid-connect/certs" Here we need to provide the keycloak endpoint details from where my gatwatserver service can fetch the public certificates. You can see, instead of 'localhost', I am mentioning what is the service name i.e., keycloak that we are trying to use inside our docker network. The port number I am using, 8080 but not 7080 because 7080 is the port which is exposed to the outside world.  Whereas since my gatewayserver service is going to interact with the keycloak service inside our docker network directly using the service name, we need to mention the port number where the keycloak service started inside our docker network. That's why we need to mention the port number 8080 but not the 7080.
 * 2. Created a new service with the name keycloak.
 * 3. For the observability and monitoring related services, I didn't make any changes. I also didn't make any changes on the configserver as well as eurekaserver services. But when it comes to the accounts, cards, loans service I have deleted the port mapping. Previously I used to expose our internal custom ms's to the outside world. And since now, we have deleted the port mapping inside our docker-compose file for these services, our custom services i.e., cards, loans and accounts are not going to be exposed to the outside world.
 *    Only the services which are deloyed inside this same docker network should be able to communicate with our custom services at their respective ports.
 * For all the changes we have done inside the docker-compose.yml file. Copy the same into other profiles as well i.e., qa and dev.
 * Next, start all our containers with the help of docker compose up -d command.  Before running this command make sure all the running instances of your applications in the IDE are stopped. Also in docker. Otherwise, you may run into issue.
 * The services should start successfully and you can also confirm the same by checking in the docker desktop. Especially the new boy in town, KeyClaok container, running at the port number 7080. Coming to the cards-ms, accounts-ms, loans-ms1, and loans-ms containets, you can see that we do not have any port(s) mapping related information because we are not exposing any traffic to the outside world. Anyone who wants to access these services has to go through the gateway server only or has to be inside the same docker network. We can validate this by trying to invoke the direct urls of accounts, cards and even loans ms's. Inside the Microservices postman location, we have accounts, cards as well as loans folders wth direct requests to the respective service. You can open any of the GET APIs like 'build-info' for example. You can see that the url we are trying to invoke is a direct API path i.e., http://localhost:8080/api/build-info. If I try to invoke you should happily get a connection refused error i.e., Error: connect ECONNREFUSED 127.0.0.1:8080. That's because these services are not exposed to the outside world. I mean, there is no application/service running inside my local system at the port 8080.
 *  You can confirm the same for the other ms's as well i.e., cards - http://localhost:9000/api/java-version and loans - http://localhost:8090/api/contact-info. Like this any GET API you can try to invoke. You will get an Error: connect ECONNREFUSED 127.0.0.1:****. This confirms that all our individual ms's like loans, cards and accounts are started withing the docker network but are not exposed to the outside world. As a next step, in order to test the changes related to security, inside the folder gatewayserver_security, I will try to invoke few of the GET APIs i.e., Accounts_GET_PermitAll which has the url i.e., http://localhost:8072/eazybank/accounts/api/contact-info. You should get a successful response. You can also try to invoke Cards_GET_PermitAll and Loans_GET_PermitAll which have the urls i.e., http://localhost:8072/eazybank/cards/api/java-version and http://localhost:8072/eazybank/loans/api/build-info respectively. In both cases you should get a successful response. This confirms that the permit all configurations that we have done inside the resource server are working fine. As a next step, lets try to invoke some of the secured API's i.e., gatewayserver_security/Accounts_POST_NoAuth/http://localhost:8072/eazybank/accounts/api/create - For this request, I am not providing any authentication details as can be seen in the Authorization tab of this postman request.
 *  As expected, happily you should get a 401 unauthorized error. This confirms that the security configurations we enforced as per our requirements are working. Now let's invoke the request gatewayserver_security/Accounts_POST_ClientCredentials/http://localhost:8072/eazybank/accounts/api/create with the help of Client Credentials Grant type flow. In the Authorization tab we have some auth configuration with regards to this Client credentials grant type flow. At first if you click on the Get new access token button, you will get an Authentication failed error. haha.  Reason - when we try to create the KeyClaok service with the help of docker compose, a brand-new container of the KeyClaok is going to be created which simply means my KeyCloak is not going to have any configurations yet i.e.,Custom Client as well as end-user details that we have created previously. That's why we are getting this authentication error. To resolve this issue, and to test our requests, same drill like we were doing before you need to register the respective client, end-user details or both depending on the Grant type that you are following and then happily you should be able to test your requests. And btw in the postman collection i.e., gatewayserver_security, I have named those requests accordingly based on what scenario you are trying to test and even what grant type you are trying follow. So, we always
 *  have to do this because a new container of KeyClaok is created, and we are using an in-memory DB for the KeyCloak as could even be seen with the "command" :"start-dev" configuration of the KeyCloak service inside the docker-compose.yml file. This means, any time you run the docker compose down command followed by a docker compose up command, all your previously created/configured client, end-user details or both depending on the grant type will be lost whenever a new container of KeyCloak is created or whenever a KeyCloak container is deleted. In real production environments, KeyCloak is going to have a supporting DB where it can store all these custom created/configured client, end-user details or both depending on the grant type flow. So, same drill depending on the grant type flow, access your KeyCloak container from the exposed outside world port and then do the required custom configurations/set-up. Then finally you should be able to easily test your requests i.e., gatewayserver_security/Accounts_POST_ClientCredentials, gatewayserver_security/CARDS_POST_ClientCredentials, gatewayserver_security/LOANS_POST_ClientCredentials, gatewayserver_security/Accounts_POST_AuthCode, gatewayserver_security/CARDS_POST_AuthCode, gatewayserver_security/LOANS_POST_AuthCode, and finally gatewayserver_security/Accounts_GET_PermitAll. The drill is the same just as we did before, nothing new. You can do it!!
 * With all these, it is pretty clear that our security related changes are working fine without any issues inside the docker containers as well. In fact, now, our ms's are more secured because we didn't expose loans, cards and accounts ms's to the outside world. Only the gateway server is authorized to connect with the loans, accounts and cards ms's. You should be super clear with all the changes we have done in regard to ms's security. Now you have all the stepping stone knowledge to go and create even better secured security requirements. And if anything you have GitHub repos which are updated every quarter for any enhancements and new stuff in the industry. You are now among one of the proud developers who knows how to secure their ms's.
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
