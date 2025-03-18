package org.klimuz.google;

import java.io.IOException;

public class FileUploaderWithOverwrite {

    public static void uploadOrReplaceFile(java.io.File localFile) throws IOException {
        String fileName = localFile.getName();

        // Шаг 1: Проверяем, существует ли файл
        String existingFileId = FileChecker.findFileByName(fileName);

        if (existingFileId != null) {
            // Шаг 2: Удаляем существующий файл
            FileDeleter.deleteFile(existingFileId);
        }

        // Шаг 3: Загружаем новый файл
        FileUploader.uploadFile(localFile);
        System.out.println("Файл " + fileName + " успешно перезаписан.");
    }
}

