package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Long> {
}
