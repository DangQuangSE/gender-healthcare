package com.S_Health.GenderHealthCare.modules.feedback.controller;

import com.S_Health.GenderHealthCare.dto.request.ServiceFeedbackRequest;
import com.S_Health.GenderHealthCare.dto.response.feedback.AverageRatingResponse;
import com.S_Health.GenderHealthCare.dto.response.feedback.ServiceFeedbackResponse;
import com.S_Health.GenderHealthCare.modules.feedback.FeedbackMessages;
import com.S_Health.GenderHealthCare.modules.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-feedback")
@SecurityRequirement(name = "api")
public class ServiceFeedbackController {
    private final FeedbackService feedbackService;

    public ServiceFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @Operation(summary = FeedbackMessages.CREATE_SERVICE_FEEDBACK)
    public ServiceFeedbackResponse create(@Valid @RequestBody ServiceFeedbackRequest request) {
        return feedbackService.createFeedback(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = FeedbackMessages.GET_SERVICE_FEEDBACK)
    public ServiceFeedbackResponse getById(@PathVariable Long id) {
        return feedbackService.getById(id);
    }

    @GetMapping("/appointments/{appointmentId}")
    @Operation(summary = FeedbackMessages.GET_FEEDBACK_BY_APPOINTMENT)
    public List<ServiceFeedbackResponse> getByAppointment(@PathVariable Long appointmentId) {
        return feedbackService.getByAppointmentId(appointmentId);
    }

    @PutMapping("/{id}")
    @Operation(summary = FeedbackMessages.UPDATE_SERVICE_FEEDBACK)
    public ServiceFeedbackResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceFeedbackRequest request) {
        return feedbackService.update(id, request);
    }

    @GetMapping("/services/{serviceId}/average-rating")
    @Operation(summary = FeedbackMessages.GET_AVERAGE_RATING)
    public AverageRatingResponse getAverageRating(@PathVariable Long serviceId) {
        return feedbackService.getAverageRatingByServiceId(serviceId);
    }

    @GetMapping("/services/{serviceId}")
    @Operation(summary = FeedbackMessages.GET_FEEDBACK_BY_SERVICE)
    public List<ServiceFeedbackResponse> getByService(@PathVariable Long serviceId) {
        return feedbackService.getByServiceId(serviceId);
    }

    @GetMapping
    @Operation(summary = FeedbackMessages.GET_ALL_FEEDBACK)
    public List<ServiceFeedbackResponse> getAll() {
        return feedbackService.getAllServiceRating();
    }
}
