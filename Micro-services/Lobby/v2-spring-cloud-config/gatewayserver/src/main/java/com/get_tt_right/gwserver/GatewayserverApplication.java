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
 * As a next step, to the gatewayserver service definition, mention the depends-on after the ports, then mention all the dependent services with the condition service-healthy. With these changes, we should be good. Now run the docker compose up -d command and see if everything goes up properly. This will start all our containers and the very first service that will start is the configserver, then eurekaserver, accounts, loans and cards services. Once all my individual services health is good then my gatewayserver is going to start towards the end.
 *
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
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
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
