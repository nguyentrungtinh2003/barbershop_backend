package com.TrungTinhBackend.barbershop_backend.Service.Cart;

import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.security.core.Authentication;

public interface CartService {
    APIResponse addCart(Long userId);
    APIResponse addItem(Authentication authentication,Long productId, Integer quantity);
    APIResponse getCartItemByCart(Long cartId);
    APIResponse updateItem(Long id,Integer quantity);
    APIResponse deleteItem(Long id);
}
