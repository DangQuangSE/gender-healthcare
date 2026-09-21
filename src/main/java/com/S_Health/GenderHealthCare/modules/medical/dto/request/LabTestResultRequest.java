package com.S_Health.GenderHealthCare.modules.medical.dto.request;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
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
@Schema(description = "Request để nhập kết quả xét nghiệm")
public class LabTestResultRequest {
    @NotNull(message = MedicalMessages.APPOINTMENT_DETAIL_ID_REQUIRED)
    @Positive(message = MedicalMessages.APPOINTMENT_DETAIL_ID_POSITIVE)
    @Schema(description = "ID của appointment detail liên quan đến xét nghiệm", example = "123")
    private Long appointmentDetailId;

    @NotBlank(message = MedicalMessages.DESCRIPTION_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DESCRIPTION_TOO_SHORT)
    @Schema(description = "Mô tả về mục đích hoặc tình trạng xét nghiệm", 
            example = "Kiểm tra định kỳ HIV theo yêu cầu của bệnh nhân")
    private String description;

    @NotBlank(message = MedicalMessages.DIAGNOSIS_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DIAGNOSIS_TOO_SHORT)
    @Schema(description = "Kết luận từ kết quả xét nghiệm", 
            example = "Âm tính với HIV, không phát hiện kháng thể")
    private String diagnosis;

    @NotBlank(message = MedicalMessages.TREATMENT_PLAN_REQUIRED)
    @Size(min = 10, message = MedicalMessages.TREATMENT_PLAN_TOO_SHORT)
    @Schema(description = "Hướng dẫn điều trị / theo dõi", 
            example = "Không cần điều trị, kiểm tra lại sau 6 tháng")
    private String treatmentPlan;

    // === Các trường riêng cho LAB TEST ===
    @NotBlank(message = MedicalMessages.TEST_NAME_REQUIRED)
    @Schema(description = "Tên xét nghiệm", example = "HIV Ag/Ab Combo Test")
    private String testName;

    @NotBlank(message = MedicalMessages.TEST_RESULT_REQUIRED)
    @Schema(description = "Kết quả xét nghiệm", example = "Non-reactive")
    private String testResult;

    @Schema(description = "Giá trị bình thường", example = "Non-reactive")
    private String normalRange;

    @Schema(description = "Phương pháp xét nghiệm", example = "ELISA")
    private String testMethod;

    @Schema(description = "Loại mẫu xét nghiệm", example = "Blood")
    private String specimenType;

    @NotNull(message = MedicalMessages.TEST_STATUS_REQUIRED)
    @Schema(description = "Trạng thái kết quả xét nghiệm", example = "NORMAL")
    private TestStatus testStatus;

    @Schema(description = "Thời gian lấy mẫu", example = "2025-01-15T10:30:00")
    private LocalDateTime sampleCollectedAt;

    @Schema(description = "Ghi chú từ phòng lab", example = "Mẫu đạt chất lượng, kết quả tin cậy")
    private String labNotes;

    @NotNull(message = MedicalMessages.TREATMENT_PROTOCOL_ID_REQUIRED)
    @Positive(message = MedicalMessages.TREATMENT_PROTOCOL_ID_POSITIVE)
    Long treatmentProtocolId;
}
