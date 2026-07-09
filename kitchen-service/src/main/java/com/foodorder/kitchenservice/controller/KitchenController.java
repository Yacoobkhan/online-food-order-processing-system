package com.foodorder.kitchenservice.controller;

import com.foodorder.kitchenservice.dto.KitchenPrepareRequest;
import com.foodorder.kitchenservice.model.KitchenTicket;
import com.foodorder.kitchenservice.repository.KitchenTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * REST controller handling kitchen ticket creation / food preparation simulation.
 */
@RestController
@RequestMapping("/api/kitchen")
public class KitchenController {

    private final KitchenTicketRepository kitchenTicketRepository;
    private final Random random = new Random();

    @Autowired
    public KitchenController(KitchenTicketRepository kitchenTicketRepository) {
        this.kitchenTicketRepository = kitchenTicketRepository;
    }

    /**
     * Simulate food preparation for an order.
     * Generates a random prep time between 5 and 20 minutes and creates a KitchenTicket with status "READY".
     */
    @PostMapping("/prepare")
    public ResponseEntity<Map<String, Object>> prepareFood(@RequestBody KitchenPrepareRequest request) {
        int prepTime = 5 + random.nextInt(16); // Generates 5..20 inclusive
        KitchenTicket ticket = KitchenTicket.builder()
                .orderId(request.getOrderId())
                .status("READY")
                .prepTimeMinutes(prepTime)
                .build();
        kitchenTicketRepository.save(ticket);
        System.out.println("[KitchenService] Order #" + request.getOrderId() + " - Kitchen ticket created, preparing food... READY");
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", request.getOrderId());
        response.put("status", "READY");
        response.put("prepTimeMinutes", prepTime);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
