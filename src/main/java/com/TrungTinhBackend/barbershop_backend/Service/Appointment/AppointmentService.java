package com.TrungTinhBackend.barbershop_backend.Service.Appointment;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;

public interface AppointmentService {
    APIResponse addAppointment(AppointmentDTO appointmentDTO);
    APIResponse getAppointmentByPage(int page, int size);
    APIResponse getAppointmentById(Long id);
    APIResponse searchAppointment(String keyword, int page, int size);
    APIResponse updateAppointment(Long id, AppointmentDTO appointmentDTO);
    APIResponse deleteAppointment(Long id);
}
