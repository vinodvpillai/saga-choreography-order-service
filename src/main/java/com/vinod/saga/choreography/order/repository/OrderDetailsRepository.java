package com.vinod.saga.choreography.order.repository;

import com.vinod.saga.choreography.order.model.Order;
import com.vinod.saga.choreography.order.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
}
