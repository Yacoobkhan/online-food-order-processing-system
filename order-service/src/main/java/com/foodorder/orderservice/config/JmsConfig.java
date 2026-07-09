package com.foodorder.orderservice.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.jms.Queue;

/**
 * JMS configuration defining the destination queues.
 */
@Configuration
public class JmsConfig {

    @Bean(name = "order.created")
    public Queue orderCreatedQueue() {
        return new ActiveMQQueue("order.created");
    }
}
