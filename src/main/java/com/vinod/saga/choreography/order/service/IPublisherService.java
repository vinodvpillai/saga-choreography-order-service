package com.vinod.saga.choreography.order.service;

public interface IPublisherService {

    <T> String publish(String topic, T msg, String subject);
}
