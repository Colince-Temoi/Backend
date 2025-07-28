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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
/** Updated on 7/26/2025
 * --------------------------
 * On top of this class I am going to mention a couple of class level annotations: i.e., @Configuration annotation which is very obvious. The next one is @EnableWebSecurity. Inside this class we need to define a good amount of configurations. Let's go to the official website and understand what kind of configurations we need to define and accordingly we can make the configurations inside this file. So, navigate to the site Spring Authorization server project https://spring.io/projects/spring-authorization-server and then click on the Learn tab and navigate to the latest reference doc. As of today we are at the version 1.5.1. My instructor at the time of teaching we were at 1.3.1 version of this project Spring Authorization Server. We will try to learn how to read these documentations and accordingly in future if something changes you should be able to handle that by yourself. My instructor believed that a lot is going to change in Spring Authorization Server project in the future. Because at that time, it was supporting a bare minimum amount of features. This is not going to be a fancy product like KeyCloak where we have a beautiful admin UI console to perform everything. This project as of now, it does not support any admin UI. Everything we have to achieve with the help of Java code or application properties configurations. My Instructors assumption is that this Project is going to evolve a lot in the future.
 * So, click on the Getting Started button.  Here you can see we have information like; to set up the spring Authorization server, we need to add the dependency like: spring-boot-starter-oauth2-authorization-server inside our pom.xml file if you are building a Spring Boot application, otherwise for normla Spring based applications we have to add the same dependency but with a version tag i.e., <version>1.5.1</version>. Since we are building a Spring Boot application, we added the dependency without the version tag and we are good from this first step perspective. In the next step, "Developing Your First Application", they are trying to explain on how to set up Spring Authorization Server related configurations with the help of properties that we can define inside the application.properties or application.yml file. We will not follow this approach because this approach is going to be super complex and is not going to support the majority of the complex scenarios that we have. But for simpler applications this properties approach may work. So skip this approach and we will go to the other approach which is: "Defining Required Components" programmatic approach where we can configure everything with the help of Java methods/classes and Java beans. Once familiar and clear with this programmatic approach, then these properties approach is also going to make sense to you and how to use them. For now, don't worry about these properties approach and how to use them.
 * Once you get clarity on how we are going to approach the programmatic stuff then obviously every other thing is going to be clear for you. So as can be seen, we have already created a Class and annotated just like the one in the doc is. Next, I am going to copy the contents of this entire class and pasted inside this Class of ours as can be seen below. Next, we need to resolve the compilation errors by importing the corresponding classes. The SecurityContext class I need to import from the package com.nimbusds.jose.proc.SecurityContext. Pronounce numbusds as "nimbus ds" jose as "jos" proc as " p r o c". Now, lets try to analyze these configurations one by one. As a very first step, they are trying to create a bean of type SecurityFilterChain. The same kind of bean which we created inside our normal Resource server while learning Spring security and also while learning Ms's security. Check its method docstring for more details.
 *
 * */
