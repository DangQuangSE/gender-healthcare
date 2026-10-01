package com.S_Health.GenderHealthCare.modules.appointment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.mail.EmailService;
import com.S_Health.GenderHealthCare.integrations.zoom.ZoomMeetingClient;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;
import com.S_Health.GenderHealthCare.modules.catalog.enums.ServiceType;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZoomMeetingServiceTest {

    @Mock private ZoomMeetingClient zoomMeetingClient;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private AuthUtil authUtil;
    @Mock private EmailService emailService;
    @Mock private AppointmentDetailRepository appointmentDetailRepository;
    @Mock private PaymentRepository paymentRepository;

    private ZoomMeetingService service;
    private Appointment appointment;
    private AppointmentDetail detail;
    private Payment successfulPayment;

    @BeforeEach
    void setUp() {
        service = new ZoomMeetingService(
                zoomMeetingClient,
                appointmentRepository,
                authUtil,
                emailService,
                appointmentDetailRepository,
                paymentRepository);

        User customer = User.builder().id(7L).email("customer@test.local").build();
        User consultant = User.builder().id(9L).email("consultant@test.local").build();
        Service onlineService = Service.builder()
                .id(3L)
                .name("Online consultation")
                .type(ServiceType.CONSULTING_ON)
                .build();

        appointment = new Appointment();
        appointment.setId(11L);
        appointment.setCustomer(customer);
        appointment.setService(onlineService);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setIsActive(true);

        detail = AppointmentDetail.builder()
                .appointment(appointment)
                .service(onlineService)
                .consultant(consultant)
                .slotTime(LocalDateTime.of(2026, 10, 1, 10, 30))
                .status(AppointmentStatus.CONFIRMED)
                .isActive(true)
                .build();

        successfulPayment = Payment.builder()
                .appointment(appointment)
                .status(PaymentStatus.SUCCESS)
                .build();

        when(appointmentRepository.findById(11L)).thenReturn(Optional.of(appointment));
        lenient().when(appointmentDetailRepository.findByAppointmentIdForUpdate(11L))
                .thenReturn(Optional.of(detail));
        when(authUtil.getCurrentUserId()).thenReturn(7L);
        lenient().when(paymentRepository.findByAppointmentIdAndStatus(11L, PaymentStatus.SUCCESS))
                .thenReturn(Optional.of(successfulPayment));
    }

    @Test
    void createsMeetingOnlyForOwnedConfirmedPaidOnlineAppointment() {
        when(zoomMeetingClient.createMeeting(
                eq("Online consultation"),
                eq("2026-10-01T10:30:00+07:00")))
                .thenReturn(Map.of(
                        "join_url", "https://zoom.us/j/123",
                        "start_url", "https://zoom.us/s/123"));

        Map<String, String> result = service.createMeeting(11L);

        assertThat(result).containsEntry("join_url", "https://zoom.us/j/123");
        verify(appointmentDetailRepository).save(detail);
        verify(emailService).sendUrlCurtomerZoom(
                "customer@test.local",
                "2026-10-01T10:30:00+07:00",
                "https://zoom.us/j/123",
                "Online consultation");
        verify(emailService).sendUrlConsultantZoom(
                "consultant@test.local",
                "2026-10-01T10:30:00+07:00",
                "https://zoom.us/s/123",
                "Online consultation");
    }

    @Test
    void returnsAnExistingCompletePairWithoutCallingZoomOrSendingEmail() {
        detail.setJoinUrl("https://zoom.us/j/existing");
        detail.setStartUrl("https://zoom.us/s/existing");

        Map<String, String> result = service.createMeeting(11L);

        assertThat(result).containsEntry("join_url", "https://zoom.us/j/existing");
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
        verify(emailService, never()).sendUrlCurtomerZoom(any(), any(), any(), any());
        verify(emailService, never()).sendUrlConsultantZoom(any(), any(), any(), any());
    }

    @Test
    void rejectsPartialExistingLinksAsDataConflict() {
        detail.setJoinUrl("https://zoom.us/j/partial");

        assertThatThrownBy(() -> service.createMeeting(11L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.CONFLICT);
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
    }

    @Test
    void rejectsAnUnpaidAppointmentBeforeCallingZoom() {
        when(paymentRepository.findByAppointmentIdAndStatus(11L, PaymentStatus.SUCCESS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createMeeting(11L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BAD_REQUEST);
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
    }

    @Test
    void rejectsAnUnauthorizedCustomerBeforeDisclosingLinkState() {
        when(authUtil.getCurrentUserId()).thenReturn(99L);

        assertThatThrownBy(() -> service.createMeeting(11L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN);
        verify(appointmentDetailRepository, never()).findByAppointmentIdForUpdate(11L);
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
    }

    @Test
    void rejectsInactiveAppointmentsBeforeCallingZoom() {
        appointment.setIsActive(false);

        assertThatThrownBy(() -> service.createMeeting(11L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BAD_REQUEST);
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
    }

    @Test
    void rejectsOfflineServicesBeforeCallingZoom() {
        appointment.getService().setType(ServiceType.CONSULTING);

        assertThatThrownBy(() -> service.createMeeting(11L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BAD_REQUEST);
        verify(zoomMeetingClient, never()).createMeeting(any(), any());
    }

    @Test
    void replayReturnsThePersistedPairWithoutAnotherProviderOrEmailCall() {
        Map<String, String> links = Map.of(
                "join_url", "https://zoom.us/j/replayed",
                "start_url", "https://zoom.us/s/replayed");
        when(zoomMeetingClient.createMeeting(any(), any())).thenAnswer(invocation -> {
            detail.setJoinUrl(links.get("join_url"));
            detail.setStartUrl(links.get("start_url"));
            return links;
        });

        service.createMeeting(11L);
        Map<String, String> replay = service.createMeeting(11L);

        assertThat(replay).isEqualTo(links);
        verify(zoomMeetingClient).createMeeting(any(), any());
        verify(emailService).sendUrlCurtomerZoom(any(), any(), any(), any());
        verify(emailService).sendUrlConsultantZoom(any(), any(), any(), any());
    }
}
