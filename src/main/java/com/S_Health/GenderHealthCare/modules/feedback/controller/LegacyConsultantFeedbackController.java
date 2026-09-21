package com.S_Health.GenderHealthCare.modules.feedback.controller;



import com.S_Health.GenderHealthCare.modules.feedback.dto.request.ConsultantFeedbackRequest;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.ConsultantFeedbackResponse;
import com.S_Health.GenderHealthCare.modules.feedback.service.FeedbackService;
import com.S_Health.GenderHealthCare.modules.feedback.FeedbackMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultant-feedbacks")
@SecurityRequirement(name = "api")
@Tag(name = FeedbackMessages.CONSULTANT_FEEDBACK_TAG,
        description = FeedbackMessages.CONSULTANT_FEEDBACK_TAG_DESCRIPTION)
public class LegacyConsultantFeedbackController {
    private final FeedbackService feedbackService;

    public LegacyConsultantFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<ConsultantFeedbackResponse> create(@RequestBody ConsultantFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.createConsultantFeedback(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantFeedbackResponse> update(@PathVariable Long id,
                                                             @RequestBody ConsultantFeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateConsultantFeedback(id, request));
    }

    @GetMapping("/service-feedback/{serviceFeedbackId}")
    public ResponseEntity<List<ConsultantFeedbackResponse>> getByServiceFeedback(@PathVariable Long serviceFeedbackId) {
        return ResponseEntity.ok(feedbackService.getByServiceFeedbackId(serviceFeedbackId));
    }

    @GetMapping("/my-feedbacks")
    @Operation(summary = FeedbackMessages.GET_MY_CONSULTANT_FEEDBACK)
    public ResponseEntity<List<ConsultantFeedbackResponse>> getMyFeedbacks() {
        return ResponseEntity.ok(feedbackService.getByConsultantId());
    }
}
