package com.get_tt_right.authserver.config;

import com.get_tt_right.authserver.model.Customer;
import com.get_tt_right.authserver.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** In this class we are simply loading the user details from the DB by leveraging the CustomerRepository interface.
 * So, we are populating the authorities and at last we are trying to create an object of User by passing email, pwd and authorities as inputs. The same we are returning from this method.
 * Back to our Get_tt_rightBankUsernamePwdAuthenticationProvider with the help of PasswordEncoder we are trying to check if the pwds are matching after the hashing process. Check the Get_tt_rightBankUsernamePwdAuthenticationProvider class for more details.
 * */
@Service
@RequiredArgsConstructor
public class Get_tt_rightBankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new
                UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> authorities = customer.getAuthorities().stream().map(authority -> new
                SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new User(customer.getEmail(), customer.getPwd(), authorities);
    }

}