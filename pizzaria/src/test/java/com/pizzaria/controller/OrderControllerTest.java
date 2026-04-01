package com.pizzaria.controller;

import com.pizzaria.model.Order;
import com.pizzaria.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void shouldReturn404ErrorWhenIdNotFound() throws Exception {
        mockMvc.perform(get("/orders/99")).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200OkWhenIdExists() throws Exception {
        Order fakeOrder = new Order();
        fakeOrder.setId(1L);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(fakeOrder));

        mockMvc.perform(get("/orders/1")).andExpect(status().isOk());
    }
}