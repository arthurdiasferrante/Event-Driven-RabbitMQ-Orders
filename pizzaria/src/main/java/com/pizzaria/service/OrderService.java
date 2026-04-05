package com.pizzaria.service;

import com.pizzaria.dto.order.OrderRequestDTO;
import com.pizzaria.dto.order.OrderResponseDTO;
import com.pizzaria.exception.ClientNotFoundException;
import com.pizzaria.exception.OrderNotFoundException;
import com.pizzaria.exception.PizzaNotFoundException;
import com.pizzaria.model.Client;
import com.pizzaria.model.Order;
import com.pizzaria.model.Pizza;
import com.pizzaria.repository.ClientRepository;
import com.pizzaria.repository.OrderRepository;
import com.pizzaria.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final PizzaRepository pizzaRepository;


    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, PizzaRepository pizzaRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.pizzaRepository = pizzaRepository;
    }

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Client clientEntity = clientRepository.findById(orderRequestDTO.clientId())
                        .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com ID " + orderRequestDTO.clientId()));

        List<Pizza> pizzaEntities = pizzaRepository.findAllById(orderRequestDTO.pizzaIds());

        if (pizzaEntities.size() != orderRequestDTO.pizzaIds().size()) {
            throw new PizzaNotFoundException("Uma ou mais pizzas não foram encontradas");
        }
        List<String> pizzaNames = new ArrayList<>();

        Order orderEntity = new Order();
        orderEntity.setClient(clientEntity);
        orderEntity.setPizzas(pizzaEntities);

        Order savedOrder = orderRepository.save(orderEntity);

        for (Pizza pizza : savedOrder.getPizzas()) {
            pizzaNames.add(pizza.getName());
        }

        return new OrderResponseDTO(savedOrder.getId(), savedOrder.getClient().getName(), pizzaNames);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID " + id));
    }

    public Order updateOrder(Order orderBody, Long id) {
        Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        orderEntity.setPizzas(orderBody.getPizzas());
        return orderRepository.save(orderEntity);
    }

    public void deleteOrder(Long id) {
        Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        orderRepository.delete(orderEntity);
    }
}
