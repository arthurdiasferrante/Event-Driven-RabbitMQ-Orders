package com.pizzaria.service;

import com.pizzaria.dto.order.OrderRequestDTO;
import com.pizzaria.dto.order.OrderResponseDTO;
import com.pizzaria.enums.OrderStatus;
import com.pizzaria.exception.PizzaNotFoundException;
import com.pizzaria.model.Client;
import com.pizzaria.model.Order;
import com.pizzaria.model.Pizza;
import com.pizzaria.repository.ClientRepository;
import com.pizzaria.repository.OrderRepository;
import com.pizzaria.repository.PizzaRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    AmqpTemplate amqpTemplate;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    PizzaRepository pizzaRepository;
    @InjectMocks
    OrderService orderService;

    @Test
    void shouldSendMessageToRabbitWithCorrectParameters() {
        List<Long> genericList = List.of(1L, 4L, 3L);
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, genericList);

        orderService.createOrder(requestDTO);

        Mockito.verify(amqpTemplate).convertAndSend("order.exchange", "order.created.routingKey", requestDTO);
    }

    @Test
    void processOrderShouldCreateOrderWithDTO() {
        Pizza mozzarella = new Pizza();
        Pizza marguerita = new Pizza();
        List<Pizza> pizzaList = List.of(mozzarella, mozzarella, marguerita);

        List<Long> genericList = List.of(1L, 1L, 3L);
        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, genericList);

        Client client = new Client();
        client.setName("java");


        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(pizzaRepository.findAllById(genericList)).thenReturn(pizzaList);

        orderService.processOrder(requestDTO);

        Mockito.verify(clientRepository).findById(1L);
        Mockito.verify(pizzaRepository).findAllById(genericList);

        Mockito.verify(orderRepository).save(Mockito.any(Order.class));

    }

    @Test
    void processOrderShouldThrownException() {
        Pizza mozzarella = new Pizza();
        Pizza marguerita = new Pizza();
        List<Pizza> imcompletePizzaList = List.of(mozzarella, marguerita);
        List<Long> genericList = List.of(1L, 1L, 3L);

        Client client = new Client();
        client.setName("java");
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        OrderRequestDTO requestDTO = new OrderRequestDTO(1L, genericList);

        Mockito.when(pizzaRepository.findAllById(genericList)).thenReturn(imcompletePizzaList);

        assertThrows(PizzaNotFoundException.class, () -> {
            orderService.processOrder(requestDTO);
        });
    }

}