package com.TrungTinhBackend.barbershop_backend.Service.VNPay;

import com.TrungTinhBackend.barbershop_backend.DTO.PaymentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface VNPayService {
    APIResponse createPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws NoSuchAlgorithmException, InvalidKeyException;
    APIResponse executePayment(HttpServletRequest request);
}
