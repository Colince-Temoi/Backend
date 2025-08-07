package com.get_tt_right.authserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /** Updated on 8/7/2025
     *---------------------------
     * Revert back to Self_contained/jwt token scenario
     * ---------------------------------------------------
     * Not recommended to use the Opaque/reference token scenario and so revert back the changes we have done in regard to opaque token scenario back to Jwt token scenario in both the auth server and the resource server i.e.,
     * 1. Change any clients configured inside registeredClientRepository to have the accessTokenFormat constrained to SELF_CONTAINED instead of REFERENCE. With this, the auth server is going to generate the Jwt token format
     * 2. In the resource server side, inside the ProjectSecurityConfig, comment and uncomment out the code ie.,
     *   /*@Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
     *     String introspectionUri;
     *
     *     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
     *     String clientId;
     *
     *     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
     *     String clientSecret;//
     *
     *     /*http.oauth2ResourceServer(rsc ->
     *                 rsc.opaqueToken(otc ->
     *                         otc.authenticationConverter(new KeycloakOpaqueRoleConverter())
     *                                 .introspectionUri(this.introspectionUri)
     *                                 .introspectionClientCredentials(this.clientId, this.clientSecret)));//
     *
     *   http.oauth2ResourceServer(rsc ->
     *                 rsc.jwt(jwtConfigurer ->
     *                         jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
     *
     *  In the application.properties file of the resource server also comment and uncomment the lines of code like:
     *    #spring.security.oauth2.resourceserver.opaque.introspection-uri= ${INTROSPECT_URI:http://localhost:8081/realms/eazybankdev/protocol/openid-connect/token/introspect}
     *    #spring.security.oauth2.resourceserver.opaque.introspection-client-id=${INTROSPECT_ID:get_tt_right-bankintrospect}
     *    #spring.security.oauth2.resourceserver.opaque.introspection-client-secret=${INTROSPECT_SECRET:YwBiML6JqwIwhFengHACDMol7CkwbOpN}
     *
     *    spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${JWK_SET_URI:http://localhost:9000/oauth2/jwks}
     *
     *  You can now do a build of your auth server and resource server and try to test the flows once more again just to make sure the Jwt token format is working fine as expected. Hurreey! everything worked as expected.
     *  With all we have discussed, we can call it a wrap as far as security is concerned. Now, you have the power to read - the docs of course.
     *  Disclaimer!
     *  ------------
     *  Spring Authorization server framework is a pretty new framework in the Spring ecosystem and is going to evolve a lot in the coming days. My humble request is keep yourself updated with the docs for any newer stuff and find out how you can make your auth server even better.
     *  So, that's more or less around the 'Getting started' with Spring Authorization Server project. For any questions and doubts or anything whatsoever, always check with the official documentation - you are big boy now. There is a 'How-to Guides' section in the official documentation that can answer most of you questions.
     *
     * */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient clientCredClient  = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("get_tt_right-api")
                .clientSecret("{noop}VxubZgAXyyTq9lGjj3qGvWNsHtE4SqTq")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)

//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/")

                .scopes(scopeConfig -> {
                    scopeConfig.addAll(List.of(OidcScopes.OPENID,"ADMIN","USER"));
                })
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)

                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())

//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        RegisteredClient authCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("get_tt_right-client")
                .clientSecret("{noop}Qw3rTy6UjMnB9zXcV2pL0sKjHn5TxQqB")
//                .clientSecret(passwordEncoder.encode("Qw3rTy6UjMnB9zXcV2pL0sKjHn5TxQqB"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope(OidcScopes.OPENID).scope(OidcScopes.EMAIL)
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(8)).reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build()).build();

        RegisteredClient pkceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("get_tt_right-publicclient")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .scope(OidcScopes.OPENID).scope(OidcScopes.EMAIL)
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                        .refreshTokenTimeToLive(Duration.ofHours(8)).reuseRefreshTokens(false)
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build()).build();

        return new InMemoryRegisteredClientRepository(clientCredClient,authCodeClient,pkceClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claims((claims) -> {
                    if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
                        Set<String> roles = context.getClaims().build().getClaim("scope");
                        claims.put("roles", roles);
                    } else if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.AUTHORIZATION_CODE)) {
                        Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .map(c -> c.replaceFirst("^ROLE_", ""))
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.put("roles", roles);
                    }
                });
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance()); // This line this defeats the security purpose of bcrypt, and should be avoided in production. Make sure to comment it.

        DelegatingPasswordEncoder delegatingPasswordEncoder =
                new DelegatingPasswordEncoder(encodingId, encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        // Set a default encoder for matching passwords without a prefix

        return delegatingPasswordEncoder;
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
