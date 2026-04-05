package com.pizzaria.service;

import com.pizzaria.dto.pizza.PizzaRequestDTO;
import com.pizzaria.dto.pizza.PizzaResponseDTO;
import com.pizzaria.exception.PizzaNotFoundException;
import com.pizzaria.model.Pizza;
import com.pizzaria.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public PizzaResponseDTO createPizza(PizzaRequestDTO pizzaRequestDTO) {
        Pizza pizzaEntity = new Pizza();

        pizzaEntity.setName(pizzaRequestDTO.name());
        pizzaEntity.setIngredients(pizzaRequestDTO.ingredients());
        pizzaEntity.setImageUrl(pizzaRequestDTO.imageUrl());

        Pizza createdPizza = pizzaRepository.save(pizzaEntity);

        return new PizzaResponseDTO(createdPizza.getId(), createdPizza.getName(), createdPizza.getIngredients(), createdPizza.getImageUrl());
    }

    public List<PizzaResponseDTO> getPizzas() {
        List<PizzaResponseDTO> pizzaResponseDTOList = new ArrayList<>();
        List<Pizza> pizzas = pizzaRepository.findAll();

        for (Pizza pizza : pizzas) {
            pizzaResponseDTOList.add(new PizzaResponseDTO(pizza.getId(), pizza.getName(), pizza.getIngredients(), pizza.getImageUrl()));
        }

        return pizzaResponseDTOList;
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
