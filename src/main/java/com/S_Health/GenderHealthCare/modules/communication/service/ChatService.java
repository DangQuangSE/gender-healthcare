package com.S_Health.GenderHealthCare.modules.communication.service;

import com.S_Health.GenderHealthCare.modules.communication.enums.ChatStatus;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.ChatReaderRequest;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.communication.domain.ChatMessage;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.communication.domain.ChatSession;
import com.S_Health.GenderHealthCare.modules.communication.enums.SenderType;


import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatMessageResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.ChatSessionResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.SendMessageRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.StartChatRequest;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.repository.ChatMessageRepository;
import com.S_Health.GenderHealthCare.repository.ChatSessionRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
@Transactional
public class ChatService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(
            ChatSessionRepository chatSessionRepository,
            ChatMessageRepository chatMessageRepository,
            AuthUtil authUtil,
            SimpMessagingTemplate messagingTemplate) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.authUtil = authUtil;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Customer bắt đầu chat session mới
     */
    public ChatSessionResponse startChatSession(StartChatRequest request) {
        // Tạo unique session ID
        String sessionId = "chat_" + UUID.randomUUID().toString().substring(0, 8);

        ChatSession session = ChatSession.builder()
                .sessionId(sessionId)
                .customerName(request.getCustomerName())
                .status(ChatStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        session = chatSessionRepository.save(session);

        // Notify staff về session mới
        notifyStaffNewSession(session);

        return convertToSessionDTO(session);
    }

    /**
     * Gửi tin nhắn trong chat session
     */
    public ChatMessageResponse sendMessage(SendMessageRequest request) {
        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(request.getSessionId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        // Xác định sender type
        SenderType senderType = determineSenderType(request.getSenderName(), session);

        ChatMessage message = ChatMessage.builder()
                .chatSession(session)
                .senderName(request.getSenderName())
                .senderType(senderType)
                .message(request.getMessage())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();

        message = chatMessageRepository.save(message);

        // Update session
        session.setUpdatedAt(LocalDateTime.now());
        if (session.getStatus() == ChatStatus.WAITING && senderType == SenderType.STAFF) {
            session.setStatus(ChatStatus.ACTIVE);
        }
        chatSessionRepository.save(session);

        ChatMessageResponse messageDTO = convertToMessageDTO(message);

        // Send realtime message
        sendRealtimeMessage(messageDTO);

        return messageDTO;
    }

    /**
     * Staff join vào chat session
     */
    public ChatSessionResponse joinChatSession(String sessionId) {
        User currentStaff = authUtil.getCurrentUser();
        if (currentStaff.getRole() != UserRole.STAFF) {
            throw new DomainException(CommunicationMessages.STAFF_ONLY_CHAT_ACTION);
        }

        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        session.setStaff(currentStaff);
        session.setStatus(ChatStatus.ACTIVE);
        session.setUpdatedAt(LocalDateTime.now());

        session = chatSessionRepository.save(session);

        return convertToSessionDTO(session);
    }

    /**
     * Lấy danh sách chat sessions cho staff theo status
     */
    public List<ChatSessionResponse> getChatSessionsForStaff(ChatStatus requestedStatus) {
        User currentStaff = authUtil.getCurrentUser();
        if (currentStaff.getRole() != UserRole.STAFF) {
            throw new DomainException(CommunicationMessages.STAFF_ONLY_VIEW_CHAT);
        }

        List<ChatSession> sessions;

        if (requestedStatus == null) {
            // Nếu không có status parameter, lấy WAITING và ACTIVE (mặc định)
            List<ChatStatus> activeStatuses = List.of(ChatStatus.WAITING, ChatStatus.ACTIVE);
            sessions = chatSessionRepository.findByStatusInAndIsActiveTrueOrderByUpdatedAtDesc(activeStatuses);
        } else {
            sessions = chatSessionRepository
                    .findByStatusAndIsActiveTrueOrderByUpdatedAtDesc(requestedStatus);
        }

        return sessions.stream()
                .map(this::convertToSessionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Overload method for backward compatibility
     */
    public List<ChatSessionResponse> getChatSessionsForStaff() {
        return getChatSessionsForStaff((ChatStatus) null);
    }

    /**
     * Keeps the old string-based service call working for the legacy controller.
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public List<ChatSessionResponse> getChatSessionsForStaff(String statusParam) {
        if (statusParam == null || statusParam.isBlank()) {
            return getChatSessionsForStaff((ChatStatus) null);
        }
        try {
            return getChatSessionsForStaff(ChatStatus.valueOf(statusParam.toUpperCase()));
        } catch (IllegalArgumentException exception) {
            throw new DomainException(CommunicationMessages.INVALID_CHAT_STATUS.formatted(statusParam));
        }
    }

    /**
     * Lấy tin nhắn của một session
     */
    public List<ChatMessageResponse> getSessionMessages(String sessionId) {
        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        List<ChatMessage> messages = chatMessageRepository.findByChatSessionOrderBySentAtAsc(session);
        return messages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());
    }

    /**
     * Kết thúc chat session - Xóa cứng session và tất cả messages
     */
    public void endChatSession(String sessionId) {
        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        // Xóa tất cả messages của session này trước
        chatMessageRepository.deleteByChatSession(session);

        // Xóa session
        chatSessionRepository.delete(session);

        // Notify participants về việc session đã kết thúc
        messagingTemplate.convertAndSend(
                CommunicationMessages.CHAT_TOPIC.formatted(sessionId)
                        + CommunicationMessages.ENDED_TOPIC_SUFFIX,
                CommunicationMessages.CHAT_SESSION_ENDED);
    }

    /**
     * Đánh dấu tin nhắn đã đọc khi staff/customer xem
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public void markMessagesAsRead(String sessionId, String readerName) {
        ChatReaderRequest request = new ChatReaderRequest();
        request.setReaderName(readerName);
        markMessagesAsRead(sessionId, request);
    }

    public void markMessagesAsRead(String sessionId, ChatReaderRequest request) {
        String readerName = request.getReaderName();
        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        // Lấy tất cả tin nhắn chưa đọc trong session này
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findByChatSessionAndIsReadFalse(session);

        // Đánh dấu đã đọc những tin nhắn không phải của người đang đọc
        unreadMessages.stream()
                .filter(message -> !message.getSenderName().equals(readerName))
                .forEach(message -> {
                    message.setIsRead(true);
                    chatMessageRepository.save(message);
                });

        // Notify real-time về việc đã đọc
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId + "/read",
                Map.of("readerName", readerName, "readAt", LocalDateTime.now()));
    }

    /**
     * Lấy số lượng tin nhắn chưa đọc cho một session
     */
    @Deprecated(since = "1.0", forRemoval = false)
    public Integer getUnreadCount(String sessionId, String readerName) {
        ChatReaderRequest request = new ChatReaderRequest();
        request.setReaderName(readerName);
        return getUnreadCount(sessionId, request);
    }

    public Integer getUnreadCount(String sessionId, ChatReaderRequest request) {
        String readerName = request.getReaderName();
        ChatSession session = chatSessionRepository.findBySessionIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CommunicationMessages.CHAT_SESSION_NOT_FOUND));

        return chatMessageRepository.countUnreadMessagesForReader(session, readerName);
    }

    // Helper methods
    private SenderType determineSenderType(String senderName, ChatSession session) {
        if (session.getCustomerName().equals(senderName)) {
            return SenderType.CUSTOMER;
        }
        return SenderType.STAFF;
    }

    private void sendRealtimeMessage(ChatMessageResponse messageDTO) {
        // Send to specific session topic
        messagingTemplate.convertAndSend(
                CommunicationMessages.CHAT_TOPIC.formatted(messageDTO.getSessionId()), messageDTO);
        
        // Send to staff dashboard
        messagingTemplate.convertAndSend(CommunicationMessages.STAFF_MESSAGES_TOPIC, messageDTO);
    }

    private void notifyStaffNewSession(ChatSession session) {
        ChatSessionResponse sessionDTO = convertToSessionDTO(session);
        messagingTemplate.convertAndSend(CommunicationMessages.STAFF_NEW_SESSION_TOPIC, sessionDTO);
    }

    private ChatSessionResponse convertToSessionDTO(ChatSession session) {
        // Tính unread count cho staff (không đếm tin nhắn của chính staff)
        Integer unreadCount = 0;
        try {
            User currentUser = authUtil.getCurrentUser();
            if (currentUser != null && currentUser.getRole() == UserRole.STAFF) {
                unreadCount = chatMessageRepository.countUnreadMessagesForReader(session, currentUser.getFullname());
            } else {
                // Fallback to old method for backward compatibility
                unreadCount = chatMessageRepository.countUnreadMessagesBySession(session);
            }
        } catch (Exception e) {
            // If no authenticated user, use old method
            unreadCount = chatMessageRepository.countUnreadMessagesBySession(session);
        }

        String lastMessage = chatMessageRepository.findLastMessageBySession(session)
                .map(ChatMessage::getMessage)
                .orElse(null);

        return ChatSessionResponse.builder()
                .id(session.getId())
                .sessionId(session.getSessionId())
                .customerName(session.getCustomerName())
                .staffName(session.getStaff() != null ? session.getStaff().getFullname() : null)
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .lastMessage(lastMessage)
                .unreadCount(unreadCount)
                .build();
    }

    private ChatMessageResponse convertToMessageDTO(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .sessionId(message.getChatSession().getSessionId())
                .senderName(message.getSenderName())
                .senderType(message.getSenderType())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .isRead(message.getIsRead())
                .build();
    }
}
