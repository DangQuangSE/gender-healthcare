package com.S_Health.GenderHealthCare.modules.medical.dto.request;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request để nhập kết quả tư vấn khám bệnh")
public class ConsultationResultRequest {
    @NotNull(message = MedicalMessages.APPOINTMENT_DETAIL_ID_REQUIRED)
    @Positive(message = MedicalMessages.APPOINTMENT_DETAIL_ID_POSITIVE)
    @Schema(description = "ID của appointment detail mà bác sĩ đang nhập kết quả", example = "123")
    private Long appointmentDetailId;

    @NotBlank(message = MedicalMessages.DESCRIPTION_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DESCRIPTION_TOO_SHORT)
    @Schema(description = "Mô tả chi tiết về triệu chứng, vấn đề của bệnh nhân", 
            example = "Bệnh nhân có triệu chứng ngứa, đau rát vùng kín, có dịch tiết bất thường")
    private String description;

    @NotBlank(message = MedicalMessages.DIAGNOSIS_REQUIRED)
    @Size(min = 10, message = MedicalMessages.DIAGNOSIS_TOO_SHORT)
    @Schema(description = "Chẩn đoán của bác sĩ dựa trên kết quả khám", 
            example = "Viêm âm đạo do nấm Candida")
    private String diagnosis;

    @NotBlank(message = MedicalMessages.TREATMENT_PLAN_REQUIRED)
    @Size(min = 10, message = MedicalMessages.TREATMENT_PLAN_TOO_SHORT)
    @Schema(description = "Kế hoạch điều trị, tư vấn hoặc theo dõi tiếp theo", 
            example = "Sử dụng thuốc kháng nấm, tái khám sau 1 tuần")
    private String treatmentPlan;

    @NotNull(message = MedicalMessages.TREATMENT_PROTOCOL_ID_REQUIRED)
    @Positive(message = MedicalMessages.TREATMENT_PROTOCOL_ID_POSITIVE)
    Long treatmentProtocolId;
}
