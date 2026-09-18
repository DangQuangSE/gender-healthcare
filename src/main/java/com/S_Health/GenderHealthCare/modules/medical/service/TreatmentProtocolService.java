package com.S_Health.GenderHealthCare.modules.medical.service;

import com.S_Health.GenderHealthCare.modules.medical.domain.TreatmentProtocol;


import com.S_Health.GenderHealthCare.modules.medical.dto.request.TreatmentProtocolRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.TreatmentProtocolResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.repository.TreatmentProtocolRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class TreatmentProtocolService {
    private final ModelMapper modelMapper;
    private final TreatmentProtocolRepository treatmentProtocolRepository;

    public TreatmentProtocolService(
            ModelMapper modelMapper,
            TreatmentProtocolRepository treatmentProtocolRepository) {
        this.modelMapper = modelMapper;
        this.treatmentProtocolRepository = treatmentProtocolRepository;
    }


    @Transactional
    public TreatmentProtocolResponse create (TreatmentProtocolRequest request){
        TreatmentProtocol treatmentProtocol = modelMapper.map(request, TreatmentProtocol.class);
        treatmentProtocolRepository.save(treatmentProtocol);
        return modelMapper.map(treatmentProtocol, TreatmentProtocolResponse.class);

    }

    @Transactional(readOnly = true)
    public List<TreatmentProtocolResponse> getAll (){
        List<TreatmentProtocol> treatmentProtocol = treatmentProtocolRepository.findByActiveTrue();
        return treatmentProtocol.stream().map(x
                -> modelMapper.map(x, TreatmentProtocolResponse.class))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public TreatmentProtocolResponse getById (Long id){
        TreatmentProtocol treatmentProtocol = treatmentProtocolRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.PROTOCOL_ID_NOT_FOUND));
        return modelMapper.map(treatmentProtocol, TreatmentProtocolResponse.class);

    }
    @Transactional
    public TreatmentProtocolResponse update(Long id, TreatmentProtocolRequest request){
        TreatmentProtocol treatmentProtocol = treatmentProtocolRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.PROTOCOL_ID_NOT_FOUND));

        modelMapper.map(request, treatmentProtocol);

        treatmentProtocolRepository.save(treatmentProtocol);
        return modelMapper.map(treatmentProtocol, TreatmentProtocolResponse.class);
    }

    @Transactional
    public void delete(Long id){
        TreatmentProtocol treatmentProtocol = treatmentProtocolRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.PROTOCOL_ID_NOT_FOUND));

        treatmentProtocol.setActive(false);
        treatmentProtocolRepository.save(treatmentProtocol);
    }
}
