package com.S_Health.GenderHealthCare.modules.feedback.service;

import com.S_Health.GenderHealthCare.modules.feedback.domain.ServiceFeedback;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.feedback.domain.ConsultantFeedback;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;


import com.S_Health.GenderHealthCare.modules.feedback.dto.request.ConsultantFeedbackRequest;
import com.S_Health.GenderHealthCare.modules.feedback.dto.request.ServiceFeedbackRequest;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.AverageRatingResponse;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.ConsultantFeedbackResponse;
import com.S_Health.GenderHealthCare.modules.feedback.dto.response.ServiceFeedbackResponse;
import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.modules.feedback.FeedbackMessages;
import com.S_Health.GenderHealthCare.repository.*;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class FeedbackService {
    private final AppointmentRepository appointmentRepository;
    private final ServiceFeedbackRepository serviceFeedbackRepository;
    private final ConsultantFeedbackRepository consultantFeedbackRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public FeedbackService(
            AppointmentRepository appointmentRepository,
            ServiceFeedbackRepository serviceFeedbackRepository,
            ConsultantFeedbackRepository consultantFeedbackRepository,
            UserRepository userRepository,
            AuthUtil authUtil) {
        this.appointmentRepository = appointmentRepository;
        this.serviceFeedbackRepository = serviceFeedbackRepository;
        this.consultantFeedbackRepository = consultantFeedbackRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    public ServiceFeedbackResponse createFeedback(ServiceFeedbackRequest request){
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.APPOINTMENT_NOT_FOUND));

        Long userId = authUtil.getCurrentUserId();

        // Kiểm tra null safety
        if (appointment.getCustomer() == null) {
            throw new ApiException(FeedbackMessages.CUSTOMER_INVALID);
        }
        Long customerId = appointment.getCustomer().getId();

        if (!customerId.equals(userId)) {
            throw new ApiException(FeedbackMessages.APPOINTMENT_NOT_OWNED);
        }
        ServiceFeedback serviceFeedback = ServiceFeedback.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .createAt(LocalDateTime.now())
                .appointment(appointment)
                .build();
        serviceFeedbackRepository.save(serviceFeedback);

        // Tạo consultant feedback cho tất cả consultant trong appointmentDetails
        List<ConsultantFeedbackResponse> consultantFeedbacks = new ArrayList<>();

        if (appointment.getAppointmentDetails() != null && !appointment.getAppointmentDetails().isEmpty()) {
            // Lấy danh sách unique consultant IDs từ appointmentDetails
            Set<Long> consultantIds = appointment.getAppointmentDetails().stream()
                    .filter(detail -> detail.getConsultant() != null)
                    .map(detail -> detail.getConsultant().getId())
                    .collect(Collectors.toSet());

            // Tạo consultant feedback cho mỗi consultant
            for (Long consultantId : consultantIds) {
                if (request.getCommentConsultant() != null && !request.getCommentConsultant().trim().isEmpty()) {
                    ConsultantFeedback consultantFeedback = ConsultantFeedback.builder()
                            .rating(request.getRating())
                            .comment(request.getCommentConsultant())
                            .consultantId(consultantId)
                            .serviceFeedback(serviceFeedback)
                            .createAt(serviceFeedback.getCreateAt())
                            .build();
                    consultantFeedbackRepository.save(consultantFeedback);

                    consultantFeedbacks.add(
                            ConsultantFeedbackResponse.builder()
                                    .id(consultantFeedback.getId())
                                    .rating(consultantFeedback.getRating())
                                    .comment(consultantFeedback.getComment())
                                    .createdAt(consultantFeedback.getCreateAt())
                                    .consultantId(consultantFeedback.getConsultantId())
                                    .build()
                    );
                }
            }
        }

        return ServiceFeedbackResponse.builder()
                .id(serviceFeedback.getId())
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(serviceFeedback.getCreateAt())
                .appointmentId(request.getAppointmentId())
                .consultantFeedbacks(consultantFeedbacks)
                .build();
    }

    public ServiceFeedbackResponse getById(Long id) {
        ServiceFeedback feedback = serviceFeedbackRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.FEEDBACK_NOT_FOUND));

        return ServiceFeedbackResponse.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreateAt())
                .appointmentId(feedback.getAppointment().getId())
                .consultantFeedbacks(List.of())
                .build();
    }

    public List<ServiceFeedbackResponse> getByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.APPOINTMENT_NOT_FOUND));

        ServiceFeedback  serviceFeedback = serviceFeedbackRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.FEEDBACK_NOT_FOUND_FOR_APPOINTMENT));

        List<ConsultantFeedbackResponse> consultantFeedbacks = consultantFeedbackRepository.findByServiceFeedbackId(serviceFeedback.getId())
                .stream()
                .map(cf -> ConsultantFeedbackResponse.builder()
                        .id(cf.getId())
                        .rating(cf.getRating())
                        .comment(cf.getComment())
                        .createdAt(cf.getCreateAt())
                        .consultantId(cf.getConsultantId())
                        .build())
                .collect(Collectors.toList());

        return List.of(ServiceFeedbackResponse.builder()
                .id(serviceFeedback.getId())
                .rating(serviceFeedback.getRating())
                .comment(serviceFeedback.getComment())
                .createdAt(serviceFeedback.getCreateAt())
                .appointmentId(serviceFeedback.getAppointment().getId())
                .consultantFeedbacks(consultantFeedbacks)
                .build());
    }

    public ServiceFeedbackResponse update(Long id, ServiceFeedbackRequest request) {
        ServiceFeedback feedback = serviceFeedbackRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.FEEDBACK_NOT_FOUND));

        Long userId = authUtil.getCurrentUserId();

        Long appointmentUserId = feedback.getAppointment().getCustomer().getId();
        if (!appointmentUserId.equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, FeedbackMessages.FEEDBACK_UPDATE_FORBIDDEN);
        }

        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setUpdateAt(LocalDateTime.now());
        ServiceFeedback updated = serviceFeedbackRepository.save(feedback);

        List<ConsultantFeedback> consultantFeedbacks = consultantFeedbackRepository.findByServiceFeedbackId(feedback.getId());
        if (consultantFeedbacks.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.CONSULTANT_FEEDBACK_NOT_FOUND);
        }
        ConsultantFeedback consultantFeedback = consultantFeedbacks.get(0);

        consultantFeedback.setComment(request.getCommentConsultant());
        consultantFeedback.setRating(request.getRating());
        consultantFeedback.setUpdateAt(LocalDateTime.now());
        consultantFeedbackRepository.save(consultantFeedback);

        return ServiceFeedbackResponse.builder()
                .id(updated.getId())
                .rating(updated.getRating())
                .comment(updated.getComment())
                .createdAt(updated.getCreateAt())
                .updateAt(updated.getUpdateAt())
                .appointmentId(updated.getAppointment().getId())
                .consultantFeedbacks(List.of())
                .build();
    }

    public ConsultantFeedbackResponse createConsultantFeedback(ConsultantFeedbackRequest request){
        ServiceFeedback feedback = serviceFeedbackRepository.findById(request.getServiceFeedbackId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.FEEDBACK_NOT_FOUND_FOR_APPOINTMENT));

        Long userId = authUtil.getCurrentUserId();

        Long customerId = feedback.getAppointment().getCustomer().getId();

        if (!customerId.equals(userId)) {
            throw new ApiException(FeedbackMessages.APPOINTMENT_NOT_OWNED);
        }

        User consultant = userRepository.findByIdAndRole(request.getConsultantId(), UserRole.CONSULTANT)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.CONSULTANT_NOT_FOUND));

        // Kiểm tra consultant có thuộc appointment này không (qua appointmentDetails)
        boolean consultantBelongsToAppointment = feedback.getAppointment().getAppointmentDetails().stream()
                .anyMatch(detail -> detail.getConsultant() != null &&
                         detail.getConsultant().getId() == request.getConsultantId());

        if (!consultantBelongsToAppointment) {
            throw new ApiException(FeedbackMessages.CONSULTANT_NOT_IN_APPOINTMENT);
        }


        ConsultantFeedback consultantFeedback = ConsultantFeedback.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .consultantId(request.getConsultantId())
                .serviceFeedback(feedback)
                .createAt(LocalDateTime.now())
                .build();
        consultantFeedbackRepository.save(consultantFeedback);

        return ConsultantFeedbackResponse.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(consultantFeedback.getCreateAt())
                .consultantId(request.getConsultantId())
                .build();
    }

    public ConsultantFeedbackResponse updateConsultantFeedback(Long id, ConsultantFeedbackRequest request) {
        ConsultantFeedback cf = consultantFeedbackRepository.findById(id)
                .orElseThrow(() -> new ApiException(FeedbackMessages.NO_CONSULTANT_FEEDBACK));

        Long userId = authUtil.getCurrentUserId();

        Long appointmentUserId = cf.getServiceFeedback().getAppointment().getCustomer().getId();
        if (!appointmentUserId.equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, FeedbackMessages.FEEDBACK_UPDATE_FORBIDDEN);
        }

        User consultant = userRepository.findByIdAndRole(request.getConsultantId(), UserRole.CONSULTANT)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, FeedbackMessages.CONSULTANT_NOT_FOUND));

        // Kiểm tra consultant có thuộc appointment này không (qua appointmentDetails)
        boolean consultantBelongsToAppointment = cf.getServiceFeedback().getAppointment().getAppointmentDetails().stream()
                .anyMatch(detail -> detail.getConsultant() != null &&
                         detail.getConsultant().getId() == request.getConsultantId());

        if (!consultantBelongsToAppointment) {
            throw new ApiException(FeedbackMessages.CONSULTANT_NOT_IN_APPOINTMENT);
        }

        cf.setComment(request.getComment());
        cf.setRating(request.getRating());
        cf.setUpdateAt(LocalDateTime.now());
        ConsultantFeedback updated = consultantFeedbackRepository.save(cf);

        return ConsultantFeedbackResponse.builder()
                .id(updated.getId())
                .consultantId(updated.getConsultantId())
                .comment(updated.getComment())
                .createdAt(updated.getCreateAt())
                .updateAt(updated.getUpdateAt())
                .build();
    }

    public List<ConsultantFeedbackResponse> getByServiceFeedbackId(Long feedbackId) {
        return consultantFeedbackRepository.findByServiceFeedbackId(feedbackId)
                .stream()
                .map(cf -> ConsultantFeedbackResponse.builder()
                        .id(cf.getId())
                        .rating(cf.getRating())
                        .consultantId(cf.getConsultantId())
                        .comment(cf.getComment())
                        .createdAt(cf.getCreateAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ConsultantFeedbackResponse> getByConsultantId() {
        Long consultantId = authUtil.getCurrentUserId();
        return consultantFeedbackRepository.findByConsultantId(consultantId)
                .stream()
                .map(cf -> ConsultantFeedbackResponse.builder()
                        .id(cf.getId())
                        .rating(cf.getRating())
                        .consultantId(cf.getConsultantId())
                        .comment(cf.getComment())
                        .createdAt(cf.getCreateAt())
                        .updateAt(cf.getUpdateAt())
                        .build())
                .collect(Collectors.toList());
    }

    public AverageRatingResponse getAverageRatingByServiceId(Long serviceId) {
        List<Appointment> appointment = appointmentRepository.findByServiceIdAndIsRatedTrue(serviceId);

        if (appointment.isEmpty()) {
            return AverageRatingResponse.builder()
                    .serviceId(serviceId)
                    .averageRating(0.0)
                    .totalAppointment(0L)
                    .build();
            }

        List<Long> appointmentIds = appointment
                .stream()
                .map(Appointment::getId)
                .collect(Collectors.toList());

        List<ServiceFeedback> feedbacks = serviceFeedbackRepository.findByAppointmentIdIn(appointmentIds);

        if (feedbacks.isEmpty()) {
            return AverageRatingResponse.builder()
                    .serviceId(serviceId)
                    .averageRating(0.0)
                    .totalAppointment(0L)
                    .build();
            }


        double sumRate = feedbacks.stream()
                .mapToDouble(ServiceFeedback::getRating)
                .sum();

        double averageRating = sumRate / feedbacks.size();

        return AverageRatingResponse.builder()
                .serviceId(serviceId)
                .averageRating(averageRating)
                .totalAppointment((long) feedbacks.size())
                .build();
    }

    public List<ServiceFeedbackResponse> getByServiceId(Long serviceId) {
        List<Appointment> appointment = appointmentRepository.findByServiceIdAndIsRatedTrue(serviceId);
        // liệt kê tất cả id appointment của 1 service
        List<Long> appointmentIds = appointment
                .stream()
                .map(Appointment::getId)
                .collect(Collectors.toList());

        List<ServiceFeedback> feedbacks = serviceFeedbackRepository.findByAppointmentIdIn(appointmentIds);
        // liệt kê tất cả id serviceFeedback trong các appointment của service
        List<Long> serviceFeedbackIds =feedbacks
                .stream()
                .map(ServiceFeedback::getId)
                .collect(Collectors.toList());

        Map<Long, List<ConsultantFeedbackResponse>> consultantFeedbackMap =
                consultantFeedbackRepository.findByServiceFeedbackIdIn(serviceFeedbackIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                cf -> cf.getServiceFeedback().getId(),
                                Collectors.mapping(cf -> ConsultantFeedbackResponse.builder()
                                        .id(cf.getId())
                                        .rating(cf.getRating())
                                        .comment(cf.getComment())
                                        .createdAt(cf.getCreateAt())
                                        .consultantId(cf.getConsultantId())
                                        .updateAt(cf.getUpdateAt())
                                        .build(), Collectors.toList())
                        ));

        return feedbacks.stream()
                .map(feedback -> {
                    List<ConsultantFeedbackResponse> consultantFeedbacksOfThisFeedback =
                            consultantFeedbackMap.getOrDefault(feedback.getId(), Collections.emptyList());

                    return ServiceFeedbackResponse.builder()
                            .id(feedback.getId())
                            .rating(feedback.getRating())
                            .comment(feedback.getComment())
                            .createdAt(feedback.getCreateAt())
                            .appointmentId(feedback.getAppointment().getId())
                            .consultantFeedbacks(consultantFeedbacksOfThisFeedback)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<ServiceFeedbackResponse> getAllServiceRating() {
        return serviceFeedbackRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServiceFeedbackResponse mapToResponse(ServiceFeedback feedback) {
        ServiceFeedbackResponse response = new ServiceFeedbackResponse();
        response.setId(feedback.getId());
        response.setRating(feedback.getRating());
        response.setComment(feedback.getComment());
        response.setCreatedAt(feedback.getCreateAt());
        response.setCustomerName(feedback.getAppointment().getCustomer().getFullname());

        if (feedback.getAppointment().getService().getName() != null) {
            response.setServiceFeedbackName(feedback.getAppointment().getService().getName());
        }





        return response;
    }

}
