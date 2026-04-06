package com.pizzaria.dto.order;

import com.pizzaria.enums.OrderStatus;

import java.util.List;

public record OrderResponseDTO(Long id, String clientName, List<String> pizzaNameList, OrderStatus orderStatus) {
}