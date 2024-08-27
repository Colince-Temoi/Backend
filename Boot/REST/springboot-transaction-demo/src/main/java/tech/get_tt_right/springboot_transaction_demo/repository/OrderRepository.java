package tech.get_tt_right.springboot_transaction_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.springboot_transaction_demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
