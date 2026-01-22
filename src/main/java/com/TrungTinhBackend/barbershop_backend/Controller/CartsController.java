package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.AddToCartRequestDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.ProductsDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class CartsController {

    @Autowired
    private CartService cartService;

    @PostMapping("/customer/cart/item")
    public ResponseEntity<APIResponse> addItem(@RequestBody AddToCartRequestDTO addToCartRequestDTO, Authentication authentication) throws IOException {
        return ResponseEntity.ok(cartService.addItem(authentication, addToCartRequestDTO.getProductId(),addToCartRequestDTO.getQuantity()));
    }

    @GetMapping("/customer/cart/{cartId}")
    public ResponseEntity<APIResponse> getCartItemByCart(@PathVariable Long cartId) throws IOException {
        return ResponseEntity.ok(cartService.getCartItemByCart(cartId));
    }

    @PutMapping("/customer/update/cart-item/{id}")
    public ResponseEntity<APIResponse> updateCartItem(@PathVariable Long id,@RequestBody Map<String,Integer> body) throws IOException {
        Integer quantity = body.get("quantity");
        return ResponseEntity.ok(cartService.updateItem(id,quantity));
    }

    @DeleteMapping("/customer/delete/cart-item/{id}")
    public ResponseEntity<APIResponse> deleteCartItem(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(cartService.deleteItem(id));
    }
}
