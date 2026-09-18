package com.S_Health.GenderHealthCare.modules.identity.client;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Adapter for validating Google ID tokens.
 */
@Service
public class GoogleAuthClient {
    private final String googleClientId;

    public GoogleAuthClient(@Value("${google.client.id}") String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public GoogleUser verify(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null || idToken.getPayload().getEmail() == null) {
                throw new DomainException(ErrorCode.BAD_REQUEST, IdentityMessages.GOOGLE_TOKEN_INVALID);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            return new GoogleUser(
                    payload.getEmail(),
                    (String) payload.get("name"),
                    (String) payload.get("picture"));
        } catch (DomainException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new DomainException(
                    ErrorCode.INTEGRATION_ERROR,
                    IdentityMessages.GOOGLE_LOGIN_FAILED,
                    exception);
        }
    }

    public record GoogleUser(String email, String name, String imageUrl) {
    }
}
