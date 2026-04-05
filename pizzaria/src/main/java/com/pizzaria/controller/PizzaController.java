    package com.pizzaria.controller;

    import com.pizzaria.dto.pizza.PizzaRequestDTO;
    import com.pizzaria.dto.pizza.PizzaResponseDTO;
    import com.pizzaria.model.Pizza;
    import com.pizzaria.repository.PizzaRepository;
    import com.pizzaria.service.PizzaService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RequestMapping("/pizzas")
    @RestController
    public class PizzaController {

        private final PizzaService pizzaService;

        public PizzaController(PizzaService pizzaService) {
            this.pizzaService = pizzaService;
        }

        @PostMapping
        public ResponseEntity<PizzaResponseDTO> createPizza(@RequestBody PizzaRequestDTO pizzaRequestDTO) {
            PizzaResponseDTO responseDTO = pizzaService.createPizza(pizzaRequestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        }

        @GetMapping
        public ResponseEntity<List<PizzaResponseDTO>> getPizzas() {
            List<PizzaResponseDTO> responseDTOs = pizzaService.getPizzas();
            return ResponseEntity.ok(responseDTOs);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
            return ResponseEntity.ok(pizzaService.getPizzaById(id));
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
            Pizza pizza = pizzaService.updatePizza(id, newData);
            return ResponseEntity.ok(pizza);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
            pizzaService.deletePizza(id);
            return ResponseEntity.noContent().build();
        }
    }
