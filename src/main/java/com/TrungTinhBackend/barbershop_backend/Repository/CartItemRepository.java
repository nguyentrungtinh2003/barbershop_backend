package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.CartItems;
import com.TrungTinhBackend.barbershop_backend.Entity.Carts;
import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    CartItems findByCartAndProduct(Carts cart, Products product);
    List<CartItems> findByCartId(Long cartId);
}
