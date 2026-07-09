package com.foodorder.orderservice.delegate;

import com.foodorder.orderservice.model.Order;
import com.foodorder.orderservice.repository.OrderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Cancels the order by setting its status to CANCELLED.
 */
@Component
public class CancelOrderDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(CancelOrderDelegate.class);

    private final OrderRepository orderRepository;

    public CancelOrderDelegate(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long orderId = (Long) execution.getVariable("orderId");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for id " + orderId));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
        log.info("[OrderService] Order #{} - CANCELLED", orderId);
    }
}
