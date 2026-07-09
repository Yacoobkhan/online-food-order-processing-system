package com.foodorder.deliveryservice.repository;

import com.foodorder.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Delivery} entities.
 */
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Future custom query methods can be added here
}
