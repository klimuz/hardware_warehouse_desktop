package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.klimuz.Globals;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadDB {
    private static final String FOLDER_ID = "1sICRM3OPxKSzJDwdx1HW1EQG73NLBScu";

    public static void downloadDatabase() {
        try {
            // Инициализация Google Drive API
            Drive driveService = InitializeAPI.getDriveService();

            // Путь к файлу inventory.db в текущей директории
            String localDatabasePath = System.getProperty("user.dir") + "/inventory.db";

            // Ищем файл inventory.db на Google Drive
            String fileId = getFileIdByNameInFolder(driveService, "inventory.db", FOLDER_ID);
            if (fileId == null) {
                System.err.println("Файл inventory.db не найден в папке на Google Drive.");
                return;
            }

            // Скачивание файла
            try (FileOutputStream outputStream = new FileOutputStream(localDatabasePath)) {
                driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
                Globals.isDownloaded = true;
                System.out.println("Файл inventory.db успешно скачан в " + localDatabasePath);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при скачивании базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getFileIdByNameInFolder(Drive driveService, String fileName, String folderId) throws IOException {
        FileList result = driveService.files().list()
                .setQ("name='" + fileName + "' and '" + folderId + "' in parents and trashed=false")
                .setFields("files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        return (files != null && !files.isEmpty()) ? files.get(0).getId() : null;
    }
}

