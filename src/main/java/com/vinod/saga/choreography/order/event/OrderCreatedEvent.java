package com.vinod.saga.choreography.order.event;

import com.vinod.saga.choreography.order.dto.OrderCreatedEventData;
import com.vinod.saga.choreography.order.service.IPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.vinod.saga.choreography.order.util.ApplicationConstant.ORDER_CREATED_SQS;

@Service
@Slf4j
public class OrderCreatedEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.order.created}")
    private String snsOrderCreated;

    public void raiseEventForOrderCreated(OrderCreatedEventData orderCreatedEventData) {
        log.trace("Request came to raise event for order created: {}",orderCreatedEventData);
        try{
            publisherEventService.publish(snsOrderCreated,orderCreatedEventData,ORDER_CREATED_SQS);
            log.info("Successfully publish message for order created: {}",orderCreatedEventData);
        } catch (Exception e) {
            log.error("Error occurred while raising event for order created: {}",orderCreatedEventData);
        }
    }
}
