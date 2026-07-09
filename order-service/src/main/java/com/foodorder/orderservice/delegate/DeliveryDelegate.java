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
 * Updates order status to DELIVERY, simulates a delivery delay,
 * then calls the delivery-service to assign a driver.
 */
@Component
public class DeliveryDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(DeliveryDelegate.class);

    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    @Value("${delivery.service.url}")
    private String deliveryServiceUrl;

    public DeliveryDelegate(RestTemplate restTemplate, OrderRepository orderRepository, PlatformTransactionManager transactionManager) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");

        // Step 1: Immediately update order status to DELIVERY in a new transaction so the dashboard can show it during the sleep
        transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for id " + orderId));
            order.setStatus("DELIVERY");
            return orderRepository.save(order);
        });
        log.info("[DeliveryDelegate] Order #{} - Status updated to DELIVERY", orderId);

        // Step 2: Simulate delivery delay (2 seconds)
        Thread.sleep(2000);

        // Step 3: Call the delivery-service to assign a driver
        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        String url = deliveryServiceUrl + "/api/delivery/assign";
        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
        if (response == null) {
            throw new RuntimeException("Delivery service returned null response");
        }
        String driver = (String) response.get("driverName");
        log.info("[DeliveryDelegate] Order #{} - Driver assigned: {}, out for delivery", orderId, driver);
    }
}
