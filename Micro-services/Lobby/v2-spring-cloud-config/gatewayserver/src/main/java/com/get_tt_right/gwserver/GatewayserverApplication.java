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

/** Update as of 27/05/2025
 * Implementation of log aggregation With the help of Grafana, Loki and Promtail
 * ------------------------------------------------------------------------------
 * 1. Inside the pom.xml files of all our services, change the docker image tag name to V7.
 * 2. Inside the .yml file of gateway server:
 *   As can be seen we have the configuration httpclient.response-timeout set to 2 seconds. Which means my gateway server is going to wait a maximum of 2 seconds to get a response from the actual ms. But since we are going to set up more and more containers inside our local system , definitely inside my local system I will not be able to get the response within 2 seconds due to low availability of memory. So, to avoid any inconveniences inside the local testing, increase this timeout to 10 seconds. With this my gatewayserver is going to wait a maximum of 10 seconds to get a response from the actual ms. And with this we should be able to get any kind of response from the individual ms's and will not face any kind of issues inside the local system regardless of how many containers or supporting components you are trying to set up.
 *    . Hope you have clarity about this change and in future if you are facing any timeout issues, feel free to increase this timeout based upon your local testing requirements.
 * 3. Now, let's go to the official doc of grafana - https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/. If you scroll a bit down, past the architectural diagram proposed by Grafana team, they are going to tell you what are the prerequisite which is, you should have a running docker + docker compose inside your local system. These we already set up long back. Post that, you will see a label , 'To install Loki locally, follow these steps:" where at first they are telling you to Create a directory called evaluate-loki, mkdir evaluate-loki,for the demo environment and make it your current working directory, cd evaluate-loki. But we don't have to follow this because we will try to implement all these changes inside our docker-compose folder present inside our workspace location.
 *    After this, they are asking you to download 3 different .yml files i.e., loki-config.yaml, alloy-local-config.yaml, and docker-compose.yaml. They have given url's for each of these files i.e.,
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/loki-config.yaml -O loki-config.yaml
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/alloy-local-config.yaml -O alloy-local-config.yaml
 *      wget https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/docker-compose.yaml -O docker-compose.yaml
 *    Like this they are trying to extract the 3 files using the same respective names.
 * Now, lets try to understand what is present inside these 3 files.
 * 1. loki-config.yaml  # Check with the previous commit. I have done some docstring around what my instructor was saying
 * 2. alloy-local-config.yaml # Check with the previous commit. I have done some docstring around what my instructor was saying
 * 3. docker-compose.yaml # Check with the previous commit. I have done some docstring around what my instructor was saying
 *
 * In case something wasn't clear, you can use that docstring as an input to ChatGPT and ask for help ofr the unclear parts. Or try to find some blogs around the same.
 * Now, we understand all these 3 yml files and as a next step we need to set them up inside our docker-compose file. which we already have inside our ms's project.
 *Steps
 * ----------
 * 1. Download loki-config.yaml and alloy-local-config.yaml.
 *    - Under the docker-compose folder, create a new folder called observability. Inside it, create another directory called loki - Here we are going to save the files related to the Loki configurations. i.e., loki-config.yaml. Create another directory called alloy - Here we are going to save the files related to the Alloy configurations. i.e., alloy-local-config.yaml. All these configuration files are what we have already discussed previously.
 * 2. Make changes in any of the docker compose files i.e., prod or qa or default. Lets do the changes in the docker-compose file present inside the prod profile. Once we test the changes inside prod profile we can go ahead and copy the changes to qa and default profiles as well. And btw, to many containers will slow down your system. That's why my Instructor defaulted to removing some containers i.e, redis container that we had added previously inside the docker-compose file. We were using it to test the rate limiter pattern. So he feels it is unnecessary to continue with it. But I am retaining it.
 *    He also removed all the depends-on configuration of redis and environment variables configured earlier inside our docker-compose file.
 *    Next, copy the contents present inside the docker-compose file present here, https://raw.githubusercontent.com/grafana/loki/main/examples/getting-started/docker-compose.yaml. Don't copy its networks configurations. Copy everything that is under services except the flog service and paste it under the services inside our docker-compose file. We don't need the flog service because it is a test app provided by the garafana team, and we are not going to be using the same. Everything about the services that we have just copied into our existed docker-compose.yml file, we already discussed.
 *    Next, we need to make some small changes to the services that we have just copied into our existed docker-compose.yml file. i.e.,
 *    +. read service
 *      . Under volumes, we have - ./loki-config.yaml:/etc/loki/config.yaml, Which implies that, copy the loki-config.yml file present under the same folder location as this docker-compose.yml file to the docker container location i.e., /etc/loki/config.yaml. But, right now the loki-config.yml is present in the observability/loki folder, so we need to change the path to something like this - ../observability/loki/loki-config.yaml:/etc/loki/config.yaml
 *      . Also under the read service, we have a networks configuration with the name 'loki' which is not the same as what we had already existed inside the common-config.yml file i.e., get_tt_right. So, change that to get_tt_right. For the aliases tag, leave it as 'loki' because maybe internally, the grafana team maybe using it. Also the content under this networks tag, I am going to still anchor it with the same name which is 'loki-dns'.
 *        With this, we should be good with the read component/service.
 *    +. write service
 *      . Just change the correct path location under the volumes.
 *    + alloy service
 *      . Just change the correct path location under the volumes.
 *      . Under the networks tag, we have hardcoded the name of the network as 'loki'. Instead of this, we need to use the same name that we had already used inside the common-config.yml file i.e., get_tt_right. But don't hardcode it like they've done for 'loki'. Try to refer to the configurations that we have defined inside the common-config.yml file with the service name as, 'network-deploy-service'. So, everything under and including the networks tag, you can replace that with the extends tag i.e.,
 *            extends:
 *               file: common-config.yml
 *               service: network-deploy-service.
 *    + minio service
 *      . We don't have to change anything else, except the networks related configurations by mentioning the extends related configuration.
 *    + grafana service
 *      . We don't have to change anything else, except the networks related configurations by mentioning the extends related configuration.
 *    + backend service
 *      . We don't have to change anything else, except the networks related configurations by mentioning the extends related configuration
 *    + gateway service
 *      . We don't have to change anything else, except the networks related configurations by mentioning the extends related configuration.
 *
 * With this, we have updated the docker-compose.yml file and also copied the configurations related to alloy and loki into their respective folders.
 * 3. Generate docker images specific to this section
 *   . I didn't generate any docker images for this section. Reason, I am only generating new image versions if I make a change in the respective services source codes. So for me,am good so far and our docker-compose.yml file should be okay and ready.
 *   . As of now we have not made any changes inside our ms's related to log aggregation. We are going to achieve log aggregation without making any changes with the help of grafana, loki and alloy.
 * 4. Run docker-compose up -d command from the prod profile.
 *  - All the containers should start successfully and btw, if you went to the docker desktop while just immediately after running this docker compose up -d command, you will see the containers like: minio, write, read , alloy, grafana, backend and even gateway, they started very quickly because all these are very lightweight containers. They are not going to use a lot of CPU inside your local system or inside any docker environment. That's an advantage for us.
 *  - Our individual ms's are going to take some good time in order to start compared to the logging related containers discussed above.
 *  - Once all your containers started successfully - Actually you can confirm this inside your respective ms's logs. You will see a log like, 'Started xxx application in xx seconds (JVM running for xx)' and this means that the application started successfully and it took xx seconds to start.
 *    For some reason if the services are not starting inside your local system due to any reason like low memory or low CPU, in such cases for all healthcheck definitions inside your docker-compose.yml file, you can increase the interval and retries to a higher value i.e., double what is already defined or even triple. With this, you will be giving more time to your containers to get started and to prove their health check status. If you have a higher memory and CPU, then no need for this as everything should work perfectly.
 * 5. Test our individual ms's
 * - We will be testing some few scenarios via postman so that logs can be generated by our ms's. i.e.,
 *    + gatewayserver/FetchCustomerDetails request.
 *      . If using a h2 db, since our containers just started. You won't be getting any data as expected. You will get some 404 not found response. Otherwise, if you are using some persistent db container i.e., mysql, then you will get some data if you had already persisted that via other requests. This you know very well, but it is good to just remind you.
 *        . My instructor is using a h2 db. So, he will be getting some 404 not found response. So he went ahead inside the postman collection for each ms and invoked the respective CREATE endpoints i.e., gatewayserver/eazybank/accounts/api/create, gatewayserver/eazybank/cards/api/create and gatewayserver/eazybank/loans/api/create and then invoked the fetch customer details endpoint i.e., gatewayserver/FetchCustomerDetails.
 * - If you receive a timeout exception -> Means that if response not received within a given defined timeout duration then timeout. It doesn't mean that the request will be killed. The processing will keep getting executed to completion by the service. Only that, it is taking longer than expected to return a response and hence the timeout response.
 *   So anytime you see or experience a timeout response don't pannick!! It is expected based upon your defined timeout duration and the processing time.
 * - If you get a response like, "An error occured, please try after some time or contact support team!!!" -> This means that the circuit breaker is tripping and what you are getting is a fallback response. Which implies that the service execution is not able to get executed. Reason for such: Inside your system, there are too many containers running that are slowing down the request processing capacity of my containers. That's why as the error response says, you should try sending the request one more time. But in production, since you are going to have very big servers such issues you may not experience.
 * Now, based on the requests we have sent, we might have generated lot many requests inside our ms's/ distributed applications. First, the very basic approach if we don't have log aggregation concept. If there is an issue or exception arising inside our services/distributed applications, developers have to visit each of the containers or each of the locations where the container logs are present. This you know! For example on docker desktop, if I go to the container gatewayserver-ms, there you should be able to see all the logs related to this container/gateway-server that got generated due to actions/requests that I performed. In this fashion, the drill will be the same for all the other containers/services i.e., loans, cards, accounts, ...etc. This is going to be a super cumbersome process. We are trying to avoid this with the help of Alloy, Loki and Grafana.
 *  To see the demo using the Grafana, go to the browser link, lh:3000, because grafana is going to start at this port. Why grafana? Like we discussed before, using grafana we can try to search the logs present inside the Loki system/container. So, how is the link between Grafana and Loki established? To see the connection details, if you click on the toggle menu on the top LHS you should be able to see a tab related to Connections. Under it, you will see a Data Sources tab which on clicking by default, a Loki related datasource is created. If you can click on it, you will see several settings details configured. Among them, you should see a label i.e, Connection having a table with row i.e., 'URL*' and some value like http://gateway:3100 using which my Grafana can  connect to the Loki container. Here in the Settings configurations you will see several pieces of information i.e., Connection related like we have discussed,Authentication related details, etc. Some of these details we have mentioned inside our docker-compose file.
 *   - Inside docker-compose file, if you can go to the service details related to the grafana, under the grafana service, if you can scroll to the entrypoint command you can see we are trying to create datasource(s) with the name 'Loki' and type 'loki' and the same 'url' that you can see in the grafana connection details, same to the header, i.e., 'X-Scope-OrgID' and  value i.e., 'tenant1'. with the help of all these configurations i.e.,
 *    datasources:
 *           - name: Loki
 *             type: loki
 *             access: proxy
 *             url: http://gateway:3100
 *             jsonData:
 *               httpHeaderName1: "X-Scope-OrgID"   // Header name/key
 *             secureJsonData:
 *               httpHeaderValue1: "tenant1"  // Header value
 *
 * Using these same datasource configurations as can be seen in the docker-compose.yml file, while my Grafana service is getting created, the datasource details/settings or the Connection details/settings to the Loki container are also getting created automatically. If you don't define this while defining your grafana service in the docker-compose file, then you will be required to manually create these settings/configurations inside your Grafana page by clicking on the 'Add new Connection' tab.
 *  - Since we want to avoid that, we are going to define this datasource settings/configurations while defining our grafana service in the docker-compose file as discussed above.
 * As a next step you can click on the 'explore' nav link in this Grafana page. Here under the 'select label' dropdown, I am going to select a label which is 'container' - This same label we defined inside the 'alloy-local-config.yaml' file. i.e., target_label  = "container". So, all the logs that are scraped by my alloy agent are going to get assigned to the target_label 'container' And the source label is going to be what is the docker container name. This you can also see we have configured inside the alloy-local-config.yaml file. i.e., source_labels = ["__meta_docker_container_name"]
 *  - Once you click on this 'Select label' you can see under the 'Select Value' dropdown, I have all my docker container names. For example, if I want to see the logs related to accounts ms, I can select the same and post tha  can click on the 'Run Query' button, and you will be able to see the logs related to the accounts ms inside your grafana UI. If needed you can also click on the 'Live Streaming' button to see the logs in real-time as they are getting generated as a result of the actions/requests that are going on inside the accounts ms. So that's the power of grafana. From a centralized location, we can see all the logs that are getting generated inside all the containers that we have defined inside our docker-compose file.
 * You should also be able to see information about the 'Logs Volume' - Nothing but at what time do we have too many logs being generated. All these you can be able to see.
 * Apart from 'container' level logs, we can also search for a specific log text inside you respective container logs. For example if you select your gatewayserver container logs, we have so many of them, but I am interested in the logs that contain the string, "eazyBank-correlation-id". For that, select the option 'Line contain" where we have a dropdown just above the "Text to find" text box. We have many other options in this dropdown i.e., Line does not Contain, Line contain regex match, etc. With all these, we are now making our lives eazy. Think  like inside the production you have 1000 of ms's. For all such, you can simply make use of Alloy, Loki and grafana. With the help of Grafana UI, you can search through any number of logs ins any service/distributed application.
 *
 * So far, have you see us anywhere doing any changes inside our ms's? I didn't make even one line change. haha. Everything we have discussed so far around log aggregation or centralized logging is happening automatically with the help of Alloy, Loki and Grafana. You may have a question like, But we are able to use these tools inside our containers, how about inside my local system when I am trying to do local development with the help of IntelliJ IDEA. Ans. For local development, you don't need to have all this complex set-up because, you know that inside your local system you can easily find the logs inside the IDE itself under the console tab and if needed you can leverage break-points and try to debug them. So, this solution whatver we have implemented is not for the local development. Of course for local testing and debugging we have full controll with the hekp of intellij IDEA. That's wjhy this set-up can be much more useful in higher environments like dev, qa and prod environments.
 *  - And of course when you try to set this up in the production environment you may need to take the from your platform team to configure some cloud storage. As of now, all the logs that we generated, are getting saved inside the minio inside our local system itself. The same you can see inside the docker-compose/prod folder where you were running the docker compose up -d command. There you can see a hidden folder is created with the name '.data' and inside we have a 'minio' folder. Inside that we have other folders related to 'loki-dat' and 'loki-ruler'. The same folder which is '.data' we mounted into the docker container using volumes. In you docker-compose.yml file if you can search for the string '.data', you will notice that under minio service definition inside the docker-compose file, we have mounted the local folder '.data/minio' to the container by copying into a folder named '/data'. i.e., ./.data/minio:/data so, whatever logs are saved inside this local folder, './.data/minio' they will get copied/mounted into the minio container folder '/data'.
 *    But in real production applications we can leverage cloud storage systems like Aws S3 or any other cloud systems. That way we can store any amount of logs for any number of ms's.
 * Up-to now, you should be clear with the demo that we have discussed. As of now, we have discussed one of the pillars of observability and monitoring which is logs. I mean, without making any changes inside my ms's we are now able to store all our logs inside a centralized location. This will help our developer in understanding the inner state of our ms's. We will be focusing on metrics from the next session.
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
