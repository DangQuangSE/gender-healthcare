package com.S_Health.GenderHealthCare.modules.communication;

/**
 * Messages and transport paths used by chat and notification features.
 */
public final class CommunicationMessages {
    public static final String CHAT_SESSION_NOT_FOUND = "Không tìm thấy chat session";
    public static final String STAFF_ONLY_CHAT_ACTION = "Chỉ staff mới có thể thực hiện thao tác chat này";
    public static final String STAFF_ONLY_VIEW_CHAT = "Chỉ staff mới có thể xem chat sessions";
    public static final String INVALID_CHAT_STATUS =
            "Invalid status: %s. Valid values: WAITING, ACTIVE, ENDED";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String APPOINTMENT_NOT_FOUND = "Cuộc hẹn không tồn tại";
    public static final String CYCLE_NOT_FOUND = "Không tìm thấy chu kỳ theo dõi";
    public static final String NOTIFICATION_NOT_FOUND = "Thông báo không tồn tại";

    public static final String CHAT_SEND_MAPPING = "/chat.send";
    public static final String CHAT_JOIN_MAPPING = "/chat.join";
    public static final String CHAT_MARK_READ_MAPPING = "/chat.markRead";
    public static final String CHAT_TOPIC = "/topic/chat/%s";
    public static final String STAFF_MESSAGES_TOPIC = "/topic/staff/messages";
    public static final String STAFF_NEW_SESSION_TOPIC = "/topic/staff/new-session";
    public static final String JOINED_TOPIC_SUFFIX = "/joined";
    public static final String ERROR_TOPIC_SUFFIX = "/error";
    public static final String ENDED_TOPIC_SUFFIX = "/ended";
    public static final String READ_TOPIC_SUFFIX = "/read";
    public static final String CHAT_SESSION_ENDED = "Chat session ended";
    public static final String CHAT_SOMEONE_JOINED = "Someone joined the chat";
    public static final String CHAT_SEND_ERROR = "Error sending message: %s";
    public static final String CHAT_JOIN_ERROR = "Error joining session: %s";
    public static final String CHAT_MARK_READ_ERROR = "Error marking messages as read: %s";
    public static final String START_CHAT = "Start a chat session";
    public static final String SEND_CHAT_MESSAGE = "Send a chat message";
    public static final String JOIN_CHAT = "Join a chat session";
    public static final String GET_CHAT_SESSIONS = "Get chat sessions";
    public static final String GET_CHAT_MESSAGES = "Get chat messages";
    public static final String END_CHAT = "End a chat session";
    public static final String MARK_CHAT_READ = "Mark chat messages as read";
    public static final String GET_UNREAD_CHAT_COUNT = "Get unread chat count";
    public static final String CREATE_NOTIFICATION = "Create a notification";
    public static final String GET_NOTIFICATIONS = "Get my notifications";
    public static final String GET_NOTIFICATION = "Get a notification";
    public static final String MARK_NOTIFICATION_READ = "Mark a notification as read";
    public static final String MARK_ALL_NOTIFICATIONS_READ = "Mark all notifications as read";
    public static final String GET_UNREAD_NOTIFICATION_COUNT = "Get unread notification count";
    public static final String DELETE_NOTIFICATION = "Delete a notification";
    public static final String READER_NAME_REQUIRED = "Reader name is required";
    public static final String CUSTOMER_NAME_REQUIRED = "Customer name is required";
    public static final String SESSION_ID_REQUIRED = "Session id is required";
    public static final String MESSAGE_REQUIRED = "Message is required";
    public static final String SENDER_NAME_REQUIRED = "Sender name is required";
    public static final String CHAT_TAG = "Chat API";
    public static final String CHAT_TAG_DESCRIPTION = "APIs for customer support chat";
    public static final String CHAT_STATUS_FILTER_DESCRIPTION =
            "WAITING, ACTIVE, or ENDED";

    private CommunicationMessages() {
    }
}
