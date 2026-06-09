package uz.hojiakbar.child_tracking.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                String firebaseBase64 = System.getenv("FIREBASE_SERVICE_ACCOUNT");

                InputStream serviceAccount;
                if (firebaseBase64 != null && !firebaseBase64.isEmpty()) {
                    byte[] decodedBytes = Base64.getDecoder().decode(firebaseBase64.trim());
                    serviceAccount = new ByteArrayInputStream(decodedBytes);
                } else {
                    serviceAccount = new FileInputStream(
                            "src/main/resources/firebase-service-account.json"
                    );
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Firebase config xato: " + e.getMessage());
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) { // ← firebaseApp inject
        return FirebaseMessaging.getInstance(firebaseApp); // ← faqat shu yetarli
    }
}
