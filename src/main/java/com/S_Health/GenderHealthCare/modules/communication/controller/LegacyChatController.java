package com.S_Health.GenderHealthCare.modules.communication.controller;



import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatMessageResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatSessionResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.StartChatRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatReaderRequest;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@SecurityRequirement(name = "api")
@Tag(name = CommunicationMessages.CHAT_TAG, description = CommunicationMessages.CHAT_TAG_DESCRIPTION)
public class LegacyChatController {
    private final ChatService chatService;

    public LegacyChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/start")
    @Operation(summary = CommunicationMessages.START_CHAT)
    public ResponseEntity<ChatSessionResponse> startChatSession(@Valid @RequestBody StartChatRequest request) {
        ChatSessionResponse session = chatService.startChatSession(request);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/send")
    @Operation(summary = CommunicationMessages.SEND_CHAT_MESSAGE)
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        ChatMessageResponse message = chatService.sendMessage(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/join/{sessionId}")
    @Operation(summary = CommunicationMessages.JOIN_CHAT)
    public ResponseEntity<ChatSessionResponse> joinChatSession(@PathVariable String sessionId) {
        ChatSessionResponse session = chatService.joinChatSession(sessionId);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/sessions")
    @Operation(summary = CommunicationMessages.GET_CHAT_SESSIONS)
    public ResponseEntity<List<ChatSessionResponse>> getChatSessions(
            @Parameter(
                description = CommunicationMessages.CHAT_STATUS_FILTER_DESCRIPTION,
                example = "WAITING"
            )
            @RequestParam(required = false) String status) {
        List<ChatSessionResponse> sessions = chatService.getChatSessionsForStaff(status);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = CommunicationMessages.GET_CHAT_MESSAGES)
    public ResponseEntity<List<ChatMessageResponse>> getSessionMessages(@PathVariable String sessionId) {
        List<ChatMessageResponse> messages = chatService.getSessionMessages(sessionId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/sessions/{sessionId}/end")
    @Operation(summary = CommunicationMessages.END_CHAT)
    public ResponseEntity<Void> endChatSession(@PathVariable String sessionId) {
        chatService.endChatSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sessions/{sessionId}/mark-read")
    @Operation(summary = CommunicationMessages.MARK_CHAT_READ)
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable String sessionId,
            @Valid @ModelAttribute ChatReaderRequest request) {
        chatService.markMessagesAsRead(sessionId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sessions/{sessionId}/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_CHAT_COUNT)
    public ResponseEntity<Integer> getUnreadCount(
            @PathVariable String sessionId,
            @Valid @ModelAttribute ChatReaderRequest request) {
        Integer count = chatService.getUnreadCount(sessionId, request);
        return ResponseEntity.ok(count);
    }
}
