package com.eazybytes.config;

import com.eazybytes.model.Customer;
import com.eazybytes.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* This class is responsible for authenticating the user based on the username and password provided.
* */
@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    /* We are autowiring the CustomerRepository and PasswordEncoder to fetch the user details from the database and to encode the password respectively.*/
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* Inside this method, it's our responsibility to define all the authentication logic right from :
    *  1. Fetching the user details from the database based on the username provided.
    * 2. Validating the password provided by the user with the password stored in the database.
    * 3. Post that we should create a successful authentication object by populating all the information into UsernamePasswordAuthenticationToken (i.e., username/principal, pwd/credentials, authorities )and return it to the Spring Security, ProviderManager.
    * * If you have any other custom logic to be implemented, you can do it here. I.e., Age validation, Country validation, etc. Based on the requirement.
    * * Based upon that if you want to perform the authentication or not you can write any kind of authentication logic here.
    * */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        // The raw password provided by the user.
        String pwd = authentication.getCredentials().toString();
        List<Customer> customer = customerRepository.findByEmail(username);
        /* If the user is present in the database, then we are checking the password provided by the user with the password stored in the database.
        * If the password matches, then we are creating a successful authentication object and returning it to the Spring Security.
        *  This code you can copy and adjust from what we have inside the authenticate method of DaoAuthenticationProvider.
         */
        if (customer.size() > 0) {
            if (passwordEncoder.matches(pwd, customer.get(0).getPwd())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                /* Adding the role of the user to the authorities list.
                * These details are present inside the user object which we fetched from the database.(role column in the database)
                * Trying to convert that string role value into a SimpleGrantedAuthority object and adding it to the authorities list.
                 */
                authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
                /* Creating a successful authentication object and returning it to the Spring Security.
                * If you go into its constructor, you can see we rae just setting the username, password/credentials and authorities to the object.
                * But the main thing is that we are setting the authenticated flag to true. Implying/conveying to the ProviderManager that the user is successfully authenticated.
                 */
                return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        }else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    /* Conveying to Spring Security that we are supporting Username Password style of Authentication .
    * You can copy this from DaoAuthenticationProvider.
    * */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}