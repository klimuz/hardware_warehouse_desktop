package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;

public class FileChecker {

    // Метод для поиска файла по имени
    public static String findFileByName(String fileName) throws IOException {
        // Получаем экземпляр Google Drive API
        Drive driveService = GoogleDriveService.getDriveService();

        // Выполняем запрос на поиск файла
        FileList result = driveService.files().list()
                .setQ("name = '" + fileName + "' and trashed = false") // Условие: имя файла и не удалён
                .setFields("files(id, name)") // Указываем, какие поля нам нужны
                .execute();

        // Проверяем результат
        if (!result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId(); // Возвращаем ID первого найденного файла
        }
        return null; // Если файл не найден, возвращаем null
    }
}

