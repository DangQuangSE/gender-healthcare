package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatMessageDTO;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.modules.communication.service.ChatService;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

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
    public void sendMessage(@Payload SendMessageRequest request) {
        try {
            ChatMessageDTO message = chatService.sendMessage(request);
            
            // Send to specific session
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(request.getSessionId()), message);
            
            // Send to staff dashboard
            messagingTemplate.convertAndSend(CommunicationMessages.STAFF_MESSAGES_TOPIC, message);
            
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
            // Notify others that someone joined
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                            + CommunicationMessages.JOINED_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_SOMEONE_JOINED);
        } catch (Exception e) {
            log.error(CommonMessages.LOG_UNEXPECTED_ERROR, CommunicationMessages.CHAT_JOIN_MAPPING, e);
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                            + CommunicationMessages.ERROR_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_JOIN_ERROR);
        }
    }

    @MessageMapping(CommunicationMessages.CHAT_MARK_READ_MAPPING)
    public void markAsRead(@Payload Map<String, String> payload) {
        try {
            String sessionId = payload.get("sessionId");
            String readerName = payload.get("readerName");

            chatService.markMessagesAsRead(sessionId, readerName);
        } catch (Exception e) {
            log.error(CommonMessages.LOG_UNEXPECTED_ERROR, CommunicationMessages.CHAT_MARK_READ_MAPPING, e);
            String sessionId = payload.get("sessionId");
            messagingTemplate.convertAndSend(
                    CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                            + CommunicationMessages.ERROR_TOPIC_SUFFIX,
                    CommunicationMessages.CHAT_MARK_READ_ERROR);
        }
    }
}
