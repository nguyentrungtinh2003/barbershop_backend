package com.TrungTinhBackend.barbershop_backend.Controller;

import com.TrungTinhBackend.barbershop_backend.DTO.FeedbackDTO;
import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import com.TrungTinhBackend.barbershop_backend.Service.Feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbacksController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addFeedback(@RequestPart(name = "feedback")FeedbackDTO feedbackDTO,
                                                   @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(feedbackService.addFeedback(feedbackDTO, img));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getFeedbackByPage(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "4") int size) throws IOException {
        return ResponseEntity.ok(feedbackService.getFeedbackByPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getFeedbackById(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<APIResponse> getFeedbackByShopId(@PathVariable Long shopId) throws IOException {
        return ResponseEntity.ok(feedbackService.getFeedbackByShopId(shopId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<APIResponse> getFeedbackByCustomerId(@PathVariable Long customerId) throws IOException {
        return ResponseEntity.ok(feedbackService.getFeedbackByCustomerId(customerId));
    }

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<APIResponse> getFeedbackByBarberId(@PathVariable Long barberId) throws IOException {
        return ResponseEntity.ok(feedbackService.getFeedbackByBarberId(barberId));
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchFeedback(@RequestParam(name = "keyword") String keyword,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "6") int size) throws IOException {
        return ResponseEntity.ok(feedbackService.searchFeedback(keyword, page, size));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse> updateFeedback(@PathVariable Long id,
                                                      @RequestPart(name = "feedback")FeedbackDTO feedbackDTO,
                                                      @RequestPart(name = "img", required = false) MultipartFile img) throws IOException {
        return ResponseEntity.ok(feedbackService.updateFeedback(id,feedbackDTO,img));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteFeedback(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(feedbackService.deleteFeedback(id));
    }
}
