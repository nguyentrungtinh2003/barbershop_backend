package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.OrderDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/customer/orders/add")
    public ResponseEntity<APIResponse> addOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @GetMapping("/customer/orders")
    public ResponseEntity<APIResponse> getOrderByPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(orderService.getOrderByPage(page,size));
    }

    @GetMapping("/customer/orders/customerId/{customerId}")
    public ResponseEntity<APIResponse> getOrderByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrderByCustomerId(customerId));
    }
}
