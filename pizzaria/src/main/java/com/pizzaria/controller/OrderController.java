    package com.pizzaria.controller;


    import com.pizzaria.model.Order;
    import com.pizzaria.repository.OrderRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RequestMapping("/orders")
    @RestController
    public class OrderController {

        private final OrderRepository orderRepository;

        public OrderController(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        @PostMapping
        public ResponseEntity<Order> createOrder(@RequestBody Order order) {
            Order newOrder = orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        }


        @GetMapping
        public ResponseEntity<List<Order>> getOrders() {
            return ResponseEntity.ok(orderRepository.findAll());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
            var orderEntity = orderRepository.findById(id);
            if (orderEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Order order = orderEntity.get();
            return ResponseEntity.ok(order);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Order> updateOrder(@RequestBody Order newOrder, @PathVariable Long id) {
            var orderEntity = orderRepository.findById(id);
            if (orderEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Order updatedOrder = orderEntity.get();
            updatedOrder.setPizzas(newOrder.getPizzas());
            orderRepository.save(updatedOrder);

            return ResponseEntity.ok(updatedOrder);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
            var orderEntity = orderRepository.findById(id);
            if (orderEntity.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

    }
