package org.klimuz;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelExporter {

    // Метод для создания xls-файла и сохранения его на рабочий стол
    public static void createExcelFile(int jobIndex) {
        try {
            // Получаем название таблицы из Globals.jobs
            String sheetName = Globals.jobs.get(jobIndex);
            // Создаём книгу и лист
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setBorderTop(BorderStyle.THICK);
            headerStyle.setBorderBottom(BorderStyle.THICK);
            headerStyle.setBorderLeft(BorderStyle.THICK);
            headerStyle.setBorderRight(BorderStyle.THICK);

            CellStyle cellStyleLeft = workbook.createCellStyle();
            cellStyleLeft.setBorderTop(BorderStyle.THIN);
            cellStyleLeft.setBorderBottom(BorderStyle.THIN);
            cellStyleLeft.setBorderLeft(BorderStyle.THICK);
            cellStyleLeft.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyleRight = workbook.createCellStyle();
            cellStyleRight.setBorderTop(BorderStyle.THIN);
            cellStyleRight.setBorderBottom(BorderStyle.THIN);
            cellStyleRight.setBorderLeft(BorderStyle.THIN);
            cellStyleRight.setBorderRight(BorderStyle.THICK);

            CellStyle cellStyleBottomLeft = workbook.createCellStyle();
            cellStyleBottomLeft.setBorderTop(BorderStyle.THIN);
            cellStyleBottomLeft.setBorderBottom(BorderStyle.THICK);
            cellStyleBottomLeft.setBorderLeft(BorderStyle.THICK);
            cellStyleBottomLeft.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyleBottomRight = workbook.createCellStyle();
            cellStyleBottomRight.setBorderTop(BorderStyle.THIN);
            cellStyleBottomRight.setBorderBottom(BorderStyle.THICK);
            cellStyleBottomRight.setBorderLeft(BorderStyle.THIN);
            cellStyleBottomRight.setBorderRight(BorderStyle.THICK);

            // Определяем путь к рабочему столу
            String fileName = String.format("%s.xls", Globals.jobs.get(jobIndex));
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + File.separator + "Desktop";

            // Формируем полный путь к файлу
            File file = new File(desktopPath, fileName);

            // Добавляем заголовок таблицы
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Предмет");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Штук");
            headerCell1.setCellStyle(headerStyle);
            headerCell2.setCellStyle(headerStyle);

            int total = 0;
            for (Equipment equipment : Globals.items){
                if (equipment.getJobsInfo(jobIndex) > 0){
                    total++;
                }
            }

            // Заполняем таблицу данными из Globals.items
            int rowIndex = 1;
            for (Equipment equipment : Globals.items) {
                if (equipment.getJobsInfo(jobIndex) > 0) {
                    Row row = sheet.createRow(rowIndex);
                    Cell nameCell = row.createCell(0);
                    nameCell.setCellValue(equipment.getName()); // "Инструмент"
                    nameCell.setCellStyle(cellStyleLeft);
                    if (rowIndex == total) {
                        nameCell.setCellStyle(cellStyleBottomLeft);
                    }
                    Cell quantityCell = row.createCell(1);
                    quantityCell.setCellValue(equipment.getJobsList().get(jobIndex)); // "Количество"
                    quantityCell.setCellStyle(cellStyleRight);
                    if (rowIndex == total){
                        quantityCell.setCellStyle(cellStyleBottomRight);
                    }
                    rowIndex++;
                }
            }

            // Автоматическая настройка ширины столбцов
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Сохранение файла
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                System.out.println("Файл " + file.getAbsolutePath() + " успешно создан.");
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

