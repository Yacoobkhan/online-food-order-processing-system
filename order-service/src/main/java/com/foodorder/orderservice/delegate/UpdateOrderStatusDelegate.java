package com.foodorder.orderservice.delegate;

import com.foodorder.orderservice.model.Order;
import com.foodorder.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Final step: updates the order status to DELIVERED and completes the Camunda workflow.
 */
@Component
public class UpdateOrderStatusDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(UpdateOrderStatusDelegate.class);

    private final OrderRepository orderRepository;

    public UpdateOrderStatusDelegate(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for id " + orderId));
        order.setStatus("DELIVERED");
        orderRepository.save(order);
        log.info("[UpdateOrderStatusDelegate] Order #{} - Workflow COMPLETE, status updated to DELIVERED", orderId);
    }
}
