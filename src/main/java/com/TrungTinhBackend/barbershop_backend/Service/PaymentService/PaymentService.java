package com.TrungTinhBackend.barbershop_backend.Service.PaymentService;

import com.TrungTinhBackend.barbershop_backend.DTO.PaymentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;

public interface PaymentService {
    APIResponse addPayment(PaymentDTO paymentDTO);
    APIResponse getPaymentByPage(int page, int size);
    APIResponse getPaymentById(Long id);
}
