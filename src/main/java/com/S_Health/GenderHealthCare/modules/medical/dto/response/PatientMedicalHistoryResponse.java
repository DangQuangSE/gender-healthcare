package com.S_Health.GenderHealthCare.modules.medical.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentHistoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Lịch sử khám bệnh cần thiết cho bác sĩ")
public class PatientMedicalHistoryResponse {

    @Schema(description = "Thông tin cơ bản bệnh nhân")
    PatientBasicInfoResponse patientInfo;

    @Schema(description = "Lịch sử khám bệnh")
    Page<AppointmentHistoryResponse> appointments;

    @Schema(description = "Kết quả xét nghiệm gần nhất")
    List<RecentTestResultResponse> recentTests;

    @Schema(description = "Tổng số lượt khám")
    Integer totalVisits;
}


