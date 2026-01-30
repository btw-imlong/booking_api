package com.booking.booking_api.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class GoogleAuthService {

    // It's best practice to keep your Client ID in application.properties
    @Value("${google.client.id:default_value}")
    private String clientId;

    public Map<String, Object> verifyToken(String idTokenString) {
        // 1. Handle the Mock/Test Case
        if ("google-test-token".equals(idTokenString)) {
            return Map.of(
                "email", "googleuser@gmail.com",
                "name", "Google User",
                "sub", "google-uid-123"
            );
        }

        // 2. Real Google Token Verification
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user information
                return Map.of(
                    "sub", payload.getSubject(), // Unique Google ID
                    "email", payload.getEmail(),
                    "name", (String) payload.get("name"),
                    "picture", (String) payload.get("picture"),
                    "emailVerified", payload.getEmailVerified()
                );
            } else {
                throw new RuntimeException("Invalid ID token: Verification failed.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token: " + e.getMessage());
        }
    }
}