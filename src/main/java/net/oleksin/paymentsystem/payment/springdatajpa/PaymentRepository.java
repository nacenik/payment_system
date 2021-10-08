package net.oleksin.paymentsystem.payment.springdatajpa;

import net.oleksin.paymentsystem.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
