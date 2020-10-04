package com.vinod.saga.choreography.order.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinod.saga.choreography.order.dto.EventCustomerCreditLimit;
import com.vinod.saga.choreography.order.util.GlobalUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerCreditReservedListener {
    @Value("${sqs.queue.customer.credit.reserved}")
    private String sqsCustomerCreatedLimitReserved;
    @Autowired
    private JmsTemplate jmsTemplate;
    private ObjectMapper objectMapper= GlobalUtility.getDateFormatObjectMapper();

    @JmsListener(destination = "${sqs.queue.customer.credit.reserved}")
    void reserveCreditListener(String message) throws Exception {
        log.trace("Event received that a customer credit limit reserved");
        JsonNode jsonMessage= GlobalUtility.isNotNull(objectMapper.readTree(message)) ? objectMapper.readTree(message).get("Message"):null;
        if(GlobalUtility.isNotNull(jsonMessage)){
            EventCustomerCreditLimit receivedCustomerObject=objectMapper.readValue(jsonMessage.asText(), EventCustomerCreditLimit.class);
            log.trace("Received customer credit limit reserved object: {}",receivedCustomerObject);
        } else {
            log.warn("No message found in the message for: {} and the message: {}"+ sqsCustomerCreatedLimitReserved, message);
        }
    }
}
