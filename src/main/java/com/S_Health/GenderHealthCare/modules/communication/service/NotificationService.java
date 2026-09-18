package com.S_Health.GenderHealthCare.modules.communication.service;

import com.S_Health.GenderHealthCare.modules.healthtracking.domain.CycleTracking;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.communication.domain.Notification;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;


import com.S_Health.GenderHealthCare.modules.communication.dto.request.NotificationRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.notification.NotificationAppointmentResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.notification.NotificationCycleTrackingResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.notification.NotificationResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import com.S_Health.GenderHealthCare.repository.CycleTrackingRepository;
import com.S_Health.GenderHealthCare.repository.NotificationRepository;
import com.S_Health.GenderHealthCare.repository.UserRepository;
import com.S_Health.GenderHealthCare.integrations.mail.EmailService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final CycleTrackingRepository cycleTrackingRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final EmailService emailService;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            AppointmentRepository appointmentRepository,
            CycleTrackingRepository cycleTrackingRepository,
            ModelMapper modelMapper,
            AuthUtil authUtil,
            EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.cycleTrackingRepository = cycleTrackingRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
        this.emailService = emailService;
    }

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Long userId = authUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.USER_NOT_FOUND));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.APPOINTMENT_NOT_FOUND));
        }

        CycleTracking cycleTracking = null;
        if (request.getCycleTrackingId() != null) {
            cycleTracking = cycleTrackingRepository.findById(request.getCycleTrackingId())
                    .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CYCLE_NOT_FOUND));
        }

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .appointment(appointment)
                .cycleTracking(cycleTracking)
                .isActive(true)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notification = notificationRepository.save(notification);

//        NotificationResponse notificationResponse = modelMapper.map(notification, NotificationResponse.class);
//        notificationResponse.getAppointment().setDoctorName(appointment.getConsultant().getFullname());
//        notificationResponse.getAppointment().setAppointmentDate(appointment.getPreferredDate());

        return mapToResponse(notification);
    }

    public List<NotificationResponse> getNotificationsByUser() {
        Long userId = authUtil.getCurrentUserId();
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .collect(Collectors.toList());
    }

    public NotificationResponse getNotificationById(Long notificationId) {
        Long userId = authUtil.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.NOTIFICATION_NOT_FOUND));
        return mapToResponse(notification);
    }


    @Transactional
    public void markAsRead(Long notificationId) {
        Long userId = authUtil.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.NOTIFICATION_NOT_FOUND));
        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }


    @Transactional
    public void markAllAsRead(Long ignoredUserId) {
        Long currentUserId = authUtil.getCurrentUserId();
        List<Notification> notifications = notificationRepository
                .findAllByUserIdOrderByCreatedAtDesc(currentUserId);
        for (Notification notification : notifications) {
            if (!notification.getIsRead()) {
                notification.setIsRead(true);
                notificationRepository.save(notification);
            }
        }
    }

    @Transactional
    public void markAllAsReadForCurrentUser() {
        markAllAsRead(authUtil.getCurrentUserId());
    }


    public Long countUnread(Long ignoredUserId) {
        return notificationRepository.countByUserIdAndIsReadFalse(authUtil.getCurrentUserId());
    }

    public Long countUnreadForCurrentUser() {
        return countUnread(authUtil.getCurrentUserId());
    }


    @Transactional
    public void deleteNotification(Long notificationId) {
        Long userId = authUtil.getCurrentUserId();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.NOTIFICATION_NOT_FOUND));
        notification.setIsActive(false);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .type(notification.getType().name())
                .createdAt(notification.getCreatedAt())
                .appointment(notification.getAppointment() != null
                        ? NotificationAppointmentResponse.builder()
                        .id(notification.getAppointment().getId())
//                        .doctorName(notification.getAppointment().getConsultant().getFullname())
                        .serviceName(notification.getAppointment().getService().getName())
                        .appointmentDate(notification.getAppointment().getPreferredDate())
                        .build()
                        : null)
                .cycleTracking(notification.getCycleTracking() != null
                        ? NotificationCycleTrackingResponse.builder()
                        .id(notification.getCycleTracking().getId())
                        .cycleStartDate(notification.getCycleTracking().getStartDate())
//                        .duration(notification.getCycleTracking().)
                        .build()
                        : null)
                .build();
    }

    @Scheduled(cron = "0 00 08 * * *")
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> appointments = appointmentRepository
                .findByPreferredDateAndIsActiveTrue(tomorrow);

        for (Appointment appt : appointments) {
            LocalDate date = appt.getPreferredDate();

            emailService.sendAppointmentReminder(appt.getCustomer().getEmail(), date);
        }
    }
}
