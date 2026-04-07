package com.pizzaria.config;

import com.pizzaria.dto.order.OrderRequestDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderQueue() {
            return new Queue("order.v1.order-created");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("meu retorno");
    }

    @Bean
    public Binding binder(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("order.created.routingKey");
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() { // super chute
        return new Jackson2JsonMessageConverter();
    }

}
