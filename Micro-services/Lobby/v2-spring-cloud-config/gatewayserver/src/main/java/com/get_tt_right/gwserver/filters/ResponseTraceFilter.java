package com.get_tt_right.gwserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

/**
 * Inside this ResponseTraceFilter class, I have used a different flavor of defining the implementation GlobalFilter. Previously, we have been using the old-school way of implementing an interface i.e., Create a class then use implements key word to implement the interface i.e., RequestTraceFilter implements GlobalFilter. Instead of this approach, we can also define our own custom Filter creating a bean whose return type is GlobalFilter by using the Java-based Spring Configuration class approach.
 * As you can see, I have created Spring Configuration class called ResponseTraceFilter and I have annotated it with the stereotype annotation @Configuration which short of nothing but communicates to the framework that this is a Configuration Class. I have also created a logger reference variable that I can use to log whatever information that I want. Along with this, I also have injected a secondary type i.e., FilterUtility class. Inside this Configurations class I have created a behavior which is going to return a GlobalFilter interface reference i.e., + postGlobalFilter():GlobalFilter.
 * Since we want to convert this returned object reference that we are storing inside GlobalFilter type as a bean, we need to annotate it with the stereotype annotation @Bean. The method name can be anything, in our case I am giving postGlobalFilter(). Try to make sure it makes some sense, that all. Inside this method, I have written a lambda implementation logic which takes 2 inputs (ServerWebExchange, GatewayFilterChain). Inside this lambda, I have written some logic. The logic is again a lambda implementation.
 * By this return chain.filter(exchange).then(...), my ResponseTraceFilter class is going to be executed only after the given request is sent to the respective ms, processed and a response is received back at the gatewayserver application. That's the purpose of then method here. Once we receive the response from a respective ms, we are trying to get the request headers using which we are trying to fetch the trace/correlationid with the help of getCorrelationIdmethod available inside the filter utility class. This will return me the trace/correlationId value and the same trace/correlationid value, I am trying to set inside the response headers with the help of add method. Its key/name is going to be CORRELATION_ID and value is going to be the trace/correlationid value which we set initially inside the request headers.
 * By Mono.fromRunnable(() -> { ... }), we are trying to convert the lambda implementation logic into a Mono type. Which nothing but means that this lambda implementation logic is going to be executed asynchronously in a separate thread.
 * We also have a meaningful logger saying that we have updated the correlation id to the outbound headers. This way, once all our request is processed and while we are sending the response back to the client(s), My Response Trace Filter is going to act as a PostFilter and it is going to intercept the response and add a new header to the response headers. Now, inside our filters, totally we have added 3 loggers,One inside the ResponseTraceFilter, and 2 inside the RequestTraceFilter and all these loggers are of type 'debug' level. In order to print the debug loggers inside the server console or log output file, we need to activate them by mentioning a property inside the application.yml file. The property/configuration is; logging.level.com.get_tt_right.gwserver=debug. With this, we are trying to tell to the Spring boot framework that logger statements that you see inside the packages under com.get_tt_right.gwserver are of debug level, so please print then. In a nutshell, print all of them if they are of type 'debug'
 * With this, we have now created all the required filters for our business scenario. As a next step, we now need to make changes inside the individual ms's because my gateway server whenever it is trying to forward the requests, with it, it is going to send a new request header with the trace/correlationid value. This same header, my ms's have to accept, read the trace/correlationid value and then using this same value, they can try to add some logger statements inside their own business logic. These are the changes we will do next. But before then, make sure you have a crisp understanding on what we have just discussed comprehensively and make sure to understand it well.
 *
 *
 * From the intelliJ, I had the suggestion: Statement lambda can be replaced with expression lambda for the code snippet:
 @Bean
 public GlobalFilter postGlobalFilter() {
 return (exchange, chain) -> {
 return chain.filter(exchange).then(Mono.fromRunnable(() -> {
 HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
 String correlationId = filterUtility.getCorrelationId(requestHeaders);
 logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
 exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);
 }));
 };
 }
 * On, accepting this suggestion, below is the outcome:
 @Bean
 public GlobalFilter postGlobalFilter() {
 return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
 HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
 String correlationId = filterUtility.getCorrelationId(requestHeaders);
 logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
 exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);
 }));
 }
 *  * Bug Fix
 *  * --------
 *  * As a next step, we want to fix a small bug that we have inside our GatewayServer application. To understand more about this defect, previously when we were trying to test the retry pattern related changes inside the gateway server, we invoked the request gatewayserver/eazybank/loans/api/contact-info. If you check inside the response headers in your postman client we have eazybank-correlation-id header being populated multiple times with the same value according to my instructor. He said this is happening because whenever there is a retry attempt happening at the gatewayserver, the response filter that we have written to populate this eazybank-correlation-id is going to be executed. Solution to this:
 *  *  - Open the ResponseTraceFilter class that is present under the filters package inside gatewayserver application. Whenever a response comes, positive or negative, regardless of whether it is an exception response or a response as a result of retry, we are always simply trying to add the header with the name present inside the constant filterUtility.CORRELATION_ID. As a result of what I have just said, due to that the header is getting populated multiple times inside the response. This is not the correct and expected behavior.
 *  *  - So, we can try and add some if statement. Check this class for more details.
  *  With the check, if(!(exchange.getResponse().getHeaders().containsKey(filterUtility.CORRELATION_ID))) {...}
  *   - First I am trying to check if there is any header inside the response with the same key/name. If present, then the outcome will be true and the not condition of true i.e., !true is false and in such scenarios my if block is not going to be executed. Whereas if there is no header with the key/name "eazybank-correlation-id" the expression will be false and !false os true which means that our if block will get executed.
  *   - So, yea, that was the small fix that we are providing to handle the defect that we saw previously.
  * With this, consider us having completed all the foundational discussions about the retry pattern. You got the power now to do whatever you want with it in the right way and based upon your requirements.
  * Now, you can go to the Accounts controller and remove the simulated TimeoutException related line of code that we were using to visualize our simulations and demos and then uncomment our return statement
  * We should now be good!!
  * For a summary around the discussions we've been having in regard to implementing retry pattern in normal/non-reactive Spring boot applications, you can check with the slides.
 *  * In my visualization, it is populating multiple times with different values. This must be a bug on my side as this is not what I am expecting. From the GatewayServer logs, you can also note that the logger statement, "eazyBank-correlation-id generated in RequestTraceFilter" is executing multiple times with different values. Its like for every retry attempt + the initial request a separate traceId/CorrellationId is being generated. My expectation is seeing the correlationId/Trace Id being of the same value for all the retry attempts + the initial request attempt. Try finding out why?
 *  *
 * */
@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    /**
     * Creates a GlobalFilter type that adds the correlation/trace id to the response headers.
     * @return a GlobalFilter type that adds the correlation/trace id to the response headers.
     */
    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
            String correlationId = filterUtility.getCorrelationId(requestHeaders);
            if(!(exchange.getResponse().getHeaders().containsKey(filterUtility.CORRELATION_ID))) {
                logger.debug("Updated the correlation id to the outbound headers: {}", correlationId);
                exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);
            }
        }));
    }

}
