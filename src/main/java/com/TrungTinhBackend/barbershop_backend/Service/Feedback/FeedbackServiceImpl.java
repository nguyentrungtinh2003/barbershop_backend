package com.TrungTinhBackend.barbershop_backend.Service.Feedback;

import com.TrungTinhBackend.barbershop_backend.DTO.FeedbackDTO;
import com.TrungTinhBackend.barbershop_backend.Entity.Feedbacks;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Exception.NotFoundException;
import com.TrungTinhBackend.barbershop_backend.Repository.FeedbacksRepository;
import com.TrungTinhBackend.barbershop_backend.Repository.UsersRepository;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Img.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    private FeedbacksRepository feedbacksRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ImgService imgService;

    @Override
    public APIResponse addFeedback(FeedbackDTO feedbackDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = new Feedbacks();

        Users customer = usersRepository.findById(feedbackDTO.getCustomer().getId()).orElseThrow(
                () -> new NotFoundException("Customer not found !")
        );

        Users barber = usersRepository.findById(feedbackDTO.getBarber().getId()).orElseThrow(
                () -> new NotFoundException("Barber not found !")
        );

        feedback.setCustomer(customer);
        feedback.setBarber(barber);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setUpdatedAt(null);

        if(img != null) {
            feedback.setImg(imgService.uploadImg(img));
        }

        feedbacksRepository.save(feedback);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Add feedback success");
        apiResponse.setData(feedback);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackByPage(int page, int size) {

        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Feedbacks> feedbacks = feedbacksRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by page = "+page+" size = "+size+" success");
        apiResponse.setData(feedbacks);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getFeedbackById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = feedbacksRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Feedback not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get feedback by id = "+id+" success");
        apiResponse.setData(feedback);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse updateFeedback(Long id, FeedbackDTO feedbackDTO, MultipartFile img) throws IOException {
        APIResponse apiResponse = new APIResponse();

        Feedbacks feedback = feedbacksRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Feedback not found !")
        );

        if(feedbackDTO.getCustomer() != null) {
            Users customer = usersRepository.findById(feedbackDTO.getCustomer().getId()).orElseThrow(
                    () -> new NotFoundException("Customer not found !")
            );
            feedback.setCustomer(customer);
        }

        if(feedbackDTO.getBarber() != null) {
            Users barber = usersRepository.findById(feedbackDTO.getBarber().getId()).orElseThrow(
                    () -> new NotFoundException("Barber not found !")
            );
            feedback.setBarber(barber);
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
