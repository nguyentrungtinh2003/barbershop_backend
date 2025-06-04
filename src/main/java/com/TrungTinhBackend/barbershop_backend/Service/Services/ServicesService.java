package com.TrungTinhBackend.barbershop_backend.Service.Services;

import com.TrungTinhBackend.barbershop_backend.DTO.ServicesDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicesService {
    APIResponse addServices(ServicesDTO servicesDTO, MultipartFile img) throws IOException;
    APIResponse getServicesByPage(int page, int size);
    APIResponse getServicesById(Long id);
    APIResponse updateServices(Long id, ServicesDTO servicesDTO, MultipartFile img) throws IOException;
    APIResponse searchServices(String keyword, int page, int size);
    APIResponse deleteServices(Long id);
    APIResponse restoreServices(Long id);
}
