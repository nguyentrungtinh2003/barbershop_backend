package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Orders;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> , JpaSpecificationExecutor<Orders> {
    Page<Orders> findByCustomer_Id(Long customerId,Pageable pageable);
    Page<Orders> findByShop_Id(Long shopId, Pageable pageable);
}
