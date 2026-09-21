package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagDetailResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.TreatmentProtocolRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.TagRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.TreatmentProtocolResponse;
import com.S_Health.GenderHealthCare.modules.medical.service.TreatmentProtocolService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyTreatmentProtocolController {
    private final TreatmentProtocolService treatmentProtocolService;

    public LegacyTreatmentProtocolController(TreatmentProtocolService treatmentProtocolService) {
        this.treatmentProtocolService = treatmentProtocolService;
    }

    @PostMapping
    public ResponseEntity<TreatmentProtocolResponse> createTag(@RequestBody TreatmentProtocolRequest request) {
        return ResponseEntity.ok(treatmentProtocolService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TreatmentProtocolResponse>> getAllTreatmentProtocol() {
        return ResponseEntity.ok(treatmentProtocolService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreatmentProtocolResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(treatmentProtocolService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreatmentProtocolResponse> update(@PathVariable Long id,
                                                            @RequestBody TreatmentProtocolRequest request)  {
        TreatmentProtocolResponse response = treatmentProtocolService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
         treatmentProtocolService.delete(id);
        return ResponseEntity.ok(MedicalMessages.PROTOCOL_DELETED);
    }
}
