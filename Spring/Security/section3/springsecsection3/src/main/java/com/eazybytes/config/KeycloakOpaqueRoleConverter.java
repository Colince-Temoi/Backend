package com.eazybytes.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** It implements the OpaqueTokenAuthenticationConverter interface and overrides the convert() method.
 * Once the Resource Server is going to validate my access token with the auth server, this method is going to be invoked.
 * The very first parameter to this method indicates the introspected token. This is the same token that my client application will send to the Resource Server
 * The same token behind the scenes my Resource Server will validate with the Auth server by invoking an introspection endpoint.
 * Once the introspection is completed, this method is going to be invoked again with the introspected token and the OAuth2AuthenticatedPrincipal object.
 * You can also see the method input parameters description in the method documentation.
 * During the token introspection, what is going to happen is, the Resource Server is going to give the token received from the client to the Auth Server.
 * The Auth Server in-turn is going to validate the introspected token and give more details about the end-user like his username, roles, etc. All those details we are going to get in the form of OAuth2AuthenticatedPrincipal object.
 * Now, lets discuss the implementation of this method.
 * You can be able to see that from the OAuth2AuthenticatedPrincipal object, I am trying to get an attribute with the name preferred_username. This is the username of the client application whereas if an end-user is involved then we are going to get their username.
 * Next, I am trying to get the realm_access attribute from the OAuth2AuthenticatedPrincipal object. This realm_access attribute is going to give me an output which is a Map object of type String, Object.
 * From this realm_access object, I am trying to get the details by passing the roles key. This roles key is going to give me a list of roles that are associated with the client application.
 * Next, whatever logic we had initially written inside the KeycloakRoleConverter class, I am trying to replicate the same logic here. i.e., I am trying to convert the roles into a list of GrantedAuthority objects.
 * Once we have the roles information and username information, we need to create an object of UsernamePasswordAuthenticationToken indicating successful authentication.
 * The very first parameter we need to pass is the name of the end-user or the client application by invoking the getName() method of the OAuth2AuthenticatedPrincipal object or you can also pass the username variable that we got from preferred_username attribute.
 * The second parameter is the credentials. In our case, we are passing null here. The third parameter you can pass the roles which is of type Collection<GrantedAuthority>.
 * Once this object is created with enough information around the user the actual authorization logic is going to be taken care by my Spring Security framework.
 * And that's how we are going to leverage the Opaque token format inside our Resource Server.
 * Before we try to configure this KeycloakOpaqueRoleConverter inside our ProjectSecurityConfig class, we need to add few properties inside the application.properties file.
 *
 * */
public class KeycloakOpaqueRoleConverter implements OpaqueTokenAuthenticationConverter {

    /**
     * @param introspectedToken the bearer token used to perform token introspection
     * @param authenticatedPrincipal the result of token introspection
     * @return
     */
    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        String username = authenticatedPrincipal.getAttribute("preferred_username");
        Map<String, Object> realmAccess = authenticatedPrincipal.getAttribute("realm_access");
        Collection<GrantedAuthority> roles = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getName(), null,
                roles);
    }
}
