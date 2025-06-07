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

/** Update as of 01/06/2025
 * Managing metrics & monitoring with Actuator, Micrometer, Prometheus & Grafana
 * ------------------------------------------------------------------------------------------
 * As of now, we have discussed one of the pillars of observability and monitoring which is Logging. We will be discussing the second pillar of observability and monitoring which is Metrics. Do you think by using logs alone we can monitor our applications? Of course not, because with the help of event logs we can only try to understand what happened under a specific scenario or under a specific method. With the help of the logs, we can't understand what is the overall health of my accounts ms or any other ms. So to properly monitor our ms's we need to understand metrics details like, what is the CPU usage, what is the memory usage, heap dump usage, threads, connections usage, error requests, etc. Like this, there are many metrics available to properly monitor our distributed applications. That's why we need to make sure we are also collecting the metrics and using the same we need to build dashboards so that operations team can always monitor our ms's.
 * Like we discussed before, metrics represents numerical measurements of an application's performance. These metrics we need to collect and aggregate at regular intervals so that we can use them to monitor the application's health and performance and if needed we can set alerts and notifications when a specific when a specific threshold is exceeded. How to achieve this? With the help of components like; Spring Boot Actuator, Micrometer, Prometheus & Grafana. Let's try to understand the role played by each of these components. Spring boot actuator - Responsible to generate the metrics inside a ms instance. As of now, we have added Spring boot actuator in all our ms's. Whenever we add this dependency inside our Spring boot ms. My Spring boot ms is going to expose all the metrics related to the application, the same we can also try to understand by navigating to the path/endpoint which is /actuator/metrics. Under this metrics actuator path, there will be many metrics that can help you to monitor your ms instances. These metrics that are exposed by the actuator are going to help you monitor your ms instance(s).
 *  - But do you think it is a feasible option for me to navigate to all the actuator url's of all my ms instances everytime in order to understand the overall of a particular ms instance? NO! that is going to be a super tedious job. If I have 100 different ms's running with different number of instances, navigating each of these instance actuator url's is going to be a super tedious job. That's why we need to make sure there is a component which is responsible to extract and aggregate all the metrics from the ms's instances running inside my ms network. We already have such a component in the open-source world which is Prometheous. But Prometheous cannot understand the metrics provided by the actuator because actuator exposes the metrics information in a json format which we can easily understand as humans. Since the same format cannot be understood by the Prometheous, we need to use micrometer. So, what is the purpose of this micrometer? It will automatically expose the /actuator/metrics data into a format that a particular monitoring system can understand. We as developer need to add vendor specific micrometer dependency inside our application(s) and post that, the micrometer is going to take care of exposing the metrics information in a format that is going to be easily consumed by the monitoring system.
 *    Here since we decided to use Prometheous, we need to add a dependency related to micrometer and Prometheous and micrometer is going to take care of exposing all your metrics in a format that Prometheous can understand. This micrometer is very similar to SLF4J, which is a facade pattern that will help us to implement a lot of logging frameworks. Just like SLF4J for logging, we can safely assume that micrometer is for the metrics. Same drill, different hustle. haha! This is the link to the micrometer website - https://micrometer.io/. On opening it, you can see that it is going to act as vendor neutral application observability facade. What is facade? We have a pattern facade design pattern whose purpose - It is going to deploy a front facing interface that will handle a lot of complexity behind the scenes. We as clients can use this front-facing interface to make our lives easy. So, behind the scenes, this Vendor-neutral application observability facade can expose the metrics based upon different vendor specific implementations. One such vendor is Prometheus. If you scroll down on that page, you can see it is going to support many other vendors like; AppOptics, Azure Monitor, Netflix Atlas, CloudWatch, Datadog, Dynatrace, Elastic, Ganglia, Graphite, Humio, Influx/Telegraf, JMX, KairosDB, New Relic, OpenTelemetry Protocol (OTLP), Prometheus, SignalFx, Google Stackdriver, StatsD, and Wavefront. So, beind the scenes it is going to support all those vendors.
 *    As a developer, I don't have to worry how my micrometer is going to expose the metrics information based upon the product I choose - whether Prometheous or CloudWatch or whatever other product. I just need to add the dependency of micrometer that is specific to a vendor, and I am good to go. Behind the scenes, micrometer is going to do a lot of job for you.  This is very similar to SLF4J facade pattern. SLF4J - Simple Logging Framework for Java. Inside Java, there are many Logging frameworks, Like with the help of java.util.Logging we can do logging, very similarly we have LOG4J, LogBack, etc. Like this, there are many logging frameworks and sometimes we as a developer if we want to use many logging frameworks inside our applications, we don't have to handle all such complexity, instead we can simply leverage the SLF4J which is going to act as facade for all your logging frameworks. With the help of SL4J we can simply implement any kind of logging framework inside your web applications without worrying about the complexities involved. With all these discussion, you should be clear about what micrometer is. Now, once my micrometer exposes the metrics information, Prometheus is going to regularly export that metrics information from my individual ms's containers. Using the same metrics that my Prometheus exported, it is going to consolidate all of them into a single place. This is very similar to Loki. The Job of Loki is that with the help of Loki, we are trying to aggregate the logs whereas with the help of Prometheus we are trying to aggregate the metrics of all your containers and ms's. So, lets try to understand more details about Prometheus.
 *    Prometheus - Here is a link to their website,https://prometheus.io/
 *    --------------------------------------------------------------------
 *    . It is an open-source product which is going to help you to collect all the metrics information from the respective containers and ms's. We can also try to build dashboards, graphs which will help us to monitor our ms's. On their website you can clearly see this information. We will see a demo of prometheus in the coming sessions but for now know that, it is going to collect metrics information from the individual services and is going to store all of them in a single location which can help us monitor easily with the help of the UI provided by the Prometheus.
 * - Next, we should integrate Prometheus with Grafana. You may have a question like, what is the need of Grafana when we already have Prometheus which will help us to monitor our ms's. Yes I agree with you but Prometheus has certain limitations. We cannot build very complex dashboards, and we cannot create alerts and notifications with Prometheus alone. When we integrate prometheus with grafana, we can build a lot of dashboards and apart from these we can also create alerts and notifications that's we need to make sure we are integrating Prometheus with Grafana. Just like how e integrated Loki with grafana. Very similarly we are going to integrate Prometheus with Grafana and using the metrics information present inside the prometheus we are going to build the dashboards that will help us to monitor our ms's.
 * Up to now, you should be clear about what is the role played by each of the components like; Spring Boot Actuator, Micrometer, Prometheus & Grafana. Next, we will be implementing these components inside our ms's and visualize how they are going to help us to monitor our applications.
 *
 * Set up of micrometer inside our ms's - We will try to implement the changes related to the micrometer inside our ms's.
 * -----------------------------------
 * As developers, we don't need to write a lot of business logic/Java logic. We just simply need to add the dependency related to micrometer and prometheus inside our applications and with that we should be good. So, in the pom.xml of accounts ms, just after the actuator dependency, I am going to add a new dependency with a group id of io.micrometer and artifact id of micrometer-registry-prometheus. By simply adding this dependency,we are telling to the micrometer to expose the actuator metrics in a format that my prometheus can understand. In future if your organization decided to go with some other monitoring system other prometheus, then you just simply need to change the artifact id and with that you should be good. The same dependency you can copy and mention inside the pom.xml file of the other ms's i.e., cards, loans, configserver, gatewayserver, eurekaserver, etc.
 * As a next step, we are introducing one new property inside the application.yml. Let's start with the accounts ms application.yml file. Under the management parent property, I am going to add a child property that is related to metrics i.e., metrics.tags.application:${spring.application.name}. Its value is the application name. ${spring.application.name} means we are trying to assign a value from the property which is spring.application.name. In this same application.yml file you can see this property and its respective value. So at runtime the value of this property will be accounts and will be assigned to our just added property i.e., metrics.tags.application. You may have a question like what is the purpose of this property. With the help of this property, we are telling to the micrometer and prometheus that, " Please group all my metrics related to the accounts ms under an application name called accounts." This is going to help you to identify the metrics of each of the ms's, otherwise you cannot really identify which metrics belongs to which ms's. So yea, that's the purpose of this property. Add this same property inside other ms's i.e., cards ms, loans ms, configserver ms, gatewayserver ms, eurekaserver ms, etc.
 * Next, do a clean build for all your services and try to start all your services starting with - configserver, eurekaserver, accountsdbaccounts, cardsdb, cards, loansdb, loans and finally gatewayserver. As a next step we can try to understand the actuator url's of all the ms's and visualize how the micrometer is exposing the metrics information. For the same inside the browser open the actuator url of accounts ms.
 * My accounts ms started at the port 8080
 * --------------------------------------------
 * http://localhost:8080/actuator/metrics - So after mentioning the base path which is /actutor make sure to mention the path which is /metrics as can be seen in the url. If you access this in the browser, you should be able to see all the metrics exposed by my actuator. i.e.,
 * {
 *   "names": [
 *     "application.ready.time",
 *     "application.started.time",
 *     "disk.free",
 *     "disk.total",
 *     "executor.active",
 *     "executor.completed",
 *     "executor.pool.core",
 *     "executor.pool.max",
 *     "executor.pool.size",
 *     "executor.queue.remaining",
 *     "executor.queued",
 *     "hikaricp.connections",
 *     "hikaricp.connections.acquire",
 *     "hikaricp.connections.active",
 *     "hikaricp.connections.creation",
 *     "hikaricp.connections.idle",
 *     "hikaricp.connections.max",
 *     "hikaricp.connections.min",
 *     "hikaricp.connections.pending",
 *     "hikaricp.connections.timeout",
 *     "hikaricp.connections.usage",
 *     "http.client.requests",
 *     "http.client.requests.active",
 *     "http.server.requests.active",
 *     "jdbc.connections.active",
 *     "jdbc.connections.idle",
 *     "jdbc.connections.max",
 *     "jdbc.connections.min",
 *     "jvm.buffer.count",
 *     "jvm.buffer.memory.used",
 *     "jvm.buffer.total.capacity",
 *     "jvm.classes.loaded",
 *     "jvm.classes.unloaded",
 *     "jvm.compilation.time",
 *     "jvm.gc.concurrent.phase.time",
 *     "jvm.gc.live.data.size",
 *     "jvm.gc.max.data.size",
 *     "jvm.gc.memory.allocated",
 *     "jvm.gc.memory.promoted",
 *     "jvm.gc.overhead",
 *     "jvm.gc.pause",
 *     "jvm.info",
 *     "jvm.memory.committed",
 *     "jvm.memory.max",
 *     "jvm.memory.usage.after.gc",
 *     "jvm.memory.used",
 *     "jvm.threads.daemon",
 *     "jvm.threads.live",
 *     "jvm.threads.peak",
 *     "jvm.threads.started",
 *     "jvm.threads.states",
 *     "logback.events",
 *     "process.cpu.time",
 *     "process.cpu.usage",
 *     "process.start.time",
 *     "process.uptime",
 *     "rabbitmq.acknowledged",
 *     "rabbitmq.acknowledged_published",
 *     "rabbitmq.channels",
 *     "rabbitmq.connections",
 *     "rabbitmq.consumed",
 *     "rabbitmq.failed_to_publish",
 *     "rabbitmq.not_acknowledged_published",
 *     "rabbitmq.published",
 *     "rabbitmq.rejected",
 *     "rabbitmq.unrouted_published",
 *     "spring.integration.channels",
 *     "spring.integration.handlers",
 *     "spring.integration.sources",
 *     "system.cpu.count",
 *     "system.cpu.usage",
 *     "tomcat.sessions.active.current",
 *     "tomcat.sessions.active.max",
 *     "tomcat.sessions.alive.max",
 *     "tomcat.sessions.created",
 *     "tomcat.sessions.expired",
 *     "tomcat.sessions.rejected"
 *   ]
 * }
 *
 * Suppose if you want to understand a specif metric like system.cpu.usage,you can append this in the url and try to access it in the browser. i.e., http://localhost:8080/actuator/metrics/system.cpu.usage
 * {
 *   "name": "system.cpu.usage",
 *   "description": "The \"recent cpu usage\" of the system the application is running in",
 *   "measurements": [
 *     {
 *       "statistic": "VALUE",
 *       "value": 1
 *     }
 *   ],
 *   "availableTags": [
 *     {
 *       "tag": "application",
 *       "values": [
 *         "accounts"
 *       ]
 *     }
 *   ]
 * }
 * Like this you can see I am getting the details about the system.cpu.usage metric of accounts application/service.As of now the value is 1 which indicates that the cpu usage is 1%.
 * If I want to understand process.uptime metric, I can access it in the browser by appending this in the url i.e., http://localhost:8080/actuator/metrics/process.uptime
 * {
 *   "name": "process.uptime",
 *   "description": "The uptime of the Java virtual machine",
 *   "baseUnit": "seconds",
 *   "measurements": [
 *     {
 *       "statistic": "VALUE",
 *       "value": 915.252
 *     }
 *   ],
 *   "availableTags": [
 *     {
 *       "tag": "application",
 *       "values": [
 *         "accounts"
 *       ]
 *     }
 *   ]
 * }
 * Like my Accounts service is up and running without any issues from last 915.252 seconds. If you try to refresh the url, the number will change of course.
 * In this fashion, we can understand each of the metric of each of the services we have in our ms network. Now, do you think it is going to be feasible to look all these metrics manually like we have done for system.cpu.usage and process.uptime by using the port number of a service instance + the actuator specific metric url? NO! That is going to be a super tedious job. That's why we are going to expose all these metrics information to prometheus.
 * When we add the dependency related to micrometer and prometheus inside our applications/ms's - like we have already discussed and done, the micrometer is going to expose a url which is http://localhost:8080/actuator/prometheus. We can access this url in the browser and see all the metrics information for a particular service instance running inside our ms network. All these information is present in a format which prometheus can understand. A lot of information you will see.
 *  - What will happen is, my prometheus is going to invoke the api path like http://localhost:8080/actuator/prometheus for every 5 seconds or every 10 seconds or every 1 minute based upon your configurations inside the prometheus. It will do this for all the ms instances present inside your ms network. You can validate for other ms's as well i.e., 8090 for loans, 9000 for cards, 8070 for eurekaserver, 8071 for configserver, 8072 for gatewayserver, etc. and so on. With this, we can confirm that all our ms's are exposing the actuator metrics information in a format which prometheus can understand with the help of micrometer. So, up to now you should have crisp clarity of all the changes made so far.
 *
 * Set up of Prometheus inside microservices
 * ------------------------------------------
 * - As of now, our ms's are exposing the metrics information in a format which prometheus can understand. Now, we need to set up prometheus inside our ms's and integrate the same with grafana and post that we can also see some demo with the help of docker-compose. 1st stop all the running instances you local system. Then open the observability folder that we have under the docker-compose folder in our project. As of now, we have defined the configurations related to Loki and Alloy under the folders loki and alloy respectively. Very similarly we will have to create a new folder which is specific to prometheus. Inside this folder,I am going to paste a prometheus configuration related file. i.e., prometheus.yml. Now let's try to understand what is present inside this file:
 * global:    # So here we are making some global configurations for our prometheus. With the below scrape_interval and evaluation_interval, I am telling to my prometheus please fetch the metrics from the individual ms containers every 5 seconds and using the same metrics, try to evaluate and try to show the metrics inside the prometheus dashboards for every 5 seconds. This means that the metrics wil be fetched every 5 seconds and using the same metrics, the data or the graphs inside the prometheus is going to be re-evaluated after every 5 seconds. So those are the global configurations.
 *   scrape_interval:     5s # Set the scrape interval to every 5 seconds.
 *   evaluation_interval: 5s # Evaluate rules every 5 seconds.
 *
 * # Now after defining the global configurations, prometheus next question will be what are the details of ms's instances using which I can fetch the metric details. For the same we need to define the below scrape configs under which we can define any number of jobs. Each job will have details like what is the job name, what is the metrics path, what is the static configs - under which we will be defining the targets.
 * scrape_configs:
 *   - job_name: 'accounts'   #  So based upon the ms name, accordingly I have mentioned the job name
 *     metrics_path: '/actuator/prometheus'  # Coming to the metrics path, since the actuator and micrometer are going to expose the metrics information using the path /actuator/prometheus, we need to mention the same.
 *     static_configs:  # Here we need to mentin what are the list of targets/instances running under accounts ms. As of now we only have a single instance which is running at the port 8080 and here I am not mentioning the localhost because inside the docker-compose.yml file, the service name of accounts ms we have mentioned as accounts hence the configuration of target coming out as: 'accounts:8080' Reason: We are going to start prometheus using docker compose inside the same network and my prometheus can interact with my ms instances base upon the service name(s)
 *       - targets: [ 'accounts:8080' ]
 *   - job_name: 'loans'
 *     metrics_path: '/actuator/prometheus'
 *     static_configs:
 *       - targets: [ 'loans:8090' ]
 *   - job_name: 'cards'
 *     metrics_path: '/actuator/prometheus'
 *     static_configs:
 *       - targets: [ 'cards:9000' ]
 *   - job_name: 'gatewayserver'
 *     metrics_path: '/actuator/prometheus'
 *     static_configs:
 *       - targets: [ 'gatewayserver:8072' ]
 *   - job_name: 'eurekaserver'
 *     metrics_path: '/actuator/prometheus'
 *     static_configs:
 *       - targets: [ 'eurekaserver:8070' ]
 *   - job_name: 'configserver'
 *     metrics_path: '/actuator/prometheus'
 *     static_configs:
 *       - targets: [ 'configserver:8071' ]
 *
 * So yea, these kind of configurations you can easily identify inside the official documentation of prometheus. So, once we define these configurations related to prometheus. As a next step I am going to open the docker-compose.yml file present inside the prod profile. As of now, inside this docker-compose.yml file, we have set up loki, grafana, alloy , minio, etc. All the services we have set up. So just above the grafana, create a new service related to prometheus as shown below.
 * prometheus:  # I am trying to create a new service name with the name prometheus
 *     image: prom/prometheus:v3.1.0  # This is the image it is going to use.
 *     container_name: prometheus  # Container name
 *     ports:
 *       - "9090:9090"  # Port mapping
 *     volumes:  # Coming to the volumes, whatever prometheus.yml file we have created previously, we are trying to copy that into the docker container at the /etc/prometheus/ location so that during the startup my prometheus container is going to look for this .yml file and based upon the configurations that we have mentioned in it the prometheus server is going to be set-up.
 *       - ../observability/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
 *     extends: # And since we need to make sure we need to start up the prometheus in the same server where the other ms's and components started, I am defining the below extends configurations.
 *       file: common-config.yml
 *       service: network-deploy-service
 *
 * Like this, we have prometheus setup inside our docket-compose.yml file. As a next step, we need to establish a link between prometheus and grafana. If you scroll to the grafana service definition, previously under the 'entrypoint' definition we are trying to create a file with the name 'ds.yml' and just after the creation of this ds.yml file we have mentioned datasource definition(s) related to loki. Very similarly we need to make sure we mention prometheus related 'datasource' related details inside the ds.yml. Mentioning all those datasource related configurations while defining the grafana service in our docker-compose.yml file is going to make the docker-compose.yml file very lengthy. So, what to do?
 *   - Create a folder inside the observability folder with the name grafana. Under this grafana folder, I am going to paste a new yml file which has datasource details.i.e. 'datasource.yml'. Below, lets try to understand what is there inside this datasource.yml file.
 *   apiVersion: 1  # First I am trying to mention this which is a standard that we need to follow.
 *
 * deleteDatasources:  # Post that, I am going to delete any existing datasource's with the names: prometheus, loki, tempo. As of now ignore tempo, we will discuss it later.
 *   - name: Prometheus
 *   - name: Loki
 *   - name: Tempo
 *
 * datasources:  # Post that, under the datasources definition, I am going to create a new datasource with the name: 'Prometheus'. We also have created other datasources related to loki and tempo.
 *   - name: Prometheus
 *     type: prometheus  # If you try to understand, under the prometheus datasource, we have mentioned the type of datasource as prometheus.
 *     uid: prometheus   # And I am trying to give some unique id for this datasource connection.
 *     url: http://prometheus:9090 # Mentioning the url where my prometheus started. This you can verify from your docker-compose.yml file prometheus service definition ports mapping. Since grafana service is going to start in the same network as 'prometheus' service, I am mentioning the url as 'http://prometheus:9090' and not 'http://localhost:9090'. Post this url definition, we need to mention the below other properties which you can configure/define further based upon your own project requirements.
 *     access: proxy
 *     orgId: 1
 *     basicAuth: false  # Here I am trying to disable all authentication stuff.
 *     isDefault: false
 *     version: 1
 *     editable: true
 *     jsonData:
 *       httpMethod: GET
 *   - name: Tempo
 *     type: tempo
 *     uid: tempo
 *     url: http://tempo:3100
 *     access: proxy
 *     orgId: 1
 *     basicAuth: false
 *     isDefault: false
 *     version: 1
 *     editable: true
 *     jsonData:
 *       httpMethod: GET
 *       serviceMap:
 *         datasourceUid: 'prometheus'
 *   - name: Loki  # Loki datasource related configurations/definitions. As of now, these same loki datasource connection related definitions/configurations we have inside the docker-compose.yml file, like if you go and check the 'grafana' entrypoint command, we are trying to create a directory i.e., 'datasources' and inside the directory, we are trying to create a file with the name 'ds.yml'. And post that we are trying to mention the loki related datasource configurations inside this 'ds.yml' file. Since we are already mentioning all this inside the datasource.yml file in our new approach, We can remove all the the configuration under the entrypoint command starting from the line 'mkdir -p /etc/grafana/provisioning/datasources' to  the line 'httpHeaderValue1: "tenant1"'.
 *     type: loki
 *     uid: loki
 *     access: proxy
 *     orgId: 1
 *     editable: true
 *     url: http://gateway:3100
 *     jsonData:
 *       httpHeaderName1: "X-Scope-OrgID"
 *       derivedFields:
 *         - datasourceUid: tempo
 *           matcherRegex: "\\[.+,(.+),.+\\]"
 *           name: TraceID
 *           url: '$${__value.raw}'
 *     secureJsonData:
 *       httpHeaderValue1: "tenant1"
 *
 * As of now, these same loki datasource connection related definitions/configurations we have inside the docker-compose.yml file, like if you go and check the 'grafana' entrypoint command, we are trying to create a directory i.e., 'datasources' and inside the directory, we are trying to create a file with the name 'ds.yml'. And post that we are trying to mention the loki related datasource configurations inside this 'ds.yml' file. Since we are already mentioning all this inside the datasource.yml file in our new approach, We can remove all the the configuration under the entrypoint command starting from the line 'mkdir -p /etc/grafana/provisioning/datasources' to  the line 'httpHeaderValue1: "tenant1"'.
 *  - After removing all those configurations, we need to make sure we are providing the information about this 'datasource.yml' file that we have just created inside the grafana package. For the same I am going to create some volumes related configurations. i.e.,
 *    volumes:
 *       - ../observability/grafana/datasource.yml:/etc/grafana/provisioning/datasources/datasource.yml
 *   Like this using the volumes related configurations, I am trying to copy the 'datasource.yml' file into the grafana container at the '/etc/grafana/provisioning/datasources' location. So, grafana service is going to look for this 'datasource.yml' file at the '/etc/grafana/provisioning/datasources' location. and based upon the details mentioned inside it, it is going to set up the connection details for the loki, prometheus and tempo datasources.
 * Up to now, we have made the required changes inside the docker-compose.yml file to set-up prometheus and integrate the same with grafana. Next, we will try to visually see the demo of prometheus and grafana
 *
 * Demo of prometheus
 * --------------------
 * For the same we can try to start all our containers with the help of docker-compose file. But do you think we can straight away start our containers? haha Of course we cannot start our containers directly because as of now the micrometer related code changes that we have done inside the application.yml files and pom.xml files of our services/ms's are present inside our local workspace only. To test these changes with docker-compose file, we need to make sure we are re-generating the docker images to adapt to the latest changes we have done. So for accounts, cards and loans ms's, I edited the pom.xml files image tag to V7. For configserver, eurekaserver to V2 and for gatewayserver to V3. Then finally generated the docker images. These changes you also have to adopt inside your docker-compose.yml file.
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
