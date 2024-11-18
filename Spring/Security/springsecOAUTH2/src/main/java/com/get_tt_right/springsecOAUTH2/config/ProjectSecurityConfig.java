package com.get_tt_right.springsecOAUTH2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * This method sets up authorization rules for HTTP requests. It ensures that
     * requests to the "/secure" endpoint require authentication, while all other
     * requests are permitted without authentication. It also enables form-based
     * login and OAuth2 login with default settings.
     *
     * @param httpSecurity the HttpSecurity object used to configure the security
     *                     filter chain.
     *                     Using httpSecurity reference I am invoking the authorizeHttpRequests method. To this method, I need to pass all my security related configurations conveying on how I want to secure my paths and apis.
     *                     After this configurations I am going to invoke the formLogin method which is going to enable the form based login. Though by default this is enabled by default in any Spring Security based web application.
     *                     This will enable my end user to have an option to login into my application using the form based login/ normal login flow.
     *                     To this method I am going to provide Customizer.withDefaults() as input. Inside this withDefaults() method, we have an empty lambda expression. Since I don't want to further customize my form login flow, I am just trying to use the same.
     *                     Next I am invoking the oauth2Login method which is going to enable the OAuth2 login flow. This is going to enable my end user to login into my application using the social login providers like github or facebook.
     *                     To this method I am going to provide Customizer.withDefaults() as input. Inside this withDefaults() method, we have an empty lambda expression. Since I don't want to further customize my oauth2 login flow, I am just trying to use the same.
     * @return the configured SecurityFilterChain bean.
     * At last, I am going to return the object of SecurityFilterChain by invoking the build() method on the httpSecurity object.
     * @throws Exception if an error occurs while building the security filter chain.
     *
     * With this we have enabled the oauth2 login flow, but whenever we enable it, we need to give clue to our Spring Security framework on which authorization server we are going to use.
     * Whether we are using social login providers like github or facebook or any other authorization server like our own custom authorization server.
     * To give this clue, we have create a bean of type ClientRegistrationRepository.
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((requests) -> requests.requestMatchers("/secure").authenticated()
                .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults());
        return httpSecurity.build();
    }

    /**
     * Provides a ClientRegistrationRepository bean, which is a Spring Security object that
     * holds the configuration information of the OAuth2 authorization server(s).
     * Does not take any input parameters. It returns an InMemoryClientRegistrationRepository object.
     * If you invoke its empty constructor, inside the object of InMemoryClientRegistrationRepository, there will not be any authorization server related details.
     * To start using the oauth2 login flow, we need to provide the authorization server related details.
     * Whenever you want to implement social logins inside your Spring boot application, you can leverage one of the enum classes called CommonOAuth2Provider.
     * Inside this enum class, we have a lot of social login providers like GITHUB, FACEBOOK, GOOGLE, and OKTA.
     * For these famous social login providers, Spring Security team has already created ClientRegistration object for each of these social login providers with the details like;
     * scope, authorizationUri, tokenUri, userInfoUri, jwkSetUri, issuerUri, ... etc.
     * These details usually you need to get them from the respective social login provider's developer portal.
     * For example, assuming we didn't have this CommonOAuth2Provider enum, then you would need to read the documentation of the respective social login provider, understand the details (Like what is the authorizationUri where we need to redirect the client once he clicks the login button on the UI, + the many other Uris that are required by the oauth2 login flow i.e., tokenUri, userInfoUri, jwkSetUri, issuerUri all of which we need to get from the official documentation) and create the ClientRegistration object by yourself.
     * To make our Job easy, Spring Security team has created this enum class CommonOAuth2Provider which has 4 famous social login providers enums.
     * Using these enums, we can create the ClientRegistration object for each of these social login providers.
     * Once they are created, we can try store them in the form of a Map<String, ClientRegistration> inside the InMemoryClientRegistrationRepository object by invoking the constructor InMemoryClientRegistrationRepository(github, facebook).
     * This is how we are going to provide the authorization server related details to our Spring Security framework.
     *
     * First we have created two private methods githubClientRegistration() and facebookClientRegistration() which are going to return the ClientRegistration object for github and facebook respectively.
     *
     * If you open this ClientRegistrationRepository interface, and on the LHS you will see an icon of the interface. Click on that icon and you will see the implementation classes of this interface.
     * In this case we have 2 implementation classes of this interface. One is InMemoryClientRegistrationRepository and the other one is SupplierClientRegistrationRepository
     * Most of the time developers will use InMemoryClientRegistrationRepository.
     * If you open this InMemoryClientRegistrationRepository constructor, you will see that it is taking a varargs of ClientRegistration objects. Meaning it can accept any number of ClientRegistration objects.
     * Using this, we are going to store all our authorization server related details in the form of ClientRegistration objects in a Map<String, ClientRegistration>
     *
     * In this implementation, the ClientRegistrationRepository is an InMemoryClientRegistrationRepository
     * that is pre-populated with a ClientRegistration object for each of the authorization servers
     * that we are going to use. In this case, we are using github and facebook.
     *
     * Like this, our end-user will now have the option to login into our application using the social login providers like github or facebook
     */
    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        // Create a ClientRegistration object for GitHub
        ClientRegistration github = githubClientRegistration();
        // Create a ClientRegistration object for Facebook
        ClientRegistration facebook = facebookClientRegistration();
        // Store the ClientRegistration objects in an InMemoryClientRegistrationRepository and return it.
        return new InMemoryClientRegistrationRepository(github, facebook);
    }

    /**
     * Creates a ClientRegistration object for GitHub OAuth2 login.
     * This method uses the CommonOAuth2Provider.GITHUB to build the registration
     * with the specified clientId and clientSecret.
     *
     * To getBuilder() method, I am passing the client registration id as "github".
     * Using this registration id, the details of github will be stored inside the InMemoryClientRegistrationRepository object. This name you can give anything.
     * But to make it more meaningful, I am giving the name as "github".
     * To this builder object, I am invoking the clientId() method and passing the clientId of the github application on github - springsecOAUTH2.
     * We need to go to github, register a client application and get the clientId and clientSecret.
     * To this builder object, I am invoking the clientSecret() method and passing the clientSecret of the created github application on github. I have create one i.e., springsecOAUTH2
     * Finally I am invoking the build() method. The output of this build() method is a ClientRegistration object and this is what we are returning.
     *
     * @return a ClientRegistration object configured for GitHub.
     */
    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("Ov23liG3ejbX258jnNeG")
                .clientSecret("3987ab3eee8f9ec2265bfc87ea97d48e6ba27874").build();
    }

    /**
     * Creates a ClientRegistration object for Facebook OAuth2 login.
     * This method uses the CommonOAuth2Provider.FACEBOOK to build the registration
     * with the specified clientId and clientSecret.
     *
     * For now, I'm trying to hard code the clientId and secret. But in real-time, you need to read this clientId and secret using the environment variables.
     * So you need to create these properties as environment variables and access them with the help of Environment interface available inside the Spring framework.
     *
     * @return a ClientRegistration object configured for Facebook.
     */
    private ClientRegistration facebookClientRegistration() {
        return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook").clientId("575236091665053")
                .clientSecret("e042469d193346e90bf9de1410c13a0a").build();
    }

}