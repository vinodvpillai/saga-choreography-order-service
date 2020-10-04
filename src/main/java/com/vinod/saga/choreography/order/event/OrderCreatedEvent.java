package com.vinod.saga.choreography.order.event;

import com.vinod.saga.choreography.order.dto.EventOrderProcessing;
import com.vinod.saga.choreography.order.service.IPublisherService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderCreatedEvent {
    @Autowired
    private IPublisherService publisherEventService;

    @Value("${sns.topic.order.created}")
    private String snsOrderCreated;

    public void raiseEventForOrderCreated(EventOrderProcessing eventOrderCreated) {
        log.trace("Request came to raise event for order created: {}",eventOrderCreated);
        try{
            publisherEventService.publish(snsOrderCreated,eventOrderCreated,"Order_Created");
            log.info("Successfully publish message for order created: {}",eventOrderCreated);
        } catch (Exception e) {
            log.trace("Error occurred while raising event for order created: {}",eventOrderCreated);
        }
    }
}
