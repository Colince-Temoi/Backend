package com.get_tt_right.springsecOAUTH2.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** I'm not going to build a REST API, so I'm using @Controller instead of @RestController
*  Here I'm going to build an MVC path. That's why I'm using @Controller.
 *  Create a html file under the resources/static folder with the name secure.html
 *  To application.properties add the username and password which we can use whenever we are trying to access the secure page using a normal logon flow.
 * Run your application in debug mode and try to access the secure page. Enter your credentials. You should be able to access the secure page.
 * This is a simple spring boot application which is being secured by default since we have added the spring security dependency.
 * As of now we have a normal login flow. As a next step we will add the OAuth2 login flow/capability to our application.
 * We will give options to our end users to either use the normal login flow or he should be able to login into our application with the help of social login providers like github or facebook so that he can be able to access the secure page.
 * To enhance this application to adopt oauth2 login flow, I created the class ProjectSecurityConfig.java under the config package.
 *
 * */
@Controller
public class SecureController {

    /**
     * Maps the "/secure" URL to the secure.html page. This page is accessible only when you are logged in.
     * @param authentication the authentication object, which you can use to determine if the user is logged in via OAuth2 or a regular User/Password.
     * @return the name of the view.
     * This is just a simple mvc path, we don't want to build complex logic because we are trying to see the Demo of oauth2 login but not the mvc path concepts or REST API concepts.
     * As of now inside our application, we are allowing 2 styles of authentication. One is the normal login flow and the other one is the OAuth2 login flow.
     * Regardless of whatever style of authentication that I'm trying to follow, what I'm doing is accepting an object of Authentication as an input to this method.
     * Because in all types of authentication/login flows, the framework is going to is going to create an object of type Authentication interface.
     * In the scenario of normal login flow, the object of type Authentication is going to be an object of type UsernamePasswordAuthenticationToken.
     * In the scenario of OAuth2 login flow, the object of type Authentication is going to be an object of type OAuth2AuthenticationToken.
     * So, I have a small piece of code here which is going to check whether the object of type Authentication is an object of type UsernamePasswordAuthenticationToken or an object of type OAuth2AuthenticationToken.
     * If it is an object of type UsernamePasswordAuthenticationToken, I'm going to print the object of type UsernamePasswordAuthenticationToken.
     * If it is an object of type OAuth2AuthenticationToken, I'm going to print the object of type OAuth2AuthenticationToken.
     * To test this, I'm going to run my application in debug mode (Placing breakpoints in the code where we are printing the objects) and try to access the secure page.
     * I'm going to log in with the normal login flow(Check credentials in application.properties) and I'm going to see the object of type UsernamePasswordAuthenticationToken printed on the console when I debug this method(Using step over) and the first if block is executed
     * Also inside this UsernamePasswordAuthenticationToken object, I can be able to see various properties like the principle and other details like remote address, session id, etc. It is also going to have the authenticated flag set to true which means the user is authenticated/ the authentication os successful.
     * If you release the breakpoint and try to access the secure page again, you will see the secure page/the expected output.
     * logout from the session by invoking the /logout endpoint (The default path provided by Spring Security to logout) and try to access the secure page again. You will be redirected to the login page.
     * Now try to access the secure page using the OAuth2 login flow. You will see the object of type OAuth2AuthenticationToken printed on the console when I debug this method(Using step over) and the second if block is executed
     * Click on the login with GitHub button, and you will be redirected to the GitHub login page. Enter your credentials, and you will be redirected to the secure page.
     * Like this, my end-user have full confidence that SpringsecOAUTH2 application are not asking for my GitHub credentials directly. Instead, they are leveraging the OUTH2 standards to get an access token on my behalf and then they are using that access token to access the GitHub resources on my behalf.
     * After inputting your GitHub credentials and clicking on the login button, and with that you may notice that GitHub is trying to give an option of adding a passkey. For now I don't want to add a passkey, so I'm going to skip this step.
     * Next, you will see a consent screen where GitHub is asking you to authorize the SpringsecOAUTH2 application to access your GitHub resources (Personal/profile user data) on your behalf.If you click on the drop-down you will see more information regarding the data that is being accessed.
     * You will also be able to see the scope(read-only) of the resources that are being requested by the SpringsecOAUTH2 application. If you go to the ProjectSecurityConfig.java class and open the GITHUB enum, you will see that they are requesting as scope read:user.
     * With this scope, GitHub is going to grant all the user information for the client application as a response.
     * If you click on the authorize button, you will be redirected to the secure page.
     * Sometimes you may be redirected to lh:8080 instead of lh:8080/secure, and you may see a white label error page. Its because we have not configured any page for the root path. So, you can ignore this error and try to access the secure page again.
     * You will be able to see the secure page.
     * Or you cloud initially b4 login, you can access the secure page and you will be redirected to the login page. After login, you will be redirected to the secure page. Spring security will be able to remember the path that you were trying to access before you were redirected to the login page.
     * Now if you try to debug the securePage method, you will see the object of type OAuth2AuthenticationToken printed on the console when I debug this method(Using step over) and the second if block is executed.
     * If I try to see what is present inside the OAuth2AuthenticationToken object, I can see that it is going to have the principal object which has the attributes like the authorities(Nothing but the scopes i.e., OAUTH2_USER, SCOPE_read:user), attributes(Here you will be able to see a lot of interesting information about the user's GitHub profile - login username, Id maintained by GitHub, all my followers and whom I am following can also be identified by a given page indicated by the link + lot of other information. But to this client most of the time they will be interested in my username/name and email details), etc. etc.
     * Using this username and email details send by GitHub, the client can be able to create a user/account for me behind the scenes in their application.
     * If you release the breakpoint and try to access the secure page again, you will see the secure page/the expected output.
     * Now logout and try again accessing the secure page. This time try the facebook option. You will be redirected to the facebook login page. Enter your credentials, and you will be redirected to the secure page.
     * Make sure while doing this, debug the securePage method just like we did extensively for the GitHub login flow. Pay close attention to every detail during the process especially if it's your first time.
     * If you monitor closely what is present inside the OAuth2AuthenticationToken object, you will see that inside the principal under the attributes, you will be able to see the email, name, etc. of the user.
     * Using these details the client can be able to create a user/account for me behind the scenes in their application.
     * If you release the breakpoint and try to access the secure page again, you will see the secure page/the expected output.
     * This is how we can secure our application using the OAuth2 login flow. Expounding on this, we can also add more social login providers like Google, LinkedIn, etc. to our application.
     *
     */
    @GetMapping("/secure")
    public String securePage(Authentication authentication) {
        if(authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken){
            System.out.println(usernamePasswordAuthenticationToken);
        } else if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            System.out.println(oAuth2AuthenticationToken);
        }
        return "secure.html";
    }

}