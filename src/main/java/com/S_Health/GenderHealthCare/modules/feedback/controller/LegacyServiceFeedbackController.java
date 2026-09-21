package com.S_Health.GenderHealthCare.modules.feedback.controller;

import com.S_Health.GenderHealthCare.modules.feedback.dto.request.ServiceFeedbackRequest;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.AverageRatingResponse;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.ServiceFeedbackResponse;
import com.S_Health.GenderHealthCare.modules.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@SecurityRequirement(name = "api")
public class LegacyServiceFeedbackController {
    private final FeedbackService feedbackService;

    public LegacyServiceFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<ServiceFeedbackResponse> create(@Valid @RequestBody ServiceFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createFeedback(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceFeedbackResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getById(id));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<ServiceFeedbackResponse>> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(feedbackService.getByAppointmentId(appointmentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceFeedbackResponse> update(@PathVariable Long id,
                                                          @RequestBody ServiceFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.update(id, request));
    }

    @GetMapping("average-rating/{serviceId}")
    public ResponseEntity<AverageRatingResponse> getAverageRatingByServiceId(@PathVariable Long serviceId) {
        return ResponseEntity.ok(feedbackService.getAverageRatingByServiceId(serviceId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ServiceFeedbackResponse>> getByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(feedbackService.getByServiceId(serviceId));
    }

    @GetMapping
    public ResponseEntity<List<ServiceFeedbackResponse>> getAllServiceFeedback() {
        List<ServiceFeedbackResponse> responses = feedbackService.getAllServiceRating();
        return ResponseEntity.ok(responses);
    }


}
