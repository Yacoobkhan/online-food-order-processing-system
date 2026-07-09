package com.foodorder.kitchenservice.repository;

import com.foodorder.kitchenservice.model.KitchenTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link KitchenTicket} entities.
 */
@Repository
public interface KitchenTicketRepository extends JpaRepository<KitchenTicket, Long> {
    // Future custom query methods can be added here
}
