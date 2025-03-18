package org.klimuz.google;

import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDownloader {

    // Метод для скачивания файла
    public static void downloadFile(String fileId, String destinationPath) throws IOException {
        Drive driveService = GoogleDriveService.getDriveService();

        // Явно указываем тип OutputStream
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(destinationPath))) {
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            System.out.println("Файл успешно скачан: " + destinationPath);
        }
    }

}

