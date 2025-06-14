package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addAppointment(@RequestBody AppointmentDTO appointmentDTO) throws IOException {
        return ResponseEntity.ok(appointmentService.addAppointment(appointmentDTO));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteAppointment(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }
}
