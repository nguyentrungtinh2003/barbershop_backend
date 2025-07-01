package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbacksRepository extends JpaRepository<Feedbacks,Long>, JpaSpecificationExecutor<Feedbacks> {
    List<Feedbacks> findByShopId(Long shopId);
    List<Feedbacks> findByCustomerId(Long customerId);
    List<Feedbacks> findByBarberId(Long barberId);
}
