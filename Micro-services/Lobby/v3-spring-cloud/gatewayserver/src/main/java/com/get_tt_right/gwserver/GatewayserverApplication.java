package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;


/** Update as of 4/3/2026
 * -------------------------
 * Introduction to mTLS and deep dive on how TLS works.
 * -----------------------------------------------------
 *  # Check slide for detailed discussion.
 *  Tips for ms developers.
 *  -------------------------
 *  1. Optimizing ms development with Spring Boot BOM.
 *  ---------------------------------------------------
 * We are going to learn about a best practice that every ms developer has to follow. Using this best practice, ms developers can optimize or streamline the process of maintaining the dependencies inside their ms's. Before we learn more about this best practice, first let's see the pain points or the problems that ms developers will face while building the ms's. My instructor used the section 14 codebase where he was implementing event driven ms's using kafka and sopied the same as section20 code. His primary reason is to avoid the K8S related concepts while trying to explain this bets practice. And that's why he took the section14 codebase and pasted it into section20. For me, I will just proceed with the latest codebase which does not have eurekaserver in it. So, all our ms's i.e., accounts, cards, configserver, gatewayserver, laons and message - each of them will have its own pom.xml or build.gradle. So, regardless of whether you are using maven or gradle, you need to define all the dependencies required by your ms. For example, in my case, I am using maven to declare the dependencies inside my services.
 * If you go into the pom.xml of all the services, here I have defined all my dependencies. Under the 'parent' tag, you should be able to see the spring boot version we are using. Under the 'properties' tag, you should be able to see we are defining the java version, the Spring Cloud Version and also the Open Telemetry Version. Under the dependencies section, you should be able to see all the dependencies definitions that my ms is using. Towards the end, I also defined the Google Jib Maven plugin using which I can generate the docker image of my service. This same kind of set up you will be able to see inside the cards, accounts, gatewayserver, loans as well as message ms's. The problem wth this set up is, we are trying to hardcode the spring boot version and also other properties like Java version, Spring Cloud Version or any 3rd party libraries versions inside the ms itself i.e., Open Telemetry version. Assume that your organization has more than 30 ms's - in future if you try to migrate your ms's from one version of the Spring boot to another version, means you need to visit all the pom.xml of all the ms's and make the version number changes. Do you think this is a best practice? Of course not! As the developer will be forced to visit all the ms's to update a simple version number! haha. This is the problem that we are going to solve by adopting BOM.
 * So, what is a BOM? # Check slides for this discussion.
 * To adopt BOM into our ms development, first we need to create a separate Spring Boot Maven project. Right-click on the v3-spring-cloud folder and select an option which is new >> module. Inside the window that comes up,select the Spring boot or Spring Initializer option depending on what your IDE shows you. Give the name as 'getttright-bom' - bom means nothing but bill of material. Make sure to select the language as Java, Type as Maven and the group is going to be com.get_tt_right, the artifact is going to be getttright-bom. Select the JDK as 21, Java Version as 21 and the packaging we will leave it as jar for now. After filling all those details, click on the next button >> In that page do not add any of the spring boot starter dependencies >> Straight away click on the create button. Now, we have a new Spring Boot maven project with the name getttright-bom. So open its pom.xml - it is just a regular pom.xml that you see under any of the ms's. To this pom.xml file we are going to make a good amount of changes so that this file is going to act as a parent for all our ms's. Before we try to make those changes, first I am going to delete the src folder of getttright-bom because we don't want to maintain any of the source code inside this getttright-bom project. It's a best practice to maintain only the dependencies related information but not any source code - that's why we have deleted the source folder.
 * Next, inside the pom.xml delete the parent tag related element and its contents as below:
 <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.3</version>
    <relativePath/> <!-- lookup parent from repository -->
 </parent>
 *
 * As of before we deleted the above parent element form my pom.xml - it was trying to use the spring boot related starter parent.
 * Next, mention the in the description element - mention it as 'Common BOM for getttrightbank microservices'. Here we just mention whatever makes sense to us. Next, we have the below empty tags:
 <url/>
 <licenses>
     <license/>
 </licenses>
 <developers>
     <developer/>
 </developers>
 <scm>
    <connection/>
    <developerConnection/>
    <tag/>
    <url/>
 </scm>
 *
 *Delete all the above empty tags. Replace them with the same tags, but this time, we have populated some data inside these tags - you can copy this from the repo of your instructor and modify accordingly. All those tags are optional btw, but it is a good practice to mention them inside the pom.xml files. They have nothing to do with the bom file - of needed you can completely remove them as well.
 * After mentioning these optional tags, just after the description element, you need to create a new tag with the name 'packaging' - under this tag, we usually mention either 'jar' or 'war' but whenever we are looking to create a bom project or a bom file which is going to act as a parent for all our ms's, we need to mention the packaging as 'pom'. Pom in full is nothing but Project Object Model. So, without mentioning this 'pom' as packaging you can't use the bom or Bill Of Material feature. Hope that has sunk in crisp clear.
 * As a next step, under the properties, I am going to define the list of properties that are required by my microservices. As of now we only have the 'java.version' property. So, you can cut this out and copy every other properties details from your instructors repo to this pom.xml file of getttright-bom. Check with your pom.xml of getttright-bom for a docstring on which properties we have included. In a few it will make sense to you on why we have defined/mentioned these properties inside the BOM file and how they are going to help us. For now, you should be clear on all those properties and the versions.
 * After defining all those properties, by default we have a dependency which is related to springboot starter and spring boot starter test under the dependencies section. For now, delete these 2 dependencies and keep the dependencies tag as empty. Next, just below the dependencies tag, create a new tag related to the dependencyManagement tag. Inside this dependencyManagement tag, I am going to define few dependencies - copy them from the GitHub repo of your instructor and paste them here. Inside the pom file you will find a docstring of what each of those dependencies are.
 * Now, you should be crisp clear on what we have done so far. We just created a simple spring boot maven project which is going to act as a BOM for all of our individual ms's. The thumb rule is , you need to make sure you are mentioning the packaging as pom, remove all parent tag related element, followed by you need to mention all the required properties for your ms's, then under the dependency mgt tag you need to provide all these dependencies that are required by your ms's. Next, we will try to adopt this getttright-bom inside our individual ms's and see the magic.
 * First, in order to adopt this getttright-bom, I will go to one of the individual ms's i.e., the pom.xml of accounts ms. As of now, under the parent tag, the accounts ms is trying to have the 'spring-boot-starter-parent' as the parent with so-and-so version i.e., 3.4.2 but we don't want this, instead we want our getttright-bom as the parent. Check the docstring of accounts ms pom.xml parent tag for this. We have also dicussed and documented a lot inside the account pom.xml file - you can check that out as well.
 * Now with all we have discussed if you try to do a build for the accounts ms you should not have any compilation errors. Hope you visualized the beauty - right now the entire control of the version numbers is present inside the parent pom file and nowhere in our child accounts ms we have harcoded any versions i.e., spring boot version, 3rd party related versions etc. Like this, in future if you want to change the version, you just have to make the change inside a single file which is the parent pom.xml file. Hope you are clear with the process. You can repeat this same process for the other child ms's i.e., cards, loans, etc. Once you are done, do a complete reload of all the maven projects. You can achieve this from the maven icon on the RHS nav of your IDE. This is to just confirm that there are no compilation error. Since my entire build is successful, you can try to start one of the ms's also just to make sure that everything is working as expected. You try this with the configserver which is the simplest ms that we have. Go to its main class and try to start it.
 * If you try to look at the logs of the configserver, you will right now notice that it is using the spring boot version  4.0.0. You can stop it now. Now we want to test a small change to be fully confident that everything is working fine - go to the pom.xml file of getttright-bom  and try to change the spring boot version to a lower version i.e., 3.5.12. Load the maven changes inside getttright-bom. Once this is done, inside the configserver project, delete the entire target folder content. But don't delete the target folder itself. We are doing this so that we can re-initiate the maven loading process. On the maven icon click on the icon relaod all maven projects. With this, the build is going to complete with a new spring boot version i.s., 3.5.12. Now you can try to start your configserver application in debug mode. This time you can clearly visualize that the microservice started with the spring boot version 3.5.12. If you are not able to visualize this, then that could be due to the maven cache and so please make sure you are doing the maven reload either multiple times or restart your IDE so that your maven cache is going to be invalidated.
 * With all we have discussed you should be crisp clear on what we have done so far with the help of Bill Of Materials - BOM concepts. Like this with the help of BOM we are able to streamline the development of the ms's.
 *
 *With the help of this getttright-bom project - as of now we set up Bill Of Material (BOM) inside our ms's project. With this, all of our ms's are going to follow the same version numbers which we are trying to control from the parent pom.xml. You may have a question which is - as of now we have declared all the version numbers that are supposed to be used by my ms's in the parent pom but sometimes we may have a requirement which is - a particular ms may want to use a particular version of the dependency  which is different from the version which we have mentioned inside this parent pom.xml, so how do we handle these kind of scenarios? Think like my loans ms wants to use a separate version of spring doc or lombok or even the tag name for thee image that will be generated. In such scenarios instead of referring to the version defined inside the parent pom we can define our own hardcoded version in the child pom for that respective dependency or value. Like this we have all the freedom to define our own values inside our individual ms's without having to necessarily refer to the parent pom value. This kind of flexibility you can follow for any dependencies - could be spring boot dependencies or even 3rd party library dependencies or even the image tag.
 * Apart from managing the dependencies or libraries versions, there is also one more advantage that we can leverage whenever we have implemented BOM concept inside our ms's. Sometimes inside your ms's, there is avery good chance that all of your ms's might be using some common libraries. For example, all my ms's are using this 'spring-boot-starter-test' library. If I try to global search this artifactId by ctrl shift + F i.e., 'spring-boot-starter-test' you will notice this same library is present inside all of my ms's. In this kind of scenario, what we can do is, instead of repeating the common dependency details inside all the ms's, we can try to define these dependencies at a single place which is inside the parent pom.xml that we have created so far. How to do this - 1. Delete this starter test dependency from all my 6 ms's - I commented it out for my case. Now if you try to search for this dependency it should only be present inside the BOM pom.xml. Now, if you try to do some build you may get some compilation error in all my 6 ms's related to the test annotations and import statements in our test package classes. Reason is because right now though I have defined the common dependency inside the pom.xml of getttright-bom, it is not being referred by my child ms's the reason being, we have defined that common dependency under the dependencyManagement element. Whenever we define a dependency under the dependency mgt element, it is only going to be used for the version mgt. If your requirement is to also have all your child ms's to import the dependency then you need to mention the same under the dependencies element that is outside the dependencyManagement element. So, as of now as could be visualized in the parent pom, the dependencies element outside the dependencyManagement element is empty.
 * So, copy and paste that dependency in the outside dependencies element also and remove the version tag from it. Once this is done reload the getttright-bom maven project so that all my child ms's they should be able to fetch this common dependency from the parent pom.xml. With this, all the CE's in the test package classes related to test annotations and respective imports will be happily resolved! This means that right now my individual ms's they referring to this common dependency defined inside the parent pom.xml. With what we have discussed you should be crisp clear with this advantage as well. The humble request here is you should always try to adopt the BOM concept inside your actual ms's that you are going to build as part of your job otherwise you will end up making a lot of manual changes/adjustments inside your ms's. As a next step what we can do is, generate the docker images for our ms's to just confirm that with this BOM set up that we have done there is no impact on the docker images generation. Yeey! all the docker images are generated successfully! and btw if you notice, there is no impact on the size of the generated docker image as well compared to the images we had generated earlier on. Now as next step, what we will try to od is we will try to start the container of this configserver image just to verify that container is starting without any issues. For this run the docker command i.e., docker run -p 8071:8071 colince819/configserver:final. You will see that the Spring boot application is getting started without any issues and the Spring Boot version right now being leveraged you can see from the console logs that it is 4.0.5 which i the same that we have mentioned in the parent pom.xml. Inside the docker desktop if you go to the containers you will see that there is a container started and from there also you can be able to verify the logs. With this we can conclude with confidence that the entire setup of our BOM is working without any issues. In the slides you can also see a brief summery around BOM for reference.
 *
 * Shared Libraries in Microservices.
 * ----------------------------------
 * Many times ms developers will end up writing a lot of duplicate code in multiple ms's. Let's visualize how to handle this code duplication challange. Inside my ms's there is a DTO with the name "ErrorResponseDto". This DTO class has exactly same code which is duplicated across all my ms's like accounts, cards and loans . Of course, I don't require any DTO classes inside my message, eurekaserver, configserver and gatewayserver. With this said, if you ignore these supporting services, I end up writing this ErrorResponseDto inside all the remaining ms's i.e., cards, loans and accounts ms. You can click double Shift to search this "ErrorResponseDto" classes, and you will see what I am talking about. In future if I create more ms's then definitely I will need this same exact ErrorResponseDto class there. In this kind of scenario you will get tempted to create a common library and move all this duplicate code into the common library and the same common library you will try to import inside your individual ms's. This can be your basic approach - haha. But, news flash! there are better ways on how we can handle this. So, whenever you are looking to build shared libraries in ms's you will end up with multiple approaches as discussed in the slides. - Check that out for details. Also, we will try to identify which is the valid approach we can take.
 * To get started with the shared libraries, first we need to create a submodule under my getttright-bom. Right click on it >> New >> Module. Make sure to select Spring boot. Now, give the module name as "common" - this can be anything based upon your requirements. Make sure the language is Java, type is Maven, group is com.get_tt_right, artifact is "common", jdk select 21, Java Version 21, and the packaging should be jar. After filling all those details click on the next button >> Select the Spring Web related dependency and also lombok related dependency reason: I need all these dependencies to build the common code inside this maven project. Finally click on the create button and with that under the getttright-bom you will be able to see that there is a submodule created with the name common. This way you can be able to create multi-modules under the getttright-bom with various names like security, logging, auditing, ...etc based upon your business requirements. One you create this multi-module, you need to go to the pom.xml of getttright-bom and just after the properties tag you need to create a tag with the name <modules></modules>. Under this, you need to mention your module details. Check the docstring inside the getttright-bom pom.xml for details.
 * In the pom.xml of common submodule, we need to make some changes. Check its docstring for more details. Now, we have successfully created a submodule, load the maven changes and do a build. As a next step, I need to move the "ErrorResponseDto" class from my individual ms's to this common module. Copy that class and go to the common submodule. Inside the src folder of common submodule, first what we will do is remove/delete the CommonApplication reason: We don't want anyone to execute my Spring boot application - we want to use this submodule strictly as a library. Now, try to create a new package with the name dto. Now, inside this dto package I am going to paste the new file which is "ErrorResponseDto". With this, we have moved the duplicate code. As a next step, I will need to delete the same file in all the ms's i.e., loans, cards and accounts. With this, now we have the "ErrorResponseDto" in only a single place which is only inside the common submodule. But with this setup if you try to do a build (Ctrl + Shift + f9) definitely you are going to get CE's because my individual ms's they didn't import the common module inside their pom.xml. So, all the individual ms's which require this common dependency, what they have to do is - they need to add a dependency details around common module. So, first, open accounts pom.xml and at the top under the dependency section - mention the dependency details fo common module. Check that out for the docstring. Do the same for all the individual ms's i.e., loans and cards. Load the maven changes and try to do builds on the respective individual ms's (ctr + shift + f9). And happily you should see that the errors will be happily resolved inside the files where this ErrorResponseDTo class was being used i.e., GlobalExceptionHandler.
 * When mentioning the common module as dependency in the child ms's I ran into a problem i.e., "dependency not found". As a solution to this it was advised that from the common module parent folder I mvn clean install the library into the local .m2. I ended up running into an issue as the build was not successful with the issue - Unable to find a @SpringBootConfiguration. I came to find our that since we had deleted the CommonApplication.java file my common module ended up just as a plain library (not a Spring Boot app), but the auto-generated CommonApplicationTests uses @SpringBootTest, which requires a @SpringBootApplication class — which doesn't exist in a library module. As a solution - I replaced the contents of CommonApplicationTests.java with a simple no-op test by simply removing @SpringBootTest entirely since there's no Spring Boot app to bootstrap here. The clean install was successful. But still the child ms's say dependency not found. With this we found the culprit being - The common module's own pom.xml had ${common-lib.version} as its version tag, and Maven cannot resolve a property for its own module's version during install. It's a Maven limitation — a module cannot use a property to define its own <version>. As a solution - Fix: Hardcode the Version in common/pom.xml. Then in the .m2 folder Delete the bad cache whcih is nothing but everything under the .m2\repository\com\get_tt_right\ folder. Finally, we proceeded to Reinstall from BOM root nothing but from getttright-bom by running the command - mvn clean install -DskipTests. The  Verified the folder .m2\repository\com\get_tt_right\common\1.0.0\ has common-1.0.0.jar and common-1.0.0.pom. Now on building the individual child ms's that use this common module as dependency/library everything was okay - the only thing we needed to do is to do the imports of all the usages of ErrorResponseDto and finally at last the build was successful! Yeey! And one thing to note is:
 * Going Forward
 * Whenever you bump/update the common library version, update it in two places:
 *
 * common/pom.xml → <version>1.0.1</version>
 * BOM parent pom.xml → <common-lib.version>1.0.1</common-lib.version>
 *
 * This keeps everything consistent.
 * What we can do to just confirm that everything is working fine - we will try to generate a docker image of accounts ms and later on the other remaining ms's by running the command mvn compile jib:build. Like this the image generation should be successful. For some reason if it is not successful inside your local - that will be an indication that the common module is not published properly. To publish the common maven module into the local maven repo, what you have to do is - go to the getttright-bom/common dir in your terminal and from here run the command which is mvn clean install. This we already did as was discussed earlier on in details. With this command the common module is going to be build, and it is going to be published to the local maven repository - this you can confirm/verify by navigating to the local maven repository. It's from this location that the docker image generation process of your individual ms's are going to fetch the jar.
 * This way you can create any number of multi-modules or submodules under the getttright-bom. Always be following this approach instead of creating a single jumbo maven module which is not recommended for the reasons that we discussed in details. This should be crisp clear! But like we discussed also you should be very cautious while trying to use these approaches - you need to make sure that you are not bringing deployment challenges as well as coupling challenges inside your ms's applications.
 * And that was it with regard to ms's. Always refer with your instructors GitHub repo for any new updates. You are now a master of microservices using Spring, Spring Boot,  Docker, and Kubernetes. You are one of the few developers who know how to build ms's by following all the best practices. You know how to buid your own discovery server, edge server,you know how to secure your ms's, you know how to build event driven ms's, ...etc. There are many things that we have discussed and that you have learnt throughout the journey and your patience has finally paid off. You should be able to now clear any ms's interview and you are ready to play a ms developer role in any project and in any organization who are trying to build ms's. So, please treat all that you have learnt as the initial stepping stone towards your ms journey - so please go and explore the ms world and educate others as well around the ms's.
 * * * */
@SpringBootApplication
@EnableDiscoveryClient
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
						.uri("http://accounts:8080"))

				.route(p -> p.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("http://loans:8090"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())))
						.uri("http://cards:9000")).build();
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
