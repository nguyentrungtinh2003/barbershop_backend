package com.TrungTinhBackend.barbershop_backend.Service.VNPay;

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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VNPayServiceImpl implements VNPayService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Value("${vnp_Url}")
    private String vnpUrl;

    @Value("${vnp_Return_Url}")
    private String vnpReturnUrl;

    @Value("${vnp_TmnCode}")
    private String vnpTmnCode;

    @Value("${vnp_HashSecret}")
    private String vnpHashSecret;

    private static final double COIN_RATE = 1000.0;
    private static final String ORDER_INFO = "Payment";

    public VNPayServiceImpl(UsersRepository usersRepository, PaymentsRepository paymentsRepository, @Value("${vnp_Url}") String vnpUrl,
                            @Value("${VNP_RETURN_URL}") String vnpReturnUrl,
                            @Value("${vnp_TmnCode}") String vnpTmnCode,
                            @Value("${vnp_HashSecret}") String vnpHashSecret) {
        this.usersRepository = usersRepository;
        this.paymentsRepository = paymentsRepository;
        this.vnpUrl = vnpUrl;
        this.vnpReturnUrl = vnpReturnUrl;
        this.vnpTmnCode = vnpTmnCode;
        this.vnpHashSecret = vnpHashSecret;
    }

    public String hmacSHA512(String key,String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes());
        return bytesToHex(bytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for(byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }


    @Override
    public APIResponse createPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws NoSuchAlgorithmException, InvalidKeyException {
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

        transaction.setPaymentStatus(PaymentStatus.PENDING);
        transaction.setPaymentMethod(paymentDTO.getMethod());
        if(orders != null) {
            transaction.setOrders(orders);
            transaction.setPaymentType(PaymentType.PRODUCT);
        }else if (appointments != null) {
            transaction.setAppointments(appointments);
            transaction.setPaymentType(PaymentType.BOOKING);
        }

        Payments payments = paymentsRepository.save(transaction);

        String vnp_TxnRef = String.valueOf(payments.getId());
        String vnp_IpAddr = request.getRemoteAddr();

        String orderType = "other";
        long amount = (long) (paymentDTO.getAmount() * 100);

        Map<String,String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnp_TxnRef);
        vnpParams.put("vnp_OrderInfo", ORDER_INFO);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl + "?userId=" + paymentDTO.getUserId() + "&orderId=" + paymentDTO.getOrderId());
        vnpParams.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cal = Calendar.getInstance();
        Date createdDate = cal.getTime();
        String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(createdDate);
        vnpParams.put("vnp_CreateDate",vnp_CreateDate);

        List<String> filedNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(filedNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for(String fieldName : filedNames) {
            String value = vnpParams.get(fieldName);
            if(!hashData.isEmpty()) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        }

        String vnp_SecureHash = hmacSHA512(vnpHashSecret,hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Redirect to VNPay");
        apiResponse.setData(vnpUrl + "?" + query);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse executePayment(HttpServletRequest request) {
        APIResponse response = new APIResponse();

        String responseCode = request.getParameter("vnp_ResponseCode");
        String amountStr = request.getParameter("vnp_Amount");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");

        // Kiểm tra nếu có tham số thiếu
        if (responseCode == null || amountStr == null) {
            response.setStatusCode(400L);
            response.setMessage("Thiếu thông tin thanh toán!");
            response.setData(null);
            response.setTimestamp(LocalDateTime.now());
            return response;
        }

        // Kiểm tra mã phản hồi VNPay
        if ("00".equals(responseCode)) {
            try {
                long amountVND = Long.parseLong(amountStr) / 100;

                Payments payments = paymentsRepository.findById(Long.valueOf(vnp_TxnRef)).orElseThrow(
                        () -> new NotFoundException("Payment not found")
                );

                if(amountVND != payments.getAmount()) {
                    throw new RuntimeException("Amount mismatch");
                }
                payments.setPaymentStatus(PaymentStatus.COMPLETED);
                payments.setCreatedAt(LocalDateTime.now());
                payments.setUpdatedAt(LocalDateTime.now());
                if(payments.getOrders() != null) {
                    payments.getOrders().setStatus(OrderStatus.PAID);
                }
                if(payments.getAppointments() != null) {
                    payments.getAppointments().setPaid(true);
                }

                paymentsRepository.save(payments);

                response.setStatusCode(200L);
                response.setMessage("Thanh toán thành công!");
                response.setData(null);
                response.setTimestamp(LocalDateTime.now());
                return response;
            } catch (NumberFormatException e) {
                response.setStatusCode(400L);
                response.setMessage("Invalid amount format!");
                response.setData(null);
                response.setTimestamp(LocalDateTime.now());
                return response;
            }
        }

        response.setStatusCode(500L);
        response.setMessage("Thanh toán thất bại!");
        response.setData(null);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
