package com.get_tt_right.gwserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/** Update as of 09/04/2025
 * Inside this controller I have mentioned @RestController annotation since we want to build some REST APIs.
 * We have created a REST API method with the name contactSupport whose path is "/contactSupport". Inside this method, I am returning a business error saying that "An error occurred. Please try after some time or contact support team!!!" This is a simple business logic that I have written as a fallback mechanism for circuit breaker pattern. In your real applications you may have some complex fallback requirements like:
 *  - Triggering an email to the support team.
 *  - Or sending some default response to the client as we have done inside the contactSupport method.
 *  - ...etc.
 *  So, it's up to your client requirements, and accordingly you can write the fallback logic. As of now, we have written and trying to send a simple business error message from my fallback REST API method.  Since we are trying to build a REST API inside the gatewayserver application that is implemented on top of Spring Reactive, we need to make sure we are wrapping the return String with the help of Mono<>. Very similarly, for the return statement, we have to use Mono.just(). To this just method we have to pass the String or error message that we want to send to the client applications or to the UI applications.
 *  Now the REST API that we want to invoke as a fallback mechanism is ready! As a next step, we need to integrate this REST API into our circuit-breaker pattern. For the same, inside the GatewayServer applications where we are defining the circuit breaker pattern. To the circuit breaker lambda configurations, invoke the set the setFallbackUri(-) which takes a String fallbackUri (Nothing but the fallback REST API path) parameter. Check the GatewayServerApplication class for how we have done this.
 *
 * */
@RestController
public class FallbackController {
    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error occurred. Please try after some time or contact support team!!!");
    }
}
