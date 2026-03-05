package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Feedbacks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbacksRepository extends JpaRepository<Feedbacks,Long>, JpaSpecificationExecutor<Feedbacks> {
    Page<Feedbacks> findByShopId(Long shopId, Pageable pageable);
    Page<Feedbacks> findByCustomerId(Long customerId, Pageable pageable);
    Page<Feedbacks> findByBarberId(Long barberId, Pageable pageable);
}
