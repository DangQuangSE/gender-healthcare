package com.S_Health.GenderHealthCare.integrations.zoom;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.time.Duration;

/**
 * Small adapter responsible only for communication with the Zoom API.
 */
@Service
public class ZoomMeetingClient {
    private static final String ZOOM_MEETING_URL = "https://api.zoom.us/v2/users/me/meetings";
    private static final String ZOOM_TIME_ZONE = "Asia/Ho_Chi_Minh";
    private static final int MEETING_DURATION_MINUTES = 90;

    private final ZoomOAuthService zoomOAuthService;
    private final RestTemplate restTemplate;

    public ZoomMeetingClient(
            ZoomOAuthService zoomOAuthService,
            RestTemplateBuilder restTemplateBuilder) {
        this.zoomOAuthService = zoomOAuthService;
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(15))
                .build();
    }

    public Map<String, String> createMeeting(String topic, String startTime) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(zoomOAuthService.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("topic", topic);
            body.put("type", 2);
            body.put("start_time", startTime);
            body.put("duration", MEETING_DURATION_MINUTES);
            body.put("timezone", ZOOM_TIME_ZONE);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    ZOOM_MEETING_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    new ParameterizedTypeReference<>() {
                    });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null
                    || responseBody.get("join_url") == null
                    || responseBody.get("start_url") == null) {
                throw new DomainException(
                        ErrorCode.INTEGRATION_ERROR,
                        IntegrationMessages.ZOOM_MEETING_CREATE_FAILED);
            }

            Map<String, String> meetingLinks = new HashMap<>();
            meetingLinks.put("join_url", responseBody.get("join_url").toString());
            meetingLinks.put("start_url", responseBody.get("start_url").toString());
            return meetingLinks;
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new DomainException(
                    ErrorCode.INTEGRATION_ERROR,
                    IntegrationMessages.ZOOM_MEETING_REQUEST_FAILED,
                    exception);
        }
    }
}
