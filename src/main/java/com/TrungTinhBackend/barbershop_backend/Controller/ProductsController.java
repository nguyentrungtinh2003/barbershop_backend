package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.ProductsDTO;
import com.TrungTinhBackend.barbershop_backend.DTO.ShopDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping("/owner/products/add")
    public ResponseEntity<APIResponse> addProduct(@RequestPart(name = "product") ProductsDTO productsDTO,
                                               @RequestPart(name = "img",required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(productService.addProduct(productsDTO,img));
    }

    @GetMapping("/customer/products/page")
    public ResponseEntity<APIResponse> getProductByPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(productService.getProductByPage(page, size));
    }

    @GetMapping("/customer/products/{id}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/owner/products/update/{id}")
    public ResponseEntity<APIResponse> updateProduct(@PathVariable Long id,
                                                  @RequestPart(name = "product") ProductsDTO productsDTO,
                                                  @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(productService.updateProduct(id,productsDTO,img));
    }

    @DeleteMapping("/owner/products/delete/{id}")
    public ResponseEntity<APIResponse> deleteProduct(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PutMapping("/owner/products/restore/{id}")
    public ResponseEntity<APIResponse> restoreProduct(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(productService.restoreProduct(id));
    }
}
