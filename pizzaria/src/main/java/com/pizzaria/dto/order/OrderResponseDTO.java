package com.pizzaria.dto.order;

import java.util.List;

public record OrderResponseDTO(Long id, String clientName, List<String> pizzaNameList) {
}
