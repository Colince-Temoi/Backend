package com.get_tt_right.authserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** This class of ours which is implementing AuthenticationProvider, is leveraging the UserDetailsService implementation to load the user details.
 * You can clearly see in the method - authenticate, we are trying to get the username, pwd. The username we are trying to pass to the loadUserByUsername behavior to the implementation of UserDetailsService.
 * I also created an implementation i.e., Get_tt_rightBankUserDetailsService which if you check it out, we are simply loading the user details from the DB by leveraging the CustomerRepository interface. Check out it's docstring for more details.
 * With the help of PasswordEncoder we are trying to check if the pwds are matching after the hashing process. If the pwds are matching, we are going to create an object of UsernamePasswordAuthenticationToken which represents successful authentication. Else, we are going to throw BadCredentialsException with the message 'Invalid password!'
 * After setting up the two classes i.e., Get_tt_rightBankUsernamePwdAuthenticationProvider and Get_tt_rightBankUserDetailsService, we also created a couple of beans inside the ProjectSecurityConfig class. Towards the end of the class ProjectSecurityConfig, you can see we have created a bean of PasswordEncoder, CompromisedPasswordChecker
 * */
@Component
@RequiredArgsConstructor
public class Get_tt_rightBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("Before IF block");
        System.out.println("Pwd is "+pwd);
        System.out.println("Pwd from DB is "+userDetails.getPassword());
        if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
            System.out.println("Inside IF block");
            return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
        } else {
            System.out.println("Inside ELSE block");
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}