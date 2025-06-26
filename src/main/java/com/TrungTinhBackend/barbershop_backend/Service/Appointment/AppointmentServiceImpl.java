package com.TrungTinhBackend.barbershop_backend.Service.Appointment;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import com.TrungTinhBackend.barbershop_backend.Entity.Services;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.AppointmentsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ServicesRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ShopsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Search.Specification.AppointmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ShopsRepository shopsRepository;

    @Override
    public APIResponse addAppointment(AppointmentDTO appointmentDTO) {
        APIResponse apiResponse = new APIResponse();

        Users customer = usersRepository.findById(appointmentDTO.getCustomer().getId())
                .orElseThrow(() -> new NotFoundException("Customer not found !"));

        Users barber = usersRepository.findById(appointmentDTO.getBarber().getId())
                .orElseThrow(() -> new NotFoundException("Barber not found !"));

        Shops shop = shopsRepository.findById(appointmentDTO.getShop().getId())
                .orElseThrow(() -> new NotFoundException("Shop not found !"));

        List<Long> serviceIds = appointmentDTO.getServices().stream()
                .map(Services::getId)
                .collect(Collectors.toList());

        List<Services> services = servicesRepository.findAllById(serviceIds);

        if (services.isEmpty()) {
            throw new NotFoundException("Không tìm thấy dịch vụ nào!");
        }

        // Tính thời gian bắt đầu và kết thúc
        LocalDate date = appointmentDTO.getDate();// giả sử đã sửa thành LocalDate
        LocalTime time = LocalTime.parse(appointmentDTO.getTimeSlot()); // ví dụ "10:00"
        LocalDateTime startTime = LocalDateTime.of(date, time);
        long totalDuration = services.stream().mapToLong(Services::getDuration).sum();
        LocalDateTime endTime = startTime.plusMinutes(totalDuration);

        // Tạo appointment
        Appointments appointment = new Appointments();
        appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        appointment.setCustomer(customer);
        appointment.setBarber(barber);
        appointment.setServices(services);
        appointment.setShop(shop);
        appointment.setPayments(null);
        appointment.setPrice(appointmentDTO.getPrice());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(null);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setDate(date);

        appointmentsRepository.save(appointment);

        // Trả về
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add appointment success");
        apiResponse.setData(appointment);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getAvailableTimeSlots(Long shopId, Long barberId,LocalDate date) {
        APIResponse apiResponse = new APIResponse();

        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59);

        List<Appointments> appointments = appointmentsRepository.findByShopIdAndBarberIdAndStartTimeBetween(shopId,barberId,startTime, endTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        List<String> availableTimeSlot = appointments.stream().map(appoint -> appoint.getStartTime().format(formatter)).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get appointment available timeslot success");
        apiResponse.setData(availableTimeSlot);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getAppointmentByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Appointments> appointments = appointmentsRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get appointment by page = "+page+" size = "+size+" success");
        apiResponse.setData(appointments);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getAppointmentById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Appointments appointment = appointmentsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Appointment not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get appointment by id = "+id+" success");
        apiResponse.setData(appointment);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getAppointmentByCustomerId(Long customerId) {
        APIResponse apiResponse = new APIResponse();

        List<Appointments> appointments = appointmentsRepository.findByCustomerId(customerId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get appointment by customerId = "+customerId+" success");
        apiResponse.setData(appointments);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse searchAppointment(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Appointments> specification = AppointmentSpecification.searchByKeyword(keyword);

        Page<Appointments> appointments = appointmentsRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search appointment by keyword = "+keyword+" success");
        apiResponse.setData(appointments);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        APIResponse apiResponse = new APIResponse();

        Appointments appointment = appointmentsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Appointment not found !")
        );

        Users customer = usersRepository.findById(appointmentDTO.getCustomer().getId()).orElseThrow(
                () -> new NotFoundException("Customer not found !")
        );

        Users barber = usersRepository.findById(appointmentDTO.getBarber().getId()).orElseThrow(
                () -> new NotFoundException("Barber not found !")
        );

        if(appointmentDTO.getAppointmentStatus() != null) {
            appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        }

        if(appointmentDTO.getCustomer() != null) {
            appointment.setCustomer(customer);
        }

        if(appointmentDTO.getBarber() != null) {
            appointment.setBarber(barber);
        }

        if(appointmentDTO.getServices() != null) {
            appointment.setServices(null);
        }

        if(appointmentDTO.getPayments() != null) {
            appointment.setPayments(null);
        }

        if(appointmentDTO.getPrice() != null) {
            appointment.setPrice(appointmentDTO.getPrice());
        }

        appointment.setUpdatedAt(LocalDateTime.now());

        appointmentsRepository.save(appointment);


        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update appointment by id = "+id+" success");
        apiResponse.setData(appointment);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteAppointment(Long id) {
        APIResponse apiResponse = new APIResponse();

        Appointments appointment = appointmentsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Appointment not found !")
        );

        appointmentsRepository.delete(appointment);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete appointment by id = "+id+" success");
        apiResponse.setData(appointment);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
