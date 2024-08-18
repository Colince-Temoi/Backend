package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Role;
import tech.get_tt_right.SpringDataJpa.entity.User;

import java.util.List;

@SpringBootTest
public class ManyToManyBidirectionalTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void saveRole(){
        // Create a user user - this is one user
        User user = new User();
        user.setFirstName("ramesh");
        user.setLastName("fadatare");
        user.setEmail("ramesh@gmail.com");
        user.setPassword("secrete");

        // Create a admin user - this is another user
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin");

        // Create a role - Admin role
        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        // Add the users to the Set container of users in the role object
        roleAdmin.getUsers().add(user);
        roleAdmin.getUsers().add(admin);

        // Add the role to the Set container of roles in the user object
        user.getRoles().add(roleAdmin);
        admin.getRoles().add(roleAdmin);

        // Save the role along with the users
        roleRepository.save(roleAdmin);
    }

    @Test
    void fetchRole(){
        List<Role> roles = roleRepository.findAll();
        roles.forEach((r) ->{
            System.out.println(r.getName());
            r.getUsers().forEach((u) ->{
                System.out.println(u.getFirstName());
            });
        });
    }
}

