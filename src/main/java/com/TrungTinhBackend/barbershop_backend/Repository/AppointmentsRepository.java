package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointments,Long>, JpaSpecificationExecutor<Appointments> {
    List<Appointments> findByShopIdAndBarberIdAndStartTimeBetween(Long shopId, Long barberId, LocalDateTime startTime, LocalDateTime endTime);
    List<Appointments> findByCustomerId(Long customerId);
    List<Appointments> findByShopId(Long shopId);
}
