package org.klimuz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class JobsWindow {

    private TextField inputField;
    private int selectedJobPosition = -1;

    public void openJobsWindow(Stage parentStage) {
        Stage jobsWindow = new Stage();
        jobsWindow.getIcons().add(Globals.icon);
        jobsWindow.initModality(Modality.WINDOW_MODAL);
        jobsWindow.initOwner(parentStage);

        ComboBox<String> jobsList = new ComboBox<>(Globals.jobs);
        jobsList.setMaxWidth(Double.MAX_VALUE);  // Устанавливаем ширину на всю область
        jobsList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobPosition = newValue.intValue();
        });

        inputField = new TextField();
        Button buttonAdd = new Button("Добавить");
        Button buttonDelete = new Button("Удалить");
        Button buttonClose = new Button("Закрыть");

        buttonAdd.setOnAction(e -> addJob(jobsWindow));
        buttonDelete.setOnAction(e -> deleteJob(jobsWindow));
        buttonClose.setOnAction(e -> jobsWindow.close());

        // Создаем HBox для горизонтального размещения кнопок и устанавливаем выравнивание по центру
        HBox buttonLayout = new HBox(10, buttonClose, buttonAdd, buttonDelete);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setSpacing(10);

        // Устанавливаем одинаковую ширину для кнопок
        buttonAdd.setMaxWidth(Double.MAX_VALUE);
        buttonDelete.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);

        // Устанавливаем VBox с отступами
        VBox layout = new VBox(10, jobsList, inputField, buttonLayout);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(300);  // Устанавливаем предпочтительную ширину layout

        Scene scene = new Scene(layout, 300, 150);
        jobsWindow.setTitle("Работы");
        jobsWindow.setScene(scene);
        jobsWindow.show();
    }

    private void addJob(Stage parentStage) {
        String newJob = inputField.getText();
        if (!newJob.isEmpty()) {
            if (!Globals.jobs.contains(newJob)){
                Globals.jobs.add(newJob);
                for (Equipment equipment : Globals.items){
                    equipment.setJobsInfo(0);
                }
            } else {
                Toast.makeText(parentStage, String.format("%s уже существует!", newJob), 3000, 500, 500);
            }
            inputField.clear();
        } else {
            Toast.makeText(parentStage, "Напиши название!", 2000, 300, 300);
        }
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
    }

    private void deleteJob(Stage parentStage) {
        if (selectedJobPosition > -1) {
            int counter = 0;
            for (Equipment equipment : Globals.items){
                counter += equipment.getJobsInfo(selectedJobPosition);
            }
            if (counter == 0){
                Globals.jobs.remove(selectedJobPosition);
                for (Equipment equipment : Globals.items){
                    equipment.removeJob(selectedJobPosition);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтверждение");
                alert.setHeaderText("Внимание!");
                alert.setContentText("С этой работы вернули не всё, удалить в любом случае?");
                ButtonType buttonTypeYes = new ButtonType("Да");
                ButtonType buttonTypeNo = new ButtonType("Нет");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(Globals.icon);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {
                    for (Equipment equipment : Globals.items){
                        int usedInJob = equipment.getJobsInfo(selectedJobPosition);
                        equipment.returnToStock(selectedJobPosition, usedInJob);
                        equipment.removeJob(selectedJobPosition);
                        MainApp.tableView.refresh();
                    }
                    Globals.jobs.remove(selectedJobPosition);
                }
            }
        } else {
            Toast.makeText(parentStage, "Выбери работу!", 3000, 500, 500);
        }
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
    }
}




