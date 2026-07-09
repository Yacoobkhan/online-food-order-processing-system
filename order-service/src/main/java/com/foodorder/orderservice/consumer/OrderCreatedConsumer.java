package com.foodorder.orderservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodorder.orderservice.event.OrderCreatedEvent;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Listens to the ActiveMQ queue "order.created" and starts the Camunda process
 * for every newly created order.
 */
@Component
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final RuntimeService runtimeService;

    public OrderCreatedConsumer(ObjectMapper objectMapper, RuntimeService runtimeService) {
        this.objectMapper = objectMapper;
        this.runtimeService = runtimeService;
    }

    @JmsListener(destination = "order.created")
    public void onMessage(String jsonMessage) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(jsonMessage, OrderCreatedEvent.class);
            // Start Camunda process instance with orderId variable
            runtimeService.startProcessInstanceByKey("order-process", java.util.Collections.singletonMap("orderId", event.getOrderId()));
            System.out.println("[OrderService] Order #" + event.getOrderId() + " - Workflow triggered automatically via ActiveMQ consumer");
        } catch (Exception e) {
            System.err.println("[OrderService] Failed to start workflow for message: " + jsonMessage);
            e.printStackTrace();
        }
    }
}
