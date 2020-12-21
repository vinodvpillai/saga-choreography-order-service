package com.vinod.saga.choreography.order.service;

import com.vinod.saga.choreography.order.dto.CreateOrderDto;
import com.vinod.saga.choreography.order.dto.OrderQueryDto;

public interface IOrderService {

    /**
     * Add new order.
     *
     * @param createOrderDto  - Create Order object.
     * @return          - Persisted order object.
     */
    OrderQueryDto createOrderAndRaiseEvent(CreateOrderDto createOrderDto);

    /**
     * Get order details by order id.
     *
     * @param orderId   - Order Id.
     * @return          - Order object.
     */
    OrderQueryDto getOrderById(String orderId);

    /**
     * Udpate order status.
     *
     * @param orderId
     * @param status
     */
    void updateOrderStatus(String orderId, String status);
}
