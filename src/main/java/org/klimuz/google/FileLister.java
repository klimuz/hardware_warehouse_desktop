package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;

public class FileLister {

    // Метод для получения всех файлов в Google Drive
    public static void listAllFiles() throws IOException {
        Drive driveService = GoogleDriveService.getDriveService();

        // Выполнение запроса на получение списка файлов
        FileList result = driveService.files().list()
                .setFields("files(id, name, mimeType, size)")
                .execute();

        // Вывод всех найденных файлов
        if (result.getFiles().isEmpty()) {
            System.out.println("Файлы не найдены.");
        } else {
            for (File file : result.getFiles()) {
                System.out.println("Имя: " + file.getName());
                System.out.println("ID: " + file.getId());
                System.out.println("Тип: " + file.getMimeType());
                System.out.println("Размер: " + file.getSize() + " байт");
                System.out.println("-------------");
            }
        }
    }
}

