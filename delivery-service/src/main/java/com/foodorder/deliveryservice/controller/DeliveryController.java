package com.foodorder.deliveryservice.controller;

import com.foodorder.deliveryservice.dto.DeliveryAssignRequest;
import com.foodorder.deliveryservice.model.Delivery;
import com.foodorder.deliveryservice.repository.DeliveryRepository;
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
 * REST controller handling driver assignment for deliveries.
 */
@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final Random random = new Random();
    private final String[] drivers = {"Ravi", "Kumar", "Suresh", "Anitha"};

    @Autowired
    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Assign a driver to an order and mark it as delivered.
     */
    @PostMapping("/assign")
    public ResponseEntity<Map<String, Object>> assignDriver(@RequestBody DeliveryAssignRequest request) {
        String driver = drivers[random.nextInt(drivers.length)];
        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .driverName(driver)
                .status("DELIVERED")
                .build();
        deliveryRepository.save(delivery);
        System.out.println("[DeliveryService] Order #" + request.getOrderId() + " - Driver assigned: " + driver + ", delivering... DELIVERED");
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", request.getOrderId());
        response.put("driverName", driver);
        response.put("status", "DELIVERED");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
