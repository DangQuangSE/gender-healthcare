package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import com.S_Health.GenderHealthCare.modules.communication.enums.ChatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Optional filters for the staff chat-session list.
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Filters for chat sessions")
public class ChatSessionQuery {
    @Schema(description = "Chat status filter", example = "WAITING")
    private ChatStatus status;
}
