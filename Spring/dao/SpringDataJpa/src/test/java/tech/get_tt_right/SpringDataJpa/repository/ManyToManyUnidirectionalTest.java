package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Role;
import tech.get_tt_right.SpringDataJpa.entity.User;

@SpringBootTest
public class ManyToManyUnidirectionalTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser(){
        // Create a user
        User user = new User();
        user.setFirstName("Colince");
        user.setLastName("Temoi");
        user.setEmail("tmi@gmail.com");
        user.setPassword("secrete");

        // Create role(s) - Admin and Customer roles for the user
        Role admin = new Role();
        admin.setName("ROLE_ADMIN");

        Role customer = new Role();
        customer.setName("ROLE_CUSTOMER");

        // Add the roles to the Set container of roles in the user object
        user.getRoles().add(admin);
        user.getRoles().add(customer);

        // Save the user along with the roles
        userRepository.save(user);
    }

    @Test
    void updateUser(){
        // Fetch the user from the database and update the user
        User user = userRepository.findById(1L).get();

        // Update the user details
        user.setFirstName("Col");
        user.setEmail("col@gmail.com");

        // Create and Add a new role for the user
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");

        // Add the new role to the Set container of roles in the user object
        user.getRoles().add(roleUser);

        // Save the user along with the new role
        userRepository.save(user);
    }

    @Test
    void fetchUser(){
        // Fetch the user from the database and display the user details along with the roles
        User user = userRepository.findById(1L).get();
        // Display the user detail(s)
        System.out.println(user.getEmail());

        // Display the role(s) of the user
        user.getRoles().forEach((r) -> {
            System.out.println(r.getName());
        });
    }

    @Test
    void deleteUser(){
        // Delete the user from the database along with the roles
        userRepository.deleteById(1L);
    }
}

