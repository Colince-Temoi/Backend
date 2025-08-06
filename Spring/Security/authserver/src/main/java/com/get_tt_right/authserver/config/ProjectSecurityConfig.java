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
/** Updated on 7/28/2025
 * --------------------------
 * On top of this class I am going to mention a couple of class level annotations: i.e., @Configuration annotation which is very obvious. The next one is @EnableWebSecurity. Inside this class we need to define a good amount of configurations. Let's go to the official website and understand what kind of configurations we need to define and accordingly we can make the configurations inside this file. So, navigate to the site Spring Authorization server project https://spring.io/projects/spring-authorization-server and then click on the Learn tab and navigate to the latest reference doc. As of today we are at the version 1.5.1. My instructor at the time of teaching we were at 1.3.1 version of this project Spring Authorization Server. We will try to learn how to read these documentations and accordingly in future if something changes you should be able to handle that by yourself. My instructor believed that a lot is going to change in Spring Authorization Server project in the future. Because at that time, it was supporting a bare minimum amount of features. This is not going to be a fancy product like KeyCloak where we have a beautiful admin UI console to perform everything. This project as of now, it does not support any admin UI. Everything we have to achieve with the help of Java code or application properties configurations. My Instructors assumption is that this Project is going to evolve a lot in the future.
 * So, click on the Getting Started button.  Here you can see we have information like; to set up the spring Authorization server, we need to add the dependency like: spring-boot-starter-oauth2-authorization-server inside our pom.xml file if you are building a Spring Boot application, otherwise for normla Spring based applications we have to add the same dependency but with a version tag i.e., <version>1.5.1</version>. Since we are building a Spring Boot application, we added the dependency without the version tag and we are good from this first step perspective. In the next step, "Developing Your First Application", they are trying to explain on how to set up Spring Authorization Server related configurations with the help of properties that we can define inside the application.properties or application.yml file. We will not follow this approach because this approach is going to be super complex and is not going to support the majority of the complex scenarios that we have. But for simpler applications this properties approach may work. So skip this approach and we will go to the other approach which is: "Defining Required Components" programmatic approach where we can configure everything with the help of Java methods/classes and Java beans. Once familiar and clear with this programmatic approach, then these properties approach is also going to make sense to you and how to use them. For now, don't worry about these properties approach and how to use them.
 * Once you get clarity on how we are going to approach the programmatic stuff then obviously every other thing is going to be clear for you. So as can be seen, we have already created a Class and annotated just like the one in the doc is. Next, I am going to copy the contents of this entire class and pasted inside this Class of ours as can be seen below. Next, we need to resolve the compilation errors by importing the corresponding classes. The SecurityContext class I need to import from the package com.nimbusds.jose.proc.SecurityContext. Pronounce numbusds as "nimbus ds" jose as "jos" proc as " p r o c". Now, lets try to analyze these configurations one by one. As a very first step, they are trying to create a bean of type SecurityFilterChain. The same kind of bean which we created inside our normal Resource server while learning Spring security and also while learning Ms's security. Check its method docstring for more details.
 *
 * Creating Client Credentials inside Spring Auth Server for API-API invocation.
 * ------------------------------------------------------------------------------
 * Now, we are going to configure a client application with the auth server and this client application is going to follow the Client Credentials grant type flow. As of now, inside this ProjectSecurityConfig.java class as in the official documentation, we have some client configured in the commented bean method like: registeredClientRepository, but we don't want to follow this. Check it out for mor details.
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
     *  Updated on 7/28/2025
     *  ----------------------
     *  We are going to leverage this as in the official documentation code around registeredClientRepository bean and try to change few details. Check the uncommented code's docstring for more details on the enhancements.
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

    /** Updated on 7/28/2025
     * -----------------------
     * With the help of withId method, we are trying to generate a random ID value. This is going to act as a PK or ID value for the client that I am going to create.
     * For the clientId I am trying to mention the value get_tt_right-api. For the clientSecret, I am trying to mention some complex secrete which is just a random value. Here we are trying to provide this {noop} as a prefix - pronounce it as "no op". This is going to give a confirmation to the Spring Security framework that we are trying to store the Client Secrete as plain text value. If you want to mention a BCrypt hash value, you know what to do. Visit your Spring security notes, Password Mgt, as we discussed this. You will have to calculate the BCrypt hash value for this plain text "VxubZgAXyyTq9lGjj3qGvWNsHtE4SqTq" and for the prefix, you need to mention {bcrypt}. For now, lets follow the plain text password/client secret.
     * clientAuthenticationMethod - Using this method, we need to tell to the auth server how the client applications are going to send the credentials configured above i.e., client id and client secret. Whether they are going to send as part of a headers or as part of the body. So, I am fine with the value 'CLIENT_SECRET_BASIC' - Means the Client applications are going to send the credentials by using the Http basic standards. If you delete this value and ctl + tab you will notice we have various other options i.e.,CLIENT_SECRET_POST, CLIENT_SECRET_JWT, TLS_CLIENT_AUTH, SELF_SIGNED_TLS_CLIENT_AUTH and PRIVATE_KEY_JWT. For now, lets go with CLIENT_SECRET_BASIC.
     * Next using, authorizationGrantType, we need to configure what grant type flow(s) this client application is going to support. For now, we are making it support CLIENT_CREDENTIALS grant type. If you want your client application to support multiple grant type flow, then you can repeat that line of code with the other respective grant types you want your client application to be able to support as per your requirement. For my case, I need my client applications to strictly support Client credentials grant type flow but not any other grant type flow.
     * Next, I have commented out also redirectUri and postLogoutRedirectUri. Reason - Whenever a client is registered with the help of client credentials, there is a good chance that, their won't be any UI involved. Whenever there is no UI involved and whenever there is no end-user/resource owners involved, in such scenarios we can simply get rid of such configurations.
     * Next, configure the scopes for our client application. These are nothing but the roles that we want to configure for the client application. We can define the scopes individually with the help of this scope method. Nothing but repeat the scope method invocation multiple times. As can be seen, as in official documentation, they are trying to configure the scopes of OPENID and PROFILE. Alternatively, instead of defining these scopes individually by invoking the scope method multiple times, we can try to club everything into a single configuration with the help of scopes method which takes a consumer lambda impl. So, I passed scopeConfig as a parameter name and using this same reference, I am going to invoke a method which is addAll. To this addAll method, I am going to pass a list of scopes by using List.of method. To this I am passing the roles/scopes i.e., OidcScopes.OPENID, OidcScopes.PROFILE,"ADMIN","USER"
     * The reason I am trying to configure the scopes/role i.e.,"ADMIN" and "USER" is, inside the resource server, we configured the authorization rules with the help of the roles name i.e.,"ADMIN" and "USER". That's why I am trying to mention these role names as scopes. So, whenever we are building an auth server with the help of Spring Authorization server project, we don't have the flexibility to define the roles separately, everything we need to configure with the help of scopes. Inside the resource server we can write some logic to read the roles information from the scopes, and accordingly we can populate the collection of Granted Authorities object.
     * Next, we are going to invoke an important method which is tokenSettings. With the help of this method, we can pass configurations on how the tokens have to be generated. After the builder, you can fluently invoke the method like accessTokenTimeToLive - To this method we are going to pass a Duration of minutes i.e., 10 which means I want my access token lifetime to be of 10 minutes. After the time configurations, I am going to invoke another method which is accessTokenFormat. To this method I am going to pass a constraint which is OAuth2TokenFormat.SELF_CONTAINED. If you can recall in our Spring Security discussions, we did discuss something around token formats. In general, there are 2 commonly used token formats; the very first one is JWT format and the other one is OPAQUE token format. So, whenever you are looking for the JWT format, you need to mention this OAuth2TokenFormat.SELF_CONTAINED. If you navigate to it and see its docstring you will see that "Self-contained tokens use a protected, time-limited data structure that contains token metadata and claims of the user and/or client. JSON Web Token (JWT) is a widely used format."
     *  With this configuration, the Resource server is always going to get the access tokens and other tokens in a JWT format, and it can locally validate the tokens with a public certificate/key. In the next sessions we will also see how to configure the Opaque tokens. For now, let me invoke the build method. So with this tokenSettings behavior, we are trying to pass various token related configurations.
     * Similarly, we can also configure clientSettings. With the help of clientSettings, we can enable the properties like, requireAuthorizationConsent. This configuration is going to be useful in the scenario where the end-user is trying to be logged in and once the login is completed, the auth server is going to show him/her the consent page where the end-user has to accept. But since our client is following the client credentials, we don't need this configuration(s).
     * At last, invoke the build method which is going to take care of creating a RegisteredClient. So I have also renamed the reference variable of this RegisteredClient that we are creating to 'clientCredClient' to communicate that I am trying to create a client which is going to follow the client credentials grant type flow. This reference, I am going to pass as an input to the InMemoryRegisteredClientRepository. With this, we have successfully configured a client that follows Client credentials grant type.
     * Do a build and make sure to start your MySQL DB container named 'get_tt_right'. So, as of now,our auth server does not require any DB as we have not written any DB related logic. But since we have defined the dependencies of MySQL and defined the properties related to MySQL inside the Auth server code, it is going to look for this DB and that's why I aam trying to start this container behind the scenes. Start you auth server application in debug mode and yes, it should start successfully at the port 9000. Now, inside the postman Collection, SpringSecurity, under the Section_16 folder, you can open the myAccount request. There, you will see we have populated all the required details as you already know. Most important thing that I am pointing out under the authorization tab is the 'Access Token URL', like you can see we are giving our own auth server access token url i.e., http://localhost:9000/oauth2/token. You can get all the URL details that are supported by our auth server by opening the '/.well-known/openid-configuration' url i.e.,http://localhost:9000/.well-known/openid-configuration. If you open this, you will be able to see all the URLs and the details that are supported by our auth server. If you can search for the key 'token_endpoint"', you should be able to see a url value mentioned against it and that's what we are using under the 'Access Token URL' field in postman. These things you know them. ahkk!
     * For Client ID and Client secret values, copy what you have mentioned inside the registeredClientRepository bean method for Client Credentials grant type configured client and use these in your Postman. Under the scope input in Postman, we have mentioned - openid USER ADMIN, these scopes we have defined them while we were setting up our Client Credentials grant type client in the bean method registeredClientRepository. From your code, if you navigate to OidcScopes.OPENID constant, you will see its value is 'openid'. As you can see, you need to follow the same case inside the postman. If you try to be smart and try to provide a scope which is not configured inside the auth server, then happily you are going to get an authorization error. The demo around all this we will see. For the label 'Client Authentication' in postman, we need to select the option 'Send as basic auth header' reason, in our Client configurations for the Client Credentials grant type, we configured 'CLIENT_SECRET_BASIC)' under the Client Authentication method. Once you have populated all these details, you can click on get new access token and hurreey! it worked. Under the 'Token detail' pop up, we were able to get an access_token + several more other details. Here you will not get any id_token as well as refresh_token. Reason: id_token is only applicable to the end_users. This you know. Refresh token we are not going to get it because we have not enabled the refresh grant type flow. You can copy the access token value and visit the jwt.io site to try to understand what details are present in it.
     * You will be able to see our roles under the scope key i.e., ADMIN and USER. You should be able to see more other keys i.e., sub - subject name, iss - issuing authority, etc. All these thing you know. If you want to customize this token by adding more data, then it is very possible. We will learn how to customize the tokens in the coming sessions.
     * By now, you should be clear on how to register a client that supports Client Credentials grant type flow. And as of now we are able to get an access_token successfully. Next, we will test the end to end flow by setting up the resource server as well.
     *
     * Client Credentials grant type flow demo with Spring Auth server
     * -----------------------------------------------------------------
     * To test the scenario end to end, we need to provide the access token that we have received from the auth server to the resource server. And if the resource server provides us a valid response, then it will be a confirmation to us that our configurations are working end to end. Now, we will set up our resource server - Check the one named as 'springsecsection-Last'. In this resource server, we will be making very minimal changes. i.e.,
     * 1. Inside application.properties file - You can see that for the:
     *    # JWT Configuration
     * spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${JWK_SET_URI:http://localhost:9000/oauth2/jwks}
     * We were initially pointing to the KeyCloak jwk-set-uri. In that place, we need to mention our own local authorization server URL as shown above. This value you can get it from http://localhost:9000/.well-known/openid-configuration - just search for the key 'jwks_uri'.  If you navigate to this jwks_uri, you can clearly see it - nothing but opening that url in a new tab. With this, we have established a link between the auth server and the resource server.
     * 2. Inside the KeyCloakRoleConverter class
     * - This is the converter which is going to get executed whenever we are getting an access token in a JWT format. As of before the change, we were getting the claims with the key 'realm_access" But that format is going to work only for the KeyCloak, whereas coming to our auth server we don't have any such information. We will make changes to this class in a few. But for now, go ahead and do a build. Once completed, you can go ahead and start your application in debug mode.
     *   Once the application is started, I will try to keep a breakpoint inside the KeyCloakRoleConverter class  at the first line so that we can try to see what is happening inside this class. So, inside our Postman under the collection like SpringSecurity/Section_16 we have requests there i.e., myAccount. Get a new access token in the Authorization tab and before doing that make sure the configured inputs in the authorization tab are correct. Now, if you try to send this request to your resource server, the breakpoint is going to stop inside our KeyClaokRoleConverter.
     *   So, you can try to debug this method line by line and as can be seen 'realm_access' is null and with that we are going to return empty roles for this client application. And with that if you resume your application you are going to get a beautiful 403 error. To Fix this issue, 1st we need to understand how the access token is looking like by pasting it under the jwt.io site. You can clearly see that the roles information are available under the 'scope' key. So this means, first you have to read the 'scope' details and with this you are going to get a collection of roles/scopes information.
     *   So, inside my KeyCloakRoleConverter, I need to get I need to get a claim with the key 'scope' and not 'realm_access' like initially. This is going to give me a list of String values. That's why instead of Map<String, Object> like we did before, we need to typecast the result of source.getClaims().get("scope") to (ArrayList<String>) source.getClaims().get("roles"). Check the code and you should be able to understand it.
     *   On top of the list, we are going to invoke the stream method and then fluently invoke map so that the prefix of 'ROLE_' is going to be attached to each of our roles. Then each modified role name we are going to convert into an object of SimpleGrantedAuthority and finally all these objects of SimpleGrantedAuthority we are going to collect them into a List and the same we are trying to return from this method. All these things you know and you have learnt aakh! easy!. With these changes we should be able to get a proper response when we make a request to this ResourceServer of ours. If you debug this method line by line now, you should be able to see that under the roles we have some elements which you can visually see. If you release the break point, you are going to get a successful response.
     * Hurreey! This gives a confirmation to us that our code changes are working end to end.
     * As of now when our token is generated, the roles are being added under the scope element/key, but sometimes we may get a requirement to create our own elements/claims inside the access_token or in any other tokens that our auth server will be generating. To handle such scenarios, the Spring Authorization server project gave us flexibility to define some logic which is going to be executed and accordingly the token is going to be modified during the token creation process. This we will try to understand next.
     *
     * OAuth2 token customization in Spring Auth server
     * ---------------------------------------------------
     * To customize the access token, we need to go to the project where the access_token is being generated. That is our auth server project. This is where the minting of the tokens is going to happen. So, whenever I am looking for an option to customize the tokens after the token minting/creation, I need to create a bean method for a class which implemented an interface OAuth2TokenCustomizer. You can search it using the top RHS search icon in InteliJ next to the settings icon and click on the tab classes to see even the library interfaces and classes. This interface is a fuctional interface which has a single method i.e., void customize(T context); To this method, we are going to get an object,T, in the reference 'context' which is going to have all the details of the token. Any modifications that we need to make we need to make those and update those back into this context object.
     * Check out the bean method jwtTokenCustomizer in this class which is going to return the object of OAuth2TokenCustomizer. To this interface type, I am mentioning using generics i.e., OAuth2TokenCustomizer<JwtEncodingContext> the data type of the element that our interface is going to hold/expect. If you navigate into the interface OAuth2TokenCustomizer you will clearly see that generics are being used,public interface OAuth2TokenCustomizer<T extends OAuth2TokenContext>, so whatever object we try to hold in the reference of this interface data type it has to/MUST extend another class which is OAuth2TokenContext. Since right now we are generating the tokens by using the JWT format, I need to mention the class name as JwtEncodingContext. i.e., OAuth2TokenCustomizer<JwtEncodingContext> method_name{....} The method name I have provided as, jwtTokenCustomizer. Actually the method name can be anything. And this bean method which we are creating is not going to accept anything. Check out its docstring for more details.
     *
     * create clients inside this custom auth server for Authorization Code and PKCE grant types
     * -------------------------------------------------------------------------------------------
     * We will try to register client applications with Authorization Code Grant type flow and PKCE grant type flow inside our auth server. As of now, we have registered only a single client application that follows Client Credentials Grant type flow. In the similar lines, we are going to copy and paste the logic that we wrote while creating the Client that follows Client Credentials Grant type flow and do some modifications on that logic to come up with the logic for the client that follows Authorization Code and PKCE grant types.
     * We have renamed the variables to authCodeClient and pkceClient for the respective grant types clients that we are trying to create. These variables/placeholders, I am also sending them as inputs to the constructor of InMemoryRegisteredClientRepository. If you open the constructor of this clas, you will notice that it is capable of accepting any number of registered clients as an input. public InMemoryRegisteredClientRepository(RegisteredClient... registrations){this(Arrays.asList(registrations));} As can be seen from the syntax, we have 3 dots which confirms that this constructor is capable of accepting any number of registered clients as an input - Nothing, but we call this concept as variable argument inputs. Now, lets try to make the changes inside the configurations for the 2 more client applications that we are trying to create to make them support Authorization Code and PKCE grant types respectively.
     * First, we will try to create a client application that follows the Authorization Code Grant type flow without PKCE. Btw, PKCE is a type of Authorization Code Grant type flow. That's why I said the later statement. Revise your Spring Security knowledge, and you will have crisp clarity on what I am talking about. So, I have mentioned/defined new Client Id and secret respectively. This time for the clientAuthenticationMethod I am trying to pass another value/constraint for it i.e., CLIENT_SECRET_POST - With this, my Auth server will expect the client id and client secret to be passed in the request body because we configured this constraint. Next, coming to the authorizationGrantType, I will configure this as AUTHORIZATION_CODE. And whenever we are configuring this AUTHORIZATION_CODE, we also want to support the REFRESH_TOKEN grant type flow as well. That's why I am trying to configure the authorizationGrantType as AUTHORIZATION_CODE and REFRESH_TOKEN as can be seen.
     * Next, I will try to invoke the method which is 'redirectUri'. Usually in real applications we want the end-user to be redirected to a different page once the authentication is successful, for example, a dashboard or his/her profile page. It can be anything. But since right now, we are trying to mimic the client application with the help of postman, what we have to do os, we have to mention the url which is provided by the postman. This you can find it in the Postman client under the authorization tab when you try to configure/define the various inputs whenever you select the grant type as Authorization Code and check the option which is 'Authorize using browser', we have a 'Callback url' input hardcoded for you. The same you need to copy and use as a value for this method. Next, we have the scopes configurations. Are we going to do the same way we did for Client Credentials grant type client i.e., 'ADMIN' and 'USER'. Think about it?? Since the end-user/resource owner is going to be involved in this scenario, we should not configure the 'roles' USER and ADMIN for the client application like we have done when setting up the Client Credentials grant type client.
     * Instead, we expect these roles to be assigned to the end-user/resource owner inside the storage system like a DB. And for different users/resource-owners, we are going to have different roles. That's why configuring the roles information as 'scopes' like we did for the Client Credentials grant type client, it is not going to make any sense. So, omit them and retain only the scopes i.e., EMAIL and OPENID. Use the scope method to define/configure these as can be seen in the code below. So,whatever we have defined are the scopes related to the client application but not the end-user. This you know very clearly from your Spring Security knowledge. Now, coming to the token settings, since this time along with the access token, refresh token is also going to be generated, we will try to invoke one more method which is refreshTokenTimeToLive. To this I am going to pass the duration as 8 hours, that's why I am using the ofHours behavior. With this what is going to happen is, my client application is going to get a refresh token with an expiration time of 8 hours. After this time configurations, we're also going to have an option to configure reuseRefreshTokens. To this, I am going to mention the boolean as false. With this configuration, what is going to happen is, whenever the client application is trying to leverage the refresh token grant type flow, it is going to get a new refresh token everytime it provides the previous refresh token.
     *  Otherwise, if you configured this value as true, then always the client application is going to get the same refresh token value. So, for security reasons, it is always better to re-issue the new refresh tokens whenever the refresh grant type flow is being used/invoked. The access token format we can maintain it as SELF_CONTAINED. With these configurations, I am hoping that we successfully created a proper client that leverages the Authorization Code grant type flow.
     * In the similar line, lets try to update the next client to use the PKCE grant type flow. That's what we are going to do next. Same drill, copy the configurations we have done for any of the previous clients, and we are going to edit that to suit the PKCE grant type flow. Reason is because most of the configurations of PKCE are going to be similar to Authorization Code grant type flow. So, we are going to reuse the same configurations, and we are going to just change the grant type to PKCE.
     * So, I edited the client id and for the secret value, we know that for PKCE grant type, there won't be any secret, that's why I am going to delete the client secret related configuration. Coming to the client authentication method, I need to select the option which is NONE. Reason: We know that inside PKCE flow, clients are not going to share any secrets for authentication as always the flow is going to be secured with the help of code challange and code verifier. The grant types are going to be the same as AUTHORIZATION_CODE and REFRESH_TOKEN, same way we configured for Authorization Code grant type flow client. The redirect uri also is going to be the same. Scope(s) and token settings I will also keep the same. We need to make one more configuration here which is with the help of clientSettings. So I am invoking the method ClientSettings.builder() and the fluently invoke the method which is requireProofKey and I am going to provide the boolean value tru as an input. At last I am going to fluently invoke the build method. If you navigate to the method - requireProofKey, you will be able to see a docstring. Actually, if the input to this method is set as 'true', then the client is required to provide a proof key challange and verifier when performing the authorization code grant type flow. Otherwise we can set the value as false.
     * So, if you try to analyze the differences between the authCodeClient and the pkceClient, there are only 2 differences. The very first one is the clientAuthenticationMethod is going to be set as NONE. The next one is that, under the client settings, we have configured this requireProofKey to true. We also don't need to configure the SHA256 algor as a PKCE SHA algor becuase by default Spring authorization server project is going to consider the same. Hurreey!, we have successfully created/defined a couple of clients and have provided them as an input to the constructor of InMemoryRegisteredClientRepository. Now, we should be good from Client registration perspective. Now, we will go ahead and test the token generation scenarios? Think about it haha. We can't test it because as of now my Auth server does not have clue around/about how to authenticate the end-user/resource owner. Where are the end-user details stored? how will we perform the authentication? So, such details are missing right now. Inside the official documentaion, inside the sample code that they have provided, you can clearly see that they have configured a single user inside the memory - check the bean userDetailsService as in the official documentaion provided code. It looks like below:
     * @Bean
     *        public UserDetailsService userDetailsService() {
     * 		UserDetails userDetails = User.withDefaultPasswordEncoder()
     * 				.username("user")
     * 				.password("password")
     * 				.roles("USER")
     * 				.build();
     *
     * 		return new InMemoryUserDetailsManager(userDetails);
     *    }
     * With this, what is going to happen is - whenever an end-user is trying to test the PKCE flow or the Authorization Code grant type flow, they will have to enter the username as 'user' and password as 'password' and the role 'USER' is going to be assigned to the end-user. But, since in our scenario we are looking into properly using a MySQL DB  as a storage system, what we can do is - We can try to leverage AuthenticationProviderUserDetailsService implementation that can be leveraged by the Spring Security framework during the authentication process. These things you already know and have learnt and even implemented them when you were learning Spring Security. So, next, we will see the changes we are going to make at the Resource Server service - All of which like I said you already know them from the previous discussion of the Spring Security sessions.
     *
     * Updating Spring Auth server to authenticate the end-user using DB
     * --------------------------------------------------------------------
     * Here we will make very few changes in our auth server so that it can start authenticating the end-user by leveraging the DB. Most of the changes will look very familiar to you because we already discussed all these concepts. First, I have created an AuthenticationProvider implementation inside the config package. If you can open Get_tt_rightBankUsernamePwdAuthenticationProvider.java class you can get to know more details on what it is doing. Check its docstring out.
     * Another change is the Get_tt_rightBankUserDetailsService. Also inside this ProjectSecurityConfig class we have done few changes by creating 2 beans of types i.e., CompromisedPasswordChecker and PasswordEncoder. We have also done changes inside the model package, we created a model package and inside this we have created entities representing the Customer and Authority table. These entity tables are an exact copy of what we previously discussed in our Spring Security sessions.
     * We also have a CustomerRepository in the repository package which will load the customer details based upon the customer email. With all these changes now my auth server knows how to authenticate an end-user.
     * Finally, also take note of the DB configurations that we have done inside the application.properties file. So, please make sure you have spinned up your DB container and have configured the right details in the application.properties file and also have executed some of the necessary sql scripts to populate the DB tables for testing purpose. Check the resources package inside the Resource Server application for samples from which you can build from and be the best end-to end developer with your AI pal. Next, we will see the demo of Authorization Code grant and PKCE grant flows end to end.
     *
     * Auth Code and PKCE grant type flows demo with Spring Auth Server
     * ------------------------------------------------------------------
     *  Make sure your Auth server and Normal Resource server are started and running properly. In my postman collection, SpringSecurity/Section_16, the request 'myBalance'. Configure the inputs under the Authorization tab correctly. The 'Grant type' label/input we have selected as 'Authorization Code'. The 'Callback URL' we already configured inside the configurations that we have made for the client, authCodeClient, inside the ProjectSecurityConfig.java file. Also in the same 'Authorization' tab please make sure you have selected the check-box which is 'Authorize using browser'. The 'Access Token URL' input value you can always get from the '.well-known/openid-configuration'. The same applies for the 'Auth URL'. Client Id - get this from the configurations you have made while setting up the authCodeClient. Same applies for the client secret. Coming to the scope, we need to mention the scope as 'openid email' Reason: As of now, for our client, authCodeClient, we have only configured the 2 scopes. If you try to provide any other scope then happily you are going to get an error. And 'state' which is a random csrf token value we have mentioned some random value - you can mention any random value. Next, coming to the 'Client Authentication' we have selected 'send client credentials in body'
     * Now, if you click the button, 'Get new access token' you will get a login page from where you can enter the end-user credentials that you have stored inside the DB. We have end-user details configured in the DB with the details like; Username - happy@example.com and the pwd as - 12345. If you click on the sign in, the redirection to the postman should successfully be completed and you should be able to see your access as well as an id token of the end user. Similarly, you should also get/see a refresh token. You can copy the access token and paste it in the jwt.io site and try to analyze the claim/elements/keys it has/contains. If you can see, under the 'roles' claim, whatever values are in the 'scope' claim, the same are present in the 'roles' claim. We did this in our implementation earlier - copying whatever values of 'scope' claim into 'roles' claim. Hope you remember. With the code that we have written in the bean method, jwtTokenCustomizer, the 'scope' claim value were getting copied into the 'roles' claim. We know very well that 'scopes' are related to the client applications but not the end users/ resource owners. So, in the scenario of AuthorizationCode grant type flow, we need to make sure we are populating the roles information which are related to the end-user. That's why what we are going to do is, update the code inside the jwtTokenCustomizer bean by building an if block to check what is the current grant type flow happening.
     * So, with the help of context.getAuthorizationGrantType() I should be able to invoke the equals method and to it pass the constraint (AuthorizationGrantType.CLIENT_CREDENTIALS). So in the case of Client Credentials grant type flow, the piece of code inside the if block will execute. Otherwise, we are going to build an else if block where we are going to have a condition to check if the current flow is AuthorizationCode grant type flow. Check that bean's docstring for more details.
     *
     * Now, lets try to test the PKCE flow with the help of SpringSecurity/Section_16/myLoans request. Under the Grant type label, I will select Authorization Code(With PKCE). Enter the client id as configured when we were defining pkceClient. Client Secret, don't pass anything - You know why. Code Challenge Method should have the value SHA-256. For Code Verifier - postman is going to generate for me if I left this field blank. The scope I will mention 'openid email'. We have also given a random csrf token value. And we are going to send the credentials inside the body as can be seen under the 'Client Authentication' label. Now, you can click on the get new access token. Click on Use token and send the request.Hurrey! everything worked as expected. You should be ahppy by seeing these demos. Next, we will see demos to negative scenarios as well for the Client Credentials, Authorization Code as well as PKCE grants.
     * Client Credentials grant negative scenario test - Like we have discussed before, whenever we are sending scopes, we need to send those that are allowed for the client application. Inside our auth server, we have configured 3 scopes for our clientCredClient i.e., 'OidcScopes.OPENID,"ADMIN","USER"'. In our postman request, SpringSecurity/Section_16/myAccount, under the authorization tab, If I try to be smart and try to pass a scope which is not among the 3 for the configured client i.e., MANAGER. We will happily get an authentication failed error whenever we try to get an access token. And if you view in the postman console you will get an actual error which is 'Invalid scope'. Same drill, the same kind of behavior you should be able to see for the other clients as well whenever you are testing their negative scenarios. With this, we have tested all the important grant types with our own authorization server.
     *
     * Refresh grant type demo with Spring Auth server
     * ---------------------------------------------------
     * We already know how to test this refresh grant type flow. Actually, if you click on the refresh button under the authorization tab in postman, the refresh token grant type is going to be automatically initiated behind the scenes. You may face an issue like: 'Could not refresh OAuth2.0 access token'. If you view the console of the Spring auth server in the case of this issue arising you will notice an exception being thrown i.e., OAuth2AuthenticationException with message: client authentication failed: authentication_method. This mean that the authentication method whatever right now we configured for our client is not enough to test the refresh token grant type flow. If you debu this, you will notice the reason as to why we are getting this issue is. As of now, for both the clients authcodeClient and pkceClient, we have configured the client authentication method as clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST). But during the refresh grant type flow, postman is going to send the refresh token request using the Http basic format. Since our 2 clients are not supporting that method, that's why we are facing the issue.
     *  To solve this you can try to repeat the line of code i.e., clientAuthenticationMethod(ClientAuthenticationMethod.'constraint') and pass the constraint CLIENT_SECRET_BASIC. With this, we should be good to go. And for the first client which is 'clientCredClient' - For this client we have not enabled the refresh token grant type flow. The reason is very simple, this client is supposed to be used by backend services. So, whenever they are making a service call, they should get an access token and should be able to use the same with the resource server. We should not give the flexibility of the refresh token for the API to API communication scenario because that is not standard. That's why we have not configured the refresh token grant type flow for the client credentials client. Now, you can build and restart your auth server to have these changes we have done to the authCodeClient and pkceClient reflected. So, make sure to get a new access token 1st before trying to test the refresh token grant type. After getting a new token and licking on use token, in the same authorization tab, you should be able to see that my token is going to expire in the next 10 minutes.
     *  So, within these 10 minutes if I try to refresh this token by clicking on the refresh link/button, I should be able to get a new token with a new expiration time. Now you know how(Clicking on the refresh button in postman) and when(Before the token expires) to invoke the refresh token grant type flow. You can also check the postman console for these requests that you are firing to see what they contain in the headers as well as body. For example, when you invoke the refresh link/button and then check the request in the console - the body, you should be able to see the refresh_token as well as the grant_type as "refresh_token". All this should confirm to you that refresh token grant type os also working fine. As a last step, in the next session, we will try to test one scenario by changing the constraint inside accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) from SELF_CONTAINED to the OPAQUE token format.
     *
     * Demo of Opaque Tokens with Spring Auth server
     * ---------------------------------------------------
     *
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

    /** Inside this method, we are writing the logic to update the token that we will receive. All the token values we are going to have inside this context object i.e., JwtEncodingContext
     * We have written a lambda expression, which is going to take care of overriding the values inside this context i.e., JwtEncodingContext and finally return the updated context. That's why you can see the entire lambda implementation I am directly returning as an output from this method.
     * My lambda expression is going to accept a lambda input variable with the name 'context'. The framework is going to provide an object reference of JwtEncodingContext which is going to be stored in the input reference of our lambda i.e., 'context'
     * As a next step, we are going to build an if condition to check what is the token type that this 'context' reference os holding. If it is equals to OAuth2TokenType.ACCESS_TOKEN, then only I want my logic to be executed because right now we are looking to update the access token but not the refresh token. Similarly, you can also try to update the id token, its up to you. But let's first focus on updating the access token and that's why we have this if condition to make sure we are only altering the 'context' object which is related to the OAuth2TokenType.ACCESS_TOKEN.
     * Inside this if block, we are going to get the claims details. First we have, context.getClaims() - which is going to give me all the claims which are available inside our context object/access_token. Claims are nothing but all the keys/elements in our access_token as we could see inside the jwt.io site. We saw claims like, "scope", "aud", "sub", ..etc. Now, we are looking to add/inject a new/custom claim/key/element with the name 'roles'. That's whatI am trying to do with the help of this 'jwtTokenCustomizer' bean method. Next, I am going to fluently invoke a method which is claims which is going to help us to add new information to the existing claims. If you open that method you will notice that it is accepting a claims consumer which of course is a lambda expression. So, whatever lambda expression that we are going to provide as an input to this claims method, as can be seen in the docstring of this claims method, it is going to be capable of adding, replacing and even removing the existing claims. So, we need to provide a lambda implementation as an input to this claims method based upon the Consumer functional interface.
     * Consumer means nothing, but it is always going to accept an input, but it is not going to return anything. So inside that lambda implementation I am going to provide the input variable name as 'claims'. So by using this reference 'claims' only, I need to add a new claim/element to the framework already minted access_token. For this, we are invoking the put method. The key name that we want to maintain is 'roles' and to this key name, we want to pass roles information But where and how to get this roles' information. As of now, like we already discussed, all the roles information like 'USER' and 'ADMIN' we are storing into the 'scopes' claim/element of our access token. So, what we will do is create a primary attribute to this lambda implementation with the name roles and type Set<String> . To this variable I am going to assign the output that I am going to get from the piece of code like; context.getClaims().build().getClaim("scope"); - With this piece of code, I am trying fetch details available inside a specific claim with the name 'scope'. Under the scope claim we have the values '"openid", "ADMIN", and "USER"'. The same I am assigning to our newly created custom claim/element i.e., 'roles'.
     * So, yea, those are the only changes that we need to make. Do a build and try to keep a breakpoint on our if block that checks for the token type. In postman, when you try to get a new access token for the request i.e., SpringSecurity/Section_16/myAccount under the Authorization tab, you will see that the execution stops at our breakpoint. If you step over each line, you will notice that the if block/the logic we have written inside this if block is going to be executed. You can resume the execution and if you go back to your postman, you will see that we are able to get a new access token. If you take this token and paste it into the jwt.io website , this time you will realise that we have a new claim/key/element with the name 'roles' that we have custom created/defined. Actually, it's just a copy of the scope claim. The most important thing is you have learnt the process that you need to follow when customizing the default minted tokens by the Spring Authorization server project. This way you can try to add/delete/update whatever information you want inside the token(s) - access or id or whatever. As a next step, in our resource server, as of now we are trying to read the privileges/roles information from the scope 'claim' of an access token. Change this to read from the 'roles' claim - the new custom claim that we have added.
     *  This will help give a confirmation to us that the code is working end-to-end. So, use the generated token when sending a request to the resource server and hurreey!! you should get an expected response. You can also try to debug the code/method in the resource server with the help of break points line by line where we are converting the token roles into a Collection of GrantedAuthority to visualize and confirm that we are extracting the privileges/roles from the 'roles' claim of the token that we are receiving in the resource server when a client makes a request. All this make sure to test using the docker image named 'springsecsection - Last' -> Okay I din't create this image but you can if you want. I run the 2 applications locally in intelliJ idea when you open the project 'springsecsection-Last'. Everything is there for testing.
     * With all this, you should be clear on how to customize an access_token that is minted/generated by the auth server. Next, we will create clients inside this custom auth server for Authorization Code and PKCE grant types. Check out the bean method 'registeredClientRepository' inside ProjectSecurityConfig.java for more details.
     *
     * With the help of context.getAuthorizationGrantType() I should be able to invoke the equals method and to it pass the constraint (AuthorizationGrantType.CLIENT_CREDENTIALS). So in the case of Client Credentials grant type flow, the piece of code inside the if block will execute. Otherwise, we are going to build an else if block where we are going to have a condition to check if the current flow is AuthorizationCode grant type flow. Now lets try to understand the code present inside this else if block.
     *  First, from the context reference, I am trying to invoke getPrincipal behavior from which I am trying to invoke the getAuthorities method i.e., context.getPrincipal().getAuthorities(). This is going to give me the authorities related to the end-user but not the client application. So, using the framework utility class i.e., AuthorityUtils, I am trying to invoke its member i.e., authorityListToSet which will convert the received authorities list to set. If there are duplicate roles, they are going to be removed because I am trying to convert the List to Set. Now, on top of the Set, I am streaming and mapping each of the elements with some map logic as can be seen. At last, I am trying to collect the final output as a Set.
     *  Finally, the same 'roles' information, I am adding to the claim i.e., 'roles' by invoking the put method. Hope you are clear. I know you may have complexity understanding the map logic that I have written i.e., .map(c -> c.replaceFirst("^ROLE_", "")). If you try to understand what is happening inside this map logic. Whatever role name that I am getting from the DB, inside the same role name, I am trying to replace the prefix 'ROLE_' with an empty value. Actually we know that inside the DB we are going to store the end-user roles with the prefix 'ROLE_'. But in our map logic we are removing the prefix on each role. Reason: Inside the resource server under the KeyCloakRoleConverter class, we have a logic where we are trying to add the same prefix i.e., 'ROLE_'. If you can control that logic so that it cannot be executed for the authorization code grant type flow, then happily you don't need the map logic to remove the 'ROLE_' prefix as discussed. You can directly convert the stream into a Set object.
     * With this we should be good and if you try to get a new access token by navigating to the Authorization tab of the request SpringSecurity/Section_16/myBalance and clicking on the get new access token and then copy the token and paste it inside the jwt.io site, you should be able to see what is present inside the claims. Btw, the browser may not ask you to enter the end user details because we already have an existing session with the end user credentials. This time under the 'roles' claim you should be able to see we have 'ADMIN' and 'USER'. With this, our code should work end to end. Now, you can click on the use token and click on the send button. Happily you should be able to get a successful response from the resource server.
     * */

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

    /** Here, we are creating a bean of PasswordEncoder with the help of the shown code because right now all our pwds in the DB are hashed using the BCrypt hashing algor
     * */
  /*  @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }*/
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


    /**
     * From Spring Security 6.3 version
     * Next, I have also created this bean of type CompromisedPasswordChecker so that our authorization server will not accept any simple pwds during the registration process.
     * As of now, we don't have any registration process supported by the auth server but if needed you can implement that part by exposing a REST API.
     * @return
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }


}
