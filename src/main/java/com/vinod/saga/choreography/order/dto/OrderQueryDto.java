package com.vinod.saga.choreography.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryDto implements Serializable {
    private String orderId;
    private String customerId;
    private String inventoryId;
    private BigDecimal amount;
    private String status;
}
