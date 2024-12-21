package com.eazybytes.config;

import com.eazybytes.filter.*;
import jakarta.servlet.http.HttpServletRequest;
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
/** For the other configurations already present in this class, check your last commit for explanation. You will notice that we have also removed several chunks of code from this class. For why we have removed them, check with your notes from where we started the discussion on setting up a resource server.
* Inside this class we are also trying to configure KeycloakRoleConverter class.
 * I am creating an object of JwtAuthenticationConverter as using this object only we are going to configure the KeyCloakRoleConverter.
 * So, with the help of this object reference I am going to invoke a method called setJwtGrantedAuthoritiesConverter
 * The name oof this method itself is self-explanatory. This method is going to be responsible to convert the roles from the Jwt token to the Granted Authorities. To this method, we need to pass the object of KeycloakRoleConverter.
 * If you open this method, you can see that it is expecting an object of type Converter<Jwt, Collection<GrantedAuthority>>. So, we are going to pass the object of KeycloakRoleConverter, which is a subclass of Converter<Jwt, Collection<GrantedAuthority>>, to this method.
 * Since we implemented the interface Converter<Jwt, Collection<GrantedAuthority>> in the KeycloakRoleConverter class, so we can pass the object of KeycloakRoleConverter to this method.
 * Once this the JwtAuthenticationConverter is configured with the KeycloakRoleConverter. As a next step, we need to go at the end of these configurations and there from our initial configurations, we were trying to perform the form login and http basic style of authentication.
 * Since from now onwards my Springboot application is only going to act as a ResourceServer we should not have these kind of configurations enabled in our application. So, delete these configurations.
 * Instead, if form login and http basic style of authentication, we are going to configure the OAuth2ResourceServer. So, we need to invoke the method oauth2ResourceServer because we want our application to act as a resource server.
 * We need to provide the input as configurations inform of a lambda expression. First, mention rsc -resource server configurations as an input variable name. Using the same rsc variable name, we are going to invoke the method jwt which is going to take the input as a lambda expression.
 * With the help of this jwt method, we are telling to the resource server that the format of the access token is going to be jwt format. We know from our previous discussion that there are two types of access token formats. One is jwt and the other one is opaque.
 * We will see a demo on opaque tokens in the coming lectures. For now we are going to configure the jwt token format. To this jwt method we need to provide the configurations in the form of a lambda expression and that's why I am trying to provide a variable name as jwtConfigurer.
 * Using this jwtConfigurer variable name, we are going to invoke the method jwtAuthenticationConverter. This method is going to take the input as the object of JwtAuthenticationConverter that we have created at the beginning of this method.
 * We also deleted all the /apiLogin and /invalidSession related path from ignoringRequestMatchers and requestMatchers method configurations.
 * The same configurations as present here we also need to make inside the ProjectSecurityProdConfig class.
 * With this we should be good with the changes made inside this file. Do a build and yeey! it is successful.
 * Next, we are heading to the controller package and opening one of the controllers i.e., AccountsController.java file. Here, we need to also make some changes.
* */
@Configuration
public class ProjectSecurityConfig {
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
        http.oauth2ResourceServer(rsc ->
                rsc.jwt(jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
