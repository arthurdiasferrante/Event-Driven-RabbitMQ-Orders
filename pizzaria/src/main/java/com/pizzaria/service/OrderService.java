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

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();

        for (Order order : orders) {
            List<String> pizzasName = new ArrayList<>();

            for (Pizza pizza : order.getPizzas()) {
                pizzasName.add(pizza.getName());
            }

            orderResponseDTOList.add(new OrderResponseDTO(order.getId(), order.getClient().getName(), pizzasName));
        }
        return orderResponseDTOList;
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID " + id));

        List<String> pizzasName = new ArrayList<>();
        for (Pizza pizza : orderEntity.getPizzas()) {
            pizzasName.add(pizza.getName());
        }

        return new OrderResponseDTO(orderEntity.getId(), orderEntity.getClient().getName(), pizzasName);
    }

    public OrderResponseDTO updateOrder(OrderRequestDTO orderRequestDTO, Long id) {
        Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        List<Pizza> pizzaList = pizzaRepository.findAllById(orderRequestDTO.pizzaIds());
        orderEntity.setPizzas(pizzaList);

        Order savedOrder = orderRepository.save(orderEntity);

        List<String> pizzasName = new ArrayList<>();
        for (Pizza pizza : pizzaList) {
            pizzasName.add(pizza.getName());
        }

        return new OrderResponseDTO(savedOrder.getId(), savedOrder.getClient().getName(), pizzasName);
    }

    public void deleteOrder(Long id) {
        Order orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        orderRepository.delete(orderEntity);
    }
}
