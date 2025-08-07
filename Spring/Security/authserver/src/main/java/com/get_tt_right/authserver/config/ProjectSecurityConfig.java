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

    /** Updated on 8/6/2025
     *---------------------------
     * Demo of Opaque Tokens with Spring Auth server
     * ---------------------------------------------------
     * We are going to make changes inside the auth server and resource server to accept and process Opaque tokens. The amin difference between Opaque and Jwt tokens is - Jwt tokens are self-contained tokens. Which means the resource server can validate the Jwt tokens locally without depending on the auth server for each and every request. With opaque tokens, the resource server is going to rely on the auth server to validate the token received for each and every request. In most of the real scenarios, Jwt tokens are going to be used because they have lot many advantages.
     * Opaque tokens are rarely used for super critical applications. You also have to note that Opaque tokens are going to bring some performance issues as well since the resource server is going to rely on the auth server for each and every request. All these information we already discussed. Anyway, we will see the demo of opaque tokens as well. So, under the client that we have configured that is going to support client credentials, we are going to change the accessTokenFormat(OAuth2TokenFormat.'constraint') constraint from SELF_CONTAINED to REFERENCE. Whenever we configure this REFERENCE as a token format, behind the scenes the auth server is going to generate opaque tokens. If you navigate to the 'REFERENCE' constraint, you should be able to see a docstring  highlighting what this is. I.e.,Reference (opaque) tokens are unique identifiers that serve as a reference to the token metadata and claims of the user and/or client, stored at the provider.
     * Any time the resource server needs more details about the client or about the end user, it needs to rely on the auth server to fetch those details by providing the initial received opaque token. So, yea, that's the first change we need to make inside the auth server. As a next step, we need to make some changes to the resource server so that it can connect with the auth server for each and every token introspection. This kind of change we already made when we were testing the KeyCloak changes - You can review that, but we are also going to see. In the ProjectSecurityConfig in the resource server, you will notice I have commented out some opaque token related changes. Uncomment them and then comment out the jwt related configurations i.e.,
     * /*http.oauth2ResourceServer(rsc ->
     *                 rsc.jwt(jwtConfigurer ->
     *                         jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));//
     *
     *  http.oauth2ResourceServer(rsc ->
     *                 rsc.opaqueToken(otc ->
     *                         otc.authenticationConverter(new KeycloakOpaqueRoleConverter())
     *                                 .introspectionUri(this.introspectionUri)
     *                                 .introspectionClientCredentials(this.clientId, this.clientSecret)));//
     *
     * Don't use the 2 at once. If you are using opaque token configurations, then make sure you have commented out the Jwt token configurations as should above. Reason: The things are not going to work and your auth server is going to fail to start as Spring Security framework is going to happily throw you an exception.
     * So here, rsc. opaqueToken, we are trying to tell the format as opaque token and under the 'otc.authenticationConverter...' we are trying to use the KeycloakOpaqueRoleConverter. Next we are trying to provide what is the introspectionUri and also what is the introspectionClientCredentials.
     * To resolve the CE's un comment the code i.e.,
     *
     * /*  @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
     *     String introspectionUri;
     *
     *     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
     *     String clientId;
     *
     *     @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
     *     String clientSecret;//
     *
     * It is present inside the class ProjectSecurityConfig just on top.
     * As a next step, in the application.properties of the resource server uncomment the below 3 properties ie..,
     *
     * # Opaque Token Configuration
     * #spring.security.oauth2.resourceserver.opaque.introspection-uri= ${INTROSPECT_URI:http://localhost:8081/realms/eazybankdev/protocol/openid-connect/token/introspect}
     * #spring.security.oauth2.resourceserver.opaque.introspection-client-id=${INTROSPECT_ID:eazybankintrospect}
     * #spring.security.oauth2.resourceserver.opaque.introspection-client-secret=${INTROSPECT_SECRET:YwBiML6JqwIwhFengHACDMol7CkwbOpN}
     *
     * Make sure to change the values i.e., In the 1st property, you can see we have the introspection uri of KeyCloak, instead make sure to mention the introspect URI of my Spring Authorization server. This you can get form the http://localhost:9000/.well-known/openid-configuration with the key 'introspection_endpoint'
     * Next, we need to configure the client id and the client secret i.e.,get_tt_right-bankintrospect and YwBiML6JqwIwhFengHACDMol7CkwbOpN respectively. Using this client id and client secret, my resource server is going to invoke the introspection uri. Make sure the 2 are configured in the auth server where we are configuring a client(s). In our case, we want to leverage clientCredClient, so copy/duplicate its configurations and rename it to 'introspectClient'. Make sure the same variable name you pass into the constructor of InMemoryRegisteredClientRepository.
     * So, for the introspect client, change the client id and secret to get_tt_right-bankintrospect and YwBiML6JqwIwhFengHACDMol7CkwbOpN respectively. And for the introspectClient we don't need 'ADMIN' and 'USER' roles in the scope configurations. After making these changes, do a build which will restart the auth server behind the scenes.
     * Now, in the resource server, open KeycloakOpaqueRoleConverter - Inside this class, we have changes related to the KeyCloak scenario. Check its docstring for continuation of this discussion.
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
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .build())

//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        RegisteredClient introspectClient  = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("get_tt_right-bankintrospect")
                .clientSecret("{noop}YwBiML6JqwIwhFengHACDMol7CkwbOpN")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)

//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/")

                .scopes(scopeConfig -> {
                    scopeConfig.addAll(List.of(OidcScopes.OPENID));
                })
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)

                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
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

        return new InMemoryRegisteredClientRepository(clientCredClient,introspectClient,authCodeClient,pkceClient);
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
