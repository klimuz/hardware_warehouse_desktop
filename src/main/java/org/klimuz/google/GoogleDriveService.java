package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class GoogleDriveService {

    private static Drive driveService;

    public static Drive getDriveService() throws IOException {
        if (driveService == null) {
            // Инициализация сервисных учетных данных
            ServiceAccountCredentials credentials = (ServiceAccountCredentials) ServiceAccountCredentials.fromStream(
                    Files.newInputStream(Paths.get("hardwarewarehouse-142b52cda4ef.json"))
            ).createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

            // Создание Drive-сервиса
            driveService = new Drive.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            ).setApplicationName("HardwareWarehouseApp").build();
        }
        return driveService;
    }
}



