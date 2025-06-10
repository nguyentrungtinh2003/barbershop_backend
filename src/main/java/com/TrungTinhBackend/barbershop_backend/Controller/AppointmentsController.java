package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.FeedbackDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteAppointment(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }
}
