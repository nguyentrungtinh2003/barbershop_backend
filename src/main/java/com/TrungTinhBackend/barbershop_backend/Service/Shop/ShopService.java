package com.TrungTinhBackend.barbershop_backend.Service.Shop;

import com.TrungTinhBackend.barbershop_backend.DTO.ShopDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ShopService {
    APIResponse addShop(ShopDTO shopDTO, MultipartFile img) throws IOException;
    APIResponse getShopByPage(int page, int size);
    APIResponse getAllShop();
    APIResponse getShopById(Long id);
    APIResponse getShopByOwnerId(Long ownerId);
    APIResponse updateShop(Long id, ShopDTO shopDTO, MultipartFile img) throws IOException;
    APIResponse deleteShop(Long id);
    APIResponse restoreShop(Long id);
}
