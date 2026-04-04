package com.pizzaria.service;

import com.pizzaria.exception.OrderNotFoundException;
import com.pizzaria.model.Order;
import com.pizzaria.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.hibernate.metamodel.mapping.ordering.ast.OrderByComplianceViolation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order orderBody) {
        return orderRepository.save(orderBody);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID " + id));
    }

    public Order updateOrder(Order orderBody, Long id) {
        Order orderEntity = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        orderEntity.setPizzas(orderBody.getPizzas());
        return orderRepository.save(orderEntity);
    }

    public void deleteOrder(Long id) {
        Order orderEntity = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID + " + id));

        orderRepository.delete(orderEntity);
    }
}
