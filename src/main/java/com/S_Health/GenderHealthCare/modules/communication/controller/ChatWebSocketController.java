package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatSessionResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatReaderRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatReadRequest;
import com.S_Health.GenderHealthCare.modules.communication.service.ChatService;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketController.class);

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(
            ChatService chatService,
            SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping(CommunicationMessages.CHAT_SEND_MAPPING)
    public void sendMessage(@Valid @Payload SendMessageRequest request) {
        try {
            chatService.sendMessage(request);
        } catch (Exception e) {
            log.error(CommonMessages.LOG_UNEXPECTED_ERROR, CommunicationMessages.CHAT_SEND_MAPPING, e);
            // Send error message
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(request.getSessionId())
                            + CommunicationMessages.ERROR_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_SEND_ERROR);
        }
    }

    @MessageMapping(CommunicationMessages.CHAT_JOIN_MAPPING)
    public void joinSession(@Payload String sessionId) {
        try {
            ChatSessionResponse session = chatService.joinChatSession(sessionId);
            // Notify others that someone joined
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                            + CommunicationMessages.JOINED_TOPIC_SUFFIX,
                    session);
        } catch (Exception e) {
            log.error(CommonMessages.LOG_UNEXPECTED_ERROR, CommunicationMessages.CHAT_JOIN_MAPPING, e);
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                            + CommunicationMessages.ERROR_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_JOIN_ERROR);
        }
    }

    @MessageMapping(CommunicationMessages.CHAT_MARK_READ_MAPPING)
    public void markAsRead(@Valid @Payload ChatReadRequest request) {
        try {
            ChatReaderRequest readerRequest = new ChatReaderRequest();
            readerRequest.setReaderName(request.getReaderName());
            chatService.markMessagesAsRead(request.getSessionId(), readerRequest);
        } catch (Exception e) {
            log.error(CommonMessages.LOG_UNEXPECTED_ERROR, CommunicationMessages.CHAT_MARK_READ_MAPPING, e);
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(request.getSessionId())
                            + CommunicationMessages.ERROR_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_MARK_READ_ERROR);
        }
    }
}
