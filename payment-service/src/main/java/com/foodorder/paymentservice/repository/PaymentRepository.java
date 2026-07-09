package com.foodorder.paymentservice.repository;

import com.foodorder.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Payment} entities.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Additional query methods can be defined here in the future.
}
