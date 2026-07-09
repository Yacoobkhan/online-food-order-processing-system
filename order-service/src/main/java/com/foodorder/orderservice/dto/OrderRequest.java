package com.foodorder.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new order via the REST API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customerName;
    private String item;
    private java.math.BigDecimal amount;
}
