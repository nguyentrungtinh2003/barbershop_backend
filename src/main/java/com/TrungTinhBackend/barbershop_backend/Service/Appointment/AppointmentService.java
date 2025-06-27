package com.TrungTinhBackend.barbershop_backend.Service.Appointment;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;

import java.time.LocalDate;

public interface AppointmentService {
    APIResponse addAppointment(AppointmentDTO appointmentDTO);
    APIResponse getAvailableTimeSlots(Long shopId, Long barberId,LocalDate date);
    APIResponse getAppointmentByPage(int page, int size);
    APIResponse getAppointmentById(Long id);
    APIResponse getAppointmentByCustomerId(Long customerId);
    APIResponse getAppointmentByShopId(Long shopId);
    APIResponse searchAppointment(String keyword, int page, int size);
    APIResponse updateAppointment(Long id, AppointmentDTO appointmentDTO);
    APIResponse deleteAppointment(Long id);
}
