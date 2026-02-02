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


/** Update as of 29/01/2026
 * Introduction to Server-Side Service Discovery and Load balancing
 * -------------------------------------------------
 * # Check slides for theoretical discussions.
 * How to Set up Discovery Server in K8s cluster using Spring Cloud K8S
 * ---------------------------------------------------------------------
 * To get started with the Server-Side Service discovery and load balancing inside K8S cluster, we need to take help from one of the Spring Cloud projects which is Spring Cloud K8S - https://spring.io/projects/spring-cloud-kubernetes. This is the project which is going to help you to set up Discovery Server whenever you are using K8S inside your production deployments. We are going to use Libraries from this project only to set up the Server Side service discovery and Load Balancing. By default, your K8S cluster is not going to have any Service Discovery and Service registration Server. So, it is our responsibility to set up some Service Discovery Server inside K8S just like how we did for Eureka-Server. To get started around this, there is a blog which is written by the Spring Cloud K8S team itself - https://spring.io/blog/2021/10/26/new-features-for-spring-cloud-kubernetes-in-spring-cloud-2021-0-0-m3. They have written this somewhere in 2021 when they first released this Service Discovery and registration capabilities inside this Spring Cloud K8S project. Inside this blog, they have clearly highlighted what are the steps that we need to follow to set up the Discovery Server inside K8S cluster. If you scroll down a bit, you will see they have given a K8S manifest file that we can use to set up this discovery server inside the K8S cluster. We are going to copy and use the same, and we are going to make few modifications to this manifest file before we try to install it inside our local K8S cluster.
 * Inside our workspace location, we are going to create a new folder which is v3-spring-cloud. Inside this, we are going to write all the code that we are going to discuss. Inside our K8S folder, I am going to create a K8S manifest file that is going to help us to set up the Discovery Server inside the K8S cluster. This manifest file is named as kubernetes-discoveryserver.yml. You can create this file from the terminal i.e., touch kubernetes-discoveryserver.yml. Open it inside Vs Code and paste the contents that you copied from the Spring Cloud K8s blog as shown and in details discussed below:
 * ---
 * apiVersion: v1 # They have mentioned the apiVersion as V1
 * kind: List # And the kind as List. Whenever you define kind as 'List' inside your k8s manifest file, means you can create any number of k8s objects under the 'items' configuration as shown below.
 * items:
 *   - apiVersion: v1
 *     kind: Service # The very first item that they are trying to create is of kind/type 'Service' object. That's why they have mentioned 'kind: Service'
 *     metadata:
 *       labels:
 *         app: spring-cloud-kubernetes-discoveryserver # Under the metadata labels app they also gave a name to this service i.e., spring-cloud-kubernetes-discoveryserver
 *       name: spring-cloud-kubernetes-discoveryserver
 *     spec:
 *       ports: # Now under the specifications ports,
 *         - name: http
 *           port: 80 # the port that is going to be exposed to the other ms's is 80
 *           targetPort: 8761 # Whereas it is going to start internally at the port number 8761
 *       selector:
 *         app: spring-cloud-kubernetes-discoveryserver
 *       type: ClusterIP # And the 'Service' type is going to be ClusterIP. That's why it is not going to create any issues for my KeyCloak server because as of now we are also exposing our KeyCloak server at the port 80 but with the Service type as LoadBalancer. Since the type of this Discovery server is ClusterIP, we should not experience any issues for my KeyCloak service.
 *   - apiVersion: v1
 *     kind: ServiceAccount # After the 'Service' k8s object discussed above, they are also trying to create a 'ServiceAccount' k8s object.
 *     metadata:
 *       labels:
 *         app: spring-cloud-kubernetes-discoveryserver
 *       name: spring-cloud-kubernetes-discoveryserver # The 'ServiceAccount' object name is going to be spring-cloud-kubernetes-discoveryserver
 *   - apiVersion: rbac.authorization.k8s.io/v1
 *     kind: RoleBinding # To this 'ServiceAccount' object, they are trying to do the role binding as seen from this 'RoleBinding' k8s object configuration. Previously we already discussed in details what these k8s objects are i.e., Role, RoleBinding and ServiceAccount. So, with the help of this RoleBinding k8s object configuration, we are going to bind a 'Role' k8s object to the 'ServiceAccount' k8s object configured below and above respectively. That's why under the roleRef configuration below, you can visualize that we have mentioned the 'subjects' configurations/definitions where we have mentioned/defined the 'kind' as 'ServiceAccount' and this, 'spring-cloud-kubernetes-discoveryserver', is the name of the 'ServiceAccount' object. And what is the role that we are trying to bind here? As can be visualized from the 'roleRef' definition, the role that we are trying to bind here is the 'namespace-reader' role. If you can scroll down, you should be able to see that there is a k8s object created of kind/type 'Role' with the name 'namespace-reader'.
 *     metadata:
 *       labels:
 *         app: spring-cloud-kubernetes-discoveryserver
 *       name: spring-cloud-kubernetes-discoveryserver:view
 *     roleRef:
 *       kind: Role
 *       apiGroup: rbac.authorization.k8s.io
 *       name: namespace-reader
 *     subjects:
 *       - kind: ServiceAccount
 *         name: spring-cloud-kubernetes-discoveryserver
 *   - apiVersion: rbac.authorization.k8s.io/v1
 *     kind: Role
 *     metadata:
 *       namespace: default
 *       name: namespace-reader
 *     rules: # Inside this role,namespace-reader, we have defined the rules that this particular role is going to have whenever it is going to be bound to the other 'ServiceAccount'
 *       - apiGroups: ["", "extensions", "apps"] # So, it can query the apiGroups, the resources with the verbs defined below.
 *         resources: ["services", "endpoints", "pods"] # But here under the resources they only gave access to the 'services' amd 'endpoints' and this blog is written somewhere in 2021 and this might have worked at that time, but right now based on my instructors' research, it is not working because apart from 'services' and 'endpoints' we should also give access to read details about the 'pods'. That's why we need to make sure that we are making this change inside this file compared to what what we just copied from the official blog of the Spring Cloud Kubernetes team.
 *         verbs: ["get", "list", "watch"] # Like it can get the details, list the details and watch the details. Like this, it only has read access here.
 *   - apiVersion: apps/v1
 *     kind: Deployment # After making the above role bindings, the next item that we have here is of type 'Deployment'. Under these Deployment details, first we are trying to provide some metadata information about the Deployment i.e.,the name of the deployment is going to be spring-cloud-kubernetes-discoveryserver-deployment.
 *     metadata:
 *       name: spring-cloud-kubernetes-discoveryserver-deployment
 *     spec: # Post that under the specifications, we are trying to mention what are the matchLabels for the application.
 *       selector:
 *         matchLabels:
 *           app: spring-cloud-kubernetes-discoveryserver
 *       template:
 *         metadata:
 *           labels:
 *             app: spring-cloud-kubernetes-discoveryserver
 *         spec: # Now, under the template specification, we are trying to bind a ServiceAccount to this deployment i.e., spring-cloud-kubernetes-discoveryserver - This we created/mentioned/configured/defined previously.
 *           serviceAccount: spring-cloud-kubernetes-discoveryserver
 *           containers: # Post the ServiceAccount details, under the containers config, we are trying to define a name for the container.
 *           - name: spring-cloud-kubernetes-discoveryserver
 *             image: springcloud/spring-cloud-kubernetes-discoveryserver:2.1.0-M3 # After the container name above, here we have the image details. So, this is the image that we should use whenever we want to set up discovery server inside k8s cluster. Basically this Spring Cloud K8s team, they built a Spring boot application which is going to act as a discovery server inside your k8s cluster. We as developers don't need to build any application like we did explicitly with eureka server. We can simply leverage this docker image which is built by the Spring Cloud K8s team and set up the discovery server inside our k8s cluster. But here, there is a tag name that they have mentioned which is 2.1.0-M3 but this might be a pretty old version. You can look for the latest version and tag name from the dockerhub. So, inside the dockerhub I just searched the image name, springcloud/spring-cloud-kubernetes-discoveryserver, and with this you should be able to identify all the tags related to this image name. If you scroll down you should be able to see a stable version(Non snapshot version), for example my instructor as at the time of delivering his lecture he picked 3.0.4 while I at my time I decided to pick a 5.0.0-M4 as the latest image of k8s discovery server.
 *             imagePullPolicy: IfNotPresent # After the image details, you can see, there is imagePullPolicy. With this policy we are telling that please pull the image only if it is not present in the local system.
 *             readinessProbe: # After the imagePullPolicy, they also defined the readiness and liveness probes as shown below. So, that is how we need to define the readiness and liveness probes whenever we are using k8s. Long back we in details dicussed what is liveness and readiness and how to check for these probes using docker compose file and health checks exposed by the spring boot actuator. The very same paths they are also trying to use as can be seen in the configurations below i.e., /actuator/health/readiness and /actuator/health/liveness. So, in future whenever you want to define liveness and readiness probes you can follow the below configured format. The reason we didn't configure this for our individual ms's is we are good without defining these values because like we said and discussed earlier - k8s is capable of restarting my pods multiple times if they face any issues whereas inside the docker compose environment like we visualized earlier - if my container is not able to start, the docker compose is not going to restart again. Since I have an advantage with k8s which is going to take care of restarting multiple times and trying to set up my pod based upon my replica count we should be good even if we don't define the readiness and liveness probes.
 *               httpGet:
 *                 port: 8761
 *                 path: /actuator/health/readiness
 *               initialDelaySeconds: 100 # Here, I am passing a value which is 100 and represents 100 seconds. With this, my k8s is going to wait for the 100 seconds before it tries to perform the readiness probe very first time. I am sure that this discovery server is going to start within 100 seconds. But if I don't mention these values, by default my k8s is looking for the readiness probe within 10 or 15 seconds and with that it may end up performing restarts multiple times as had earlier own discussed. So, to avoid that, I am giving some longer duration which is 100 seconds. If this 100 seconds is also not working for you, then try to increase that value to 150 or 200 seconds.
 *               periodSeconds: 30 # Post that I have mentioned the periodSeconds as 30 seconds. With the help of this configuration, we are telling to the k8s cluster that please validate the readiness probe every 30 seconds. Like initially, it will try to perform the initial readiness probe after 100 seconds, based upon my initialDelaySeconds configured. So, think like after 100 seconds, my readiness probe gave a success response and with that my k8s is not going to be satisfied, it will want to regularly check the readiness probe of your application. So, if you don't provide this periodSeconds, your k8s is going to check for the readiness probe every 5 or 10 seconds. So, instead of that short period we can define our own duration which in this case os 30 seconds. These same values I have also defined under the liveness probe also so that my liveness probe will not fail.
 *             livenessProbe:
 *               httpGet:
 *                 port: 8761
 *                 path: /actuator/health/liveness
 *               initialDelaySeconds: 100
 *               periodSeconds: 30
 *             ports: # At last under the ports mapping, they have mentioned the container port as 8761.
 *             - containerPort: 8761
 *
 *  If you try to install this k8s manifest file, you may face some issues - the reason is, by defauly your readiness and liveness probes are going to check for the respective health details on the actuator endpoints as defined above respectively within a very short period of time i.e., 10 seconds or 20 seconds. Within that short period of time, if your discovery server is not started your k8s is going to attempt a restart and everytime it will try to restart and within a short period of time if your application is not starting it will again try to restart - so this is going to be a continuous loop. That's why to be on a safer side, we need to provide more configurations here. Those we need to mention just under the 'httpGet' definition. And as seen obec the 2 configurations that we are adding are initialDelaySeconds and periodSeconds as discussed above.
 *  With all we have discussed above we should be good, in the next session we will try to set up the discovery server inside the k8s with the help of this manifest file.
 *
 *  Install Spring Cloud Kubernetes Discovery server in K8S cluster
 *  ----------------------------------------------------------------
 *  We will try to set up discovery server with the help of the k8s manifest file. You're maybe having a question on why we are not using helm chart. There are 2 reasons, 1. Discovery Server is a one time set up inside your k8s cluster as you don't have to do multiple changes everytime. With this reason, manually running this manifest file should be fine becuase this is only a one time activity. 2. As of now, there is no helm chart developed by the helm charts community including bitnami for this dicovery server. So, if we try to integrate this manifest file into our getttrightbank helm chart, then it is going to need lots of efforts which my instructor felt like it is not worth since this is only going to be a one time activity to run this and hence we are fine running the manifest file manually. To install the discovery server from the terminal we need to run the command kubectl apply -f kubernetes-discoveryserver.yml.
 PS C:\Users\ctemoi\Desktop\K8s> kubectl apply -f kubernetes-discoveryserver.yml
 service/spring-cloud-kubernetes-discoveryserver created
 serviceaccount/spring-cloud-kubernetes-discoveryserver created
 rolebinding.rbac.authorization.k8s.io/spring-cloud-kubernetes-discoveryserver:view created
 role.rbac.authorization.k8s.io/namespace-reader created
 deployment.apps/spring-cloud-kubernetes-discoveryserver-deployment created
 *
 * As can be seen from the output above, this is going to create a Service, ServiceAccount, RoleBinding, Roles and Deployment. You go to the k8s dashboard and make sure that Discovery Server is started successfully. So, inside the dashboard you can go to Pods then open the logs to the discovery server just to confirm that my discovery server is started. Like you can see in the log statement, 'Started DiscoveryServerApplication in 37.014 seconds (Process running for 60.721). And btw, this is a Spring boot application, and we can tell this from the Spring banner log at the start of these logs confirming that this is a Spring boot application. With this, we successfully set up our Discovery Server inside the K8s cluster. As a next step, we need to make some code changes inside our ms's to remove the Eureka related changes and on top of that we should also make some changes inside our ms's to leverage the discovery server exposed by the k8s. To get started around those changes, I am going to copy all the ms's code that we have inside the v2-spring-cloud-config workspace as It's where the latest and greatest ms's code changes are present. So, from this workspace select, accounst, cards, loans, configserver, gatewayserver and message ms's. We don't have to copy the eurekaserver because we are rtying to get rid of it. We will also not copy the docker-compose folder as we will be deploying all our ms's into k8s cluster and docker compose files is of no use right now. The folders/ms's copied paste them into our new workspace which is v3-spring-cloud. Apart from the changes we will be doing in our ms's, we will also make some changes in our helm charts as well in the coming sessions as we need to remove the details related to the eureka server inside our helm charts. With this, our new workspace folder is now ready. As a next step, you can open it inside intelliJ idea. Check with that workspace for the continuation of this docstring.
 * * * */
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
