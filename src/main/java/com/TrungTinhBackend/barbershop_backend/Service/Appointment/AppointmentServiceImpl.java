package com.TrungTinhBackend.barbershop_backend.Service.Appointment;

import com.TrungTinhBackend.barbershop_backend.DTO.AppointmentDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.AppointmentsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Search.Specification.AppointmentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public APIResponse addAppointment(AppointmentDTO appointmentDTO) {
        APIResponse apiResponse = new APIResponse();

        Users customer = usersRepository.findById(appointmentDTO.getCustomer().getId()).orElseThrow(
                () -> new NotFoundException("Customer not found !")
        );

        Users barber = usersRepository.findById(appointmentDTO.getBarber().getId()).orElseThrow(
                () -> new NotFoundException("Barber not found !")
        );


        Appointments appointment = new Appointments();
        appointment.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        appointment.setCustomer(customer);
        appointment.setBarber(barber);
        appointment.setServices(null);
        appointment.setPayments(null);
        appointment.setPrice(appointmentDTO.getPrice());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(null);

        appointmentsRepository.save(appointment);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add appointment success");
        apiResponse.setData(appointment);
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
