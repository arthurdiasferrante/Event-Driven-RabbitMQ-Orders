    package com.pizzaria.controller;


    import com.pizzaria.model.Order;
    import com.pizzaria.repository.OrderRepository;
    import com.pizzaria.service.OrderService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RequestMapping("/orders")
    @RestController
    public class OrderController {

        private final OrderService orderService;

        public OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        @PostMapping
        public ResponseEntity<Order> createOrder(@RequestBody Order order) {
            Order newOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        }

        @GetMapping
        public ResponseEntity<List<Order>> getOrders() {
            return ResponseEntity.ok(orderService.getAllOrders());
        }

        @GetMapping("/{id}")
        public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Order> updateOrder(@RequestBody Order newOrder, @PathVariable Long id) {
            Order updatedOrder = orderService.updateOrder(newOrder, id);
            return ResponseEntity.ok(updatedOrder);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
            orderService.deleteOrder(id);

            return ResponseEntity.noContent().build();
        }

    }
