package com.TrungTinhBackend.barbershop_backend.Service.PaymentService;

import com.TrungTinhBackend.barbershop_backend.DTO.PaymentDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Payments;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentMethod;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentStatus;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.PaymentsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public APIResponse addPayment(PaymentDTO paymentDTO) {
        APIResponse apiResponse = new APIResponse();

        Users customer = usersRepository.findById(paymentDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("Customer not found")
        );

        Payments payment = new Payments();

        payment.setCustomer(customer);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getMethod());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);

//        if(paymentDTO.get)
//        payment.set

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add payment success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getPaymentByPage(int page, int size) {
        return null;
    }

    @Override
    public APIResponse getPaymentById(Long id) {
        return null;
    }
}
