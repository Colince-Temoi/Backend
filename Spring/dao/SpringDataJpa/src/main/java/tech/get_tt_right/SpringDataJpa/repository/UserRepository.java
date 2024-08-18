package tech.get_tt_right.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.SpringDataJpa.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
