package com.S_Health.GenderHealthCare.modules.medical.dto.request;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.medical.enums.ResultType;
import com.S_Health.GenderHealthCare.modules.medical.enums.TestStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request để nhập kết quả khám bệnh/xét nghiệm")
public class ResultRequest {
    @NotNull(message = MedicalMessages.APPOINTMENT_DETAIL_ID_REQUIRED)
    @Positive(message = MedicalMessages.APPOINTMENT_DETAIL_ID_POSITIVE)
    @Schema(description = "ID của appointment detail mà bác sĩ đang nhập kết quả", example = "123")
    Long appointmentDetailId;

    @NotNull(message = MedicalMessages.RESULT_TYPE_REQUIRED)
    @Schema(description = "Loại kết quả: CONSULTATION (tư vấn) hoặc LAB_TEST (xét nghiệm)", example = "LAB_TEST")
    ResultType resultType;

    // === THÔNG TIN CHUNG ===
    @NotBlank(message = MedicalMessages.DESCRIPTION_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DESCRIPTION_TOO_SHORT)
    @Schema(description = "Mô tả chi tiết về triệu chứng, vấn đề hoặc quá trình xét nghiệm",
            example = "Bệnh nhân có triệu chứng ngứa, đau rát vùng kín")
    String description;

    @NotBlank(message = MedicalMessages.DIAGNOSIS_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DIAGNOSIS_TOO_SHORT)
    @Schema(description = "Chẩn đoán của bác sĩ dựa trên kết quả khám/xét nghiệm",
            example = "Không phát hiện HIV")
    String diagnosis;

    @NotBlank(message = MedicalMessages.TREATMENT_PLAN_REQUIRED)
    @Size(min = 10, message = MedicalMessages.TREATMENT_PLAN_TOO_SHORT)
    @Schema(description = "Kế hoạch điều trị, tư vấn hoặc theo dõi tiếp theo",
            example = "Tiếp tục theo dõi, xét nghiệm định kỳ 6 tháng/lần")
    String treatmentPlan;

    // === THÔNG TIN XÉT NGHIỆM (optional, chỉ dành cho LAB_TEST) ===
    @Schema(description = "Tên xét nghiệm (chỉ dành cho LAB_TEST)", example = "HIV Ag/Ab Combo Test")
    String testName;

    @Schema(description = "Kết quả xét nghiệm (chỉ dành cho LAB_TEST)", example = "Non-reactive")
    String testResult;

    @Schema(description = "Giá trị bình thường (chỉ dành cho LAB_TEST)", example = "Non-reactive")
    String normalRange;

    @Schema(description = "Phương pháp xét nghiệm (chỉ dành cho LAB_TEST)", example = "ELISA")
    String testMethod;

    @Schema(description = "Loại mẫu xét nghiệm (chỉ dành cho LAB_TEST)", example = "Blood")
    String specimenType;

    @Schema(description = "Trạng thái kết quả xét nghiệm (chỉ dành cho LAB_TEST)", example = "NORMAL")
    TestStatus testStatus;

    @Schema(description = "Thời gian lấy mẫu (chỉ dành cho LAB_TEST)", example = "2025-01-15T10:30:00")
    LocalDateTime sampleCollectedAt;

    @Schema(description = "Ghi chú từ phòng lab (chỉ dành cho LAB_TEST)", example = "Mẫu đạt chất lượng, kết quả tin cậy")
    String labNotes;
}
