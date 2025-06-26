package com.TrungTinhBackend.barbershop_backend.Service.Services;

import com.TrungTinhBackend.barbershop_backend.DTO.ServicesDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Services;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.ServicesRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ShopsRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import com.TrungTinhBackend.barbershop_backend.Service.Search.Specification.ServiceSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ServicesServiceImpl implements ServicesService{

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse addServices(ServicesDTO servicesDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Shops shop = shopsRepository.findById(servicesDTO.getShopId()).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        Services service = new Services();

        service.setName(servicesDTO.getName());
        service.setDescription(servicesDTO.getDescription());
        service.setDuration(servicesDTO.getDuration());
        service.setPrice(servicesDTO.getPrice());
        service.setDeleted(false);
        service.getShops().add(shop);

        if(img != null) {
            service.setImg(imgService.uploadImg(img));
        }

        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(null);
        service.setAppointments(new ArrayList<>());

        shop.getServices().add(service);

        servicesRepository.save(service);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add service success");
        apiResponse.setData(service);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getServicesByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Services> services = servicesRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get services by page = "+page+" size = "+size+" success");
        apiResponse.setData(services);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getServicesById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Services service = servicesRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Services not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get service by id = "+id+" success");
        apiResponse.setData(service);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateServices(Long id, ServicesDTO servicesDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Services service = servicesRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Services not found !")
        );

        if(servicesDTO.getName() != null && !servicesDTO.getName().isEmpty()) {
            service.setName(servicesDTO.getName());
        }

        if(servicesDTO.getDescription() != null && !servicesDTO.getDescription().isEmpty()) {
            service.setDescription(servicesDTO.getDescription());
        }

        if(servicesDTO.getDuration() != null) {
            service.setDuration(servicesDTO.getDuration());
        }

        if(servicesDTO.getPrice() != null && !servicesDTO.getPrice().isInfinite()) {
            service.setPrice(servicesDTO.getPrice());
        }

        if(img != null) {
            service.setImg(imgService.updateImg(service.getImg(),img));
        }

        if(servicesDTO.getShopId() != null) {
            Shops shop = shopsRepository.findById(servicesDTO.getShopId()).orElseThrow(
                    () -> new NotFoundException("Shop not found")
            );
            service.getShops().add(shop);
            shop.getServices().add(service);
        }

        service.setUpdatedAt(LocalDateTime.now());

        servicesRepository.save(service);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update service by id = "+id+" success");
        apiResponse.setData(service);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse searchServices(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Services> specification = ServiceSpecification.searchByKeyword(keyword);

        Page<Services> services = servicesRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search services by keyword = "+keyword+" page = "+page+" size = "+size+" success");
        apiResponse.setData(services);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteServices(Long id) {
        APIResponse apiResponse = new APIResponse();

        Services service = servicesRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Services not found !")
        );

        service.setDeleted(true);
        servicesRepository.save(service);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete service by id = "+id+" success");
        apiResponse.setData(service);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreServices(Long id) {
        APIResponse apiResponse = new APIResponse();

        Services service = servicesRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Services not found !")
        );

        service.setDeleted(false);
        servicesRepository.save(service);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore service by id = "+id+" success");
        apiResponse.setData(service);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
