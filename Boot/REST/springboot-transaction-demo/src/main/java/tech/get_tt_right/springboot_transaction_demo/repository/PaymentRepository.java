package tech.get_tt_right.springboot_transaction_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.get_tt_right.springboot_transaction_demo.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
