package com.get_tt_right.gwserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** Detailed explanation of what this class does.
 * This filter I am trying to define as a bean and that's why you can see the @Component annotation. Its only in this way that my filter class will be recognized by the Spring Cloud Gateway server/Application.
 * We also have annotated it with @Order(1) which means that this filter will be executed first. Sometimes, you may want to define any number of filters and in such scenarios If you want to define an order of execution for your filters then you can use the @Order annotation and mention its input value as 1, 2, 3, 4, 5 and so on. So, based upon the order that you have mentioned, using the same order all the filters will be executed in the same order. So whatever we have defined here as @Order(1) will make sure that this filter will always be executed first inside my gateway server.
 *
 * You can also see that our class RequestTraceFilter implements GlobalFilter. Whenever you want your filter to be executed for all kinds of traffic that comes to your gateway server then you have to implement the GlobalFilter interface. Whenever you are implementing this interface we need to implement a method called + filter(ServerWebExchange, GatewayFilterChain) : Mono<Void>. It is worth noting that, the project of Spring Cloud Gateway Server is build based upon the Spring Reactive module but not based upon the traditional Servlet module. That's why you will be able to see some different code like: ServerWebExchange, Mono<Void> etc. These all are related to the Spring Reactive module. If you are not clear about this Reactive module, don't worry, we will for sure understand whatever code that is present inside this filter classes.
 * Using this ServerWebExchange in reactive projects, we can be able to access the request and response associated with an exchange. Inside Gateway, there can be any number of filters that we can configure ranging from predefined filters to custom filters. All those filters will be executed in a chain manner. That's why whenever I am done executing my own particular filter, I need to invoke my next filter in the chain using the GatewayFilterChain interface reference i.e. chain.filter(exchange). And that's what we are trying to return here i.e., return chain.filter(exchange); The input here has to be the same ServerWebExchange reference that we have received in the filter method. That's why we need to mention this at the end with the help of return chain.filter(exchange).
 * Since we are not returning anything specifically, we need to make sure the return type of this filter method is Mono<Void>. Obviously Void means we are not returning anything specifically from this method, we are just trying to invoke the next filter. Inside the Reactive module, the return type can be either Mono or Flux. Mono is a container that can hold only one value - It short of nothing but indicates that it can hold a single object. Flux is a container that can hold more than one value - It short of nothing but indicates that it can hold more than one object/a collection of objects. Here since we do not have a collection of objects, and we are not returning anything, we can use Mono<Void>.
 *
 * Now, lets try to understand what is present inside this filter method. First, from the exchange reference I am trying to get the request, and from the request I am trying to get the headers. So, inside all my Http request headers, I am checking if there is any header already available that is returned as a result of invoking the isCorrelationIdPresent method. If you can go to this method which is available inside this same class, you can see that it takes the request headers as input. Inside this same method, I am again checking if the output as a result of invoking getCorrelationId(requestHeaders) method of FilterUtility class is !null. If you open this method getCorrelationId(requestHeaders) which is present inside the filter utility class you will see ome docstring that will help you understand what is happening.
 *
 * This is all you need to understand about what we have written inside this RequestTraceFilter class. Nothing fancy or magical here. Just some small logic.
 * Check with the ResponseTraceFilter class for what we have written and discussed inside that class.
 *
 * */
@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    /**
     * Filter method which will be executed as part of the chain of filters inside the Spring Cloud Gateway server.
     * The method takes two parameters: ServerWebExchange and GatewayFilterChain.
     * ServerWebExchange is a class that gives us access to the request and response associated with an exchange.
     * GatewayFilterChain is an interface that provides a method to invoke the next filter in the chain.
     * So, this method is responsible for adding the correlation id to the request headers if it is not already present. So, if the condition is true, it indicates that there is already a header with the same key present inside the request headers. In such scenarios, I don't ant to generate the correlation/trace id again. This check is going to be super useful in scenarios like, my Gateway Server initially received the request from the client, generated a correlation/trace id , added it as a header to the request, but later on due to some re-directions my Gateway server received the same request again, the correlation/trace id is already present in the request headers, so I don't need to generate it again. Hope you are visualizing!
     *  In such scenarios, if my gateway server tries to generate the correlation/trace id again, it will mean I am overwriting the previously generated value which we need to avoid like a plaque. That's why we need to have this if check. Inside the else block, we have logic to generate the trace/correlation id and set the same inside the request using the setCorrelationId method of FilterUtility class.
     *  Simply, this filter is generating a new correlation id. trace id and setting the same inside the request headers. That is if it is not already present. Nothing fancy!
     *  Along with that, we also have logger statements to debug the same.
     * @param exchange This is the ServerWebExchange reference that is associated with the current request.
     * @param chain This is the GatewayFilterChain reference that gives us the capability to invoke the next filter in the chain.
     * @return We are returning a Mono of Void as we are not returning anything specifically from this filter method. We are just trying to invoke the next filter.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
            logger.debug("eazyBank-correlation-id found in RequestTraceFilter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}", correlationID);
        }
        return chain.filter(exchange);
    }

    /**
     * Method to check if correlation id is present inside the request headers.
     * @param requestHeaders The request headers associated with the exchange.
     * @return true if correlation id is present, false otherwise.
     */
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates a correlation/trace id that is unique and random.
     * The default strategy is to use a UUID which is a 128 bit number used to identify information in computer systems.
     * The UUID is generated using SecureRandom number generator.
     * @return a String that represents a unique correlation/trace id.
     */
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}
