package com.TrungTinhBackend.barbershop_backend.Service.Feedback;

import com.TrungTinhBackend.barbershop_backend.DTO.FeedbackDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import com.TrungTinhBackend.barbershop_backend.Entity.Feedbacks;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.AppointmentsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.FeedbacksRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.ShopsRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import com.TrungTinhBackend.barbershop_backend.Service.Search.Specification.FeedbackSpecification;
import com.TrungTinhBackend.barbershop_backend.Service.Websocket.WebsocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    private FeedbacksRepository feedbacksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ShopsRepository shopsRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private WebsocketSender websocketSender;

    @Override
    public APIResponse addFeedback(FeedbackDTO feedbackDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = new Feedbacks();

        Users customer = usersRepository.findById(feedbackDTO.getCustomerId()).orElseThrow(
                () -> new NotFoundException("Customer not found !")
        );

        Users barber = usersRepository.findById(feedbackDTO.getBarberId()).orElseThrow(
                () -> new NotFoundException("Barber not found !")
        );

        Shops shop = shopsRepository.findById(feedbackDTO.getShopId()).orElseThrow(
                () -> new NotFoundException("Shop not found !")
        );

        Appointments appointment = appointmentsRepository.findById(feedbackDTO.getAppointmentId()).orElseThrow(
                () -> new NotFoundException("Appointment not found !")
        );

        feedback.setCustomer(customer);
        feedback.setBarber(barber);
        feedback.setShop(shop);
        feedback.setAppointment(appointment);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(null);

        if(img != null) {
            feedback.setImg(imgService.uploadImg(img));
        }

        appointment.setFeedback(feedback);
        feedbacksRepository.save(feedback);

        FeedbackDTO feedbackDTO1 = new FeedbackDTO();

        feedbackDTO1.setId(feedback.getId());
        feedbackDTO1.setRating(feedback.getRating());
        feedbackDTO1.setComment(feedback.getComment());
        feedbackDTO1.setCreatedAt(feedback.getCreatedAt());
        feedbackDTO1.setUpdatedAt(feedback.getUpdatedAt());
        feedbackDTO1.setCustomerId(feedback.getCustomer().getId());
        feedbackDTO1.setCustomerName(feedback.getCustomer().getUsername());
        feedbackDTO1.setCustomerImg(feedback.getCustomer().getImg());
        feedbackDTO1.setBarberId(feedback.getBarber().getId());
        feedbackDTO1.setBarberName(feedback.getBarber().getUsername());
        feedbackDTO1.setBarberImg(feedback.getBarber().getImg());
        feedbackDTO1.setShopId(feedback.getShop().getId());
        feedbackDTO1.setShopName(feedback.getShop().getName());
        feedbackDTO1.setShopImg(feedback.getShop().getImg());
        feedbackDTO1.setImg(feedback.getImg());
        websocketSender.sendFeedback(feedbackDTO1);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add feedback success");
        apiResponse.setData(feedback);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackByPage(int page, int size) {

        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Feedbacks> feedbacks = feedbacksRepository.findAll(pageable);

        Page<FeedbackDTO> feedbackDTOList = feedbacks.map(feedback -> {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setId(feedback.getId());
            feedbackDTO.setRating(feedback.getRating());
            feedbackDTO.setComment(feedback.getComment());
            feedbackDTO.setCreatedAt(feedback.getCreatedAt());
            feedbackDTO.setUpdatedAt(feedback.getUpdatedAt());
            feedbackDTO.setCustomerId(feedback.getCustomer().getId());
            feedbackDTO.setCustomerName(feedback.getCustomer().getUsername());
            feedbackDTO.setCustomerImg(feedback.getCustomer().getImg());
            feedbackDTO.setBarberId(feedback.getBarber().getId());
            feedbackDTO.setBarberName(feedback.getBarber().getUsername());
            feedbackDTO.setBarberImg(feedback.getBarber().getImg());
            feedbackDTO.setShopId(feedback.getShop().getId());
            feedbackDTO.setShopName(feedback.getShop().getName());
            feedbackDTO.setShopImg(feedback.getShop().getImg());
            feedbackDTO.setImg(feedback.getImg());

            return feedbackDTO;
        });

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by page = "+page+" size = "+size+" success");
        apiResponse.setData(feedbackDTOList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = feedbacksRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Feedback not found !")
        );

        FeedbackDTO feedbackDTO = new FeedbackDTO();

        feedbackDTO.setId(feedback.getId());
        feedbackDTO.setRating(feedback.getRating());
        feedbackDTO.setComment(feedback.getComment());
        feedbackDTO.setCreatedAt(feedback.getCreatedAt());
        feedbackDTO.setUpdatedAt(feedback.getUpdatedAt());
        feedbackDTO.setCustomerId(feedback.getCustomer().getId());
        feedbackDTO.setCustomerName(feedback.getCustomer().getUsername());
        feedbackDTO.setCustomerImg(feedback.getCustomer().getImg());
        feedbackDTO.setBarberId(feedback.getBarber().getId());
        feedbackDTO.setBarberName(feedback.getBarber().getUsername());
        feedbackDTO.setBarberImg(feedback.getBarber().getImg());
        feedbackDTO.setShopId(feedback.getShop().getId());
        feedbackDTO.setShopName(feedback.getShop().getName());
        feedbackDTO.setShopImg(feedback.getShop().getImg());
        feedbackDTO.setImg(feedback.getImg());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by id = "+id+" success");
        apiResponse.setData(feedbackDTO);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackByShopId(Long shopId) {
        APIResponse apiResponse = new APIResponse();

        List<Feedbacks> feedbacks = feedbacksRepository.findByShopId(shopId);
        feedbacks.sort(Comparator.comparing(Feedbacks::getCreatedAt));

        List<FeedbackDTO> feedbackDTOList = feedbacks.stream().map(feedback -> {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setId(feedback.getId());
            feedbackDTO.setRating(feedback.getRating());
            feedbackDTO.setComment(feedback.getComment());
            feedbackDTO.setCreatedAt(feedback.getCreatedAt());
            feedbackDTO.setUpdatedAt(feedback.getUpdatedAt());
            feedbackDTO.setCustomerId(feedback.getCustomer().getId());
            feedbackDTO.setCustomerName(feedback.getCustomer().getUsername());
            feedbackDTO.setCustomerImg(feedback.getCustomer().getImg());
            feedbackDTO.setBarberId(feedback.getBarber().getId());
            feedbackDTO.setBarberName(feedback.getBarber().getUsername());
            feedbackDTO.setBarberImg(feedback.getBarber().getImg());
            feedbackDTO.setShopId(feedback.getShop().getId());
            feedbackDTO.setShopName(feedback.getShop().getName());
            feedbackDTO.setShopImg(feedback.getShop().getImg());
            feedbackDTO.setImg(feedback.getImg());

            return feedbackDTO;
        }).toList();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by ShopId = "+shopId+" success");
        apiResponse.setData(feedbackDTOList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackByCustomerId(Long customerId) {
        APIResponse apiResponse = new APIResponse();

        List<Feedbacks> feedbacks = feedbacksRepository.findByCustomerId(customerId);
        feedbacks.sort(Comparator.comparing(Feedbacks::getCreatedAt));

        List<FeedbackDTO> feedbackDTOList = feedbacks.stream().map(feedback -> {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setId(feedback.getId());
            feedbackDTO.setRating(feedback.getRating());
            feedbackDTO.setComment(feedback.getComment());
            feedbackDTO.setCreatedAt(feedback.getCreatedAt());
            feedbackDTO.setUpdatedAt(feedback.getUpdatedAt());
            feedbackDTO.setCustomerId(feedback.getCustomer().getId());
            feedbackDTO.setCustomerName(feedback.getCustomer().getUsername());
            feedbackDTO.setCustomerImg(feedback.getCustomer().getImg());
            feedbackDTO.setBarberId(feedback.getBarber().getId());
            feedbackDTO.setBarberName(feedback.getBarber().getUsername());
            feedbackDTO.setBarberImg(feedback.getBarber().getImg());
            feedbackDTO.setShopId(feedback.getShop().getId());
            feedbackDTO.setShopName(feedback.getShop().getName());
            feedbackDTO.setShopImg(feedback.getShop().getImg());
            feedbackDTO.setImg(feedback.getImg());

            return feedbackDTO;
        }).toList();


        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by customerId = "+customerId+" success");
        apiResponse.setData(feedbackDTOList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackByBarberId(Long barberId) {
        APIResponse apiResponse = new APIResponse();

        List<Feedbacks> feedbacks = feedbacksRepository.findByBarberId(barberId);
        feedbacks.sort(Comparator.comparing(Feedbacks::getCreatedAt));

        List<FeedbackDTO> feedbackDTOList = feedbacks.stream().map(feedback -> {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setId(feedback.getId());
            feedbackDTO.setRating(feedback.getRating());
            feedbackDTO.setComment(feedback.getComment());
            feedbackDTO.setCreatedAt(feedback.getCreatedAt());
            feedbackDTO.setUpdatedAt(feedback.getUpdatedAt());
            feedbackDTO.setCustomerId(feedback.getCustomer().getId());
            feedbackDTO.setCustomerName(feedback.getCustomer().getUsername());
            feedbackDTO.setCustomerImg(feedback.getCustomer().getImg());
            feedbackDTO.setBarberId(feedback.getBarber().getId());
            feedbackDTO.setBarberName(feedback.getBarber().getUsername());
            feedbackDTO.setBarberImg(feedback.getBarber().getImg());
            feedbackDTO.setShopId(feedback.getShop().getId());
            feedbackDTO.setShopName(feedback.getShop().getName());
            feedbackDTO.setShopImg(feedback.getShop().getImg());
            feedbackDTO.setImg(feedback.getImg());

            return feedbackDTO;
        }).toList();


        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by barberId = "+barberId+" success");
        apiResponse.setData(feedbackDTOList);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse searchFeedback(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
        Specification<Feedbacks> specification = FeedbackSpecification.searchByKeyword(keyword);

        Page<Feedbacks> feedbacks = feedbacksRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search feedback by keyword = "+keyword+" success");
        apiResponse.setData(feedbacks);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateFeedback(Long id, FeedbackDTO feedbackDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = feedbacksRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Feedback not found !")
        );

        if(feedbackDTO.getCustomerId() != null) {
            Users customer = usersRepository.findById(feedbackDTO.getCustomerId()).orElseThrow(
                    () -> new NotFoundException("Customer not found !")
            );
            feedback.setCustomer(customer);
        }

        if(feedbackDTO.getBarberId() != null) {
            Users barber = usersRepository.findById(feedbackDTO.getBarberId()).orElseThrow(
                    () -> new NotFoundException("Barber not found !")
            );
            feedback.setBarber(barber);
        }

        if(feedbackDTO.getShopId() != null) {
            Shops shop = shopsRepository.findById(feedbackDTO.getShopId()).orElseThrow(
                    () -> new NotFoundException("Shop not found !")
            );
            feedback.setShop(shop);
        }

        if(feedbackDTO.getComment() != null && !feedbackDTO.getComment().isEmpty()) {
            feedback.setComment(feedbackDTO.getComment());
        }

        if(feedbackDTO.getRating() != null) {
            feedback.setRating(feedbackDTO.getRating());

        }

        feedback.setUpdatedAt(LocalDateTime.now());

        if(img != null) {
            feedback.setImg(imgService.updateImg(feedback.getImg(),img));
        }

        feedbacksRepository.save(feedback);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update feedback success");
        apiResponse.setData(feedback);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteFeedback(Long id) {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = feedbacksRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Feedback not found !")
        );

        feedbacksRepository.delete(feedback);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete feedback by id = "+id+" success");
        apiResponse.setData(feedback);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}
