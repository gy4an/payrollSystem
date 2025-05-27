package org.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.Nullable;


public class FirebaseUtil {


    @Nullable
    private static Firestore db;


    public static void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("src/main/java/org/example/payrollsystemdb-firebase-adminsdk-fbsvc-263b91294b.json"); // Replace with your path
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Firestore getDb() {
        if (db == null) {
            initializeFirebase(); // Ensure initialization
        }
        return db;
    }
}