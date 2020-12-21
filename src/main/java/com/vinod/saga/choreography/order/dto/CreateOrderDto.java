package com.vinod.saga.choreography.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {
    private Long customerId;
    private Long inventoryId;
    private BigDecimal amount;
}
