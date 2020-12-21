package com.vinod.saga.choreography.order.service.impl;

import com.vinod.saga.choreography.order.dto.CreateOrderDto;
import com.vinod.saga.choreography.order.dto.OrderCreatedEventData;
import com.vinod.saga.choreography.order.dto.OrderQueryDto;
import com.vinod.saga.choreography.order.event.OrderCreatedEvent;
import com.vinod.saga.choreography.order.model.Order;
import com.vinod.saga.choreography.order.repository.OrderRepository;
import com.vinod.saga.choreography.order.service.IOrderService;
import com.vinod.saga.choreography.order.util.ApplicationConstant;
import com.vinod.saga.choreography.order.util.GlobalUtility;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.vinod.saga.choreography.order.util.ApplicationConstant.FORMAT_LEADING_ZERO;

@Slf4j
@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderCreatedEvent orderCreatedEvent;

    /**
     * Add new order.
     *
     * @param createOrderDto  - Create Order object.
     * @return          - Persisted order object.
     */
    @Override
    public OrderQueryDto createOrderAndRaiseEvent(CreateOrderDto createOrderDto) {
        log.trace("Request came to add new order having details: {}", createOrderDto);
        Order order=orderRepository.save(mapDataToOrder(createOrderDto));
        OrderQueryDto orderQueryDto = mapOrderToOrderQueryDto(order);
        raiseOrderCreatedEvent(orderQueryDto);
        log.trace("Successfully added new order and the details: {}",orderQueryDto);
        return orderQueryDto;
    }

    /**
     * Map CreateOrderDto to Order object.
     *
     * @param createOrderDto - CreateOrderDto object.
     * @return  - Order object.
     */
    private Order mapDataToOrder(CreateOrderDto createOrderDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(CreateOrderDto.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        Order order = modelMapper.map(createOrderDto, Order.class);
        order.setStatus(ApplicationConstant.OrderStatus.CREATED.value());
        log.trace("Mapped data: {} to order object: {}",createOrderDto,order);
        return order;
    }

    /**
     * Raise the customer created event.
     *
     * @param orderQueryDto - Order query Dto object.
     */
    private void raiseOrderCreatedEvent(OrderQueryDto orderQueryDto) {
        log.trace("Request came to raise event for order created event for order: {}", orderQueryDto);
        OrderCreatedEventData orderCreatedEventData = mapDataToOrderCreatedEventData(orderQueryDto);
        orderCreatedEvent.raiseEventForOrderCreated(orderCreatedEventData);
    }

    private OrderCreatedEventData mapDataToOrderCreatedEventData(OrderQueryDto orderQueryDto) {
        log.trace("Request came to map order object to event order created data for: {}", orderQueryDto);
        OrderCreatedEventData orderCreatedEventData = modelMapper.map(orderQueryDto, OrderCreatedEventData.class);
        return orderCreatedEventData;
    }

    /**
     * Map Order object to OrderQueryDto object.
     *
     * @param order - Order object.
     * @return      - OrderQueryDto object.
     */
    private OrderQueryDto mapOrderToOrderQueryDto(Order order) {
        modelMapper.typeMap(Order.class, OrderQueryDto.class).addMappings(mapper -> mapper.skip(OrderQueryDto::setOrderId));
        OrderQueryDto orderQueryDto = modelMapper.map(order, OrderQueryDto.class);
        orderQueryDto.setOrderId(GlobalUtility.formatId(String.valueOf(order.getId()),'O',FORMAT_LEADING_ZERO));
        orderQueryDto.setCustomerId(GlobalUtility.formatId(String.valueOf(order.getCustomerId()),'C',FORMAT_LEADING_ZERO));
        orderQueryDto.setInventoryId(GlobalUtility.formatId(String.valueOf(order.getId()),'I',FORMAT_LEADING_ZERO));
        log.trace("Mapped order object: {} to order query dto: {}",order,orderQueryDto);
        return orderQueryDto;
    }

    /**
     * Get order details by order id.
     *
     * @param orderId   - Order Id.
     * @return          - Order object.
     */
    @Override
    public OrderQueryDto getOrderById(String orderId) {
        log.trace("Request came to fetch the order details for order id : {}",orderId);
        Optional<Order> optionalOrder=orderRepository.findById(GlobalUtility.reformatId(orderId));
        if(optionalOrder.isPresent()){
            OrderQueryDto orderQueryDto=mapOrderToOrderQueryDto(optionalOrder.get());
            log.trace("Successfully fetched order details : {} for order id: {}",orderQueryDto,orderId);
            return orderQueryDto;
        }
        return null;
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        log.trace("Request came to update the order status to: {} for order id: {}",status,orderId);
        Optional<Order> optionalOrder=orderRepository.findById(GlobalUtility.reformatId(orderId));
        if(optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
            log.trace("Successfully updated status for order id: {} to order: {}",orderId, order);
        }
    }
}
