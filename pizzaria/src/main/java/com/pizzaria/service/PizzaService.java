package com.pizzaria.service;

import com.pizzaria.model.Pizza;
import com.pizzaria.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public Pizza createPizza(Pizza pizzaBody) {
        return pizzaRepository.save(pizzaBody);
    }

    public List<Pizza> getPizzas() {
        return pizzaRepository.findAll();
    }

    public Pizza updatePizza(Long id, Pizza pizzaBody) {
        Pizza pizzaEntity = pizzaRepository.findById(id)
                .orElseThrow(() -> new )
    }

}
