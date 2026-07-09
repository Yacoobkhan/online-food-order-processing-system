package com.foodorder.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for assigning a driver to an order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAssignRequest {
    private Long orderId;
}
