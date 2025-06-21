package com.TrungTinhBackend.barbershop_backend.Service.Shop;

import com.TrungTinhBackend.barbershop_backend.DTO.ShopDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.ShopsRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ShopServiceImpl implements ShopService{

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse addShop(ShopDTO shopDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Shops shop = new Shops();

        shop.setName(shopDTO.getName());
        shop.setEmail(shopDTO.getEmail());
        shop.setDescription(shopDTO.getDescription());
        shop.setPhoneNumber(shopDTO.getPhoneNumber());
        shop.setCreateAt(LocalDate.now());
        if(img != null) {
            shop.setImg(imgService.uploadImg(img));
        }
        shop.setSlogan(shopDTO.getSlogan());
        shop.setAddress(shopDTO.getAddress());
        shop.setDeleted(false);

        shopsRepository.save(shop);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add shop success");
        apiResponse.setData(shop);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getShopByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Shops> shops = shopsRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get shop by page = "+page+" size = "+size+" success");
        apiResponse.setData(shops);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getShopById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Shops shop = shopsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get shop by id = "+id+" success");
        apiResponse.setData(shop);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateShop(Long id, ShopDTO shopDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Shops shop = shopsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        if (shopDTO.getName() != null) {
            shop.setName(shopDTO.getName());
        }
        if (shopDTO.getEmail() != null) {
            shop.setEmail(shopDTO.getEmail());
        }
        if (shopDTO.getDescription() != null) {
            shop.setDescription(shopDTO.getDescription());
        }
        if (shopDTO.getPhoneNumber() != null) {
            shop.setPhoneNumber(shopDTO.getPhoneNumber());
        }
        if (shopDTO.getSlogan() != null) {
            shop.setSlogan(shopDTO.getSlogan());
        }
        if (shopDTO.getAddress() != null) {
            shop.setAddress(shopDTO.getAddress());
        }

        if (img != null) {
            shop.setImg(imgService.updateImg(shop.getImg(), img));
        }

        shop.setUpdateAt(LocalDate.now());
        shop.setDeleted(false);

        shopsRepository.save(shop);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update shop success");
        apiResponse.setData(shop);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;

    }

    @Override
    public APIResponse deleteShop(Long id) {
        APIResponse apiResponse = new APIResponse();

        Shops shop = shopsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        shop.setDeleted(true);
        shopsRepository.save(shop);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete shop success");
        apiResponse.setData(shop);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreShop(Long id) {
        APIResponse apiResponse = new APIResponse();

        Shops shop = shopsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        shop.setDeleted(false);
        shopsRepository.save(shop);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore shop success");
        apiResponse.setData(shop);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
