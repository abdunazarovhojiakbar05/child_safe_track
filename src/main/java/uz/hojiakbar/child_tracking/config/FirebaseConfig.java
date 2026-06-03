package uz.hojiakbar.child_tracking.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                String firebaseJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");

                InputStream serviceAccount;
                if (firebaseJson != null && !firebaseJson.isEmpty()) {
                     serviceAccount = new ByteArrayInputStream(firebaseJson.getBytes());
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
}