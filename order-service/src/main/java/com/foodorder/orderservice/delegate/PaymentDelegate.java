package com.foodorder.orderservice.delegate;

import com.foodorder.orderservice.model.Order;
import com.foodorder.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Updates order status to PAYMENT, simulates a processing delay,
 * then calls the payment-service and stores the result in a process variable.
 */
@Component
public class PaymentDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(PaymentDelegate.class);

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public PaymentDelegate(RestTemplate restTemplate, OrderRepository orderRepository, PlatformTransactionManager transactionManager) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");

        // Give React polling time to fetch the initial 'PLACED' status before overriding it
        Thread.sleep(2000);

        // Step 1: Immediately update order status to PAYMENT in a new transaction so the dashboard can show it during the sleep
        transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for id " + orderId));
            order.setStatus("PAYMENT");
            return orderRepository.save(order);
        });
        log.info("[PaymentDelegate] Order #{} - Status updated to PAYMENT", orderId);

        // Step 2: Simulate payment processing delay (2 seconds)
        Thread.sleep(2000);

        // Step 3: Call the payment-service to determine SUCCESS or FAILED
        // Need to fetch fresh order object after sleep in case we need its properties
        Order freshOrder = orderRepository.findById(orderId).orElseThrow();
        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        request.put("amount", freshOrder.getAmount());

        String url = paymentServiceUrl + "/api/payments/process";
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        if (response == null || !response.containsKey("status")) {
            throw new RuntimeException("Invalid response from payment service");
        }
        String paymentStatus = (String) response.get("status");
        execution.setVariable("paymentStatus", paymentStatus);
        log.info("[PaymentDelegate] Order #{} - Payment result: {}", orderId, paymentStatus);
    }
}
