package com.S_Health.GenderHealthCare.integrations.zoom;


import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;
import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class ZoomOAuthService {

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.account-id}")
    private String accountId;

    public String getAccessToken() {
        try {
        // Bước 1 - Tạo Basic Auth header
        String credentials = clientId + ":" + clientSecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + basicAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Bước 2 - Tạo URL gọi Zoom API
        String url = "https://zoom.us/oauth/token"
                + "?grant_type=account_credentials"
                + "&account_id=" + accountId;

        HttpEntity<String> request = new HttpEntity<>("", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("access_token")) {
            throw new ApiException(ErrorCode.INTEGRATION_ERROR, IntegrationMessages.ZOOM_TOKEN_NOT_FOUND);
        }

        String token = (String) body.get("access_token");
        return token;
        } catch (ApiException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ApiException(
                    ErrorCode.INTEGRATION_ERROR,
                    IntegrationMessages.ZOOM_TOKEN_REQUEST_FAILED,
                    exception);
        }
    }

    public static class ZoomTokenResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }

        public void setAccessToken(String access_token) {
            this.access_token = access_token;
        }
    }
}
