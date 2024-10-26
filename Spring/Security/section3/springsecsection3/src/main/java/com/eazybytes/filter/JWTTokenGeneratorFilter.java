package com.eazybytes.filter;

import com.eazybytes.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/** In this code, I have created a class called JWTTokenGeneratorFilter which extends OncePerRequestFilter class.
* I have a very valid business design that my filter should be executed only once per request.
* I don't want to execute this filter multiple times for a single request as I don't want to generate multiple JWT tokens for a single request.
* Logic
* -------
* 1. We want this filter execute ONLY once after initial login operation.
* 2. In all the other requests, we don't want this filter to be executed because at that time we want to validate the JWT token.
* 3. So, I'm going to override the method shouldNotFilter() and return false if the request path is "/user" and return true for all other requests.
*
* Once Authentication is successful. In the doFilterInternal() method, I'm going to get the Authentication object from the SecurityContextHolder.
* If the Authentication object is not null, then I'm going to generate a JWT token using the Jwts.builder() method. Otherwise I'm going invoke the filterChain.doFilter() method.
* To generate a JWT token, my backend server needs to maintain a secret key. I'm going to create a secret key using the Keys.hmacShaKeyFor() method.
* To this method we need to pass the secret key value in bytes with the option of StandardCharsets.UTF_8.
* There is a class SecretKey in Java which is part of the javax.crypto package. In this class reference, I'm going to assign the secret key value that we want to use during the JWT token generation.
* I'm going to use the Jwts.builder() method to generate the JWT token.
* I'm going to set the issuer, subject, claim, issuedAt, expiration and sign with the secret key.
* I'm going to set the issuer as Eazy Bank and subject as JWT Token. This will give some clarity to the client  that which organisation or application has issued so-and-so JWT token. Since in our case, we have imagined a bank application that has issued JWT token, it is Eazy Bank.
* For subject, I'm going to set JWT Token. This is just a string that I'm going to set as a subject. In real time, we can set any string as a subject based upon the business requirement.
* For claim, I'm going to set the username and authorities. I'm going to get the username from the Authentication object and authorities from the Authentication object.
* Like we said before, inside Jwt tokens we should be able to send the logged in user details inside the payload. So, I'm going to set the username and authorities as claims.
* We can set any number of user details inside the claims. But please don't set any sensitive information like password or any other sensitive information because someone can easily decode the JWT token and get the information. Your client application should never ask for sensitive information like password inside the JWT token.
* I'm going to set the issuedAt as the current date and time and expiration as the current date and time plus 30000000 milliseconds (This is approximately 8 hours).
* I'm going to sign the JWT token with the secret key that we have created.
* At last we need to invoke the compact() method which is going to return the JWT token in a string format.
* Once the JWT token is generated, I'm going to set the JWT token in the response header with the key SecurityConstants.JWT_HEADER.
 * Finally, I'm going to invoke the filterChain.doFilter() method.
* */
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Eazy Bank").setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000))
                    .signWith(key).compact();
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/user");
    }

    /**
     * Populates the authorities by collecting the GrantedAuthority objects from the given collection and converting it into a comma separated string.
     * @param collection The collection of GrantedAuthority objects
     * @return A comma separated string of the authorities
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

}