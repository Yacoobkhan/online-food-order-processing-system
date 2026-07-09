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
 * Updates order status to KITCHEN, simulates a food preparation delay,
 * then calls the kitchen-service to create a kitchen ticket.
 */
@Component
public class KitchenDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(KitchenDelegate.class);

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    @Value("${kitchen.service.url}")
    private String kitchenServiceUrl;

    public KitchenDelegate(RestTemplate restTemplate, OrderRepository orderRepository, PlatformTransactionManager transactionManager) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");

        // Step 1: Immediately update order status to KITCHEN in a new transaction so the dashboard can show it during the sleep
        transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for id " + orderId));
            order.setStatus("KITCHEN");
            return orderRepository.save(order);
        });
        log.info("[KitchenDelegate] Order #{} - Status updated to KITCHEN", orderId);

        // Step 2: Simulate food preparation delay (2 seconds)
        Thread.sleep(2000);

        // Step 3: Call the kitchen-service to create a kitchen ticket
        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        String url = kitchenServiceUrl + "/api/kitchen/prepare";
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        if (response == null) {
            throw new RuntimeException("Kitchen service returned null response");
        }
        log.info("[KitchenDelegate] Order #{} - Kitchen ticket created, food is READY", orderId);
    }
}
