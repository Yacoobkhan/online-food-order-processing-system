package com.foodorder.paymentservice.controller;

import com.foodorder.paymentservice.dto.PaymentRequest;
import com.foodorder.paymentservice.model.Payment;
import com.foodorder.paymentservice.repository.PaymentRepository;
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
 * REST controller handling payment processing.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    @Autowired
    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Process a payment for an order.
     * Simulates an 80% success rate.
     */
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody PaymentRequest request) {
        boolean success = random.nextDouble() < 0.80; // 80% chance of success
        String status = success ? "SUCCESS" : "FAILED";
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .status(status)
                .build();
        paymentRepository.save(payment);
        System.out.println("[PaymentService] Order #" + request.getOrderId() + " - Payment processing... " + status);
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", request.getOrderId());
        response.put("status", status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
