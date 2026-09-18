package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatMessageResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatSessionResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.StartChatRequest;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatReaderRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatSessionQuery;
import com.S_Health.GenderHealthCare.modules.communication.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@SecurityRequirement(name = "api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions")
    @Operation(summary = CommunicationMessages.START_CHAT)
    public ChatSessionResponse start(@Valid @RequestBody StartChatRequest request) {
        return chatService.startChatSession(request);
    }

    @PostMapping("/messages")
    @Operation(summary = CommunicationMessages.SEND_CHAT_MESSAGE)
    public ChatMessageResponse send(@Valid @RequestBody SendMessageRequest request) {
        return chatService.sendMessage(request);
    }

    @PostMapping("/sessions/{sessionId}/join")
    @Operation(summary = CommunicationMessages.JOIN_CHAT)
    public ChatSessionResponse join(@PathVariable String sessionId) {
        return chatService.joinChatSession(sessionId);
    }

    @GetMapping("/sessions")
    @Operation(summary = CommunicationMessages.GET_CHAT_SESSIONS)
    public List<ChatSessionResponse> getSessions(
            @Valid @ModelAttribute ChatSessionQuery query) {
        return chatService.getChatSessionsForStaff(query.getStatus());
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = CommunicationMessages.GET_CHAT_MESSAGES)
    public List<ChatMessageResponse> getMessages(@PathVariable String sessionId) {
        return chatService.getSessionMessages(sessionId);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(summary = CommunicationMessages.END_CHAT)
    public ApiResponse<String> end(@PathVariable String sessionId) {
        chatService.endChatSession(sessionId);
        return ApiResponse.success(CommunicationMessages.CHAT_ENDED_SUCCESS, null);
    }

    @PostMapping("/sessions/{sessionId}/read")
    @Operation(summary = CommunicationMessages.MARK_CHAT_READ)
    public ApiResponse<String> markRead(
            @PathVariable String sessionId,
            @Valid @ModelAttribute ChatReaderRequest request) {
        chatService.markMessagesAsRead(sessionId, request);
        return ApiResponse.success(CommunicationMessages.CHAT_MESSAGES_MARKED_READ, null);
    }

    @GetMapping("/sessions/{sessionId}/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_CHAT_COUNT)
    public Integer unreadCount(
            @PathVariable String sessionId,
            @Valid @ModelAttribute ChatReaderRequest request) {
        return chatService.getUnreadCount(sessionId, request);
    }
}
