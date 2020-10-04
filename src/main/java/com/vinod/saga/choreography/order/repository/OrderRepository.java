package com.vinod.saga.choreography.order.repository;

import com.vinod.saga.choreography.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
