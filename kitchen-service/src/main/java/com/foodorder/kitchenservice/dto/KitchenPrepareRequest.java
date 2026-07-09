package com.foodorder.kitchenservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for kitchen preparation request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitchenPrepareRequest {
    private Long orderId;
}
