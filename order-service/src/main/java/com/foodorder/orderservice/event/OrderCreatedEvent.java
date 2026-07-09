package com.foodorder.orderservice.event;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple DTO used for publishing an order creation event to ActiveMQ.
 * This class is NOT a JPA entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String customerName;
    private String item;
    private BigDecimal amount;
    private String status;
}
