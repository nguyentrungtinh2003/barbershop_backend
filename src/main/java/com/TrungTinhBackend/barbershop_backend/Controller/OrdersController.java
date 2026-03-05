package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.OrderDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Order.OrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/customer/orders/add")
    public ResponseEntity<APIResponse> addOrder(@RequestBody OrderDTO orderDTO) throws BadRequestException {
        return ResponseEntity.ok(orderService.addOrder(orderDTO));
    }

    @GetMapping("/customer/orders")
    public ResponseEntity<APIResponse> getOrderByPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(orderService.getOrderByPage(page,size));
    }

    @GetMapping("/customer/orders/customerId/{customerId}")
    public ResponseEntity<APIResponse> getOrderByCustomerId(@PathVariable Long customerId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "4") int size) {
        return ResponseEntity.ok(orderService.getOrderByCustomerId(customerId,page,size));
    }

    @GetMapping("/owner/orders/shopId/{shopId}")
    public ResponseEntity<APIResponse> getOrderByShopId(@PathVariable Long shopId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "4") int size) {
        return ResponseEntity.ok(orderService.getOrderByShopId(shopId,page,size));
    }

    @GetMapping("/customer/orders/search")
    public ResponseEntity<APIResponse> searchUser(@RequestParam(name = "keyword") String keyword,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "4") int size) throws IOException {
        return ResponseEntity.ok(orderService.searchOrder(keyword, page, size));
    }
}
