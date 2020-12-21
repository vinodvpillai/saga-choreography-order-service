package com.vinod.saga.choreography.order.controller;

import com.vinod.saga.choreography.order.dto.CreateOrderDto;
import com.vinod.saga.choreography.order.dto.OrderQueryDto;
import com.vinod.saga.choreography.order.service.IOrderService;
import com.vinod.saga.choreography.order.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vinod.saga.choreography.order.util.GlobalUtility.buildResponseForError;
import static com.vinod.saga.choreography.order.util.GlobalUtility.buildResponseForSuccess;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getOrder(@PathVariable("id") String id) {
        log.trace("Request came to get the order details having the id: {}", id);
        OrderQueryDto orderQueryDto = orderService.getOrderById(id);
        if (null != orderQueryDto) {
            return buildResponseForSuccess(HttpStatus.SC_OK, "Successfully fetched order", orderQueryDto);
        }
        return buildResponseForError(HttpStatus.SC_BAD_REQUEST, String.valueOf(HttpStatus.SC_BAD_REQUEST), "No order detail found for the given id.", null);
    }

    @PostMapping
    public ResponseEntity<Response> createNewOrder(@RequestBody CreateOrderDto createOrderDto) {
        try {
            log.trace("Request came to create new order with following details: {}", createOrderDto);
            OrderQueryDto orderQueryDto = orderService.createOrderAndRaiseEvent(createOrderDto);
            log.trace("Successfully created the order and the details are: {}", createOrderDto);
            return buildResponseForSuccess(HttpStatus.SC_OK, "Successfully created the order", orderQueryDto);
        } catch (Exception e) {
            log.error("Exception occurred while creating the order error msg: {}", e.getMessage(), e);
            return buildResponseForError(HttpStatus.SC_INTERNAL_SERVER_ERROR, String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR), "Unable to create the order.", null);
        }
    }
}
