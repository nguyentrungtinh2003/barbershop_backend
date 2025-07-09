package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("api/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addAppointment(@RequestBody AppointmentDTO appointmentDTO) throws IOException {
        return ResponseEntity.ok(appointmentService.addAppointment(appointmentDTO));
    }

    @GetMapping("/time-slot")
    public ResponseEntity<APIResponse> getAppointmentTimeSlot(@RequestParam() Long shopId,
                                                              @RequestParam() Long barberId,
                                                              @RequestParam() LocalDate date) throws IOException {
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(shopId,barberId,date));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getAppointmentByPage(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentByPage(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getAppointmentById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<APIResponse> getAppointmentByCustomerId(@PathVariable Long customerId) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentByCustomerId(customerId));
    }

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<APIResponse> getAppointmentByBarberId(@PathVariable Long barberId) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentByBarberId(barberId));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<APIResponse> getAppointmentByShopId(@PathVariable Long shopId) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentByShopId(shopId));
    }

    @GetMapping("/shop/{shopId}/payments")
    public ResponseEntity<APIResponse> getAppointmentByShopIdAndIsPaid(@PathVariable Long shopId) throws IOException {
        return ResponseEntity.ok(appointmentService.getAppointmentByShopId(shopId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchAppointment(@RequestParam(name = "keyword") String keyword,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(appointmentService.searchAppointment(keyword, page, size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateAppointment(@PathVariable Long id,@RequestBody AppointmentDTO appointmentDTO) throws IOException {
        return ResponseEntity.ok(appointmentService.updateAppointment(id,appointmentDTO));
    }

    @PutMapping("/mark-paid/{id}")
    public ResponseEntity<APIResponse> markAsPaidAppointment(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(appointmentService.markAsPaid(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteAppointment(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }
}
