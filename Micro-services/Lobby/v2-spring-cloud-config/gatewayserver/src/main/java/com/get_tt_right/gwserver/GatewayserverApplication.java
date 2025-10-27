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

/** Update as of 19/10/2025
 *Create environment variables inside K8S cluster
 -------------------------------------------------
 * As a next step, we need to deploy all the remaining ms's into the K8S cluster. But before that, we need to create certain environment variables inside our K8S cluster which we can inject into the ms's deployments. Because, if you try to see the docker compose files that we have written, as of now all our containers are dependent on many environment variables like, what is the activated profile i.e., SPRING_PROFILES_ACTIVE: prod. What is the Spring config import url i.e., SPRING_CONFIG_IMPORT: configserver:http://cofigserver:8071/. What is the eurekaserver url i.e., EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eurekaserver:8070/eureka/, ...etc. Since all our ms's need these environment variables to get started, we need to look for an option on how to create the same kind of environment variables inside the K8'S cluster as well. For the same we are going to create an object of Configmap inside K8S. In the official docs i.e., "https://kubernetes.io/docs/concepts/configuration/configmap/". It can be noted that, "A ConfigMap is an API object used to store non-confidential data in key-value pairs". Pods and your containers they can consume ConfigMaps as environment variables, command-line arguments, or as configuration files in a volume. So, the same ConfigMap object we need to consider to define all our environment variables inside the K8S cluster. There is also a syntax provided on how we can create this. We are going to follow the same and define a K8S manifest file to create the Configmap that is specific to our ms's. In the LHS nav in the official documentation under the Configurations, you will notice, we also have 'Secrets' object which we can leverage whenever we want to store some confidential data. Previously, we created a Secret object whenever we were trying to deploy the K8S Admin dashboard/UI. If yoy scroll down in the doc where they have given an example manifest definition, you will see the 'kind' has to be 'Secret'. The same thing we did previously - refer to the previous commit docstring.
 * Right now, our focus is on Configmaps to create/define environment variables. For the same, I am going to create a new file inside my K8S folder i.e., touch configmaps.yaml. Inside this file I have pasted the below discussed configurations:
 apiVersion: v1 # The api version for our ConfigMap object we need to mention the value as v1
 kind: ConfigMap # The kind has to be ConfigMap
 metadata: # Under the metadata we are defining the name that we want to give for this ConfigMap configurations
 	name: get_tt_right-configmap # The name that we are trying to give here is 'get_tt_right-configmap'
 data: # After the metada, we need to mention an element which is 'data' under which we can provide any number of environment properties with k-v.
 	SPRING_PROFILES_ACTIVE: "prod" # The profile that I want to consider while deploying my ms's
	SPRING_CONFIG_IMPORT: "configserver:http://configserver:8071/" # What is the configserver url.
 	EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: "http://eurekaserver:8070/eureka/" # What is the eurekaserver url.
 	CONFIGSERVER_APPLICATION_NAME: "configserver" # What is the application name of configserver ms.
 	EUREKA_APPLICATION_NAME: "eurekaserver" # What is the application name of eurekaserver ms.
 	ACCOUNTS_APPLICATION_NAME: "accounts" # What is the application name of accounts ms.
 	LOANS_APPLICATION_NAME: "loans" # What is the application name of loans ms.
 	CARDS_APPLICATION_NAME: "cards" # What is the application name of cards ms.
 	GATEWAY_APPLICATION_NAME: "gatewayserver"  # What is the application name of gatewayserver ms.
 	KC_BOOTSTRAP_ADMIN_USERNAME: "admin" # What is the KeyCloak Admin username
 	KC_BOOTSTRAP_ADMIN_PASSWORD: "admin" # What is the admin password of KeyCloak.
 	SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI: "http://keycloak:7080/realms/master/protocol/openid-connect/certs" # What is the KeyCloak url where my resource server has to fetch the certificate

 * So, all the above environment variables I have mentioned. Coming to the values, the hostnames in urls have to be your respective service name i.e.,'configserver' in http://configserver:8071/. I have created/deployed my configserver with the service name as 'configserver'. Similarly, for the eurekaserver, I will create/deploy my eurekaserver with the service name as 'eurekaserver' and that's why in the value url you can see 'http://eurekaserver:8070/eureka/'. Coming to the KeyClaok url details/value which we need to mention under the resource server, I am going to start my KC service with the name 'keycloak'and that's why in the url value, I am going to mention the hostname as 'keycloak' and it is going to get exposed at the port 7080. That's why I am trying to mention the correct things here i.e., what is the hostname and what is the port number.
 * Here,I am not defining any properties related to Kafka or RabbitMQ or OpenTelemetry. Reason: Once we discuss the Helm charts, then only we are going to see how to easily set up the Kafka, Grafana related components inside K8S because writing all these K8S manifest files for industry provided components like Kafka, Grafana is unnecessary and is overcomplicated. That';s why when we try to learn about Helm charts, using the same, I am going to show you how easy it is to set up these components like Kafka, Grafana components. But as of now we will only deploy the custom ms's that we have developed along with the KeyCloak. KeyCloak is mandatory and we cannot skip it because as of now, our GatewayServer is secured and without KC server we cannot access any of the APIs to create the data inside our ms's. So, with this we have successfully created the configMap K8S manifest file. As a next step, we will try to use this file and try to feed the same to my K8S's cluster. For the same, I am going to run the command 'kubectl apply -f configmaps.yaml'. As soon as I execute this, I get an output saying that, 'configmap/getttright-configmap created'. Inside the K8S dashboard, I can validate if it is created or not.
 * In the K8S dashboard, make sure the namespace selected is 'default' then on the LHS nav, there is a section 'Config and Storage', under this click on the nav link 'Config Maps'. You should be able to see an entry of the config map which we just created named 'getttright-configmap'. If you click on it, you should be able to see all the environment variables that we have provided to the K8S cluster. As a next step, by using these environment variables, we can try to deploy all the remaining ms's. Before that, lets try to understand and visualize from the Admin dashboard what the difference between Config Maps and Secrets are. If you open the 'getttright-configmap' config maps that we just created you will notice that we are able to see the data directly whereas secrets will be base 64 encoded. As of now, on the LHS nav if you click on 'Secrets' we have no secret(s) mentioned/defined. But if, if you switch to the namespace which is 'kubernetes-dashboard' from the top LHS dropdown,you should be able to see a secret entry that we created earlier on named 'admin-user'. If you click on it, under the data section, you will not be able to see the data, by default, it is hidden from the dashboard. But if you want to see that, there is an eye symbol, on clicking it, you can be able to see the base 64 representation of that data - For example, I clicked on the eye symbol of the data labeled 'Token'. Okay this is not a super secure way to secure your secrets, that's why in K8S there is a,so a Joke that 'Secrets in K8S are not actually Secrets'. There is always better approaches to store the secrets inside Cloud environments. Your Platform/DevOps team members should be able to set up them inside your m's network.
 * With this, you should be crisp clear on how to create a ConfigMap inside K8S cluster.
 *
 * Preparing K8S manifest files for the remaining ms's
 * ----------------------------------------------------
 * Now, we will try to deploy all the remaining m's into the K8S cluster. For the same first we need to prepare the K8S manifest files for the remaining m's. Since we are already familiar with this process, behind the scenes I have created all the required Manifest files as shown below;
 * I have the manifest files named with a numeric prefix value i.e., 1, 2, 3, ...etc. With the help of these prefix values, I am giving an order in which you need to apply these yaml files inside your K8S cluster. So, in future, whenever you want to deploy all our ms's into a brand-new cluster, first we need to create the KC service, Post that we need to create the configmap, followed by configserver, eurekaserver, accounts, loans, cards and lastly gatewayserver. This is the orede that you need to follow in order to make our life easy. Below, we are going to discuss what we have defined/configured in all these files except configmaps and configserver yaml files as we have already in details discussed their respective contents.
 * Below, nothing new as for every service/ms that we want to deploy we have to create a 'Deployment' object followed by what is the 'Service' object.
 1_keycloak.yml
 ----------------
 apiVersion: apps/v1
 kind: Deployment
 metadata:
 	name: keycloak
 	labels:
 		app: keycloak # Giving a label app name as 'keycloack'
 spec:
 	replicas: 1 # After this metadata name labels app definitions, under the specifications I'm mentioning the replicas as one.
 	selector:
 		matchLabels:
 			app: keycloak # selector.matchLabels.app as 'keycloak'
 	template:
 		metadata:
 			labels:
 				app: keycloak
 		spec:
 			containers:
              - name: keycloak # Under this specifications you can see the container name is going to be 'keycloak'
       			image: quay.io/keycloak/keycloak:26.4.0 # Image
 				args: ["start-dev"] # Arguments that I want to consider while starting my container. Since we want to start our K8S cluster in a dev mode we need to provide this 'args' key with a value ["start-dev"]
 				env: # This is a new piece of configuration that you may find here compared to what we have discussed initially with respect to configserver. Here, we are trying to pass the environment variables to this deployment instructions. So to pass the environment variables, we need to mention an element i.e., 'env' under which we can define any number of environment variables.
 				- name: KC_BOOTSTRAP_ADMIN_USERNAME # With the help of this hyphen, after hyphen, we need to mention what is the name of the environment variable that you want to inject to your container. In this case the name of the property is 'KEYCLOAK_ADMIN'
                  valueFrom: # To the above defined environment variable, I want to provide the value that I stored inside the configmaps. This valueFrom property is like from where I want to fetch the value
                  	configMapKeyRef: # Under the valueFrom I have to mention 'configMapKeyRef'
                    	name: getttright-configmap # What is the configmap name where we stored this value.
 						key: KC_BOOTSTRAP_ADMIN_USERNAME # At last inside our 'getttright-configmap' configmap, we may have many environment variables. So, which environment variable it has to consider? That's why we need to give the key value i.e., 'KC_BOOTSTRAP_ADMIN_USERNAME'. So, it will go and look for this key value  inside the 'getttright-configmap' configmap. Against to that respective key, there is a value which is 'admin'. The same will be injected as a value to the property defined i.e., 'KC_BOOTSTRAP_ADMIN_USERNAME' that is just under 'env' i.e., - name: KC_BOOTSTRAP_ADMIN_USERNAME. In the getttright-configmap, under the data we have a k-v entry i.e.,  KC_BOOTSTRAP_ADMIN_USERNAME: "admin". If we want, we can mention the key as 'username' instead of 'KC_BOOTSTRAP_ADMIN_USERNAME'. The same key/name i.e., 'username' we then have to use inside the key property that is defined under 'configMapKeyRef' BUT PLEASE DON'T mention this 'username' here under the environment name i.e., - name: username Reason: Whatever you mention under the environment name, that will be fed as an input to your container or your Spring Boot application, so if you give some random property name(s) under the 'env.name' definition, it is not going to work. That's why make sure you are ALWAYS giving the correct environment name under the env.name and not just random names. But you have the flexibility to maintain any key name inside your configMap i.e., getttright-configmap and under this configMapKeyRef.key property in your manifest file. But to be consistent and to make our lives easy, I am maintaining both the configMapKeyRef.key property value same as the env.name property value.
 				- name: KC_BOOTSTRAP_ADMIN_PASSWORD # Same drill, we have injected one more environment variable with the name 'KC_BOOTSTRAP_ADMIN_PASSWORD'
                  valueFrom:
 					configMapKeyRef:
 						name: getttright-configmap
						key: KC_BOOTSTRAP_ADMIN_PASSWORD
 				ports: # At las I have also provided what are the port configurations
 					- name: http
 					  containerPort: 8080 # It has to start at the port 8080
 ---
 apiVersion: v1
 kind: Service # After the 'Deployment' instructions, we have the 'Service' related instructions as well.
 metadata:
 	name: keycloak
 	labels:
 		app: keycloak # The same label app name we are mentioning here under the Service. This is how the K8S is going to connect this 'Service' object with the 'Deployment' object of keycloak service/ms.
 spec:
 	selector:
 		app: keycloak
 	type: LoadBalancer # Here also we are trying to expose our KC service as a LoadBalancer which means anyone can access my KC server at the port 7080
 	ports:
 		- name: http
		  port: 7080
          targetPort: 8080 # But please make sure you are mentioning the targetPort value same as your containerPort value defined inside the 'Deployment' instructions. Under containerPort and targetPort, we need to always mention what is the port where your container is going to start internally inside your K8S cluster.

 *
 * 2_configmaps.yaml
 * -------------------
 * Already discussed in details
 *
 * 3_configserver.yaml
 * --------------------
 * Already discussed in details
 *
 * 4_eurekaserver.yml
 * ----------------------
 * Here also nothing new. Same drill as discussed for other manifest files. We have provided the 'Deployment' instructions as well as the 'Service' instructions. Under the 'Deployment' instructions, you can see I have provided the image name as colince819/eurekaserver:v4. My instructor was using S12 related images and that's why for him the same tag name he has mentioned inside the Deployment instructions of configserver as well i.e., s12. For me, I mentioned the tag name V4 for configserver related deployment instructions and for other deployment related instructions they may be different.
 * For eurekaserver we are feeding 2 environment variables i.e., What is the application name of eurekaserver i.e., SPRING_APPLICATION_NAME. And btw, if you are keen, in my configmaps, the eurekaserver application name is present under the key 'EUREKA_APPLICATION_NAME'. The respective value will be injected into my eurekaserver container as the application name. Do you know why I have not used the same key name in my configmaps as the environment variable name i.e., 'SPRING_APPLICATION_NAME'. haha. It's because we need to define this same application name environment variable for all the remaining ms's, with that reason I have mentioned different relevant key names i.e.,EUREKA_APPLICATION_NAME, ACCOUNTS_APPLICATION_NAME, etc inside the configmaps and the same I am going to refer in the respective K8S manifest files for different applications/ms's when defining the application name environment variable.
 * Here, I am also injecting what is the configserver endpoint url into an environment variable i.e., SPRING_CONFIG_IMPORT
 * After the deployment instructions, we have Service instructions also which you are already familiar with, nothing new or fancy.
 *
 * 5_accounts.yml
 * ---------------
 * Nothing new or fancy. Everything is familiar. Just note the image tag. My instructor is using s12 related containers for all his Deployments. For me I am trying to leverage my containers that do not have event streaming stuff. That's why you may see different inconsistent tag name for different images in different Deployment instructions. For Account ms's I am using tag name i.e., V9
 * Compared to the eurekaserver manifest file, for the accounts ms manifest file, there is a new environment variable that I am injecting which is what is the eureka server client service url default zone. Reason: My accounts ms has to register itself with the eurekaserver and that's why we are trying to provide this environment variable. And, after Deployment related instructions, we also have Service related instructions.
 *
 * 6_loans.yml/7_cards.yml/8_gateway.yml
 * --------------------------------------
 * Same kind of setup as in accounts mangiest file. For gatewayserver the tag name I am using is V5. Loans and cards is V9 same as accounts ms.
 * For gatewayserver we are injecting the extra environment variable i.e., what is the keycloak server url where my resource server has to fetch the certificate i.e., SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI
 *
 * With this, it is safe to assume that you are clear with all the K8S manifest files that we have created discussed so far for the ms's that were remaining. Next, we will be applying them into K8S cluster so that all our containers will get deployed into the cluster and post that we can also validate the same.
 *
 * Deploying the remaining ms's into the K8S cluster.
 * ----------------------------------------------------
 * Deploy them in the order prefixed in by the file name i.e., kubectl apply -f 1_keycloak.yml - This will set up the KeyCloak service. The next file which I need to execute here is one related to configMaps i.e., kubectl apply -f 2_configmaps.yaml - You will get the output i.e., configmap/getttright-configmap unchanged. Reason: We had already earlier on executed this. You may have a question like, earlier on we had already creaed this/executed this why are we re-executing it?? Reason: Here we are seeing a demo, so, when trying to provide the same instructions to the K8S cluster it is smart enough to detect that there are mo new changes and it is going to give an output like, 'configmap/getttright-configmap unchanged'. It's like saying that, 'Huh! nothing changed, and I am not doing anything based upon your instructions'. This is one of the beauty of K8S.
 * Next, 'kubectl apply -f 3_configserver.yaml'. Do you know of any change in the configserver compared to the previous deployment that we have done? My instructor changed the image name from s14 to s12. If you execute this command you will get two outputs like, 'deployment.apps/configserver-deployment configured' which means 'The new deployment is configured - of course based upon the new image name. hahah' And another output like, 'service/configserver unchanged' which means like - The Service configurations related to configserver are unchanged because we didn't make any changes in the 'Service' object related to configserver in the file 3_configserver.yaml. Now, behind the scenes, my configserver might have been set up, and we can confirm the same by going to the K8S dashboard. You will see that as of now we have 2 deployments running. So, if you try to navigate to the nav link i.e., 'Pods' on the LHS nav, and the open configserver related pod, we can see the logs of my configserver by clicking on the icon '3 and half' lines present on the top, RHS nav. Here you should be able to see some log statements saying that, 'Started ConfigserverApplication in 40.56 seconds' which means that our confiserver got started successfully. Note: Always make sure that your deployment is completed successfully before you try to apply the next set of instructions in the K8S cluster. So, once we make sure that configserver is successfully set up, as a next step, I am going to apply the K8S manifest file related to eurekaserver i.e., kubectl apply -f 4_eurekaserver.yml and this is going to deploy my eurekaserver into the K8S cluster.
 * Same drill, navigate to the pod related to eurekaserver and post that check the logs after a few seconds. You should see a log statement saying that my eurekaserver got successfully started i.e., 'Started EurekaserverApplication in 40.57 seconds'. Next, deploy the instructions related to accounts ms i.e., kubectl apply -f 5_accounts.yml, then followed by kubectl apply -f 6_loans.yml and then followed by kubectl apply -f 7_cards.yml then followed by kubectl apply -f 8_gateway.yml. So, one you have applied this loans, accounts and cards related K8S manifest files, please wait for these containers to get started completely - post that only you ca start the gateway server.
 *
 * * */
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
