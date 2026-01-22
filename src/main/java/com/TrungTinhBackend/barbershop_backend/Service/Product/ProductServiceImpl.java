package com.TrungTinhBackend.barbershop_backend.Service.Product;

import com.TrungTinhBackend.barbershop_backend.DTO.ProductsDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Products;
import com.TrungTinhBackend.barbershop_backend.Entity.Services;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.ProductsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ShopsRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse addProduct(ProductsDTO productsDTO,MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Products product = new Products();

        product.setName(productsDTO.getName());
        product.setDescription(productsDTO.getDescription());

        if(img != null) {
            product.setImg(imgService.uploadImg(img));
        }

        product.setPrice(productsDTO.getPrice());
        product.setStock(productsDTO.getStock());
        product.setCreatedAt(LocalDate.now());
        product.setUpdateAt(LocalDate.now());
        product.setDeleted(false);

        Shops shop = shopsRepository.findById(productsDTO.getShopId()).orElseThrow(
                () -> new NotFoundException("Shop not found")
        );

        product.setShop(shop);

        productsRepository.save(product);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add product success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getProductByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Products> products = productsRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get product by page = "+page+" size = "+size+" success");
        apiResponse.setData(products);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getProductById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Products product = productsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get product by id = "+id+" success");
        apiResponse.setData(product);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateProduct(Long id, ProductsDTO productsDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Products product = productsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        if(productsDTO.getName() != null && !productsDTO.getName().isEmpty()) {
            product.setName(productsDTO.getName());
        }

        if(productsDTO.getDescription() != null && !productsDTO.getDescription().isEmpty()) {
            product.setDescription(productsDTO.getDescription());
        }

        if(img != null) {
            product.setImg(imgService.updateImg(product.getImg(),img));
        }

        product.setPrice(productsDTO.getPrice());
        product.setStock(productsDTO.getStock());
        product.setUpdateAt(LocalDate.now());

        productsRepository.save(product);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update product by id = "+id+" success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteProduct(Long id) {
        APIResponse apiResponse = new APIResponse();

        Products product = productsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        product.setDeleted(true);
        productsRepository.save(product);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete product by id = "+id+" success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse restoreProduct(Long id) {
        APIResponse apiResponse = new APIResponse();

        Products product = productsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        product.setDeleted(false);
        productsRepository.save(product);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Restore product by id = "+id+" success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
