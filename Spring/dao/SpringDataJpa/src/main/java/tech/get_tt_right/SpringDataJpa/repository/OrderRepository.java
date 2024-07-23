package tech.get_tt_right.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.SpringDataJpa.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Query method - Find by orderTrackingNumber
    Order findByOrderTrackingNumber(String orderTrackingNumber);
}
