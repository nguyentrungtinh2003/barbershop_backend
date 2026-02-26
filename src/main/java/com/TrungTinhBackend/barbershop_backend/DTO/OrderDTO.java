package com.TrungTinhBackend.barbershop_backend.DTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private Long customerId;
    private Long shopId;
    private Long cartId;
    private List<Long> cartItemId;
    private String paymentMethod;
}
