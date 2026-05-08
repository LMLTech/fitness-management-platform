package com.fitness.infrastructure.security;

import com.fitness.core.auth.domain.User;
import com.fitness.core.auth.port.out.ISocialAuthPort;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class GoogleAuthAdapter implements ISocialAuthPort {

    @Value("${google.client-id}")
    private String clientId;

    @Override
    public Optional<User> verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken token = verifier.verify(idToken);
            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();
                return Optional.of(User.builder()
                        .email(payload.getEmail())
                        .fullName((String) payload.get("name"))
                        .avatarUrl((String) payload.get("picture"))
                        .googleId(payload.getSubject()) // Sub chính là google_id
                        .build());
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}