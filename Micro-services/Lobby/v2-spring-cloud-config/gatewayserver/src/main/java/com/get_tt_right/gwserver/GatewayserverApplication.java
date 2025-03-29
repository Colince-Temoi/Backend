package com.get_tt_right.gwserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/** Update as of 26/3/2025
 * Implementing Cross-Cutting Concerns Tracing and Logging using Gateway server
 * -----------------------------------------------------------------------------
 * Now, lets try to make changes inside the individual ms's to accept the request header that my gateway is going to forward along with the request.
 * For the same, lets 1st go into the Accounts ms. Check the detailed docstring of whatever changes we will be doing their in the CustomerController class.
 *  Okay we have 2 controllers inside the Accounts ms. One is the CustomerController and the other is the AccountsController. We don't have to make the changes in all the APIs available inside our individual ms's, instead we can take a single API that is going to travel all the ms's and such an API we have inside the CustomerController class.
 *  Whenever my external clients are trying to invoke my API path which is /fetchCustomerDetails, internally my Accounts ms will at some point forward the request to Loans and Cards ms's as well. That's why let's try to make the changes related to eazybank_correlation_id/trace id for the API path /fetchCustomerDetails. If you are interested you can do for the remaining APIs as well that we have inside all our individual ms's.
 *  But for now, lets learn with one API so that you can get clarity on implementing this business requirement then going forward everything will be simple and same for the remaining APIs.
 *
 *  Check the customer controller class for a docstring for more details.
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
