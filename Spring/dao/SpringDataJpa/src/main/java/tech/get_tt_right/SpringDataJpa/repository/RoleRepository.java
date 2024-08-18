package tech.get_tt_right.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.SpringDataJpa.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
