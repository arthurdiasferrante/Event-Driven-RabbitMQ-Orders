    package com.pizzaria.controller;

    import com.pizzaria.model.Pizza;
    import com.pizzaria.repository.PizzaRepository;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RequestMapping("/pizzas")
    @RestController
    public class PizzaController {

        private PizzaRepository pizzaRepository;

        public PizzaController(PizzaRepository pizzaRepository) {
            this.pizzaRepository = pizzaRepository;
        }

        @PostMapping
        public Pizza newPizza(@RequestBody Pizza pizza) {
            return pizzaRepository.save(pizza);
        }

        @GetMapping
        public List<Pizza> getPizzas() {
            return pizzaRepository.findAll();
        }

    //    @GetMapping
    //    public List<String> getPizzaIngredients(Long id) {
    //        if (pizzaRepository.findById(id).isEmpty()) {
    //            throw new RuntimeException("Pizza not found");
    //        }
    //
    //        return pizzaRepository.findById(id).get().getIngredients();
    //    }


        @PutMapping("/{id}")
        public String updatePizza(@PathVariable Long id, @RequestBody Pizza newData) {
            var pizzaEntity = pizzaRepository.findById(id);
            if (pizzaEntity.isEmpty()) {
                return "Pizza not found";
            }

            Pizza pizza = pizzaEntity.get();
            pizza.setName(newData.getName());
            pizza.setIngredients(newData.getIngredients());
            pizza.setImageUrl(newData.getImageUrl());

            pizzaRepository.save(pizza);
            return "Pizza ID: {" + pizza.getId() + "} updated successfully";
        }

        @DeleteMapping("/{id}")
        public String deletePizza(@PathVariable Long id) {
            var pizzaEntity = pizzaRepository.findById(id);
            if (pizzaEntity.isEmpty()) {
                return  "Pizza not found";
            }
            Pizza pizza = pizzaEntity.get();
            String msg = "Pizza " + pizza.getName() + " deleted successfully";
            pizzaRepository.deleteById(id);
            return msg;
        }
    }
