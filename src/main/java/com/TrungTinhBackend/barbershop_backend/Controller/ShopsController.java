package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.ShopDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Shop.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ShopsController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/admin/shops/add")
    public ResponseEntity<APIResponse> addShop(@RequestPart(name = "shop") ShopDTO shopDTO,
                                               @RequestPart(name = "img",required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(shopService.addShop(shopDTO,img));
    }

    @GetMapping("/admin/shops/page")
    public ResponseEntity<APIResponse> getShopByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(shopService.getShopByPage(page, size));
    }

    @GetMapping("/owner/shops/{id}")
    public ResponseEntity<APIResponse> getShopById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    @PutMapping("/owner/shops/update/{id}")
    public ResponseEntity<APIResponse> updateShop(@PathVariable Long id,
                                                  @RequestPart(name = "shop") ShopDTO shopDTO,
                                                  @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(shopService.updateShop(id,shopDTO,img));
    }

    @DeleteMapping("/admin/shops/delete/{id}")
    public ResponseEntity<APIResponse> deleteShop(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(shopService.deleteShop(id));
    }

    @PutMapping("/admin/shops/restore/{id}")
    public ResponseEntity<APIResponse> restoreShop(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(shopService.restoreShop(id));
    }
}
