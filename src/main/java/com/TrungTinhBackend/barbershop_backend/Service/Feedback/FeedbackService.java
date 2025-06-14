package com.TrungTinhBackend.barbershop_backend.Service.Feedback;

import com.TrungTinhBackend.barbershop_backend.DTO.FeedbackDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FeedbackService {
    APIResponse addFeedback(FeedbackDTO feedbackDTO, MultipartFile img) throws IOException;
    APIResponse getFeedbackByPage(int page, int size);
    APIResponse getFeedbackById(Long id);
    APIResponse searchFeedback(String keyword, int page, int size);
    APIResponse updateFeedback(Long id, FeedbackDTO feedbackDTO, MultipartFile img) throws IOException;
    APIResponse deleteFeedback(Long id);
}
