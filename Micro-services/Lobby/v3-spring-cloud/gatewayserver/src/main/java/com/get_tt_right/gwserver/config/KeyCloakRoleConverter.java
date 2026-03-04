package com.get_tt_right.gwserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Update as of 15/07/2025
 * -------------------------
 * This class is going to help me to extract the roles that I am getting from the KeyCloak auth server into a format that Spring security framework can understand. Make sure it is implementing the 'Converter<Jwt, Collection<GrantedAuthority>>' interface. Make sure you are importing the Jwt present inside the 'org.springframework.security.oauth2.jwt' package/library. Then implement the 'convert(Jwt source): Collection<GrantedAuthority>' method. In this method, we need to write the logic to read the role(s) information from the input/source Jwt access token and then convert the same into a Collection of 'GrantedAuthority' objects.
 * GrantedAuthority is an interface of available inside the Spring Security framework. Whenever we want to convey the role(s) or privileges information, we need to make sure we are sending those details with this interface format/specification only. Check the method docstring of the 'convert(Jwt source): Collection<GrantedAuthority>' method for more details.
 * **/
public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    /** On top of the source Jwt object reference, I am trying to invoke the method getClaims whose purpose is - will give you access to the 'payload data'/body available inside your access token. Post that I am going to fluently invoke the get method with the key 'realm_access'. Reason: If you hover on top of getClaims() method, you will realize it is of type java.util.Map<String, Object>. And that's why on top of this getClaims() method I am going to fluently invoke the get method with the Key "realm_access". Inside our Jwt access token that we decoded from the website jwt.io, you can be able to see that for the Decoded Payload we have this key, and It's what we are trying to retrieve its respective value which is an object. Inside this object value of 'realm_access', you can note that it is of type map as well because we have k-v pair inside it.
     *  That's why we are trying to assign it to a Map<String, Object> variable of types String and Object. And then we can use that Map<String, Object> variable to access the 'roles' key and get its respective value which is an array of role names. For some reason if the 'realmAccess' reference empty, then we are going to return an empty ArrayList. Otherwise, from this 'realmAccess' reference I am again going to invoke the get method with the key 'roles' which will give me the value(s) which is a List<String>. Once I have the List ready, I am fluently invoking the Stream API's stream method. On top of this stream method, I am fluently invoking the map method. Actually if you have forgotten what map method of stream API does, map method is going to iterate over all the values available inside the stream(Which are the role names - for now in our token you can be able to see  "default-roles-master","ACCOUNTS","offline_access",and "uma_authorization") and then for each of the role name, we are prefixing the string "ROLE_". Why are we doing this? Because, whenever we use the hasRole() method inside the Spring Security framework - Check with the bean method -springSecurityFilterChain - of type SecurityWebFilterChain, we have used the hasRole() method. Inside the hasRole() method, it expects the role to be in a format of "ROLE_" + roleName. If you enter into this method, you will notice that it's internally going to use a prefix "ROLE_" - this you can see from it's docstring when you download its sources. So, we are doing this to make sure we are sending the role(s) in this format. But inside the bean method -springSecurityFilterChain - of type SecurityWebFilterChain inside our custom class SecurityConfig to the hasRole() method, we don't need to do the prefixing of "ROLE_" + roleName. Because, behind the scenes inside the framework like we have seen when we dived into the hasRole() method, Spring Security framework internally is going to do the prefixing of "ROLE_" + roleName.
     *  Now, after performing the prefixing of "ROLE_" + roleName, the result is still going to be a String which Spring security won't understand. We need to pass the roles information to the framework as an object of SimpleGrantedAuthority. This SimpleGrantedAuthority is an implementation of GrantedAuthority interface. If you navigate into this SimpleGrantedAuthority framework class, you will notice there is a constructor which accepts the role name in the format of String and post that it is going to return the object of SimpleGrantedAuthority. So, the same constructor, I am invoking with the help of method reference syntax. With the 'new' operator I am trying to create the default constructor available inside the SimpleGrantedAuthority object.
     *  So, with the first map logic we are going append a prefix value for each role. Post that, again I am going to iterate over each transformed role name and convert that into a SimpleGrantedAuthority and at last the entire stream, I am trying to convert it a List which I am going to return from this method as can be seen.
     *  And yed, yeey! we have successfully created a role converter.
     *  * */
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
