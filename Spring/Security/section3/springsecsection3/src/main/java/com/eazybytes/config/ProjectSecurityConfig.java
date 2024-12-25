package com.eazybytes.config;

import com.eazybytes.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
/** As of now, inside the ResourceServer inside the ProjectSecurityConfig class, we have made the below configuration
 * http.oauth2ResourceServer(rsc ->
 *                 rsc.jwt(jwtConfigurer ->
 *                         jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
 * Here, we are invoking the jwt method and with this method, we are trying to tell to the Resource Server my Auth Server is going to issue a Jwt token and these tokens can be validated locally with the help of certificate downloaded from jwk-set-uri.
 * You can see the jwk-set-uri in the application.properties file. With this setup, right now, the Resource Server is trying to validate the access tokens locally without having any dependencies in the auth server. Only during the startup of the Resource Server
 * it will try to connect to the Auth Server to download the certificate from the jwk-set-uri. We can also see a demo on this theory very quickly.
 * First, try to get a new access token via Postman. And its worth noting while getting an access token, obviously we are going to have a dependency on the Auth Server.
 * By default, the access token life span is 5 minutes. Now, before you make a request to the Resource Server, try to stop the Auth Server. Inside Docker Desktop containers, you can stop the Key cloak Auth Server container. Don't try to delete the container because if you do so you will need to set it up again from scratch and with that, you need to create the client details again from scratch.
 * Now, try to make a request to the Resource Server with the access token you have got from the Auth Server. Click the Send button in Postman. You will see an unsuccessful response. A 500 error. Because when we are trying to send the very first request to the Resource Server it is trying to download the certificates from the Auth Server. But since the Auth Server is stopped, the Resource Server is not able to download the certificates from the Auth Server. And hence it is throwing an error.
 * Previously, I shared that, the certificates are going to be downloaded during the startup of the Resource Server. But, the certificates are going to be downloaded only when the Resource Server is trying to validate the access token which is during the very first request. And this is the proof for that.
 * Now, start the Auth Server again. And try to make a request to the Resource Server. You will see a successful response. This is because the Resource Server is able to download the certificates from the Auth Server, and it is able to validate the access token.
 * As a next step, try to stop the Auth Server again. And try to make a request to the Resource Server. You will see a successful response. This is because the Resource Server has already downloaded the certificates from the Auth Server during the first request. And it is able to validate the access token with the help of the downloaded certificates. Make sure that before making the very first request to the Resource Server, stop it and then start it again. This will guarantee that the first request is made on the Resource Server and subsequent requests are made on the Resource Server when the Auth Server is stopped. And the Resource Server is able to download the certificates from the Auth Server during the first request.
 * Otherwise, you may see some 401 errors.
 *This type of behavior we are seeing because we have configured our resource server to use the Jwt tokens and whenever we configure Jwt tokens inside the Resource Server we should set the jwk-set-uri in the application.properties file otherwise the server is not going to start.
 * As the next step, we want to see how to set up the Opaque tokens format inside the Resource Server so that the Resource Server can validate the tokens for each and every request. What we will do is.
 * First, start the Keycloak Auth Server container. Next, we need to make certain configurations inside the Resource Server.
 * 1. Create a new class KeycloakOpaqueRoleConverter inside the config package. This converter we are going to use whenever we are trying to leverage Opaque token format inside our Resource Server. Check this file for more details and explanation on the code.
 * 2. Configure introspection-uri, introspection-client-id, and introspection-client-secret inside the application.properties file. Check this file for more details and explanation.
 * 3. Comment the below configuration that we had previously made inside this ProjectSecurityConfig class. i.e., http.oauth2ResourceServer(rsc ->
 *                 rsc.jwt(jwtConfigurer ->
 *                         jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
 * Add the below configuration instead. i.e., http.oauth2ResourceServer(rsc ->
 *                                    rsc.opaqueToken(otc ->
 *                                           otc.authenticationConverter(new KeycloakOpaqueRoleConverter())
 *                                                 .introspectionUri(this.introspectionUri)
 *                                                 .introspectionClientCredentials(this.clientId, this.clientSecret)));
 * This time am also going to give the variable name as rsc.Using the same rsc input variable, I'm going to invoke the opaqueToken method. Previously, we were invoking the jwt method.
 * With this opaqueToken method, we are trying to tell to the Resource Server that my Auth Server is going to issue Opaque tokens and these tokens can be validated by invoking an introspection endpoint.
 * We are using a variable name otc - OpaqueTokenConfigurer. With this otc variable, we are trying to set the authentication converter as KeycloakOpaqueRoleConverter. This is the converter that we have created in the first step.
 * Next, we are trying to set the introspectionUri. This is the endpoint that the Resource Server is going to invoke to validate the access token. This introspectionUri is going to be the same as the Auth Server's introspection endpoint. You can get this introspection endpoint from the Auth Server's admin console.
 * Next, we are trying to set the introspectionClientCredentials. This is the client id and client secret that the Resource Server is going to use to invoke the introspection endpoint. This client id and client secret should be the same as the client id and client secret that we have created in the Auth Server.
 * To use the values of introspectionUri, clientId, and clientSecret, we need to inject them into this ProjectSecurityConfig class. For that, we need to use the @Value annotation from the Spring Framework - org.springframework.beans.factory.annotation.Value.
 * We need to create three variables introspectionUri, clientId, and clientSecret. We need to annotate these variables with the @Value annotation. Inside the @Value annotation, we need to pass the key that we have set inside the application.properties file by using a dollar symbol and curly braces. This is how we can inject the values from the application.properties file into our Java class.
 * Those were the only 3 changes we needed to do to let our Resources Server know that it is going to validate the access tokens in the Opaque token format or rather use opaque tokens instead of Jwt tokens.
 * You can do a build of the project and start the Resource Server. You can see that the Resource Server is able to start successfully. For a discussion on what we have discussed above, you can refer to your notebook.
 * */
@Configuration
public class ProjectSecurityConfig {

     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    String clientSecret;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers( "/register","/contact")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/myLoans").authenticated()
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/notices","/contact","/error","/register").permitAll());
        /*http.oauth2ResourceServer(rsc ->
                rsc.jwt(jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));*/
        http.oauth2ResourceServer(rsc ->
                rsc.opaqueToken(otc ->
                        otc.authenticationConverter(new KeycloakOpaqueRoleConverter())
                                .introspectionUri(this.introspectionUri)
                                .introspectionClientCredentials(this.clientId, this.clientSecret)));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
