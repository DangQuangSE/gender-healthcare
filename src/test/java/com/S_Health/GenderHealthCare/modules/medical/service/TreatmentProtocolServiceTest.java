package com.S_Health.GenderHealthCare.modules.medical.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.modules.medical.domain.TreatmentProtocol;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.TreatmentProtocolResponse;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalResultRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.TreatmentProtocolRepository;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentProtocolServiceTest {

    @Mock private ModelMapper modelMapper;
    @Mock private TreatmentProtocolRepository treatmentProtocolRepository;
    @Mock private MedicalResultRepository medicalResultRepository;
    @Mock private CurrentUserProvider currentUserProvider;

    private TreatmentProtocolService service;
    private TreatmentProtocol protocol;

    @BeforeEach
    void setUp() {
        service = new TreatmentProtocolService(
                modelMapper,
                treatmentProtocolRepository,
                medicalResultRepository,
                currentUserProvider);
        protocol = TreatmentProtocol.builder().id(9L).active(true).build();
        when(treatmentProtocolRepository.findByIdAndActiveTrue(9L)).thenReturn(Optional.of(protocol));
    }

    @Test
    void customerCanReadAProtocolAttachedToTheirActiveMedicalResult() {
        User customer = User.builder().id(42L).role(UserRole.CUSTOMER).build();
        TreatmentProtocolResponse expected = new TreatmentProtocolResponse();
        when(currentUserProvider.requireUser()).thenReturn(customer);
        when(medicalResultRepository.existsActiveByTreatmentProtocolIdAndCustomerId(9L, 42L))
                .thenReturn(true);
        when(modelMapper.map(protocol, TreatmentProtocolResponse.class)).thenReturn(expected);

        assertThat(service.getById(9L)).isSameAs(expected);
    }

    @Test
    void customerCannotReadAProtocolFromAnotherCustomersResult() {
        User customer = User.builder().id(42L).role(UserRole.CUSTOMER).build();
        when(currentUserProvider.requireUser()).thenReturn(customer);
        when(medicalResultRepository.existsActiveByTreatmentProtocolIdAndCustomerId(9L, 42L))
                .thenReturn(false);

        assertThatThrownBy(() -> service.getById(9L))
                .isInstanceOf(DomainException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN);
        verify(modelMapper, never()).map(eq(protocol), eq(TreatmentProtocolResponse.class));
    }

    @Test
    void consultantCanReadAProtocolWithoutCustomerOwnershipLookup() {
        User consultant = User.builder().id(7L).role(UserRole.CONSULTANT).build();
        TreatmentProtocolResponse expected = new TreatmentProtocolResponse();
        when(currentUserProvider.requireUser()).thenReturn(consultant);
        when(modelMapper.map(protocol, TreatmentProtocolResponse.class)).thenReturn(expected);

        assertThat(service.getById(9L)).isSameAs(expected);
        verify(medicalResultRepository, never())
                .existsActiveByTreatmentProtocolIdAndCustomerId(9L, 7L);
    }
}
