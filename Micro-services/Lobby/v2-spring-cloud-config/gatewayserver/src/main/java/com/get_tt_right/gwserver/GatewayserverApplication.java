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

/** Update as of 10/05/2025
 * Introduction to Bulkhead Pattern
 * -----------------------------------
 * With the help of this bulkhead pattern, we can improve the resilience and isolation of components or services within a ms network/ a system. You may be wondering why on the RHS of this slide, there is an image of a boat or Ship.  The reason we are having it is because, this bulkhead pattern is inspired from the concept of bulkheads in the ships. So what are these bulkheads? If you can see the image of the ship, using these bulkheads, we are physically partitioning the entire ship so that even if one of the compartments is flooded with water, the other compartments are safe and secure.Nothing but it is not going to affect the other compartments/partitions inside the ship. This will thus enhance the overall stability and safety of the ship or vessel. If you have watched the Titanic movie, when the Ship collides with a mountain in the middle of the sea, it didn't immediately submerge into the ocean. Actually a lot of stories happened in between the collisions and climax. The water didn't enter into the entire ship at a time because behind the scenes there are bulkheads in the ship. When you lock a compartment, the water will not enter into the other compartments very easily.
 * Using the same bulkheads inside the ships,the bulkhead pattern in the software perspective is inspired. So, with the help of this pattern we can isolate and limit the impact of failures or high loads in one component from spreading to the other components. It also helps to ensure that a heavy load in one part of the system does not bring down the entire system and this enables other components to work and continue functioning independently of each other. How bulkhead pattern is going to achieve the stability is, with the help of this pattern we can allocate or assign resources to a specific REST API or MS so that teh excessive usage of resources we can avoid. On a high level, the summery is: With the help of bulkhead pattern we can define the resource boundaries for all the components inside a ms and this will enhance the resilience and stability of the system. Check slides in order to understand this bulkhead pattern with the help of a diagram.
 *
 * How to implement the Bulkhead Pattern
 * -------------------------------------
 * Inside the Spring Cloud Gateway, there is no support for bulkhead pattern as of now. This being said, we can only implement bulkhead pattern by using the Resilience4J library. On the page - https://resilience4j.readme.io/docs/getting-started-3#configuration if you can scroll down in the Annotations section, you will notice there is an annotation with which we can leverage to implement the bulkhead pattern. You can see that with the help of @Bulkhead we can configure the bulkhead related configurations i.e.,
 * @Bulkhead(name = BACKEND, type = Bulkhead.Type.THREADPOOL)
 * public CompletableFuture<String> doSomethingAsync() throws InterruptedException {
 *         Thread.sleep(500);
 *         return CompletableFuture.completedFuture("Test");
 *  }
 *  - The above code you can find it on that page, I copied it from there.
 *  name = BACKEND
 *  type = Bulkhead.Type.THREADPOOL
 *
 *  Means, with these bulkhead configurations, we are trying to assign the thread pool to the operation/behavior 'doSomethingAsync'.
 *  The properties related to bulkhead you can identify if you scroll up in the page under the configurations section. i.e.,
 *  resilience4j.bulkhead:
 *     instances:
 *         backendA:
 *             maxConcurrentCalls: 10  // Here you can mention what is the maximum number of concurrent calls that a particular bulkhead pattern can support on top of a REST API.
 *         backendB:
 *             maxWaitDuration: 10ms
 *             maxConcurrentCalls: 20
 *
 * // The above is normal are normal bulkhead configurations where you can only control the concurrent calls. But if you want to control the threads also, with the help of the below thread pool configurations you can assign maximum thread pool size, what is the core thread pool size, what is the queue capacity. So, these are all the properties that you can use to define the bulkhead configurations. I am not going to implement any of these inside our ms's  and we are not going to see the demo of bulkhead pattern in our ms's. The reason is, to see the demo of this bulkhead pattern we need some commercial tools or some performance tools like loadrunner or Jmeter where we can see the thread usage which is a complicated process.
 *
 * resilience4j.thread-pool-bulkhead:
 *   instances:
 *     backendC:
 *       maxThreadPoolSize: 1
 *       coreThreadPoolSize: 1
 *       queueCapacity: 1
 *       writableStackTraceEnabled: true
 *
 * - Up to now, I am assuming you have the clarity of what is bulkhead pattern and how to implement it.
 * In future whenever you have these kind of scenarios, where you want to define the boundaries for your API's inside the ms, you can leverage what we have just learnt in regard to the bulkhead pattern and with the help of your performance testing team you can always try to validate and verify your changes.
 *
 * Aspect order of Resiliency patterns.
 * -----------------------------------
 * As of now, we have discussed various resilience patterns supported by this resilience4j library. We have also gone ahead and implemented these patterns inside our ms's individually. Sometimes you may have complex business logic where you may end-up combining various resilience patterns. In such scenarios, you may have a question like; " What is the order that my resilience library is going to follow if I have multiple resilience patterns defined for a single REST API method or for a single service/routing path? "
 * To understand this, inside the official doc of resilience4j library, https://resilience4j.readme.io/docs/getting-started-3#aspect-order, under the section "Aspect order" you can see various information regarding this. You can clearly see  the order of the patterns that resilience4j is going to follow, i.e, Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) ) with the innermost - Bulkhead - taking 1st priority and the outermost - retry - taking the last priority. Which means retry pattern will be applied at the end. Here 'Function' represents the actual REST API method in your individual ms or the routing path that you have defined inside your gateway server.
 * Sometimes you maybe fine with this default order but if you have some complex scenario where you want this default order then its is going to be super easy with the help of the properties like:
 * - resilience4j.retry.retryAspectOrder
 * - resilience4j.circuitbreaker.circuitBreakerAspectOrder
 * - resilience4j.ratelimiter.rateLimiterAspectOrder
 * - resilience4j.timelimiter.timeLimiterAspectOrder
 * - resilience4j.bulkhead.bulkheadAspectOrder
 * All those I have copied from the official doc. No magic here. In the yml configuration like shown below, using these properties you can define the order of execution i.e.,
 * resilience4j:
 *   circuitbreaker:
 *     circuitBreakerAspectOrder: 1
 *   retry:
 *     retryAspectOrder: 2
 * Also these yml configurations I have gotten from the official doc. The higher the number the higher the priority. So, with the above yml configurations, we are giving higher priority to the retry pattern. Higher priority means higher value which means that it is going to be applied/executed first. Post that only then the circuit breaker pattern will be applied/executed and so on.
 * In the doc you can see that - Circuit Breaker starts after Retry finish its work
 * This was some quick information that I need to give and for more other details including even stuff we have not yet discussed, you can refer to the official doc.
 * In regard to this Aspect order of Resiliency patterns, my instructors humble advice is, "Please don't try to use all these patterns simultaneously in a single ms or in a single routing path, it will be like doing over-engineering and without proper testing, you may get some surprises inside the production." Please do some due diligence and post that only, go with the required patterns inside your individual ms or routing path configurations.
 *
 * Demo of Resiliency patterns using docker containers
 * -----------------------------------------------------
 * As of now we implemented and tested various resiliency patterns inside our local system. To test the same changes using docker containers we have to create docker containers for all the services that we are leveraging in our system with an updated tags to what we had previously. For example, prior to making a change to a service, if its tag name was v5, then the new tag should be v6 to accommodate/capture these new changes. Since you are familiar on how to go about generating docker images and updating docker compose file(s) information, you can go ahead and do it by following the below simple guide:
 * 1. Inside your pom.xml change the tag name inside your jib plugin configuration.
 * 2. Inside your docker compose file change the tag name for the respective docker service container.
 * And btw, inside the GitHub repo of your instructor, you can find maven as well as docker commands that we have been using throughout this course. Make sure in future to replicate this readme file in your GitHub repo as well.
 *
 * Since we introduced a redis service, means we are doing some changes inside our docker compose file i.e.,
 * - Create a new redis service configuration inside your docker compose file i.e.,
 *   redis:
 *     image: redis
 *     ports:
 *       - "6379:6379"
 *     healthcheck:
 *       test: [ "CMD-SHELL", "redis-cli ping | grep PONG" ]  // To perform the health check this is the command that we need to use. You can always get this command from the official documentation of redis as well.
 *       timeout: 10s   // This is the timeout configuration
 *       retries: 10    // This is the retry configuration
 *     extends:
 *       file: common-config.yml   // Post that, I am trying to extend the network-deploy-service service present inside the common-config file so that my redis service will also start under the network of 'eazybank'.
 *       service: network-deploy-service
 * - Reason: To implement the Rate Limiter pattern with the help of Spring Cloud gateway, we need a redis service to store the rate limiter configurations - that is the key(s) i.e., KeyResolver and the value(s) - RedisRateLimiterConfiguration.
 * - After mentioning/defining the redis service related configurations inside the docker compose file, inside the gatewayserver service defined in this same docker compose file, under the dependencies, nothing but the depends-on section, we need to add this new dependency which is related to redis service.
 *   You may have noticed that the poor guy, "gatewayserver" has a lot of dependencies, and he has to start towards the end haha.
 *   So after defining the dependencies information, under the environment variables, nothing but the 'environment' section of this 'gatewayserver' service, we need to provide environment properties/configurations related to redis service i.e.,
 *       SPRING_DATA_REDIS_CONNECT-TIMEOUT: 2s   // This is the connection timeout of redis
 *       SPRING_DATA_REDIS_HOST: redis           // This is the host name. We are using the name of the service that we have created inside our docker compose file. We should not mention 'localhost' here.
 *       SPRING_DATA_REDIS_PORT: 6379            // This is the port number
 *       SPRING_DATA_REDIS_TIMEOUT: 1s           // This is the timeout configuration
 *
 * - At last, make sure to make change related to the tags. I.e.,
 * Accounts/Cards/Loans services previously had the tag name as v5 and now it has the tag name as v6.
 * Gatewayserver service previously had the tag name as v1 and now it has the tag name as v2.
 * Configserver/Eureka services previously had the tag name as v1 and will retain the same as we have not made any changes to it.
 * - So after making all these changes, I have copied the same docker-compose.yml file into the qa and prod profiles. We didn't make any changes inside the common-config.yml file. As a next step we can try to execute the docker compose up command for the production profile and see if everything is working as expected or not.
 * - Open the terminal in the prod profile location and run the docker compose up -d command. This wil start all my containers in detached mode. Post that we can test the resiliency related changes inside our docker environment. Once all my containers successfully started - You can always confirm this in the terminal console. You can also confirm the same by going to the docker desktop. Inside the docker desktop, if you see the logs of gatewayserver, why gatewayserver though? haha It is because it is going to start last after all the other dependent containers are started. That's why. If you can check its logs you can verify tha we have a confirmation saying that , 'Started GatewayServerGatewayServerApplication in 8.59 seconds (process running for 9.595 seconds)'. This confirms that the Gatewayserver application started successfully. With this, we can try to test few changes related to resiliency pattern as below:
 *   The very first url that I want to invoke here is gatewayserver/eazybank/accounts/api/java-version - http://localhost:8072/eazybank/accounts/api/java-version. It's on this API that we implemented RateLimiter inside the Accounts ms. First time if you invoke it you will get an output like: /opt/java/openjdk . Actually we are invoking this API from the browser because it is a GET API. If you try to send multiple requests within a second you will see an output like: Java 17 which is a response from the fallback mechanism that I have defined for my RateLimiter pattern implementation.
 *   We have one more API that we can test with the help of Apache benchmark, the command that I am going to run inside my terminal is - ab -n 10 -c 2 -v 3 http://localhost:8072/eazybank/cards/api/contact-info With this command, I am going to test the Rate Limiter pattern that I have implemented inside the gatewayserver with the help of RedisRateLimiter. You should see some output which we extensively discussed. In summery it shows that, there are a total of 10 requests sent and out of 10 8 are failed. You can be able to see that for majority of the requests we are getting the response like: " 429 Too many requests " - Just like we expect. This confirms that our Resiliency pattern related changes are also working as expected inside the docker containers environment. We will intentionally not test the other pattern like, retry and circuit breaker in the docker environment because for that we need to intentionally introduce some RTE's in our source codes which is something I don't want to do. If I decided to do this in order to test those resiliency patterns will mean I will have to generate other docker images whose sources have these induced RTE's. This is a bit necessary because we already tested these patterns inside our local/dev environment and it will just be the same thing if we had tried to test the same inside the docker container environment.
 * And that is the foundational concepts you need know as far as Resiliency of our ms's/systems is concerned. We have in details at a foundational level learnt how to make our systems/ms's fault-tolerant and resilient in nature. We have put in some substantial efforts and our ms's/systems are more matured compared to before we learnt about these fault tolerance and resiliency concepts.
 *
 * Introduction to Observability and Monitoring of Microservices
 * --------------------------------------------------------------
 * The new challange that we may face while building ms's is observability and monitoring of our ms's. We will in details discuss what is observability, what is monitoring and how we should implement them inside our ms's. Some questions/problems/challenges you may encounter before you learn how this observability and monitoring are going to help us in resolving the questions or problems or challenges that we are going to face include:
 *  Q1. How are we going to debug problems in our ms's? If there is an issue(s) identified inside your ms's, how are you going to debug them? Inside monolithic applications you have only a single application where you can debug an issue very easily but inside ms's network, a request may travel across multiple ms's/containers/servers, how are you going to trace such a request/transaction across multiple services/containers/servers? And how are you going to find out where exactly the problem/defect is? So this is one of the major challange/problem/question that we may have.
 *      Apart from tracing the request, we should also worry about combining all the logs from multiple ms's/containers/servers into a centralized location where the logs can be indexed, searched, filtered and grouped to find the bugs that are contributing to a problem. Inside monolithic applications, the maintenance of logs is very easy because a single application is generating the logs and we can store them inside a folder location and whenever a developer wants to understand the issue, he can easily download those logs and try to scan through them to understand where the issue is. But with ms's it is not going to be the same many containers/services are going to generate the logs inside their own containers/servers and as a developer we can't go into all those multiple locations and try to analyze the logs. Sometimes your request/transaction may travel through more than 20 ms's to give a response and in such scenarios, it is going to be a very tedious job to look for the logs inside all those 20 containers/servers individually. That's why to overcome this challange/problem/question that we may have, we should look for options on how we can combine all the logs from multiple ms's/containers/servers into a centralized location where all the logs can be indexed, searched, filtered and grouped to find the bugs that are contributing to a problem.
 *      With this explanation, you now have clarity about this challange/problem/question.
 *  Q2. How are we going to monitor performance of our service calls? Like we discussed, a single request may travel multiple ms's inside the ms network. If there is a performance issue inside your ms network how are we going to track the path of a specific chain service call through the ms network and see how long it took to complete at each ms? Until unless we know how much time my request is taking in a particular ms, we cannot really identify which ms is taking more time. Once we identify the ms which is taking more time, then only we can try to debug and provide a solution to the issue. That's why identifying a ms which is taking a lot of time to process a request compared to other ms's is very important to monitor and identify the performance issues inside your ms network.
 *      With this explanation, you now have clarity about this challange/problem/question.
 *  Q3. Apart from performance, we should also worry about how to monitor metrics and health of our ms's. We should look for the options using which we can easily monitor the metrics of containers and ms's like CPU usage, JVM metrics, ...etc. Since you will be having 100's of containers/services running inside your ms's network, monitoring each of them using actuator url's is going to be a super complex process. That's why we should look for the options which can help us in monitoring the status and health of our ms's in a single centralized location and create alerts and notifications for any abnormal behavior of the services. Why do we need alerts and notifications? We can't have our team members continuously looking at the dashboards or ms's to understand their health. Sometimes it is not going to be a feasible option to monitor all the ms's 24/7 and that's why we should automatically trigger some alerts and notifications if there is some abnormal behavior inside a particular ms.
 *      With this explanation, you now have clarity about this challange/problem/question.
 *  So, above are the problems/questions/challenges that we may face whenever we are trying to implement ms's inside any organization. How to overcome all these problems? With the help of Observability and Monitoring concepts, we can solve these problems/challenges/questions very easily and in this way, we are going to avoid the outages that may happen inside our ms's. Observability and Monitoring is a very interesting concept/topic. We will see a lot of interesting tools and concepts related to Observability and Monitoring in these sessions. Next we will try to understand details about this Observability and Monitoring concepts.
 *
 * Observability vs Monitoring
 * --------------------------
 * We will discuss in detail about observability and monitoring. What is Observability? It is the ability to understand the internal state of a system by observing or by understanding it's output. For example, in the context of ms's , observability can be achieved by collecting and analyzing data from a variety of sources such as metric, logs, and traces. Using all this information, we can try to understand how our ms's are running internally, how well a particular ms is processing all the incoming requests, or how many errors my ms is throwing. So, all such internal information we can try to understand with the concept of observability. There are 3 pillars for Observability:
 * 1. Metrics - They are quantitative measurements of the health of a system. They can be used to track things like, CPU usage, memory usage, response times, etc. So using this metrics, we can measure our ms's performance or health.
 * 2. Logs - They are a record of events that occur inside a system. We can put log statements inside our method calls so that whenever that particular method is being executed my logs will be generated indicating that a particular record of event is executed successfully. Using these logs, we can track things like errors, exceptions, and other unexpected events. We all know how important logs are while debugging issues. Without logs, it is close to impossible to identify an issue inside the production environment.
 * 3. Traces - Are a record of the path that a request takes through a system. Inside our ms's network, we could have 100's of ms's. A particular request may travel through multiple ms's. So, to understand what is the path that my request travelled inside the m's network we can use traces. Using this traces information we can track the performance of a request at each ms or even finer at each method and with that we should be able to easily identify any performance bottlenecks.
 *
 * By collecting data from these 3 pillars, we can analyze the data and gain a good understanding of the internal state of our microservices and this understanding can help developers to identify and troubleshoot problems. And sometimes, we can also use these information to identify the bottlenecks and improve the performance. Apart from performance and troubleshooting problems, with the help of this data we can also ensure the overall health of the system.
 * By whatever we have just discussed you should have crisp clarity about what observability is.
 *
 * What is monitoring? Monitoring in ms's context involves checking the telemetry data available for the application and defining the alerts for known failure states. We saw that inside observability we use data like logs, metrics and traces for troubleshooting any defects or performance issues. But with the help of monitoring, we can build some dashboards and define alerts and notifications based upon metrics, logs and traces information. Maybe we can try to build a dashboard which can be used by the operations team to monitor the overall health of our ms's. If the CPU utilization of a particular container or a service crossed more than 80% then I want an alert to be triggered. All such scenarios we can achieve with the help of monitoring. Monitoring is very important inside ms's because:
 * +. It will help us to identify and troubleshoot problems. If you have 100's of services running in different containers in different VM's, monitoring all of them 24/7 is going to be a super impossible task  and that's why by collecting and analyzing the data from the individual m's we can identify problems before they course any outages  or other disruptions. For example, if you take the CPU usage example, If a particular ms CPU usage is more than 80% then I can try to add one more instance of the ms so that the traffic will be divided between these 2 instances. So here, using this monitoring information, I am trying to avoid the outage by adding more instances of the ms.
 * +. Using monitoring, we can also track the health of our ms's. Using dashboards, alerts and notifications, we can easily understand which ms is underperformed or which ms is having some network problems or even any other kind of problems that will affect the performance of my ms.
 * +. At last, using monitoring we can try to optimize our ms's. For example by onboarding more number of instances or by killing the ms instance which is having problems and in the same place we can try to onboard a new container or a new ms instance.
 *
 * With all these approaches we are going to improve the performance and reliability of the ms's. You may be curious and having a question that, "But Observability and monitoring somehow look similar", haha. And you may have confusion getting the difference between them. To try to answer the same, " Monitoring and observability can be considered as 2 sides of the same coin, haha." Both rely on the same types of telemetry data to enable insights about your ms's/software distributed systems. These telemetry data types include; metrics, logs and traces. Using this kind of information only, both observability and monitoring are going to work. Next, lets try to understand in details what is the difference between Observability and monitoring.
 *
 * Difference between Observability and monitoring
 * ---------------------------------------------
 * If you try to take the example of an Iceberg, whatever you are able to see on the top of the iceberg, we can call that as monitoring because, we will be able to easily see our ms's health, CPU usage, how many threads are being used, ...etc. All such information we can easily see with the help of monitoring because inside monitoring we are going to build the dashboards, alerts and notifications. But whatever information which you cannot easily see, all such information we call as observability. There might be some NPE inside my ms network, until unless I go and look into the logs, I cannot really understand where the issue is. Such kind of information comes under observability.
 *  - Check with the table on the slide on the differences between Observability and monitoring for more information.
 * In simple words, Monitoring is about collecting data and observability is about understanding data. We can also say, monitoring is about reacting to problems while observability is about fixing them in real-time. Like any RTE's or performance issues. We have to deep-dive into the information to understand the internal state and fix the issues in real-time.
 * With this, you should be clear about the differences between observability and monitoring. Always please note that; Observability will always help you to understand the internal state of the system whereas, Monitoring will help you to identify and troubleshoot problems with the help of alerts, dashboards and notifications.
 *
 * Introduction to centralized logging or log aggregation in ms's
 * ---------------------------------------------------------------
 * We discussed that there are 3 pillars for Observability and Monitoring - Logging, Traces and Metrics. So to implement these concepts inside our ms's network  we need to make sure we are generating all these data so that using them we can try to understand the internal state of our ms's and Monitor them. So, first, lets try to implement log aggregation inside our ms's.
 * Log Aggregation
 * ----------------
 * # Check slides on what logging is. First read the slide and then add more filler notes from your instructor.
 * You may have a question like, how do we perform this log aggregation?  Do I need to write some logic inside my m's as a developer?  Maybe, but that is one of the most basic approach. You can write some logic to save or stream your container logs into a centralized location. But this approach where the developer is responsible to handle the logic of log aggregation has many disadvantages because log aggregation is not related to the business logic. It is a non-functional kind of thing. If you force your developers to worry about the log aggregation also then, you will be wasting their time on unnecessary things like log aggregation. We as developers/intelligent humans we should always focus on the client problems/the business logic. That's why we are going to look for an option which is going to perform the log aggregation without making any changes inside our ms's. There are such beautiful products which we will explore from our next sessions.
 *
 * Introduction to managing logs with Grafana, Loki and Promtail
 * --------------------------------------------------------------
 * On what Grafana, Loki and Promtail are - Check slides. This also includes what they can achieve together.
 * Previously, my Instructor was telling that we should not force developers to implement the Observability and monitoring logic. Instead, we should leverage the tools/best practices that will help us to implement observability and monitoring with less effort from the developer. For the same, in our sessions we are going to use the tools/plugins available inside Grafana. What is Grafana? It is a company which is building a lot of tools and plugins to implement observability and monitoring inside any kind of applications. Not just only ms's/distributed systems but also other types of applications like Web applications, IoT applications, ... etc. For any kind of applications, with the help of the tools provided by  Grafana we can implement observability and monitoring. The reason my instructor chooses Grafana for observability and monitoring is, it provides open-source tools for various scenarios. If you visit their website i.e., https://grafana.com/ and the click on the open source tab, you will see very many open source tools for various scenarios that they provide i.e.,
 * 1. Grafana Loki - If you want to implement a log aggregating system.
 * 2. If you are looking around metrics then you can integrate Prometheus and Grafana and build some dashboards, alerts very easily.
 * 3. For tracing information, you can leverage Grafana Tempo
 * 4. ...etc. This way there are very many tools available inside Grafana for various kind of scenarios. And it has a very good integration with other standards like Open Telemetry, Prometheus, etc.
 * We are going to be discussing in details about these tools. Grafana itself is a very fascinating subject and very many platform/operation engineers, it is a mandatory skill for them to learn about Grafana. In our sessions, we will learn some basics on how to use Grafana, how to build observability and monitoring with the help of Grafana tools. In real projects, it is not the developers responsibility to implement observability and monitoring. The developer has to work with the operations team or Platform team and implement the same. But we will learn about this, so that whenever you have challenges or problems related to observability and monitoring inside your projects and organization you should be able to give some direction to your operations/platforms team, or you can also build some demo applications with the help of all the concepts that we are going to discuss. And with this you will become a superhero inside your projects. When you claim yourself as a microservices developer, you should be aware about all these concepts otherwise it is going to be super tough to clear any interviews around ms's or distributed systems.
 *  - Any interviewer can ask you how observability and monitoring is implemented inside your ms's or distributed applications. If you don't know about these concepts and scratch your head inside the interview room by thinking that as a developer, business logic is my only responsibility then you will never be able to clear any ms/distributed systems interview.
 *  And that was it about a quick intro about grafana. Now we will try to understand how to implement Log aggregation or centralized logging with the help of Grafana.
 *
 *  Implementing Log aggregation or centralized logging with the help of Grafana
 *  ------------------------------------------------------------------------------
 *  When we decide to manage logs with the help of Grafana we need to use other tools available inside grafana ecosystem. Overall, grafana is a large ecosystem under which we have many tools. Based upon our scenario/problem we need to choose these tools and integrate with Grafana. Here, to manage the logs we need to use Grafana, Loki and promtail. Like we discussed in our slides, Grafana is an open-source analytical and interactive visualization web application. It provides various features like we can build charts, graphs, and alerts for our web applications/ms's/distributed systems by connecting with other supporting tools like Loki and promtail. We can easily install this Grafana and it's tools with the help of docker or docker compose or K8S. Overall, Grafana is a popular tool for visualizing metrics, logs and traces from a variety of sources and is being used by many organizations of all types right from start-ups to the enterprise organizations. Majority of projects are using Grafana because it is open-source. That's a quick intro about what Grafana is. Now lets talk about Loki and Promtail.
 *  Grafana Loki -> Is a horizontally scalable, highly available log aggregation system. It is designed to store any amount of logs from your ms's and applications. So, Loki is a centralized location where you can store all your ms's logs. But with Grafana Loki alone we can't implement Log aggregation. We need to have Promtail in the mix.
 *  Promtail -> Is a lightweight log agent that will run inside your same network where your containers are running. It is going to read all the logs that are getting generated from your containers. The same it will collect and send to the Grafana Loki which will store all these logs. And with the help of Grafana, we can easily see the logs inside a UI application. Together, grafana, Loki and promtail provides a powerful logging solution that can help you to understand and troubleshoot your application.
 *  I know you are curious and want to know more about these tools. Check Slide 'Managing logs with grafana, loki and promtail' for more details.
 *
 *  Important note on promtail
 *  ----------------------------
 *  +. From Grafana Loki version 3.0 onwards, Promtail, which is responsible for scraping log lines, has been replaced with a new product called Alloy. Even though we will discuss Promtail in our next few sessions, Alloy will function similarly. Since these are internal components of Grafana Loki, this changes will not have a significant impact. We just need to use the config files related to Alloy in place of Promtail.
 *  +. All docker compose files have been updates with Alloy-related changes inside the GitHub repo. You can find the documentation for Alloy along with Loki in the following Link. https://grafana.com/docs/loki/latest/get-started/quick-start/
 *  +. If for any reason your real projects use older versions of Grafana Loki, then you will need to use Promtail. Promtail-related changes are available in the older branches of the course GitHub repo.
 *
 *  In our previous sessions, we have had a very quick introduction about Grafana, Loki and Promtail. You may have many questions on how are we going ti implement this log aggregation without making any changes inside our m's/distributed systems. For the same, lets see the approach that we are going to take, how we are going to implement it, how the tools like promtail, Loki and Grafanaare going to interact with each other. For this we will have a look at some official documentation information and post that by taking it as a reference we can try implementing the same inside our ms's as well. The official documentation of the grafana loki tool is available in the path https://grafana.com/ >> Open source tab >> Click on Grafana Loki >> Click on the docs link and there you will see the documentation of Loki. You will also notice a dropdown where you can be able to select the documentation to any Grafana tool.
 *  In this Grafana Loki documentation, click on Getting started RHS nav link. If you can scroll down on that page, you will see some beautiful diagram. I didn't see it myself haha. Actually my instructor was using Grafana Loki version 2.8.x while currently I am seeing that as of today Grafana Loki is at Version 3.5.x So yea. But don't worry the diagram he is trying to refer to he has it in our slides the slide, ' Sample Demo of Logging using Grafana, Loki and Promtail '. You can check it out for more around the discussion regarding that diagram. But you can always try to find it out in the new documentation, you should get it, and you will see a detailed explanation. The diagram has explanation on how to implement log aggregation with the help of Grafana and its tools.
 *
 *  Oo, I found a solution. To get to the documentation that my instructor was using to teach. I.e., V2.8.X Click on the Find another Version Link just below the dropdown of the Current Grafana Loki doc >> This will take you to a page with the where you will see a label 'Grafana Labs documentation versions' >> Under this you will see a table and under the label version you can select the label Version 2.8.x and the click the Go to Page button and there you are. You will find everything that your instructor is taking you through under the 'Getting started' section.
 *  - If you scroll, past the diagram that we have discussed - in our slide - that is also present inside this documentation, you will see some steps that we can follow to implement the same architecture discussed inside our ms's network. There are some yml configurations given by the grafana team and by following these same instructions we can implement them inside our ms's as well.  The url to the doc - https://grafana.com/docs/loki/v2.8.x/getting-started/
 *    Read all the details inside that url page. Whatever we have been explained to, the same is present inside this same page. Also, you can check with the slide for your quick reference, ' Sample Demo of Logging using Grafana, Loki and Promtail ' that we have been discussing.
 *
 * If you try to recall the 15 factor methodology that we discussed previously, with this approach we are trying to implement one of the recommendations mentioned inside the 15-factor methodology which is 'logs'. So what does this factor recommend? haha - To treat the logs as events streamed to a standard output and not concern with how they are processed or stored. That's what this factor says.
 *  So, the individual ms's or the developers, they should not worry about streaming or storing the logs. All the log aggregation should happen from the outside. And that is what we are trying to achieve here. I hope you have clarity now. Next we will try to implement the same inside our ms's.
 *
 *  Link to the latest documentation with the architecture diagram we were discussing and steps we will be following - https://grafana.com/docs/loki/latest/get-started/quick-start/quick-start/
 *
 *
 *
 *
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
