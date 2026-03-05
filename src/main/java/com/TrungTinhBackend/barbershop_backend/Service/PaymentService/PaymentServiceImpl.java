package com.TrungTinhBackend.barbershop_backend.Service.PaymentService;

import com.TrungTinhBackend.barbershop_backend.DTO.PaymentDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import com.TrungTinhBackend.barbershop_backend.Entity.Orders;
import com.TrungTinhBackend.barbershop_backend.Entity.Payments;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Enum.OrderStatus;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentMethod;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentStatus;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentType;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.AppointmentsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.OrderRepository;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Override
    public APIResponse addPayment(PaymentDTO paymentDTO) {
        APIResponse apiResponse = new APIResponse();

        Users user = usersRepository.findById(paymentDTO.getUserId()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Orders orders = null;
        if(paymentDTO.getOrderId() != null) {
            orders = orderRepository.findById(paymentDTO.getOrderId()).orElseThrow(
                    () -> new NotFoundException("Order not found")
            );
        }
        Appointments appointments = null;
        if(paymentDTO.getAppointmentId() != null) {
            appointments = appointmentsRepository.findById(paymentDTO.getAppointmentId()).orElseThrow(
                    () -> new NotFoundException("Appointment not found")
            );
        }

        Payments transaction = new Payments();
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setCustomer(user);

        transaction.setPaymentMethod(paymentDTO.getMethod());
        if(orders != null) {
            transaction.setOrders(orders);
            transaction.setPaymentType(PaymentType.PRODUCT);
            transaction.getOrders().setStatus(OrderStatus.PAID);
        }else if (appointments != null) {
            transaction.setAppointments(appointments);
            transaction.setPaymentType(PaymentType.BOOKING);
            transaction.getAppointments().setPaid(true);
        }

        transaction.setPaymentStatus(PaymentStatus.COMPLETED);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        paymentsRepository.save(transaction);

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
