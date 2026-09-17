package com.S_Health.GenderHealthCare.modules.identity.client;

import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter for retrieving the authenticated profile from Facebook Graph API.
 */
@Service
public class FacebookAuthClient {
    private static final String FACEBOOK_PROFILE_URL =
            "https://graph.facebook.com/me?fields=id,name,email,picture.type(large)";

    private final RestTemplate restTemplate;

    public FacebookAuthClient() {
        this.restTemplate = new RestTemplate();
    }

    public FacebookUser getUser(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            ResponseEntity<String> response = restTemplate.exchange(
                    FACEBOOK_PROFILE_URL,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ApiException(ErrorCode.BAD_REQUEST, IdentityMessages.FACEBOOK_TOKEN_INVALID);
            }

            JSONObject profile = new JSONObject(response.getBody());
            String email = profile.optString("email", null);
            String name = profile.optString("name", null);
            String imageUrl = profile.getJSONObject("picture")
                    .getJSONObject("data")
                    .optString("url", null);

            if (email == null || email.isBlank()) {
                throw new ApiException(ErrorCode.BAD_REQUEST, IdentityMessages.FACEBOOK_TOKEN_INVALID);
            }

            return new FacebookUser(email, name, imageUrl);
        } catch (ApiException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ApiException(
                    ErrorCode.INTEGRATION_ERROR,
                    IdentityMessages.FACEBOOK_LOGIN_FAILED,
                    exception);
        }
    }

    public record FacebookUser(String email, String name, String imageUrl) {
    }
}
