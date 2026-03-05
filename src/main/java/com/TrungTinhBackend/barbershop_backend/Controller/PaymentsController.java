package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.PaymentDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.PaymentService.PaymentService;
import com.TrungTinhBackend.barbershop_backend.Service.VNPay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController()
@RequestMapping("/api/customer/payments")
public class PaymentsController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPayment(@Valid @RequestBody PaymentDTO paymentDTO) throws Exception {
        return ResponseEntity.ok(paymentService.addPayment(paymentDTO));
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse> createPayment(HttpServletRequest request, @Valid @RequestBody PaymentDTO paymentDTO) throws Exception {
        return ResponseEntity.ok(vnPayService.createPayment( request,paymentDTO));
    }

    @GetMapping("/execute/vnpay")
    public ResponseEntity<APIResponse> executePaymentVNPay(
            HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayService.executePayment(request));
    }
}
