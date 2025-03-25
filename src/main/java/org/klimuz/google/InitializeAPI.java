package org.klimuz.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.util.Collections;

public class InitializeAPI {
    private static Drive driveService;

    public static Drive getDriveService() {
        try {
            if (driveService == null) {
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                // Загрузка credentials.json из корневой директории проекта
                FileInputStream serviceAccountStream = new FileInputStream("credentials.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                        .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

                // Настройка Google Drive API
                driveService = new Drive.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        jsonFactory,
                        new HttpCredentialsAdapter(credentials))
                        .setApplicationName("JavaFX Drive Integration")
                        .build();

                System.out.println("Google Drive API успешно инициализирован.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации Google Drive API: " + e.getMessage());
            e.printStackTrace();
        }
        return driveService;
    }
}
