    package com.pizzaria.controller;


    import com.pizzaria.dto.order.OrderRequestDTO;
    import com.pizzaria.dto.order.OrderResponseDTO;
    import com.pizzaria.model.Order;
    import com.pizzaria.repository.OrderRepository;
    import com.pizzaria.service.ClientService;
    import com.pizzaria.service.OrderService;
    import com.pizzaria.service.PizzaService;
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
        public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
            OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);


            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
        }

        @GetMapping
        public ResponseEntity<List<OrderResponseDTO>> getOrders() {
            return ResponseEntity.ok(orderService.getAllOrders());
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
            OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
            return ResponseEntity.ok(orderResponseDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<OrderResponseDTO> updateOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable Long id) {
            OrderResponseDTO orderResponseDTO = orderService.updateOrder(orderRequestDTO, id);
            return ResponseEntity.ok(orderResponseDTO);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        }

    }
