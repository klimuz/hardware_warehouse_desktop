package org.klimuz.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import org.klimuz.Globals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UploadDB {
    private static final String FOLDER_ID = "1sICRM3OPxKSzJDwdx1HW1EQG73NLBScu";

    public static void uploadDatabase() {
        try {
            // Инициализация Google Drive API
            Drive driveService = InitializeAPI.getDriveService();

            // Путь к файлу inventory.db в текущей директории
            String localDatabasePath = System.getProperty("user.dir") + "/inventory.db";
            java.io.File databaseFile = new java.io.File(localDatabasePath);
            if (!databaseFile.exists()) {
                System.err.println("Файл базы данных не найден: " + localDatabasePath);
                return;
            }

            // Проверка существования файла inventory.db в Google Drive
            String existingFileId = getFileIdByNameInFolder(driveService, "inventory.db", FOLDER_ID);
            if (existingFileId != null) {
                // Удаляем файл, если он существует
                driveService.files().delete(existingFileId).execute();
                System.out.println("Старый файл inventory.db удалён с Google Drive.");
            }

            // Создание метаданных для нового файла
            File fileMetadata = new File();
            fileMetadata.setName("inventory.db");
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

            // Загрузка нового файла
            FileContent mediaContent = new FileContent("application/x-sqlite3", databaseFile);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            Globals.isUploaded = true;
            System.out.println("Файл inventory.db успешно загружен. File ID: " + uploadedFile.getId());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getFileIdByNameInFolder(Drive driveService, String fileName, String folderId) throws IOException {
        List<File> files = driveService.files().list()
                .setQ("name='" + fileName + "' and '" + folderId + "' in parents and trashed=false")
                .setFields("files(id, name)")
                .execute()
                .getFiles();

        return (files != null && !files.isEmpty()) ? files.get(0).getId() : null;
    }
}
