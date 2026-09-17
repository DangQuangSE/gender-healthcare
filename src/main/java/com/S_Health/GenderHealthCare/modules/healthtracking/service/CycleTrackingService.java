package com.S_Health.GenderHealthCare.modules.healthtracking.service;

import com.S_Health.GenderHealthCare.modules.healthtracking.domain.CycleTracking;
import com.S_Health.GenderHealthCare.modules.healthtracking.enums.Symptoms;
import com.S_Health.GenderHealthCare.modules.user.domain.User;


import com.S_Health.GenderHealthCare.modules.healthtracking.dto.request.CycleTrackingRequest;
import com.S_Health.GenderHealthCare.modules.healthtracking.dto.response.CycleTrackingResponse;
import com.S_Health.GenderHealthCare.repository.CycleTrackingRepository;
import com.S_Health.GenderHealthCare.repository.UserRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CycleTrackingService {
    private final CycleTrackingRepository cycleTrackingRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public CycleTrackingService(
            CycleTrackingRepository cycleTrackingRepository,
            UserRepository userRepository,
            AuthUtil authUtil) {
        this.cycleTrackingRepository = cycleTrackingRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    public CycleTrackingResponse saveDailyLog(CycleTrackingRequest request) {


        Long userId = authUtil.getCurrentUserId();
        User userTracId = new User();
        userTracId.setId(userId);

        CycleTracking log = cycleTrackingRepository.findByUser_IdAndStartDate(userId, request.getStartDate())
                .orElse(new CycleTracking());

        List<Symptoms> symptoms = Optional.ofNullable(request.getSymptoms()).orElse(List.of());

        String sym = symptoms.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));// lấy enum các triệu chứng


        log.setUser(userTracId);
        log.setStartDate(request.getStartDate());
        log.setIsPeriodStart(request.getIsPeriodStart());
        log.setSymptoms(sym);
        log.setNote(request.getNote());
        log.setCreatedAt(LocalDateTime.now());

        cycleTrackingRepository.save(log);

        CycleTrackingResponse response = CycleTrackingResponse.builder()
                .id(userId)
                .isPeriodStart(request.getIsPeriodStart())
                .startDate(request.getStartDate())
                .note(request.getNote())
                .build();

        return response;
    }

    public List<CycleTrackingRequest> getLogsByUser(Long userId) {
        return cycleTrackingRepository.findAllByUserId(userId).stream().map(log -> {
            CycleTrackingRequest dto = new CycleTrackingRequest();
            dto.setUserId(userId);
            dto.setStartDate(log.getStartDate());
            dto.setIsPeriodStart(log.getIsPeriodStart());

            List<Symptoms> symptoms = Optional.ofNullable(log.getSymptoms())
                    .filter(s -> !s.isBlank())
                    .map(s -> Arrays.stream(s.split(","))
                            .map(String::trim)
                            .map(Symptoms::valueOf)
                            .collect(Collectors.toList()))
                    .orElse(List.of());

            dto.setSymptoms(symptoms);
            return dto;
        }).collect(Collectors.toList());
    }
}
