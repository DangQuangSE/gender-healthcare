package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request để bắt đầu chat session")
public class StartChatRequest {
    
    @NotBlank(message = CommunicationMessages.CUSTOMER_NAME_REQUIRED)
    @Schema(description = "Tên khách hàng", example = "Nguyễn Văn A")
    String customerName;
}
