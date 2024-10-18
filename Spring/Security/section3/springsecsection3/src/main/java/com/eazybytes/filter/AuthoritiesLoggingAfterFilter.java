package com.eazybytes.filter;

import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthoritiesLoggingAfterFilter implements Filter {

    private final Logger LOG =
            Logger.getLogger(AuthoritiesLoggingAfterFilter.class.getName());
/* In this code, I have created a class level field for the Logger class. This is going to be used to log the user authentication details.
*  Next, I have overridden the doFilter method. Inside this method, I am trying to read the Authentication object, current authenticated user details,  from the SecurityContextHolder.
*  As you know,once the authentication is successful, the Authentication object is going to be stored in the SecurityContextHolder.
* If this object is not null, means authentication is successful ,then I am trying to log the user name and the authorities assigned to the user.
* Finally, I am passing the request and response object to the next filter in the chain.
* */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            LOG.info("User " + authentication.getName() + " is successfully authenticated and "
                    + "has the authorities " + authentication.getAuthorities().toString());
        }
        chain.doFilter(request, response);
    }

}