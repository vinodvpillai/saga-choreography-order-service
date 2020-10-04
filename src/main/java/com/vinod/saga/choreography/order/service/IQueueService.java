package com.vinod.saga.choreography.order.service;

public interface IQueueService {

    public void sendMessage(String queue, String message);
}
