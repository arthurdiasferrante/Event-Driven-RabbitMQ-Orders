package com.pizzaria.service;

import com.pizzaria.exception.PizzaNotFoundException;
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

    public Pizza getPizzaById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza não encontrada com ID " + id));
    }

    public Pizza updatePizza(Long id, Pizza pizzaBody) {
        Pizza pizzaEntity = pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza não encontrada com ID " + id));

        pizzaEntity.setName(pizzaBody.getName());
        pizzaEntity.setIngredients(pizzaBody.getIngredients());
        pizzaEntity.setImageUrl(pizzaBody.getImageUrl());

        return pizzaRepository.save(pizzaEntity);
    }

    public void deletePizza(Long id) {
        Pizza pizzaEntity = pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza não encontrada com ID " + id));

        pizzaRepository.delete(pizzaEntity);
    }
}
