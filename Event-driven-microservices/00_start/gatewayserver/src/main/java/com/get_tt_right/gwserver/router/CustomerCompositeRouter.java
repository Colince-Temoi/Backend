package com.get_tt_right.gwserver.router;

import com.get_tt_right.gwserver.handler.CustomerCompositeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/** On top of this class we are mentioning, the @Configuration annotation. To this annotation we are going to mention/define the proxyBeanMethods attribute with the value false - this is nothing but an optimization flag which will improve the startup performance of our gateway server.
 * Inside this class, I am going to create a method - route - which is going to return a bean and that's why we are first mentioning the @Bean annotation on top of it. The return type of this method is RouterFunction which wraps the ServerResponse. So from this RouterFunction we expect ServerResponse as an output that's why we are mentioning it inside the tags. To this method, we are going to pass the bean of CustomerCompositeHandler. So, whatever actual business logic that we have defined inside the CustomerCompositeHandler class, the same we are trying to provide as an input bean to this method.
 * Now from this method, I want to return RouterFunctions. Using this RouterFunctions, we are invoking a method route which is going to accept a predicate and a handler function - if you hover on it, you will be able to see its signature. So here, with the help of RequestPredicates, we are going to invoke GET and to this GET we are going to pass the API path which is "/api/composite/fetchCustomerSummary". With this what we are telling is, whenever someone is trying to make a GET request to my gatewayserver with the mentioned/defined path, then I want my CustomerCompositeHandler to handle this request/to be invoked. Inside my CustomerCompositeHandler, I have a method which is fetchCustomerSummary which is going to handle this request/which is going to be invoked.
 * Like this, it is very simple! We are nothing but just trying to define a route of type GET with the specified path/value such that whenever someone invokes this we want the method fetchCustomerSummary to be invoked. Apart from just mentioning the API path, we can define more conditions using and method. To this and method, I am trying to pass a predicate condition with the help of this accept method - here, I want my logic to be invoked ONLY if someone sends the input data using JSON format. Similarly, we are defining one more condition and this time with the same RequestPredicates we are trying to invoke the queryParam method and to it we are trying to pass some mobile number. Btw, if you hover over this queryParam method you will notice that it accepts 2 parameter the first one being String and the 2nd one being a predicate condition under which you want to accept this query param. As of now, I don't have any specific predicate conditions and that's why what I am going to do is I am going to write a simple predicate condition/lambda expression i.e., param -> true. which is going to always return true.
 * With this we have defined multiple predicate conditions that a give request has to pass. So, whenever someone wants to invoke the logic present inside the fetchCustomerSummary method, then it has to pass all these conditions which are; 1. They need to make sure they are sending a GET request to the mentioned path 2. They need to send the data in JSON format 3. They need to pass the mobile number in the query param named mobileNumber. If you don't follow these conditions, then the logic inside the fetchCustomerSummary method will not be invoked.
 * */
@Configuration(proxyBeanMethods = false)
public class CustomerCompositeRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CustomerCompositeHandler customerCompositeHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/api/composite/fetchCustomerSummary")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                        .and(RequestPredicates.queryParam("mobileNumber", param -> true)),
                customerCompositeHandler::fetchCustomerSummary);
    }
}