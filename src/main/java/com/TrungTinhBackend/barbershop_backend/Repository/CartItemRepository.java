package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.CartItems;
import com.TrungTinhBackend.barbershop_backend.Entity.Carts;
import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    CartItems findByCartAndProduct(Carts cart, Products product);
    List<CartItems> findByCartId(Long cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItems c WHERE c.cart.id = :cartId")
    void deleteByCartId(Long cartId);
}
