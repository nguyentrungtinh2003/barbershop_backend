package com.TrungTinhBackend.barbershop_backend.DTO;

import lombok.Data;

@Data
public class AddToCartRequestDTO {

    private Long productId;
    private Integer quantity;
}
