package com.eazybytes.filter;

import com.eazybytes.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
/* This filter is going to validate the JWT token received from the client during subsequent requests.
 * If the token is valid, then it is going to set the authentication object in the SecurityContextHolder. This way we are telling Spring Security that the user is authenticated don't perform any further authentication.
 * Otherwise, it is going to throw an exception.
 * We are also overriding the method shouldNotFilter() and returning true if the request path is "/user" and false for all other requests.
 * This is because we want this filter to be executed any other requests (If any other secured Api is invoked) and not for the "/user" - during login operation.
 * For some reason whenever Jwt libraries are trying to parse the JWT token based upon the secret key that I have passed, they internally try to calculate the
 * hash values and compare the hash values with the hash values that are present in the JWT token. If the hash values are not matching, then it is going to throw an exception.
 * I mean one of the different exceptions that can be thrown by parseClaimsJws() behavior. If you open parseClaimsJws() behavior in the Jwt library, you can see that it can throw multiple exceptions based upon the scenario.
 * One of the exceptions that can be thrown is ExpiredJwtException. This exception is thrown when the JWT token is expired. etc.
 * If I get any of these exceptions, I'm going to throw a BadCredentialsException with the message "Invalid Token received!".
 * After all this logic is executed, I'm going to invoke the filterChain.doFilter() method.
* */
public class JWTTokenValidatorFilter  extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the JWT token from the header
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
        if (null != jwt) {
            try {
                // Get the secret key from the SecurityConstants class.
                SecretKey key = Keys.hmacShaKeyFor(
                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

                // Parse/validate the JWT token and get the claims/body of the token.
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                // Set the authentication object in the SecurityContextHolder if the token is valid.
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/user");
    }

}