@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {

    /*
    * Inside this method they are trying to pass the input parameter of type HttpSecurity and return the SecurityFilterChain.
    * The method input, they are trying to pass it as an input to the method OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http) which is available inside the OAuth2AuthorizationServerConfiguration. As of 1.3.1 version. With the help of that line they are trying to apply some default configurations related to the Authorization Server. Next with the help of the same 'http' input variable, they are trying to invoke the getConfigurer method and to this getConfigurer method, they are passing the OAuth2AuthorizationServerConfigurer class as an input. At last, they are fluently invoking the oidc method and to this method they are passing the Customizer.withDefaults() method as an input. If you navigate into the withDefaults method you will notice it is an empty lambda expression.
    *  With this, what they are trying to do is; they are trying to enable the OpenID Connect 1.0 protocol inside this Authorization Server. After that, they have defined some exception handling configurations. So, whenever some exception happens, we are trying to configure a default authentication entrypoint with the help of defaultAuthenticationEntryPointFor behavior. With the help of this defaultAuthenticationEntryPointFor method, we are going to redirect the end-user to the login page. So, whenever there is an exception happened, the redirection of the end-user to the login page is going to happen. At last they are trying to convert this authorization server as an oauth2ResourceServer. They have also mentioned the reason i.e., To accept access tokens for User Info and/or Client Registration purpose.
    * At last, they are trying to invoke the build method and return the SecurityFilterChain. So, this is a bean with order 1. Very similarly, inside this class, there is another bean with the same type i.e., SecurityFilterChain being created but with the order as 2. This means that the bean with order1 is going to be created first and then the bean with order 2 is going to be created next. Check its docstring for more details.
    * */

//    1.3.1 version bean
    /*
@Bean
@Order(1)
public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
        throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
    http
            // Redirect to the login page when not authenticated from the
            // authorization endpoint
            .exceptionHandling((exceptions) -> exceptions
                    .defaultAuthenticationEntryPointFor(
                            new LoginUrlAuthenticationEntryPoint("/login"),
                            new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                    )
            )
            // Accept access tokens for User Info and/or Client Registration
            .oauth2ResourceServer((resourceServer) -> resourceServer
                    .jwt(Customizer.withDefaults()));

    return http.build();
} */
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
/* For SecurityFilterChain bean type with order 2, everything is still the same as was in version 1.3.1
* Here, they are trying to apply anyRequest dot authenticated, and they are also enabling the form login with the default configurations.
* You may have a question like; "Why have they defined 2 different beans of type SecurityFilterChain?" Very simple. In the very first bean, they have tried to define all the configurations which are specific to the auth server. Coming to the second bean, they are trying to configure this dot authenticated on form login. At the end of the day, the authorization server  also is going to also expose some secured API and secured pages. So,all these pages/apis since they have to be authenticated properly and in the case whenever we want to access these pages, we should be able to access them by entering our credentials with the help of form login approach. That's why they tried to create 2 different beans of type SecurityFilterChain. And that's why we are trying to follow the same inside our class as well.
* Next, check the bean userDetailsService docstring for more details
* **/
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

    /* Here they are trying to create some dummy users inside the memory of the application. They have created a user with the name 'user', password as 'password' and role as 'USER'.
    * At last, they are trying to store these user details with the help of InMemoryUserDetailsManager approach.  With this approach, we need to hard code the user details like we can see here. But since we are looking for an option where we can authenticate the end-user with the help of MySQL db, we have commented out this bean, and we are going to configure all the required changes that are needed to authenticate an end-user with the help of MySQL db.
    * This we will discuss in the next session. Check the next bean for details around it.
    * */
/*    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }*/

    /** This bean is of type RegisteredClientRepository. If you can recall, inside KeyCloak, whenever we want to register a client application; we will go to Admin console >> and try to configure/set up some client details. But coming to the Spring Authorization server framework, here we don't have an admin console, so whenever we want to register a client, this is how we have to register it, we need to register a client with the help of RegisteredClient class.
     * Inside this class we have so many methods like; withId(), clientId(), clientSecret(), clientAuthenticationMethod() - What is the type of authentication method, authorizationGrantType() - What is the authorization grant type that your client is going to support, redirectUri(), postLogoutRedirectUri(), scopes(), tokenSettings(), etc. Once the RegisteredClient object is created, they are trying to pass the object if it to the InMemoryRegisteredClientRepository.
     * This means that all the clients that we are going to configure, they are going to be saved inside the memory of the application. If you open the class InMemoryRegisteredClientRepository, you will notice that it implements the RegisteredClientRepository interface which if you open and on the LHS side open its implementations, you can clearly see that we also have a class i.e., JdbcRegisteredClientRepository extending it. So, in case if you are looking to store all the client details inside a database, we can leverage the JdbcRegisteredClientRepository class. Otherwise, if you are looking to store the client details inside a db with your own custom columns and table(s) structure then you need to implement the interface RegisteredClientRepository and create a custom implementation of it. Nothing but your own Client Repository class just like the Spring people created for you an example i.e., JdbcRegisteredClientRepository.
     *  And btw, something I have noticed and am re-iterating to you, Spring people will never leave you to do things on your own when using their specifications. If you navigate to their specifications, they will at least have created you an example implementation for it which you can either use directly or can follow as an example when creating your own custom implementation just like we have seen for the JdbcRegisteredClientRepository class and the interface/specification it is implementing i.e., RegisteredClientRepository. Inside the interface RegisteredClientRepository, you can see we have rules/method(s) to save the client details, findByID - Find the client details based upon the ID, findByClientId - Find the client details based upon the client ID, etc. So, for our small application, InMemoryRegisteredClientRepository as in the documentation should just be fine for now. So we will follow that, because in general, inside a real application as well, we may have at max - 10's of clients of applications. We will not have 100's or 1000's of client applications that are going to register with the auth server. So, if you have a limited number of client applications, you should just be happy with the approach InMemoryRegisteredClientRepository. Otherwise, you can go ahead and store the client details inside a DB by implementing the interface RegisteredClientRepository and create a custom implementation of it or using the JdbcRegisteredClientRepository class that Spring team has created for you.
     *  So, inside this bean method, registeredClientRepository, we are going to define our own clients that supports Client Credentials Grant type flow, Authorization Code Grant type flow,  PKCE Grant type flow, Refresh Token Grant type flow, etc. We will do this in the coming sessions. For now we are not changing anything inside this method as is in the documentation. Check out the docstring to the next bean that we have around JWKSource.
     * */
// As is in the documentation
    /*
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }
*/

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient clientCredClient  = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("eazybankapi")
                .clientSecret("{noop}VxubZgAXyyTq9lGjj3qGvWNsHtE4SqTq")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)

