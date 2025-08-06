package com.eazybytes.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {
/* First I am trying to read the CsrfToken available in the HttpServletRequest object.
*  Whenever the token value is generated initially by my backend application, it is going to be available as a Request attribute and the same we are trying to read it and type cast it to CsrfToken object.
*  Using this csrfToken object, first we are checking if there is a header name value available or not inside the csrfToken object.
*  If not null that means my framework has generated the token value and it is available in the csrfToken object.
* Post that, I am setting the header name and token value in the response header.
* Finally, the same response object is passed to the next filter in the filter chain.
* This way, eventually; whenever we are sending a response to the UI application, the CSRF token value will be present inside the header.
* You may have a question like; How about the cookie value? We are just sending the header value. Where is the cookie value?
* That where the beauty of the framework comes into the picture. As a developer, whenever we are sending the CSRF token value in the response header, the framework is going to take care of generating the cookie value and sending the same to the browser/UI application as part of the response.
* */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if(null != csrfToken.getHeaderName()){
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        filterChain.doFilter(request, response);
    }

}