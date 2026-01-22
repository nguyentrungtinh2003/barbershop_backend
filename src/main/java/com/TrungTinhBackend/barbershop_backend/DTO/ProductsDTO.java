package com.TrungTinhBackend.barbershop_backend.DTO;
import lombok.Data;

@Data
public class ProductsDTO {

    private Long id;
    private String name;
    private String description;
    private String img;
    private int price;
    private int stock;

    private Long shopId;
    private Long cartItemsId;
}
