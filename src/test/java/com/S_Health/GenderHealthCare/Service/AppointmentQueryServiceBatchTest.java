package com.S_Health.GenderHealthCare.Service;

import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentQueryService;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalProfileRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentQueryServiceBatchTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private AppointmentDetailRepository appointmentDetailRepository;
    @Mock private MedicalResultRepository medicalResultRepository;
    @Mock private MedicalProfileRepository medicalProfileRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private CurrentUserProvider currentUserProvider;

    private AppointmentQueryService service;

    @BeforeEach
    void setUp() {
        service = new AppointmentQueryService(
                appointmentRepository,
                appointmentDetailRepository,
                medicalResultRepository,
                medicalProfileRepository,
                modelMapper,
                currentUserProvider
        );
    }

    @Test
    void statusQueryLoadsDetailsAndMedicalResultsInBatches() {
        User customer = User.builder()
                .id(1L)
                .fullname("Customer")
                .role(UserRole.CUSTOMER)
                .build();
        Service catalogService = Service.builder().id(2L).name("Consultation").build();
        Appointment appointment = new Appointment();
        appointment.setId(3L);
        appointment.setCustomer(customer);
        appointment.setService(catalogService);
        appointment.setIsActive(true);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        AppointmentDetail detail = AppointmentDetail.builder()
                .id(4L)
                .appointment(appointment)
                .consultant(User.builder().id(5L).fullname("Consultant").build())
                .service(catalogService)
                .isActive(true)
                .build();

        when(currentUserProvider.requireUser()).thenReturn(customer);
        when(appointmentRepository.findByCustomerAndStatusAndIsActiveTrue(customer, AppointmentStatus.CONFIRMED))
                .thenReturn(List.of(appointment));
        when(appointmentDetailRepository.findByAppointmentInAndIsActiveTrue(anyList()))
                .thenReturn(List.of(detail));
        when(medicalResultRepository.findByAppointmentDetailIn(anyList())).thenReturn(List.of());
        when(medicalProfileRepository.findByCustomerIdInAndIsActiveTrue(anyList())).thenReturn(List.of());
        when(modelMapper.map(any(), eq(com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse.class)))
                .thenReturn(new com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse());
        when(modelMapper.map(any(), eq(com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDetailResponse.class)))
                .thenReturn(new com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDetailResponse());

        service.getAppointmentsByStatus(AppointmentStatus.CONFIRMED);

        verify(appointmentDetailRepository).findByAppointmentInAndIsActiveTrue(anyList());
        verify(medicalResultRepository).findByAppointmentDetailIn(anyList());
        verify(appointmentDetailRepository, never()).findByAppointmentAndIsActiveTrue(any());
        verify(medicalResultRepository, never()).findByAppointmentDetail(any());
    }
}
