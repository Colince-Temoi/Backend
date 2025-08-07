package com.eazybytes.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Inside this class, we have changes related to the KeyCloak scenario. So, delete the very first line i.e., String username = authenticatedPrincipal.getAttribute("preferred_username");
 * Now, using the line of code like, authenticatedPrincipal.getAttribute, I am going to get the authorities information by using the 'scope' attribute instead of the 'realm_access'. On the LHS of this line of code, instead of Map, Map<String, Object> realmAccess, we are going to get a List, ArrayList<String> roles,  of roles information.
 * So, using these roles, I will directly invoke the stream method and rename the attribute 'roles' on the LHS of the expression with 'grantedAuthorities'. Now pass this 'grantedAuthorities' reference as an input to UsernamePasswordAuthenticationToken constructor. You can put a breakpoint on the line of code like: ArrayList<String> roles = authenticatedPrincipal.getAttribute("scope"); so that we can test the flow once more. Actually al these things we are doing a revision as we already learnt them.
 * Now, do a build/restart your auth server then in the request SpringSecurity/Section_16/myAccount request, where we have the ClientCredentials grant type flow set up in the Authorization tab. Try to get a new access token with the help of client credentials grant. This time you can see that it is not a JWT token, it is a random String value which if you try to copy it and paste it in the jwt.io site, you will be happily be greeted with an error because this is not a JWT token. It is a simple random string value which is an opaque token. Now, if you use this opaque token and fire a request from postman, the break point stops inside the KeycloakOpaqueRoleConverter. So, if you try to analyze the contents of this authenticatePrincipal reference, you will notice that under the delegate, then under the attributes, if you can go to the scope, we have values like openid, ADMIN and USER
 *  Using the same I am trying to map the granted authorities. Now, if I release the breakpoint and resume the execution, you can happily see that we got a successful response in our postman. You may have a question like, here inside the KeyCloakOpaqueRoleConverter.java class, why are we trying to read from the 'scope' element but not from the 'roles' element? AS inside the auth server, we have made some bean configurations in the bean jwtTokenCustomizer to read the roles information from the 'roles' claim/element. This is something that you're maybe pondering on about. haha. Why is it that we are trying to read from the scope here in the KeycloakOpaqueRoleConverter.java - this maybe your question/area of doubt. Whatever jwtTokeCustomizer bean method that we have written in the ProjectSecurityConfig inside the auth server, that will not be applicable for the opaque token. With that, the code inside that bean is never going to be executed and that's why I am not using the 'roles' element inside KeyClaokOpaqueRoleConverter.java to read the roles' information, instead we are using the 'scope' element to read the roles' or scope's information.
 *  Since the logic we have written inside the class KeycloakOpaqueRoleConverter is working fine for us, we didn't write any customizer bean method in the ProjectSecurityConfig.java of auth server to handle the Opaque token scenario. So far, you should be clear with the opaque token scenario as well just like you are also with jwt scenario as well. One thing to note is, I don't recommend the usage of Opaque tokens, so always use the self-contained or Jwt token format.
 * */
public class KeycloakOpaqueRoleConverter implements OpaqueTokenAuthenticationConverter {

    /**
     * @param introspectedToken the bearer token used to perform token introspection
     * @param authenticatedPrincipal the result of token introspection
     * @return
     */
    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
//        String username = authenticatedPrincipal.getAttribute("preferred_username");
//        Map<String, Object> realmAccess = authenticatedPrincipal.getAttribute("realm_access");

        ArrayList<String> roles = authenticatedPrincipal.getAttribute("scope");

//        Collection<GrantedAuthority> roles = ((List<String>) realmAccess.get("roles"))
//                .stream().map(roleName -> "ROLE_" + roleName)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());

        Collection<GrantedAuthority> grantedAuthorities = roles .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
//        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getName(), null,
//                roles);
        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getName(), null,
                grantedAuthorities);
    }
}
