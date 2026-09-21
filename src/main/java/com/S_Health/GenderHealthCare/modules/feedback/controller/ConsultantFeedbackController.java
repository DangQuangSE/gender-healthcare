package com.S_Health.GenderHealthCare.modules.feedback.controller;

import com.S_Health.GenderHealthCare.modules.feedback.dto.request.ConsultantFeedbackRequest;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.ConsultantFeedbackResponse;
import com.S_Health.GenderHealthCare.modules.feedback.FeedbackMessages;
import com.S_Health.GenderHealthCare.modules.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultant-feedback")
@SecurityRequirement(name = "api")
public class ConsultantFeedbackController {
    private final FeedbackService feedbackService;

    public ConsultantFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @Operation(summary = FeedbackMessages.CREATE_CONSULTANT_FEEDBACK)
    public ConsultantFeedbackResponse create(@Valid @RequestBody ConsultantFeedbackRequest request) {
        return feedbackService.createConsultantFeedback(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = FeedbackMessages.UPDATE_CONSULTANT_FEEDBACK)
    public ConsultantFeedbackResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ConsultantFeedbackRequest request) {
        return feedbackService.updateConsultantFeedback(id, request);
    }

    @GetMapping("/service-feedback/{serviceFeedbackId}")
    @Operation(summary = FeedbackMessages.GET_FEEDBACK_BY_SERVICE_FEEDBACK)
    public List<ConsultantFeedbackResponse> getByServiceFeedback(
            @PathVariable Long serviceFeedbackId) {
        return feedbackService.getByServiceFeedbackId(serviceFeedbackId);
    }

    @GetMapping("/me")
    @Operation(summary = FeedbackMessages.GET_MY_CONSULTANT_FEEDBACK)
    public List<ConsultantFeedbackResponse> getMyFeedbacks() {
        return feedbackService.getByConsultantId();
    }
}
