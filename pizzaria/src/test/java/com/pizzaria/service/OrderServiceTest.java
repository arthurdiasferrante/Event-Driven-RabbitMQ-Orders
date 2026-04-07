package com.pizzaria.service;

import com.pizzaria.dto.order.OrderRequestDTO;
import com.pizzaria.dto.order.OrderResponseDTO;
import com.pizzaria.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    AmqpTemplate amqpTemplate;

    @InjectMocks
    OrderService orderService;


    @Test
    void shouldSendMessageToRabbitWithCorrectParameters() {
        List<Long> genericList = List.of(1L, 4L, 3L);
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, genericList, OrderStatus.PENDING);

        orderService.createOrder(requestDTO);

        Mockito.verify(amqpTemplate).convertAndSend("order.exchange", "order.created.routingKey", requestDTO);
    }
}