//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/")

                .scopes(scopeConfig -> {
                    scopeConfig.addAll(List.of(OidcScopes.OPENID, OidcScopes.PROFILE,"ADMIN","USER"));
                })
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)

                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(10))
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        .build())

//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        RegisteredClient authCodeClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("eazybankclient")
                .clientSecret("{noop}Qw3rTy6UjMnB9zXcV2pL0sKjHn5TxQqB")
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
                .clientId("eazypublicclient")
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
/** Any Auth server that is build around the OAuth2 standards, behind the scenes it is going to generate private and public certificates or keys. Using the private Key or certificate, the auth server is going to digitally sign the access tokens, id tokens or any other tokens. On the Resource Server side, they should be able to validate this token(s) locally by using the public certificate/Key. So, the bean method jwkSource is going to take care of generating a public and private key during the start-up.
 * It is also going to use the helper method which is generateRsaKey. Navigate to this method to see a docstring of what it is doing. For this bean method jwkSource, we don't need to worry much about what is happening here. At the end of the day, these methods are going to help the authorization server to generate a key pair which is going to have both private and public keys.
 * */
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
/* At the end of the day, these methods are going to help the authorization server to generate a key pair which is going to have both private and public keys.
* At last, we have a couple of beans methods i.e., jwtDecoder bean method. Check it's docstring for more details around it.
* */
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
    /** Inside this method, we are trying to configure the bean of JWKSource that got generated inside the bean method jwkSource inside this class.
     * So, whatever private and public keys/certs that got generated, the same we are trying to configure as a 'jwtDecoder' with the help of this 'OAuth2AuthorizationServerConfiguration'.
     * So, with the line, 'OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);' what we are trying to tell to the authorization server is, whenever you are trying to generate an access token please digitally sign it with the help of this 'jwkSource' input. That's actually what we are trying to achieve with the help fo this bean.
     * At last, based on the documentation code, we are trying to generate a bean of type AuthorizationServerSettings using the bean method authorizationServerSettings. You can check its docstring for more details.
     * */

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /** This bean is going to be responsible to configure all the settings inside the authorization server.
     *  If you check the documentation as is inside the Spring Authorization server, you will be able to see an explanation of each of these bean methods that we have discussed so far. We are going to make some good amount of changes inside this class in the coming sessions.
     *  For now, check the application.properties file of this project for some of the properties that we are trying to add.
     * */
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
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring Security 6.3 version
     *
     * @return
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
