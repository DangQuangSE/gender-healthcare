package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import jakarta.validation.constraints.NotBlank;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatReaderRequest {
    @NotBlank(message = CommunicationMessages.READER_NAME_REQUIRED)
    private String readerName;
}
