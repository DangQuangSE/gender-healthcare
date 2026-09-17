package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.dto.ChatMessageDTO;
import com.S_Health.GenderHealthCare.dto.ChatSessionDTO;
import com.S_Health.GenderHealthCare.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.dto.request.StartChatRequest;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ChatSessionDTO start(@Valid @RequestBody StartChatRequest request) {
        return chatService.startChatSession(request);
    }

    @PostMapping("/messages")
    @Operation(summary = CommunicationMessages.SEND_CHAT_MESSAGE)
    public ChatMessageDTO send(@Valid @RequestBody SendMessageRequest request) {
        return chatService.sendMessage(request);
    }

    @PostMapping("/sessions/{sessionId}/join")
    @Operation(summary = CommunicationMessages.JOIN_CHAT)
    public ChatSessionDTO join(@PathVariable String sessionId) {
        return chatService.joinChatSession(sessionId);
    }

    @GetMapping("/sessions")
    @Operation(summary = CommunicationMessages.GET_CHAT_SESSIONS)
    public List<ChatSessionDTO> getSessions(
            @Parameter(description = "WAITING, ACTIVE, or ENDED")
            @RequestParam(required = false) String status) {
        return chatService.getChatSessionsForStaff(status);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    @Operation(summary = CommunicationMessages.GET_CHAT_MESSAGES)
    public List<ChatMessageDTO> getMessages(@PathVariable String sessionId) {
        return chatService.getSessionMessages(sessionId);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(summary = CommunicationMessages.END_CHAT)
    public void end(@PathVariable String sessionId) {
        chatService.endChatSession(sessionId);
    }

    @PostMapping("/sessions/{sessionId}/read")
    @Operation(summary = CommunicationMessages.MARK_CHAT_READ)
    public void markRead(
            @PathVariable String sessionId,
            @RequestParam String readerName) {
        chatService.markMessagesAsRead(sessionId, readerName);
    }

    @GetMapping("/sessions/{sessionId}/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_CHAT_COUNT)
    public Integer unreadCount(
            @PathVariable String sessionId,
            @RequestParam String readerName) {
        return chatService.getUnreadCount(sessionId, readerName);
    }
}
