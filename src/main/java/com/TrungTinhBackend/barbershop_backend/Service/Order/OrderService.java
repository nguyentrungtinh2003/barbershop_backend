package com.TrungTinhBackend.barbershop_backend.Service.Order;

import com.TrungTinhBackend.barbershop_backend.DTO.OrderDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;

public interface OrderService {
    APIResponse addOrder(OrderDTO orderDTO);
    APIResponse getOrderByPage(int page, int size);
    APIResponse getOrderByCustomerId(Long customerId);
}
