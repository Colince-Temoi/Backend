package com.eazybytes.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class RequestValidationBeforeFilter  implements Filter {
/* First I have created these 2 class level fields. One is for the authentication scheme and the other one is for the credentials charset.
*  The authentication scheme is going to be used to validate the incoming request header value.
*  The credentials charset is going to be used to decode the credentials value.
* */
    public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
    private Charset credentialsCharset = StandardCharsets.UTF_8;

    @Override

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//        First I am trying to typecast the incoming request and response objects to HttpServletRequest and HttpServletResponse objects.
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
//        Next, I am trying to read the Authorization header value from the incoming request object. Which will be send by the UI application as part of the request header.
        String header = req.getHeader(AUTHORIZATION);
        if (header != null) {
/*          Next, I am trimming the header value so that if there are any leading or trailing spaces, they will be removed.
            If you go inside my Angular application inside the interceptor class that I have, you can see some code like;
            if(this.user && this.user.password && this.user.email){
      httpHeaders = httpHeaders.append('Authorization', 'Basic ' + window.btoa(this.user.email + ':' + this.user.password));
    }
Here, we are sending an Authorization header with the value Basic followed by space and post that, we are trying to send the Base 64 value of my email and password separated by a delim colon (:).
            I am also using the StringUtils class to check if the header value starts with the authentication scheme.
            If it does, then I am going to decode the credentials value.
            The decoded value is then split using the delimiter ":" and the first part of the split value is the email.
 */
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
//                The same header is what I'm trying to read here. What is happening here is, from my header; I'm extracting
//                the string from my 6th index to the end of the string and converting it to a byte array. Because we don't want the initial string which we have
//                in the authorization header, 'Basic '.  Actual extraction starts from the 7th index. We are ignoring both Basic and space.
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decoded;
//                I'm trying to decode the base64Token value using the Base64 decoder.
//                Once done, I'm trying to separate the email and password using the delimiter ":".
                try {
                    decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, credentialsCharset);
                    int delim = token.indexOf(":");
                    if (delim == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }
//                    With the help of this delimiter, we are going to use the substring method to extract the email value. This is the username which we are going to use to validate the user.
                    String email = token.substring(0, delim);
//                    Once we have the email value, we are trying to check if the email value contains the string test. If it contains the string test, then we are going to set the status of the response object to SC_BAD_REQUEST.
//                    Otherwise, if validation passes, then we are going to invoke the next filter in the filter chain.
                    if (email.toLowerCase().contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }
            }
        }
        chain.doFilter(request, response);
    }
}