package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/** Update as of 06/07/2025
 * Securing Gateway Server using Client Credentials Grant type flow
 * ------------------------------------------------------------------
 * - Check slides for a visualization of what we will be implementing.
 * Set up Auth server using Keycloak
 * -----------------------------------
 * - For detailed steps, check with your Spring security notebook. For now, visit the Keycloak website - https://www.keycloak.org/ and the click on the Get Started button. There are various options which you will see by using which we can set up auth server with the help of KeyCloak. Since inside our local system we have docker installed and have docker-compose file for our ms's, it will be a good idea to get started with docker. For the same, click on the docker option. Inside that page we have details on how to start KeyClaok server. For the command, before running it, change the port mapping as we know very well we are following the same port mapping for our accounts ms also. I am fine Keycloak starting at the port 8080 internally inside the docker network. But I am not going to expose KeyCloak to the outside world at the port 8080 because I am already doing that for the accounts ms. If I do that for KeyCloak, then definitely there will be a port conflict error. With that reason I am going to change that port number to 7080. So, from my local system I can access the KeyCloak server at the port 7080. Before this port mapping, I am going to give -d flag which  will start my KeyCloak server in detached mode.
 *   docker run -d -p 127.0.0.1:7080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.3.0 start-dev
 *   After the port mapping, you can see we are trying to provide some environment variables with the help of -e flag. The very first flag is KC_BOOTSTRAP_ADMIN_USERNAME to hold the username that we want to set for our KeyCloak auth server which can be used by the admin to set up the clients or to set up the auth server. The second flag is KC_BOOTSTRAP_ADMIN_PASSWORD to hold the password that we want to set for our KeyCloak auth server which can be used by the admin to set up the clients or to set up the auth server. After these environment variables we have the docker image name of KeyCloak which is - quay.io/keycloak/keycloak:26.3.0 and the last part is start-dev which is communicating that I am trying to start the KeyCloak server in the dev mode, so behind the scenes KeyCloak is going to have some internal in-memory DB using which we can store all the users/resource owners' and registered clients credentials. But in production environments we should have some dedicated DB configured for the KeyCloak auth server. If you now run this command, you will see a new container will be started inside your local system, and you can also confirm the same by going to your docker desktop. Inside the docker desktop you can be able to see there is a running container with the name KeyCloak, and it is exposing the traffic on the port 7080.
 * - Now, access the KeyCloak server at the port 7080 and you will be able to see the login page. Enter the username and password which you have mentioned in the docker run command. Now, using that admin console only, the admin of the auth server can create various clients and resource owners/users. So there is a lot of functionality available inside this auth server. We will explore more about it in the next sessions. But for now, hope you have spotted something like 'master realm'. More about realms, check your spring security notebook. For now, we only have one realm. i.e., 'master realm'. What is a realm? Inside any auth server like KeyCloak, a realm is a boundary where you want to create a set of client credentials or User credentials. Think like, this is an environment. By default, KeyCloak is going to provide a realm with the name 'master realm'. But if needed, you can create more realms like 'dev', 'qa', 'prod' based upon your requirements. Because, the end-users or the clients are not going to use the same credentials across various environments. We can't force the qa team to use the same credentials as we have inside the production. That's why to overcome this challange, we have a concept of realms inside the auth server. As of now we have only one realm which is called 'master realm' and throughout our sessions we are going to use the same.
 * So, far you should be clear with this set-up, if facing some issues, make sure you have at least Java 17 inside your local system. Otherwise, Keycloak is not going to start, because the minimum Java version is 17. With this consider that we have successfully set up an auth server inside get_tt_right bank ms's network. Very crisp easy! Behind the scenes this auth server has a lot of functionalities based upon the OAUTH2 and OIDC standards. We will explore more in the next sessions.
 *
 * Register Client details inside KeyCloak for Client Credentials Grant type flow
 * --------------------------------------------------------------------------------
 * - Now we need to register an external client application with this auth server. Assume we are the external client application(s), who are trying to communicate with the auth server to get an access token so that using the same access token we can try invoking the Gateway-server which is the resource server. So, as a developer or as a lead of this external client application, who is trying to communicate with the auth server using backend API's, 1st you need to approach to the KeyCloak Admin and make sure we are getting registered and getting approval from the KeyCloak admin and from the leaders of get_tt_right bank ms's network. Assume, the KeyCloak admin received all approvals required to register the client details of this external application, he will proceed to register the client with the auth server by coming to this KeyCloak Admin console. 1st, he needs to make sure he is under the right realm. As of now we have only of realm, master realm, and we should be just good to go with this default realm.
 *   On the LHS nav, we have a 'Clients' option. If you click on it, you will see we have certain default clients which are created by KeyCloak internally i.e., Account, Account Console, Admin CLI, Broker, master Realm, and Security Admin Console. Inside the nav of this page, we have several options i.e., 'Import Client' and Create Client' options with which we can leverage to create/set up our clients. So, lets go with the 'Create Client' option. Under the 'Client type' label, make sure you are selecting 'OpenID Connect' because we are planning to use the OAUTH2 framework. On this dropdown, like you can see, there is no separate option for OAUTH2 and OpenID Connect. When we select OIDC, internally we are going to use OAUTH2 framework, this you already know. Reason, OIDC is a wrapper that is build on top of OAUTH2 framework. Now, input the 'Client ID' which is like the username of my external client application. Think like there is a 'Call Center Application' inside the get_tt_right bank. This call center application they want to interact with the ms's network that we have build using backend API's. That's why I want to give th client ID as 'get_tt_right-bank-callcenter-cc' The 'cc' indicated that this is a clientID that I am creating for 'ClientCredentials grant type flow'
 *   For the 'Name' label, I will give an input like 'Get_tt_right Bank Call Center App'. The same I will also copy and mention inside the 'Description' label as well. Once we have given these details, we can click on the 'Next' button. Enable the toggle 'Client authentication' because we want our client to be authenticated itself with the auth server by providing its own credentials, then only my auth server is going to issue an access token. If we disable this, then any client can come and can try to invoke our auth server. Now, under 'Authentication flow' uncheck everything that has been checked i.e., 'Standard flow' grant type, and so on and check Service accounts roles, because as of now we are trying to leverage Client Credentials Grant where 2 different applications are trying to communicate with each other through REST API's or through backend logic. If you can click on the '?' icon just next to it, you will be able to see that this is what supports, Client Credentials grant type flow. So, please make sure that, ' Service accounts roles' is selected and the other 'Authentication flows' are de-selected. Like discussed before, we have various flows inside the OAUTH2 framework which we can leverage based upon the scenario we are in. So, based on the current scenario, we should select 'Service accounts roles' flow. Click on the 'Next' button.
 *   Leave the 'Root' and 'Home' URLs inputs as empty. Click on the save button and now, you should be able to see a new client is created based upon the client id which we have created and this client is going to support OIDC - This you can clearly see on the 'Settings' tab under 'Clients' >> 'Client details' >> ' get_tt_right-bank-callcenter-cc'. Now, for the credentials of this client, we have not given any, and they are going to be autogenerated by the KeyCloak client. To understand what are these credentials, my KeyCloak Admin can click on the 'Credentials' tab under 'Clients' >> Client details >> get_tt_right-bank-callcenter-cc. Here you should be able to see the client secret. The client id we already know i.e., get_tt_right-bank-callcenter-cc. So, everytime my client application wants to get an access token from the KeyCloak which is an auth server, it needs to make sure it is providing this 'ClientId' as well as the 'clientSecret'. Because as can be seen on the 'Client Authenticator' label, it is selected as 'Client ID and Secret'. There are other options also i.e., Signed Jwt, X509 Certificate, Signed Jwt with Client Secret'. But for us, we will go with the default option which is 'Client Id and Secret'. Now, with this, we have successfully created/registered a client application with the KeyCloak Auth server using the Client Credentials Grant type flow. In the next session, we will see a demo on how to get an access token from the KeyCloak auth server by using these client details to the client application that we have just created.
 *
 * Getting an access token from the KeyCloak auth server by using the Client Credentials that we have for our created client application
 * ---------------------------------------------------------------------------------------------------------------------------------------
 * -  We will see how a client application can connect with the auth server to get an access token. For the same, 1st my client application should know what is the end-point URL that it need to invoke in order to get an access token. To understand the same, we can click on the 'Realm settings' LHS nav. Under the 'General' tab, if you can scroll down, you should be able to see an 'Endpoints' label, here you can click on the 'OpenID Endpoint Configuration' link. You should be able to see a list of endpoint URL's supported by your auth server. Now since we are trying to get an access token, we need to get that specific url. It has a key 'token_endpoint'. So the client application needs to invoke this endpoint url in order to get an access token. So copy the same and inside the postman under the ms's collection, we have a folder with the name 'KeyCloak' and inside this folder we have a request with the name 'ClientCredentials_AccessToken'. For this request you can see I am mentioning the token_endpoint url that we just copied. To this endpoint, my client application whoever is trying to get an access token, they need to make a POST request. That's why you can see I have selected 'POST'. Under the body we need to select the radio, 'x-www-form-urlencoded'. After that we need to send certain details i.e.,
 *   . What is the 'grant_type' that right now my client application is trying to follow. In our case, we will give the value, 'client_credentials'. This value you can get from the webpage that we just got the token_endpoint url from. Under 'grant_types_supported' by the auth server, you should be able to see this. Right now we are trying to follow the Client Credentials Grant type flow. So, we will give the value 'client_credentials' for the grant_type. After that, we need to mention what is the 'client_id' - its value mention what we just set up earlier i.e., 'get_tt_right-bank-callcenter-cc'. Next you need to mention the key 'client_secret' with the value being the client secret associated with the client id that we just set up earlier. You can get this under in your KeyCloak admin console. Click on the 'Clients' LHS nav >> get_tt_right-bank-callcenter-cc >> 'Credentials' tab >> 'Client Secret' >> 'copy the value'. Next you need to mention the key 'scope' and assign the value 'openid email profile'. So, those are the 3 scopes that my client application wants the auth server to consider while issuing an access token. You maybe having a question like, 'We didn't assign any scopes to the client that we created'. Great question! To explain this, inside the KeyCloak Admin console, you can tyr to open the client that we have created and you should be able to see a tab with the name 'Client scopes'. You can see that there are some default scopes assigned to the client that we have just created like; get_tt_right-bank-callcenter-cc-dedicated, acr,address,basic,email,microprofile-jwt,offline_access,organization, phone, profile,roles,service_account, web-origins. All these there have a description attached to them which you can learn what they are capable of. So, these are all the default scopes provided by the KeyCloak auth server. We don't need to create or assign them, if you need something specific to your client, then you need to 'Create a Client scope'. How?
 *     On the LHS nav, we have a link 'Client scopes' >> Click on 'Create client scope'. After that, it will be available to assign to your clients. We are going to explore this in the coming sessions.  Back to our client, get_tt_right-bank-callcenter-cc, under the 'Client scopes' tab where we have the default scopes assigned to this client, you may not be able to see 'openid' scope. The reason is, since by default we are following the OpenID Connect specification, it will by default be assigned for all the clients. That's why back to our postman request to get a token id, I have mentioned 'openid' scope as part of the scopes. Now, once I have assigned the scope details value to the key 'scope' in our postman request, we should be good to make our request. Now, in the response you are going to receive a json object with the keys; 'access_token', 'expires_in', 'refresh_expires_in', 'token_type', 'id_token','not-before-policy', and 'scope'. By default, your 'access_token' is going to expire in 59 seconds. If needed you can try to change that in the admin console of the KeyClaok. In you Postman response section, there is a wrap option, if you click on it you should be able to see your long tokens completely and properly without need for scrolling on the RHS. Btw, in our request, what will happen if we don't mention this 'openid' scope in the scopes? You will get a response but this time without an 'id_token'. So, yea, some clients if they don't want an id_token, they can simply make there request without the 'openid' scope. Nothing but they can send the scopes of only email and profile and they will get the access token. But it is always recommended to use the scope 'openid' as well. As you will get to know more details about your client or your end-user. Both 'access_token' and 'id_token' are Jwt tokens btw. There is a lot to discuss around Jwt tokens, if not aware about that - you can check with the Spring security notebook for detailed discussion around the same.
 * - For now, we want to see what is present in the Jwt tokens - because as humans, you can't understand what that is the way it is in the response(It is Base64 encoded), if you copy the access_token value >> Visit the website jwt.io >> Paste it so that we can be able to see the decoded representation of what is contained in our access token. You will be able to see the Decoded Header which contains three Keys i.e., "alg":"RS256" - Represents what is the algorithm used by your auth server to generate this token. "typ":"JWT" - What is the type of the token. Like we said KeyCloak auth server is going to follow the JWT token format. "kid":""QZdT4j_YuUkt8Tt6eBopJza7pJn-6MfAa4j4lVbwkp0" - Means Key Id. You can also see what each of this means under the tab 'Claims table' in the same place where you are seeing the Json decoded representation of the access token. In the Decoded Payload section, You will be able to see keys like; "exp":1752207687 - Means when the token is going to expire. "iat":1752207627 - Means when the token is issued. "iss":"http://localhost:7080/realms/master" - Means who is the issuer. See the other parts below:
 * {
 *   "exp": 1752207687,
 *   "iat": 1752207627,
 *   "jti": "trrtcc:7ab0889d-0010-9d9a-ef65-e8bd24adb31d",
 *   "iss": "http://localhost:7080/realms/master",
 *   "aud": "account",
 *   "sub": "0af39b70-1c4d-4cd6-a1fb-61899d241882",
 *   "typ": "Bearer",
 *   "azp": "get_tt_right-bank-callcenter-cc", => What is the client id.
 *   "acr": "1",
 *   "allowed-origins": [
 *     "/*"
 *   ],
 *   "realm_access": {  => What are the roles this client id has inside the realm of the auth server.
 *     "roles": [
 *       "default-roles-master",
 *       "offline_access",
 *       "uma_authorization"
 *     ]
 *   },
 *   "resource_access": {
 *     "account": {
 *       "roles": [
 *         "manage-account",
 *         "manage-account-links",
 *         "view-profile"
 *       ]
 *     }
 *   },
 *   "scope": "openid profile email", => What is the scope(s)
 *   "clientHost": "172.17.0.1", => What is the client host.
 *   "email_verified": false,  => Is the email verified?
 *   "preferred_username": "service-account-get_tt_right-bank-callcenter-cc", => What is the preferred username
 *   "clientAddress": "172.17.0.1",
 *   "client_id": "get_tt_right-bank-callcenter-cc"  => What is the client id
 * }
 *
 * So, like seen above, we will be having basic details in the access_token about your client application. For more crisp details about what each of the k-v pairs in the Json representation of the access token, you can refer to the 'Claims table' in the same place where you are seeing the Json decoded representation of the access token. Now, let us try to analyze what is there inside the 'id_token".
 * Decoded Header:
 * {
 *   "alg": "RS256",
 *   "typ": "JWT",
 *   "kid": "QZdT4j_YuUkt8Tt6eBopJza7pJn-6MfAa4j4lVbwkp0"
 * }
 * Decoded Payload:
 * {
 *   "exp": 1752207687,
 *   "iat": 1752207627,
 *   "jti": "b6e1992b-d1b7-d44e-e76a-986d7eeed07c",
 *   "iss": "http://localhost:7080/realms/master",
 *   "aud": "get_tt_right-bank-callcenter-cc",
 *   "sub": "0af39b70-1c4d-4cd6-a1fb-61899d241882",
 *   "typ": "ID",
 *   "azp": "get_tt_right-bank-callcenter-cc", => What is the client id.
 *   "at_hash": "hfrYCdrBiG-cDNuifekP2w",
 *   "acr": "1",
 *   "clientHost": "172.17.0.1", => What is the client host.
 *   "email_verified": false,
 *   "preferred_username": "service-account-get_tt_right-bank-callcenter-cc", => What is the preferred username
 *   "clientAddress": "172.17.0.1",
 *   "client_id": "get_tt_right-bank-callcenter-cc"
 * }
 *
 *Most of the details that you are seeing as part of the id_token are also part of the access_token.  But if needed, you can try to enhance this id_token by trying to send more details about your client application. With all that we have discussed so far, you should be clear on how to get an access_token and id_token by connecting to the KeyCloak auth server whenever we are trying to follow the Client credentials grant type flow. In our Postman, we tried to mimic the scenario like, Client application is trying to invoke the auth server. But in real projects the client application is going to invoke the API that we are using in our postman i.e., "http://localhost:7080/realms/master/protocol/openid-connect/token" with the body K-V pairs from there Java code or from their whatever backend code. And with that they should be able to successfully get an access_token and id_token. Using the same access_token, they are going to invoke the Resource Server.
 *
 * Securing Gatewayserver as a Resources Server
 * -----------------------------------------------
 * - We set up our awn auth server and post that set up our client details inside Keycloak auth server. And with the same details we are able to get an access token. Everything is now set up at the auth server side and client side, the only left part is to secure our gatewayserver and make sure that it is a resource server. Now, we need to convert our gateway server as a resource server and post that we should try to send the access_token to the resource server and verify if everything will work as we expect. To get started around these changes, we will use v2-spring-cloud-config workspace. In the pom.xml of gatewayserver application, we are going to add 3 dependencies which are related to security which are: spring-boot-starter-security, spring-security-oauth2-resource-server, and spring-security-oauth2-jose. Once you have added the 3 dependencies, load the maven changes to download all the libraries related to these 3 maven dependencies. Next, create a new package, config , inside the gatewayserver. Inside this we are going to create some security related configurations. Create a new class inside this config package named - SecurityConfig.java. Add a class level annotation i.e., @Configuration because we will create some beans inside this class. So, to communicate the same to my Spring framework to create these beans during the startup, I need to add @Configuration annotation to this class. Check out this class for more details about what we will be doing inside it.
 * - So, in total, we have made 3 changes:
 * 1. Added 3 dependencies related to security.
 * 2. Create a class inside config package named - SecurityConfig.java
 * 3. Mentioned the property security.security.oauth2.resourceserver.jwt.jwk-set-uri inside the application.yml
 * - With this, now, our gatewayserver should be protected and will act as a resource server inside the Client Credentials grant type flow.
 * Next, we will be testing the changes that we have just done - the security related changes. To test these changes, start all your applications in the order of: RabbitMQ service,configserver, eurekaserver, Accounts/Loans/Cards, and at last gatewayserver. In our 'microservices' postman collection, you should be able to see a folder with the name - 'gatewayserver_security'. First, we are going to visualize that all the GET APIs inside our ms's are going to work without any security. For the same yoy can see we have defined different GET API requests under the folder - 'gatewayserver_security' i.e., Accounts_GET_PermitAll ( here we are trying to invoke the path 'contact-info'), Cards_GET_PermitAll(here we are trying to invoke the path 'java-version'), and Loans_GET_PermitAll(here we are trying to invoke the path 'build-info'). For all these requests, under the postman, you can clearly see that we have not configured anything under the 'Authorization' tab. If you fire these request you should be able to get a successful response which should confirm that, there is no security expected for GET Rest APIs invocation in any of our ms's.
 * We also have POST API's i.e., Accounts_POST_NoAuth, For this request, I am not configuring any authentication information in the Authorization tab in Postman. On firing this request, we should expect a 401 error which implies that we are not authorized to invoke this API. The reason is simple, we didn't send any access token. With this you should be super happy because our gatewayserver is now secured. Now, lets see how to test our secured APIs by passing an access token. For this, invoke the Request 'Accounts_POST_ClientCredentials' available inside the 'gatewayserver_security' folder. In this request under the authorization tab, we are going to send some authorization information. So, Approach 1: first make sure to invoke, 'ClientCredentials_AccessToken' request that is under the folder 'KeyCloak' in order to get the access token. Copy that access token and mention it under the postman request that we are going to send. This approach seems to be a lengthy one haha. Instead, lets leverage one of the beautiful features provided by the postman in our approach 2. Under the Authorization tab, If you can open the 'Type' dropdown, you should be able to see various authorization styles supported by the Postman. Since we are trying to leverage OAuth2.0, select that. Under the section, 'Configure New Token' it will ask you for various inputs i.e., 'Token Name' that you want to consider - For that I gave the random name like 'clientcredentails_accesstoken'. Post that, it will ask you for an input like, 'Grant type', it is a dropdown, so, since we are trying to use the 'Client Credentials' make sure to select that. It will then ask you for the 'Access Token URL' - You can mention the same which we were using in the request like 'ClientCredentials_AccessToken' under the microservices/KeyCloak folder in our collection. It will also ask you for inputs like, 'Client ID','Client Secret', 'Scope' details. If you can scroll down in this same section, you will see another input like 'Client Authentication' - Make sure you select from the dropdown the input like, 'Send Client Credentials in Body' After this, we also have an advanced section where you can configure more stuff, but we didn't configure anything in that section. After this, you can finally click the button 'Get new access token'.
 *  Behind the scenes a request is going to be send to the auth server and we will get a new access token which you can use as part of the actual request that we want to invoke which is 'Accounts_POST_ClientCredentials'. On clicking on the 'Use Token' button, this will then be populated in in the 'authorization' tab just below a label like, 'Current Token'. All the details you can see there. Like you can see, under the 'Use Token type' label, since I have selected the value, 'Access Token', the same will be populated in an input field just below. We also have the 'ID Token' option in that dropdown, but since we want to send the access token, we should select the 'Access Token' option. Also for the input field label 'Token', make sure to select your access token name that you just configured i.e., 'clientcredentails_accesstoken'.  On the RHS of this section in your postman, you can be able to see how you need to send your access token, check the label like, "Add Authorization data to", here you can select either, 'Request URL' or 'Request Headers'. So make sure to select 'Request Headers'. Inside the request headers we want to send this access token. With the header prefix as 'Bearer' as  you can see in your Postman authorization tab on the RHS section. So, if you go to headers, right now you should be able to see a field like 'Authorization' and inside that field, you can see the access token that you just got from the auth server having a header prefix like 'Bearer '. Note the space after the prefix. This approach 2 is going to help you to avoid the manual process of getting the access token from the auth server using a separate request i.e., 'ClientCredentials_AccessToken' that in our case is present under the 'KeyCloak' folder. And then copying it and pasting it here under the authorization tab. Instead of that all manual process, whenever you want to get an access token, you can use the approach 2. Once everything is configured and anytime you want to get a new access token, you can just be navigating to the 'authorization' tab and click on the 'Get new access token'	button and then click on the 'Use Token' button and that will replace the old access token with a new one under the 'authorization' tab 'use token type' input. Now, we want to fire our request and before that, make sure the body/input that we are sending to the endpoint are correct/sanitized.
 *  Now, if you click on the send button, you should be able to get a successful response because right now I am using a valid access token. If I fire this reqeust second time I will get 400 BAD REQUEST with a message "Customer already registered with given mobileNumber 4354437688" - this is expected. Now, if I try to tamper the access token and even remove or replace one of the characters such that the access token is some random value and then fire the request, you should get a 401 unauthorized error. This tells us that our gatewayserver/resourceserver now is smart enough to validate my access token. Same drill, you can invoke the other POST requests present in other ms's i.e., gatewayserver_security/CARDS_POST_ClientCredentials, gatewayserver_security/LOANS_POST_ClientCredentials in order to validate and cement on your understanding on what we have just discussed. As a last step, you can invoke the request "gatewayserver_security/Accounts_GET_PermitAll" to cement on what we have also discussed obout our GET APIs - they don't need any authentication to be accessible. If you click on the send button, happily you should get a successful response.
 *  With this you should have crisp clarity on all that we have discussed so far.
 *
 *
 *  * */
@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/* This method is going to create a bean of type RouteLocator and return it.
	* Inside this RouteLocator only, we are going to send all our custom routing related configurations based on our requirements.
	**/
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/eazybank/accounts/**")
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))

				.route(p -> p.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("lb://LOANS"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())))
						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 1, 1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
