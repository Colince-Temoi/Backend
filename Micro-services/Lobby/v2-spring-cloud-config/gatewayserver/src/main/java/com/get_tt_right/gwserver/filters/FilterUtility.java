package com.get_tt_right.gwserver.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Component
public class FilterUtility {

    public static final String CORRELATION_ID = "eazybank-correlation-id";

    /**
     * A utility method to retrieve the correlation id from the request headers.
     * Checks if there is any header present with the key "eazybank-correlation-id".
     * @param requestHeaders The request headers associated with the exchange.
     * @return The correlation id if it is present inside the request headers, null otherwise.
     */
//    public String getCorrelationId(HttpHeaders requestHeaders) {
//        if (requestHeaders.get(CORRELATION_ID) != null) {
//            List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
//            return requestHeaderList.stream().findFirst().get();
//        } else {
//            return null;
//        }
//    }

//    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
//        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
//    }

    /**
     * Sets the correlation id into the request headers associated with the given exchange.
     * Here simple, we are trying to create a new request header inside this 'exchange' the header key being "eazybank-correlation-id" and the header value being the correlation id.
     * @param exchange The ServerWebExchange that you want to set the correlation id for.
     * @param correlationId The value of the correlation id that you want to set.
     * @return The same ServerWebExchange with the correlation id set inside the request headers.
     */
//    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
//        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
//    }

}
