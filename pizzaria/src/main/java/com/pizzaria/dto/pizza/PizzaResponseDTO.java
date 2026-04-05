package com.pizzaria.dto.pizza;

import java.util.List;

public record PizzaResponseDTO(Long id, String name, List<String> ingredients, String imageUrl) {
}
