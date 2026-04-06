package com.pizzaria.model;


import com.pizzaria.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_orders")
public class Order {

    private OrderStatus orderStatus = OrderStatus.PENDING;

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderAt = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    @ManyToMany
    @JoinTable(
            name = "order_pizza",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private List<Pizza> pizzas;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(LocalDateTime now) {
        this.orderAt = now;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

}
