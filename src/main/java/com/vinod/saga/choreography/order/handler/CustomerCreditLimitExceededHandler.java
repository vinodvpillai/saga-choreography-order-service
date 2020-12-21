package com.vinod.saga.choreography.order.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinod.saga.choreography.order.dto.CreditLimitExceededEventData;
import com.vinod.saga.choreography.order.service.IOrderService;
import com.vinod.saga.choreography.order.util.ApplicationConstant;
import com.vinod.saga.choreography.order.util.GlobalUtility;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerCreditLimitExceededHandler {
    @Value("${sqs.queue.customer.credit.limit.exceeded}")
    private String sqsCustomerCreatedLimitExceeded;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private IOrderService orderService;
    private ObjectMapper objectMapper = GlobalUtility.getDateFormatObjectMapper();

    @JmsListener(destination = "${sqs.queue.customer.credit.limit.exceeded}")
    void limitExceededListener(String message) throws Exception {
        log.trace("Event received that a customer credit limit exceeded");
        JsonNode jsonMessage = GlobalUtility.isNotNull(objectMapper.readTree(message)) ? objectMapper.readTree(message).get("Message") : null;
        if (GlobalUtility.isNotNull(jsonMessage)) {
            CreditLimitExceededEventData creditLimitExceededEventData = objectMapper.readValue(jsonMessage.asText(), CreditLimitExceededEventData.class);
            log.trace("Received customer credit limit exceeded object: {}", creditLimitExceededEventData);
            orderService.updateOrderStatus(creditLimitExceededEventData.getOrderId(), ApplicationConstant.OrderStatus.REJECTED.value());
        } else {
            log.warn("No message found in the message for: {} and the message: {}" + sqsCustomerCreatedLimitExceeded, message);
        }
    }
}
