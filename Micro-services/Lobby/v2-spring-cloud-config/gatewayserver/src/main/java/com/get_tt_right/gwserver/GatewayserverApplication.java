package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

/** Update as of 16/04/2025
 * Retry Pattern
 * -------------
 *It is a resilience pattern.
 * With the help of this pattern, we can configure multiple retry attempts whenever a service is temporarily failing. This pattern is going to be super helpful when transient issues i.e., network disruptions occur and client requests may be successful after a retry attempt. Whenever we want to implement this pattern inside our ms's we need to be very clear on how many times we want to retry an operation. 3,5 or 10 times or even more will be actually based upon your business logic. Like this you will decide your suitable number of retries.
 *  This retry logic you can also conditionally invoke based upon many factors like error codes, exceptions or Response status.
 * While we are retrying an operation, we can follow a backoff strategy whose purpose is: Okay, without this backoff strategy if you try to perform multiple retries, then its is going to be a simple retry operation where every 1 or 2 or 3 seconds the retry operation will be effected. This clearly indicates that there is a static time difference between each retry attempt. With the backoff strategy, you can configure the time difference between each retry attempt and thus help avoid overwhelming the system. With the help of this strategy, we can gradually increase the time difference between each retry attempt. This is what we refer to as exponential backoff.
 *  Maybe the first retry you are trying to perform after 2 sec the next instead of retrying after 2 seconds, you can try to retry after 4 seconds, then 8 seconds and so on. This way, between retries, you are going to wait for more time and with this approach, there is a good chance that you may get a successful response and your transient issues i.e., network issues might have been resolved in between because you are giving enough time for such issues to resolve. That's why it is always recommended to use this backoff strategy whenever we are trying to implement this retry pattern inside the ms's.
 * Also, if needed, we can integrate this retry pattern with other resilience patterns like circuit breaker. By combining both the retry and circuit breaker patterns, we can make our circuit breaker pattern to open after a certain number of retries have failed consecutively. This informs us that there is also a possibility where we can integrate/implement multiple resilience patterns. We will discuss implementing multiple resiliency patterns later. But for now it is worth noting that if needed, we can also implement/integrate both retry and circuit breaker patterns inside our ms's.
 * Another important point to note is, whenever you are trying to implement retry pattern inside your ms's, please make sure that you are implementing this retry pattern only for the operations that are idempotent. An Idempotent operation is one which will not result in any side effects regardless of how many times you invoke them. for example fetch operation. If I try to retry a fetch operation multiple times, there is no harm that will happen behind the scenes as I am always going to get a response. But of you try to implement the same retry pattern for POST or PUT operations, then there is a good chance that you may have some side effects. Maybe you will create multiple duplicate records or maybe you are going to update the record multiple times, which will result into some data corruption or data side effects.
 *   That why, whenever you are trying to implement this retry pattern please make sure that the respective API is of type Idempotent .If the operation/endpoint is not idempotent, then this retry pattern will result into serious side effects inside your ms's.
 *
 * Implementing retry pattern in Gateway Server
 * ---------------------------------------------
 * We will implement retry pattern with the help of Spring Cloud gateway. As of now, in the RouteLocator bean method, we have some route configurations for various ms. For accounts ms route configurations, we have enabled the circuit breaker pattern with the help of circuitBreaker filter. This time, for the route configurations of loans ms, lets implement the retry pattern with the help of retry filter. For the same, on top of addResponseHeader filter, fluently invoke a new filter with the name retry. This retry filter is an overloaded behavior with up to 3 flavours using which you can simply mention/define the number of retries. But other than just mentioning/defining the number of retries, if you also want to specify/define/configure other configurations then you have to choose the right flavour of the overloaded retry behavior that will aid in achieving your objectives.
 *  For us we will invoke the overloaded retry filter behavior that takes/accepts a Consumer implementation of retry related configurations. For this behavior, I am going to write a lambda expression with the input variable name as 'retryConfig' using which we can invoke several setter methods based on what we are trying to configure/define i.e.,
 *   . setRetries - With the help of this setter, I want to let my Spring cloud gateway know, how many times I want to perform the retry operation in case of any transient issues. I will mention 3 as the value. Whic means, I want to retry an operation total 3 times.
 *   . On top of setRetries you can proceed to fluently invoking other setter methods i.e.,
 *     +. setMethods - With the help of this setter, I want to let my Spring cloud gateway know which http methods I want considered for the retry operation. So, I will mention GET as the value. So, I want to retry GET operations. Like we discussed before, we should be very careful with the retry operations amd need to make sure we are performing this retry operation only for idempotent operations.
 *                    A http GET operation is idempotent. This setMethods behavior can accept a list of http methods. For our case, we just want to pass HttpMethod.GET as the value. If needed we can specify any number of http methods as input to this setter whose signature is: setMethods(HttpMethods...methods). The 3 dots means that we can pass any number of http methods as input to this setter.
 *                    As of now, we are trying to enable thos retry pattern only for GET operations. Reason: There won't be any side effects behind the scenes when we are trying to perform GET operations/trying to invoke GET operations multiple times. It is always going to be idempotent. Whereas with POST, PATCH, UPDATE there are chances of side effects. DELETE also should be fine, but I don't want to implement retry pattern for DELETE operations. Reason: We never know if someone screws up the where conditions inside the SQL queries, as we may end up having some side effects. That's why for now, lets only go for GET Http methods/operations.
 *     +. setBackoff  - This setter method is also overloaded with 2 flavours. We have one that accepts multiple input parameters i.e.,  setBackooff(setBackoff(Duration firstBackoff, Duration maxBackoff, int factor, boolean basedOnPreviousValue) and one that accepts a BackoffConfig i.e., setBackoff(BackoffConfig backoffConfig).
 *                     Let's invoke the one with the signature that accepts multiple input parameters. I have something like below:
 *    setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
 *       - As you can see there are 4 different input parameters that we are trying to pass. If you hover on this setBackoff method, you should be able to see what are these 4 input parameters.
 *    1st input parameter - Indicates what is the first backoff that you want to follow. Using this first backoff value, my Spring Cloud Gateway will wait for 100ms whenever it is trying to initiate the very first retry operation
 *    The second input parameter, maxBackoff, Think like, you have configured the number od retries as 10. For these kind of higher number of retries configured, if we kept on applying the factor based upon the previous backoff number, at a certain point in time, the backoff time or the interval between two retry operations is going to be a very high number. That's why in order to control that behavior, we can always define this maxBackoff.
 *                 - With this maxBackoff, at any point of time, my Spring Cloud gateway will wait only for a maximum of 1000ms or 1 second between 2 retry operations.
 *    The third input parameter indicates what is the factor that we want the Spring cloud gateway to apply based upon the previous backoff value.
 *    The fourth input parameter which is a boolean - Based on it, we are trying to tell Spring Cloud gateway whether it need to apply the factor value on the previous backoff value/number or the initial backoff number. Since we are setting this value as true, my Spring Cloud gateway will always consider the previous backoff on top of which it is going to apply the factor value that we have provided.
 *
 * With this, we have configured and enabled the retry pattern for loans ms for all the Rest APIs that supports Http GET method.
 * Now, in order to test these changes. I am going to make some changes inside my loans controller for the endpoint getContactInfo i.e.,
 * 1. I have introduced a logger statement. i.e., logger.debug("Invoked Loans Contact-Info API") - With this logger statement, we can easily identify how many times my Loans ms getContactInfo API is invoked.
 *  If you can clearly see, this getContactInfo REST method is annotated with @GetMapping indicating that it is a Http GET REST method. So, whenever there is a transient issue happens with this GET getContactInfo REST API, my Spring Cloud gateway is going to retry invoking it a total number of 3 times.
 *  You can put a breakpoint at this logger.debug statement as it will help us during our visual demo to verify that indeed what we are discussing is true. Actually the reason for the breakpoint is to intentionally introduce a timeout exception inside my REST API and at the same time you will be able to see visually the retry demo in action.
 *  Now restart your ms's, especiallu Loans and GatewayServer ms's respectively. But make sure before then, the config and eureka servers are up and running. Now, you can go to postman and try to invoke this gatewayserver/eazybank/loans/api/contact-info API. The execution will stop at our breakpoint. Inside postman, you can see we got a response i.e., "error": "Gateway Timeout" with "status": 504. If you check the turnaround time, you can see it took 9.90 seconds. This means that total, there are multiple retry attempts that are happening behind the scenes. Before we implemented this retry pattern, previously we were able to visualize this turnaround time at maximum of 2 seconds which is our global timeout configuration for our gateway server. But since right now we are retrying total 3 times along with the initial first request which will make the total number of requests as 4. So, the total turnaround time will be 9.90 seconds.
 *  We can also verify the same by looking at the log statements in the server console of loans ms. Okay, my logger never executed because we put a breakpoint to it. Now, clean the console and release the breakpoint to resume execution and see what happens. You may see an exception like, " java.net.SocketException Create BreakPoint: Invalid Argument" - This is because, we put this breakpoint here for a long time and due to that, the thread got killed. You will also see a log wit the message, " Invoked Loans Contact-Info API"  in the loans server console. To see the retry pattern in action one more time, this time I am going to release the breakpoint as soon as I get the response on the postman. In the loans ms console, you should see that the log statement, " Invoked Loans Contact-Info API" is executed a total 4 times. So, you can clearly see that the retry pattern is working as expected.
 *  The first one will represent the very first initial request and since the response didn't come withing the first 2 seconds - the global timeout configuration, my gateway might have received a timeout exception and that's why it is again retrying the operation total number of 3 times, the second one will represent the first retry attempt, the third one will represent the second retry attempt and the last one will represent the last retry attempt. With this, you should be crisp clear about this demo.
 *Another approach to visualize this demo is, remove the breakpoint from the logger statement and then just after this log statement you can intentionally throw a RTE  using 'throw new RuntimeException("Intentional Exception");' and this will also help you to visualize this retry demo. As soon as you mention this RTE any code below it will throw a CE because it is never going to be reachable. That's why you can try to comment that code that is not reachable. Now the CE should be gone. Once the build is completed, restart i.e., stop and then start the loans and gateway server services. This time there is no breakpoint, you can go ahead and make a request to the getContactInfo API and you should get immediate response on your postman console i.e., "errorCode": "INTERNAL_SERVER_ERROR"  with "errorMessage": "Intentional Exception". Now if you go to the loans ms console, you should be able to see that the log statement, " Invoked Loans Contact-Info API" is executed a total 4 times.
 *  This should confirm that our gateway server is also trying to retry the operation total number of 3 times even in the scenario os RTE. With this, we are giving an opportunity to our ms to recover whenever we have any transient issues like network issues or temporary outage issues. Like this, there is a good chance that your request may get successful response and your transient issues might have been resolved in between. Since we have enabled the retry pattern inside the gateway server bean definition of type RouteLocator for the routing configurations of Loans ms, this retry pattern is going to be super helpful as it is going to make our ms fault-tolerant and resilient for these kind of transient issues. Reason: behind the scenes, the gateway-server is going to make multiple retry attempts silently without any manual interventions.  Up to now you should be crisp clear about our discussions and visualizations.
 *In a nutshell, we have also mentioned whatever code changes we have made in our slides for the retry pattern. Check them out for a quick review.
 *
 * Something to ponder about!? => In my visual demo, I noticed that after removing the breakpoint and resuming execution after sometime i.e., more than 30' minutes. Even though we already received the response in our client application i.e., postman as "error": "Gateway Timeout", with "message": "Response took longer than timeout: PT2S", after the retries + initial request (4 tries). On resuming the execution, the logic that was paused from execution for the 4 tries will still be executed irrespective of the fact that the negative response is already received. But somewhere in this discussion, my instructor after sometime mentioned something to do with a java.net.SocketException that arose as a result of the execution stopping at the breakpoint for so long and hence resulting into the thread responsible for that paused execution to be killed. But this is not the case. I am not sure why in my case even after the long pause i.e., 30+ minutes the thread is not killed and this is happening. I am also not sure if this is a bug or not.
 *                                Come to think about it! In the meantime, make sure that for retry pattern, you are trying to implement it only for the operations that are idempotent. Otherwise, this retry pattern will result into serious side effects inside your ms's.
 *
 * Implementing Retry Pattern in individual ms's for example: Accounts ms
 * -----------------------------------------------------------------------
 * We will see how to implement retry pattern inside individual ms's like Accounts ms. As of now, we implemented retry pattern with the help of gateway server in the bean RouteLocator under the route configurations of Loans ms. Sometimes we may want to implement retry pattern inside individual ms's like Accounts ms as well. To implement the retry pattern inside the accounts controller of Accounts ms, we have to do the following things:
 * 1. Choose the API on which you want to implement the retry pattern. i.e., /build-info API whose purpose in our case is to always return the current build version right now this environment is using. This API also supports GET mapping and thus this is a good REST API method to implement the retry pattern/operation.
 *    To implement the retry operation on our REST method, we need to use the @Retry annotation.
 *    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
 *    The name input parameter - Represents what is the name you want to give for this retry configuration.
 *    The fallbackMethod input parameter - Represents what the fallback method will be for this retry pattern. In our case, I will mention the same method name with suffix _Fallback i.e., getBuildInfoFallback.
 * 2. Create the fallback method inside the same controller class with the method name as getBuildInfoFallback. While creating a fallback method, we need to follow certain rules:
 *   +. The fallback method signature should match with the signature of the original REST method where we have used the @Retry annotation.
 *      - Actually just copy the entire method and the edit the method name.
 *      - Remove the @Retry annotation from the method.
 *      - This method has to accept the same number of parameters like we have in the original REST method. Since the original REST method does not have any arguments, so the fallback method should also not have any arguments.
 *      - Remove its body so that it is empty. Ins
 *  +. This fallback method should accept a method input parameter of type Throwable.
 *    - Since your original REST method is empty, this fallback method should only have one input parameter of type Throwable. If your original REST method had for example 2 input parameters, then this fallback method should have 2 input parameters from the original REST method + a third input parameter of type Throwable.
 * In the body of our fallback method, I am going to return a static/hardcoded value as a fallback mechanism i.e., 0.9 - This is just some random version number that I want to return. So, whatever logic I have written inside this fallback method will be executed only if the original REST method fails after multiple retries.
 *  - For example, if I configure 3 retry attempts, even after 3 retry attempts, if the original REST method fails, nothing but is not able to return a successful response, then this fallback method will be executed in such scenarios. So, this is one of the advantages we have if you try to implement the retry pattern inside individual ms's. Whereas with gatewayserver, there is no option for us to implement the fallback mechanism. As of now that's the limitation with implementing the retry pattern with the help of gatewayserver.
 *    So far, we have defined the retry configurations inside the @Retry annotation i.e., name of the retry pattern and the fallback mechanism/method. We have also written the fallback method implementation.
 * Now, as a next step, we need to define/configure the properties related to the retry pattern inside the application.yml file.I.e., How many times we want to retry, what is the interval between retries/exponential backoff. Just under the circuit breaker related configurations I have pasted the below properties:
 * resilience4j.retry:  -
 *   configs:
 *     default:
 *       maxAttempts: 3
 *       waitDuration: 100 // When the response is failing and this duration elapses, then the first retry will happen.
 *       enableExponentialBackoff: true  // Enabling the exponential backoff
 *       exponentialBackoffMultiplier: 2   // This is the backoff factor/multiplier
 *       ignoreExceptions:
 *         - java.lang.NullPointerException
 *       retryExceptions:
 *         - java.util.concurrent.TimeoutException
 *
 * The above means that whatever configurations I am trying to define under tha default key are applicable for all retry configurations/implementations that I have done inside my Accounts service.
 * You may be asking yourself like, from where am I getting all these properties. Like we have been seeing before - the official documentation is the answer. If you visit the resilience4j website i.e., https://resilience4j.readme.io/ >> Get Started >> Getting Started(The one for Spring Boot 2&3). You will actually see very many Getting Started for various tools and technologies i.e., Spring Reactor, RxJava 2&3 ...etc. But for us, we are interested in the one for Spring boot as we are building our services using Spring boot.
 *  - Now on the RHS nav, you will see a table of content, therefore click on Configuration hyperlink. Here you will see a good amount of details provided on how to configure various patterns inside your ms's i.e., circuitbreaker, retry, bulkhead, ratelimiter, timelimiter, ...etc.  For example for retry, the same I am trying to refer inside my Accounts ms as well.
 *  resilience4j.retry:
 *     instances:
 *         backendA:
 *             maxAttempts: 3
 *             waitDuration: 10s
 *             enableExponentialBackoff: true
 *             exponentialBackoffMultiplier: 2
 *             retryExceptions:
 *                 - org.springframework.web.client.HttpServerErrorException
 *                 - java.io.IOException
 *             ignoreExceptions:
 *                 - io.github.robwin.exception.BusinessException
 *         backendB:
 *             maxAttempts: 3
 *             waitDuration: 10s
 *             retryExceptions:
 *                 - org.springframework.web.client.HttpServerErrorException
 *                 - java.io.IOException
 *             ignoreExceptions:
 *                 - io.github.robwin.exception.BusinessException
 *
 *  You can see here, instead of 'default' key, they are trying to define different retry configurations for different retry pattern implementations under the 'instances' key. In this case, "backendA' and 'backendB" represents the retry pattern names. And for each pattern name, we have various configurations that have been specified that will be followed. This way, we can also define different retry configurations for different retry pattern names.
 *  The same approach as you can see from the doc can also be applicable for other resilience 4J patterns. As of now, inside our Accounts Controller, we have created only one retry pattern instance with the name 'getBuildInfo'. You need to use the same name inside your yml configuration file or whatever way you are defining your configuration i.e., Java DSL, ...etc. whenever you want to apply retry configurations which are specific to a given retry instance configuration i.e., @Retry(name = "getBuildInfo",fallbackMethod = "getBuildInfoFallback")
 *  Now after mentioning these configurations inside our yml file. As a next step, we need to create some logger statements so that we can have a visual clarity for us to understand how many times our REST method is getting invoked. So make sure you have created a variable/primary attribute of type Logger that accepts the class name as input. Using this 'logger' placeholder I am going to create logger statements inside my getBuildInfo REST method. The logger statement is going to be, logger.debug("getBuildInfo() method invoked"); Very similarly I am going to create a logger statement inside the getBuildInfoFallback method.
 *   With these logger statements we should be able to easily identify how many times a particular method is invoked. As a next step, to see the demo of this retry pattern in action, inside the getBuildInfo REST method, just after our logger statement, intentionally throw a RTE with the help of throw new NPE. With this, always this REST method invocation is going to fail. Comment everything below this defined RTE because it is going to be unreachable whatever the situation. Now once the build is completed stop your Accounts and GatewayServer services respectively and then start them in the same order - In debug mode of course. The reason as to why I am manually restarting these services instead of relying on devtools is: Whenever my applications get restarted they are going to register with the Eureka Server with new instance details. And since I want my gatewayserver to always have te latest information from the Eureka server, that's why I am trying to restart my Accounts and GatewayServer applications manually.
 *   Once both the applications started successfully, inside our postman client we have a request i.e., gatewayserver/eazybank/accounts/api/build-info.  As soon as I make this request I am getting the output as 0.9.
 *
 *  Something to ponder about -> If the request took more than the configured timeout to produce a response. It is very constant that the fallback to the retry will execute - as can be verified from our logger statement inside the getBuildBuildInfoFallback method, but you are not guaranteed to get the response from this fallback method of the retry pattern. In such cases, the FallbackController method i.e., contactSupport inside the gatewayserver will get executed, and you will get its response.
 *
 *  Now, by looking at the Accounts ms console, we can confirm if the retry happened or not. If you search for the string 'getBuildInfo() method invoked', you can see 3 of such logger statements and at last you can see a logget statement related to getBuildInfoFallback i.e.,"getBuildInfoFallback() method invoked". Hope you have seen a difference. haha. Actually, there is a difference between the retry pattern we implemented inside the gateway and that which we have implemented inside our individual ms i.e., accounts ms. Inside the gateway server retry pattern implementation, the try attempts happened a total 4 # of times - The initial request + the configured 3 retry attempts = 4 tries. This we verified from the logs. In our individual ms retry pattern implementation, the initial request is considered among the 3 configured retry attempts hence the total tries to invoking the getBuildInfo REST method will only be 3 as can be verified from the logs. That's why it is important to note that the very initial request is considered as part of the retry attempts whenever you are trying to implement retry pattern inside individual ms's. Don't ever forget this difference, with this clarity accordingly you can implement the retry pattern inside your projects.
 *  Now you should have clarity about the retry pattern implementation inside individual ms including the edge it has over implementing at the gatewayserver.
 *
 *  Demo of circuit breaker timeout
 *  --------------------------------------
 *  For the same, inside the Accounts ms application.yml file, as of now, we have been working with a wait duration of 100ms. Now, I changed this value to 500ms. Post that, once the changes are saved and the build is completed make sure to stop and then restart accounts and gatewayserver applications. This time lets see how the behavior is going to be when we invoke the gatewayserver/eazybank/accounts/api/build-info request. This time we are seeing a response inside our postman client like , An error occurred. Please try after some time or contact support team!!! This response is coming from the circuit breaker fallback mechanism. This means, internally/implicitly my circuit breaker pattern implementation has a time limiter. Using this time limiter, it is going to wait for a maximum period of default value present inside the circuit breaker. Since this default value of circuit breaker is less than the total time taken by the retry operation, the fallback mechanism of circuit breaker is coming into picture and that's whay you are seeing the response i.e., An error occurred. Please try after some time or contact support team!!!
 *   Whenever you end up in this kind of scenario, now you have crisp clarity on what is happening. Don't pannick!  In this case, you can:
 *    1. Either reduce your wait duration under the resilience4j.retry configurations. For example, previously we had 100ms, and it was working fine for us.
 *    2. Or you can also change the default configurations of circuit breaker time limiter. For this:
 *      - Inside the GatewayServerApplication RouteLocator bean definition, under the route configurations of Accounts ms, we have defined some circuitBreaker filter configurations. Nothing but we have enabled the circuit breaker pattern for accounts ms. In this same GatewayServerApplication class, I am going to create a new bean i.e.,
 *      @Bean
 *        public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
 * 		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
 * 				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
 * 				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
 *    }
 *    - I am trying to create a bean of type Customizer<ReactiveResilience4JCircuitBreakerFactory>. Inside the method defaultCustomizer, we have a lambda expression. Lets try to understand what it is:
 *      . using the placeholder factory, which if you hover on you will notice it is of type ReactiveResilience4JCircuitBreakerFactory, I am trying to invoke the configureDefault behavior.
 *      . To this configureDefault behavior, I am passing a new object of Resilience4JConfigBuilder. After defining this object, I am trying to fluently invoke the methods i.e., circuitBreakerConfig and timeLimiterConfig and timeLimiterConfig
 *      . To this circuitBreakerConfig, we are passing some default values.
 *      . After that, we are trying to mention what is the timelimiter that my circuit breaker has to follow using the timeLimiterConfig behavior. As if now we saw/visualized that the time limiter it is following by default is around 1 second. My requirement is to increase that time. For the same we need to invoke this timeLimiterConfig behavior as shown.
 *  - What is time limiter? This is another pattern inside Resilience4J which will help us define what is the maximum time that your application is going to wait to complete a specific operation. So, for our timeLimiterConfig, we are trying to pass an input like, TimeLimiterConfig.custom() and what is the timeoutDuration(Duration.ofSeconds(4)).build() and so we are trying to set 4 seconds. With this, my circuit breaker pattern is going to wait for a maximum of 4 seconds whenever it is trying to wait for a particular operation to complete.
 *  - Once the changes are saved and a build is completed, stop and restart your gatewayserver.
 *
 *  You're maybe wondering from where I got these kind of configurations. How do I know to define this kind of bean with such kind of lambda expressions which look very complex. haha! Boy, like we said, you know Java, and so you can always get all these kind of stuff in the official documentation. And btw as a reminder, before you write something of your own, make sure that Java or Spring people have not written that for you to avoid repeating yourself. Make use of there code. Your job is to just try to understand what they wrote and use it accordingly. Actually, most things have been written for you, just always be aon the lookout before writing your own shitty code. haha!
 *  If you are familiar with Spring Reactive concepts, then this lambda expression is going to be super easy to understand. Otherwise, don't ever worry, you can always find help in the official documentation or from the developer community. Once your gateway server started successfully, inside your postman client you can go ahead and invoke the gatewayserver/eazybank/accounts/api/build-info operation/request one more time and spot the difference as a result of whatever changes we have introduced. This time, you should not get the response as An error occurred. Please try after some time or contact support team!!! withing 4 seconds. You should get the fallback mechanism response from the retry pattern implemented inside our individual Accounts ms. i.e., 0.9. You can also check the console of Accounts ms inside which you should see some logger statements to confirm that there is a retry attempt that happened and since the retry is not successful even after the 3 attempts, the fallback mechanism came into picture.
 *  Up to now, you should be crisp clear of all the changes we have done inside this session.
 *
 *  As of now, we have been trying to  attempt the retry operation whenever there is an issue inside our getBuildInfo REST method of Accounts ms. Regardless of what type of exception happens inside your getBuildInfo REST method, in all such scenarios, the retry attempt will happen. What if there is a business requirement saying that, " Please don't retry whenever there is a NPE. Reason: If there is a NPE with a given input data, regardless of how many times you retry, you will always get a NPE. In such scenarios, we don't want our retry to happen. How to achieve this? Inside the application.yml file of Accounts ms, nothing but inside the application.yml file of the ms where you have implemented the retry pattern, under the retry configuration properties you need to define/add a new property i.e., ignoreExceptions. Under this property, we can define any number of exceptions with the help of hyphen (-). Inside yml, whenever we use the hyphen(-) or dash symbol, that indicates that it is one of the elements inside the list.
 *  ignoreExceptions:
 *         - java.lang.NullPointerException // You need to mention the fully qualified name including the package name like this.
 *         - You can define one more exception here
 *         - ...etc.
 *  Like this I am communicating that, "Please ignore the exception of type NPE as in such scenarios, please do not retry the request. Once the changes are saved and the build is completed, stop and restart Accounts and Gatewayserver applications respectively to verify our changes are working as expected. Now, inside postman if you try to invoke the request gatewayserver/eazybank/accounts/api/build-info you should be able to see the fallback response 0.9 which is coming from the retry pattern. Yes! the retry pattern is activated but, behind the scenes it is not going to make multiple retry attempts, this you can confirm from the console of the Accounts application. You should be able to see only 2 logger statements i.e.,
 * getBuildInfo() method invoked - This comes from the getBuildInfo REST method.
 * getBuildInfoFallback() method invoked - This comes from the getBuildInfoFallback method.
 *
 * This means that, with a single execution, the fallback mechanism to the retry pattern came into picture. Thus the retry is not happening whenever there is a NPE. So, this is one type of requirement that you may have. The other type of requirement that you may have is to retry only for a specific set of exceptions. Just like we have the retry configuration property i.e., ignore-exceptions, we can also define one more property which is retryExceptions as can be seen below:
 * retryExceptions:
 *      - java.util.concurrent.TimeoutException
 *      - You can define one more exception here
 *      - ...etc.
 * This way, I am saying, "Please retry the requests only in the scenario of timeout exception.
 * It is also important to note that, whenever we are configuring the rettry configuration property i.e., retryExceptions, then we don't need to define the ignoreExceptions property. Reason: Whenever we define an exception under retryExceptions configuration property, the retry operation will only happen for such kind of exceptions and all the remaining exceptions will be automatically ignored by the resilince4j. That's why it is important to note that whenever you are defining retryExceptions, you don't have to mention the ignoreExceptions configuration property.
 * Now after making these changes, insde the accounts controller as of now we have been throwing a NPE inside the getBuildInfo REST method. I will change that to TimeoutException. You will face a CE since TimeoutException is a checked exception. This will force us to add a throws TimeoutException statement to the method signature. Now once the changes are saved and the build is completed, inside the postman make the request gatewayserver/eazybank/accounts/api/build-info. This time since we are throwing a TimeoutException, behind the scenes my resilience4j framework might have retried multiple times based upon our configurations. You can confirm the same by checking with the Accounts ms console. Here you should be able to see 3 logger statements with the string "getBuildInfo() method invoked" Even after 3 attempts, since we are not sending a successful response at last the fallback method will get executed as can be confirmed by the logger statement, "getBuildInfoFallback() method invoked"
 * By now, you should be clear on how to configure ignore-exceptions and retry-exceptions based upon your requirements whenever you are configuring retry pattern inside your ms's. You may have a question like, how do we achieve the same whenever we are trying to implement retry pattern with the help of GatewayServer. It is very simple, for the same, if you go to the RouteLocator bean under the loans route configurations where we have defined a retry filter. To this retry filter you can configure more configurations using the predefine setter methods i.e., setRetries, setExceptions  - Where you can specify the list of Exceptions you want the retry attempts to apply, Actually, there is no option for ignoreExceptions here. Very similarly we have setStatuses - Where you can mention Http statuses based on which, the Http status codes, the retry attempts can be applied. Like this, there are several other setter methods which you can explore and use as per your requirements.
 *
 * Bug Fix
 * --------
 * As a next step, we want to fix a small bug that we have inside our GatewayServer application. To understand more about this defect, previously when we were trying to test the retry pattern related changes inside the gateway server, we invoked the request gatewayserver/eazybank/loans/api/contact-info. If you check inside the response headers in your postman client we have eazybank-correlation-id header being populated multiple times with the same value according to my instructor. He said this is happening because whenever there is a retry attempt happening at the gatewayserver, the response filter that we have written to populate this eazybank-correlation-id is going to be executed. Solution to this:
 *  - Open the ResponseTraceFilter class that is present under the filters package inside gatewayserver application. Whenever a response comes, positive or negative, regardless of whether it is an exception response or a response as a result of retry, we are always simply trying to add the header with the name present inside the constant filterUtility.CORRELATION_ID. As a result of what I have just said, due to that the header is getting populated multiple times inside the response. This is not the correct and expected behavior.
 *  - So, we can try and add some if statement. Check this class for more details.
 * In my visualization, it is populating multiple times with different values. This must be a bug on my side as this is not what I am expecting.
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
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}


}
