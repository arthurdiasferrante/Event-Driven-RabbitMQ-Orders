    package com.pizzaria.controller;

    import com.pizzaria.model.Pizza;
    import com.pizzaria.repository.PizzaRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
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
        public ResponseEntity<Pizza> newPizza(@RequestBody Pizza pizza) {
            Pizza newPizza = pizzaRepository.save(pizza);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPizza);
        }

        @GetMapping
        public ResponseEntity<List<Pizza>> getPizzas() {
            return ResponseEntity.ok(pizzaRepository.findAll());
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
        public ResponseEntity<Pizza> updatePizza(@PathVariable Long id, @RequestBody Pizza newData) {
            var pizzaEntity = pizzaRepository.findById(id);
            if (pizzaEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Pizza pizza = pizzaEntity.get();
            pizza.setName(newData.getName());
            pizza.setIngredients(newData.getIngredients());
            pizza.setImageUrl(newData.getImageUrl());

            Pizza newPizza = pizzaRepository.save(pizza);

            return ResponseEntity.ok(newPizza);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
            var pizzaEntity = pizzaRepository.findById(id);
            if (pizzaEntity.isEmpty()) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            pizzaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
