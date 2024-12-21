package com.eazybytes.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to convert the Keycloak roles to Spring Security roles
 * Since the received access token is going to contain the roles information we need to write a converter which is going to take the responsibility of extracting the roles information from the access token.
 * Once the roles information is extracted, we need to convert the roles information into the form of SimpleGrantedAuthority because Spring Security framework can only understand the roles and authorities when we present them in the form of Granted Authority or SimpleGrantedAuthority.
 * So, yea, that's the purpose of this class. This class is going to implement an interface called Converter which is going to take the Jwt token as the source/input, and it is going to return the Collection of GrantedAuthority. Refer with the generic <Jwt, Collection<GrantedAuthority>>.
 * The first parameter is the source/input which is going to be the Jwt token and the second parameter is the target/output which is going to be the Collection of GrantedAuthority.
 * Make sure to import the Jwt class which is present in the org.springframework.security.oauth2.jwt package. You can say it is present in the Spring security library and in the package named oauth2.jwt.
 * Next you need to also import the Converter interface which is present in the org.springframework.core.convert.converter package.
 * Implement the method convert which accepts Jwt token as an input and returns the Collection of GrantedAuthority.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    /** First from the source/input which is the Jwt token, I am trying to get the claims data by invoking the getClaims method.
     * Inside claims, we have various kind of data/claims. But I am going to get a specific claim data with the key name as realm_access.
     * This method will make sense to you later once you see the access token. This we will see later. For now try to understand the logic we have written here.
     * Once we get this realm_access claim from the Jwt token. Okay inside this realm_access we are going to have lot many keys and values in the form of a map.
     * First, I am checking if the realm_access is null or empty. If it is null or empty, then I am going to return an empty list.
     * Otherwise, from the realm_access map I am trying to fetch the roles information by passing the key name as roles.
     * The value against to this key roles is going to be a list of strings. So, I am going to cast this list of strings to a list of strings.
     * Using the same, I am trying to invoke the stream method and post that I am going to invoke the map method. Since Spring Security is going to expect a prefix ROLE_ for each role, I am going to append the ROLE_ prefix to each role name.
     * Followed by, I am trying to convert each role string value into the object of SimpleGrantedAuthority.
     * At last, I am trying to convert my Stream of elements into a list of elements by invoking the collect method and passing the Collectors.toList() as an argument.
     * The output I am storing into the parent Collection of GrantedAuthority and returning the same.
     * With this, we created a KeycloakRoleConverter class with a behavior to convert the Keycloak roles to Spring Security roles.
     * As a next step, we need to configure this KeycloakRoleConverter class in our Spring Security configuration class- ProjectSecurityConfig class.
     * */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }
}
