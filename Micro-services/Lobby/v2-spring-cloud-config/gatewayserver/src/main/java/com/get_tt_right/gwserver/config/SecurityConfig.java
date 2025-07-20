package com.get_tt_right.gwserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/** Update as of 14/07/2025
 * --------------------------
* */
@Configuration // because we will create some beans inside this class. So, to communicate the same to my Spring framework to create these beans during the startup, I need to add @Configuration annotation to this class.
@EnableWebFluxSecurity
public class SecurityConfig {
    /** springSecurityFilterChain behavior
     * ------------------------------------
     *As of now, inside our gatewayserver, we have just been checking if our client application is authenticated or not. That means, we are only performing the authentication but not the authorization. What if we have a scenario where, we only want to process the request(s) if the client application has certain role(s)/authorities/privileges. In such scenarios, instead of this 'authenticated()' method invocation like we saw in our previous session,on top of pathMatcher(....) behavior, you need to fluently invoke the hasRole(....)behavior.This method receives an input parameter like what role are you expecting which your client should have configured in the auth server. So, for all accounts related APIs, I will be expecting a client to have a role with the name 'ACCOUNTS'. Same drill, I can configure role-based access for cards and loans services as well as can be seen in the springSecurityFilterChain bean method of SecurityConfig. With this, I have now enforced the authorization inside the gatewayserver.
     * **/
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("eazybank/accounts/**").hasRole("ACCOUNTS")
                .pathMatchers("eazybank/loans/**").hasRole("LOANS")
                .pathMatchers("eazybank/cards/**").hasRole("CARDS"))
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

    /**
     * The signature of this method is - grantedAuthoritiesExtractor() : Converter<Jwt, Mono<AbstractAuthenticationToken>>. As can be seen its return type is Converter of Jwt Mono of AbstractAuthenticationToken
     * Inside this method, you can see I am trying to create an object of JwtAuthenticationConverter and assigning that in the reference jwtAuthenticationConverter. Using this reference I am invoking a method setJwtGrantedAuthoritiesConverter. Which means, I need to tell where I have written my logic to convert the roles present inside the JWT token into a GrantedAuthority(s) format. Since we have written all that logic inside the KeyCloakRolesConverter.java class, we need to pass the object of the same as can be seen in the code below with the help of new KeyCloakRoleConverter() object.
     * At last, I need to pass this object of JwtAuthenticationConverter to the constructor of ReactiveJwtAuthenticationConverterAdapter class. So, from the return statement, it is going to return an object of the data type like Converter<Jwt, Mono<AbstractAuthenticationToken>>
     * Once we have created this method, we need to mention it inside the 'springSecurityFilterChain' bean method of SecurityConfig. Before this method, we were using the default configurations i.e., Customizer.withDefaults() as an input to our jwt() method which meant that, we are telling to the Spring Security framework that "Please go with your default assumptions, I don't have any role(s) specific information". But now since we have written some role(s) specific information that we need to be processed by the Spring security framework, we need to make sure we are removing the jwt method input i.e., Customizer.withDefaults() and post that, we need to write a Lambda expression by creating some lambda variable like 'jwtSpec' and on top of this reference I am going to invoke a method which is jwtAuthenticationConverter() and inside this method, I am going to invoke the method grantedAuthoritiesExtractor() as an input.
     * This way, I will have established a link about my KeyCloakRoleConverter class with the Spring security framework inside my Spring security configurations in the springSecurityFilterChain bean method of SecurityConfig.
     * Now, we should be good to go.
     * */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeyCloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
