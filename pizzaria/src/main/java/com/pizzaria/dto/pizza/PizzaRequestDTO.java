package com.pizzaria.dto.pizza;

import java.util.List;

public record PizzaRequestDTO(String name, List<String> ingredients, String imageUrl) {
}