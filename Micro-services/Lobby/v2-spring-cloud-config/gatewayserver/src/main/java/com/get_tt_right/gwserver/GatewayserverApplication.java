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

/** Update as of 20/09/2025
 * Demo on Using/leveraging Apache Kafka to implement Event Driven Ms's inside our ms's inside Docker environment
 * ----------------------------------------------------------------------------------------------------------------
 * Now, we tested everything inside the local system, as a next step we need to test the things inside the docker environment. I will generate new containers for Accounts and Message ms's i.e., V11 and V2 respectively. We will also be making some changes inside the docker-compose.yml file which we will later execute and finally validate the entire changes inside the docker environment.
 * Docker file changes
 * -------------------
 * 1. Update the message and accounts services to use the latest images.
 * 2. Introduce the Apache Kafka service details. 1 or 2 years ago, my instructor encountered a problem. He validated the Apache Kafka website to understand if there is any information to set up kafka with the help of docker, unfortunately he didn't find anything. At your free time you can try validate and see if you will find anything. Post that, after doing some research, he found some information on how to set up Apache Kafka with the help of docker and docker compose. The information you can find in the GitHub repo which is maintained by bitnami - https://github.com/bitnami. What is bitnami? It is a kind of marketplace where they are going to help you to set up any kind of application in any kind of environment like Cloud, Docker, K8S, etc. You can check more information about them here - https://bitnami.com/
 *   They are a very trusted company/community as they are also supported by the VMWare. That's why we can safely use/leverage the docker compose instructions provided by them.
 *   The url details you can also always find them in your instructors GitHub repo. For example the url to the kafka image definition inside a docker compose file - https://github.com/bitnami/containers/blob/main/bitnami/kafka/docker-compose.yml
 * The format of that yml file is very familiar to us, first they are trying to create a service with the name 'kafka', they have also defined/provide the image details, ports mapping, there are also some volumes configurations, etc. i.e.,
 * # Copyright Broadcom, Inc. All Rights Reserved.
 * # SPDX-License-Identifier: APACHE-2.0
 *
 * services:
 *   kafka:
 *     image: docker.io/bitnami/kafka:4.1
 *     ports:
 *       - "9092:9092"
 *     volumes:
 *       - "kafka_data:/bitnami"
 *     environment:
 *       # KRaft settings
 *       - KAFKA_CFG_NODE_ID=0
 *       - KAFKA_CFG_PROCESS_ROLES=controller,broker
 *       - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093
 *       # Listeners
 *       - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093
 *       - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://:9092
 *       - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
 *       - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
 *       - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
 * volumes:
 *   kafka_data:
 *     driver: local
 *
 * Regarding the volumes configurations, volumes:
 *                                         - "kafka_data:/bitnami",
 * at the end of that compose file, you can see, volumes:
 *                                               	kafka_data:
 *                                                  	driver: local
 * that, with the help of this volumes, they are trying to create a folder 'kafka_data' inside your local system
 * The same they are trying to map to the folder, '/bitnami', which is prent inside the docker container. That means that the kafka that we are going to set up with this docker compose file, it is going to store all the data inside your local system in the folder named as 'kafka_data'
 * After these volumes configurations, we have some environment details which we need to follow i.e.,
 *  environment:
 *       # KRaft settings
 *       - KAFKA_CFG_NODE_ID=0
 *       - KAFKA_CFG_PROCESS_ROLES=controller,broker
 *       - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093
 *       # Listeners
 *       - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093
 *       - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://:9092
 *       - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
 *       - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
 *       - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
 * So, by taking this information, I have update our docker-compose files for all the environments i.e., prod, qa,default. You can check that out.
 * In our docker compose files, previously we had rabbit related service, my instructor deleted that. But for me I am not going to delete due to the reason we discussed previously in details in the previous commit.
 * Once you have defined this Kafka related information, we need to make sure, we are tagging this kafka service to the same network where we are trying to start all the other services.
 * Now lets update the accounts service definition. We initially had a depends-on rabbit configuration. My instructor deleted that. I didn't.
 * After that, I then created a new environment variable i.e., SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS: "kafka:9092". The same environment variable you also need to create for the message service definition. And don't forget to also update the image tags for the 2 services.
 * Yes, those are the only changes my instructor did inside the docker compose file. As a next step, I can try to start all my containers with the help of docker compose up command. Before that, I need o stop all the running instances and containers/service/applications inside my local system(IDE). This you already know. We should also stop the running local apache kafka server by Ctrl + C and then 'Y'. Also stop the running container of keycloak and any other containers in the docker desktop because behind the scenes my docker compose is going to start/spin new containers.
 * Once you have stopped all the running containers and running servers inside your local system, as a next step, run the docker compose up -d command. After a couple of mins, all your containers should start successfully! As a next step, we need to set up the client details inside the keycloak
 *
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
