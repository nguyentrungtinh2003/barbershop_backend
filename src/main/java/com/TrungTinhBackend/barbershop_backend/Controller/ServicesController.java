package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.ServicesDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @PostMapping("/owner/services/add")
    public ResponseEntity<APIResponse> addService(@RequestPart(name = "service") ServicesDTO servicesDTO,
                                                  @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(servicesService.addServices(servicesDTO, img));
    }

    @GetMapping("/owner/services/page")
    public ResponseEntity<APIResponse> getServiceByPage(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(servicesService.getServicesByPage(page, size));
    }

    @GetMapping("/owner/service/{id}")
    public ResponseEntity<APIResponse> getServiceById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(servicesService.getServicesById(id));
    }

    @GetMapping("/owner/services/search")
    public ResponseEntity<APIResponse> searchService(@RequestParam(name = "keyword") String keyword,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(servicesService.searchServices(keyword, page, size));
    }

    @PutMapping("/owner/services/update/{id}")
    public ResponseEntity<APIResponse> updateService(@PathVariable Long id,
                                                     @RequestPart(name = "service")ServicesDTO servicesDTO,
                                                     @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(servicesService.updateServices(id, servicesDTO, img));
    }

    @DeleteMapping("/owner/services/delete/{id}")
    public ResponseEntity<APIResponse> deleteService(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(servicesService.deleteServices(id));
    }

    @PutMapping("/owner/services/restore/{id}")
    public ResponseEntity<APIResponse> restoreService(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(servicesService.restoreServices(id));
    }
}
