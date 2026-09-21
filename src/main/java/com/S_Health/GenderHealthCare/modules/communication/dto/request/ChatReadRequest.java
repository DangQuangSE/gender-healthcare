package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WebSocket payload used to mark messages as read.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatReadRequest {
    @NotBlank(message = CommunicationMessages.SESSION_ID_REQUIRED)
    private String sessionId;

    @NotBlank(message = CommunicationMessages.READER_NAME_REQUIRED)
    private String readerName;
}
