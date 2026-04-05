package com.pizzaria.dto.order;

import com.pizzaria.model.Client;
import com.pizzaria.model.Pizza;

import java.util.List;

public record OrderRequestDTO(Long clientId, List<Long> pizzaIds) {
}
