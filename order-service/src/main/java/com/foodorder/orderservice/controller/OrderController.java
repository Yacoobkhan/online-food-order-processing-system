package com.foodorder.orderservice.controller;

import com.foodorder.orderservice.dto.OrderRequest;
import com.foodorder.orderservice.model.Order;
import com.foodorder.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller exposing order management endpoints.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final org.springframework.jms.core.JmsTemplate jmsTemplate;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    public OrderController(OrderRepository orderRepository, org.springframework.jms.core.JmsTemplate jmsTemplate, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Create a new order with status "PLACED".
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .item(request.getItem())
                .amount(request.getAmount())
                .status("PLACED")
                .build();
        Order saved = orderRepository.save(order);
        System.out.println("[OrderService] Order #" + saved.getId() + " - Status: PLACED, Order created");
        // Build event DTO
        com.foodorder.orderservice.event.OrderCreatedEvent event = new com.foodorder.orderservice.event.OrderCreatedEvent(
                saved.getId(),
                saved.getCustomerName(),
                saved.getItem(),
                saved.getAmount(),
                saved.getStatus()
        );
        try {
            String json = objectMapper.writeValueAsString(event);
            jmsTemplate.convertAndSend("order.created", json);
            System.out.println("[OrderService] Order #" + saved.getId() + " - Published to order.created queue");
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OrderCreatedEvent", e);
        }
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Retrieve all orders sorted by id descending (most recent first).
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve a single order by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            return ResponseEntity.ok(optionalOrder.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Order not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
