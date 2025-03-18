package org.klimuz.google;

import com.google.api.services.drive.Drive;

import java.io.IOException;

public class FileDeleter {

    public static void deleteFile(String fileId) throws IOException {
        Drive driveService = GoogleDriveService.getDriveService();
        driveService.files().delete(fileId).execute();
        System.out.println("Файл с ID " + fileId + " успешно удалён.");
    }
}

