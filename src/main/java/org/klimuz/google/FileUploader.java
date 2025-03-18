package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;

public class FileUploader {

    // Метод для загрузки файла
    public static void uploadFile(java.io.File localFile) throws IOException {
        // Получаем Drive-сервис
        Drive driveService = GoogleDriveService.getDriveService();

        // Создаём метаданные для файла
        File fileMetadata = new File();
        fileMetadata.setName(localFile.getName()); // Указываем имя файла

        // Создаём запрос на загрузку
        Drive.Files.Create request = driveService.files().create(
                fileMetadata,
                new com.google.api.client.http.FileContent("application/octet-stream", localFile)
        );

        // Выполняем запрос и выводим ID загруженного файла
        File uploadedFile = request.execute();
        System.out.println("Файл успешно загружен: " + uploadedFile.getId());
    }
}

