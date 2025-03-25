package org.klimuz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Issue {
    private TextField fieldQuantity;
    private int selectedJobPosition = -1;
    private Equipment equipment;
    private Stage issueWindow;

    public void openIssueWindow(Stage parentStage){
        equipment = Globals.items.get(Globals.position);
        issueWindow = new Stage();
        issueWindow.getIcons().add(Globals.icon);
        issueWindow.initModality(Modality.WINDOW_MODAL);
        issueWindow.initOwner(parentStage);

        Label labelItem = new Label(equipment.getName());
        Label labelInStock = new Label(String.valueOf(equipment.getInStock()));
        Label labelInThisJobText = new Label("Уже выдано :");
        Label labelInThisJob = new Label("0");

        ComboBox<String> jobsList = new ComboBox<>(Globals.jobs);
        jobsList.setMaxWidth(Double.MAX_VALUE);  // Устанавливаем ширину на всю область
        jobsList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobPosition = newValue.intValue();
            labelInThisJob.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
        });

        Label labelQuantityToGive = new Label("Отдать :");
        fieldQuantity = new TextField();
        fieldQuantity.setMaxWidth(50);
        Button buttonGive = new Button("Выдать");
        Button buttonClose = new Button("Закрыть");
        buttonGive.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);

        buttonGive.setOnAction(e -> issueEquipment(issueWindow));
        buttonClose.setOnAction(e -> issueWindow.close());

        HBox layoutItem = new HBox(5,labelItem, labelInStock);
        layoutItem.setAlignment(Pos.CENTER);

        HBox layoutInThisJob = new HBox(5, labelInThisJobText, labelInThisJob);
        layoutInThisJob.setAlignment(Pos.CENTER);

        HBox layoutQuantity = new HBox(5, labelQuantityToGive, fieldQuantity);
        layoutQuantity.setAlignment(Pos.CENTER);
        layoutQuantity.setSpacing(10);

        // Создаем HBox для горизонтального размещения кнопок и устанавливаем выравнивание по центру
        HBox layoutButtons = new HBox(10, buttonClose, buttonGive);
        layoutButtons.setAlignment(Pos.CENTER);
        layoutButtons.setSpacing(10);

        // Устанавливаем одинаковую ширину для кнопок
        buttonGive.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);

        // Устанавливаем VBox с отступами
        VBox layout = new VBox(10,layoutItem, jobsList,layoutInThisJob, layoutQuantity, layoutButtons);
        layout.setPadding(new Insets(20));
        layout.setPrefWidth(300);  // Устанавливаем предпочтительную ширину layout

        Scene scene = new Scene(layout, 300, 200);
        issueWindow.setTitle("Выдача");
        issueWindow.setScene(scene);
        issueWindow.show();
    }

    private void issueEquipment(Stage parentStage){
        String quantityString = fieldQuantity.getText();
        if (!quantityString.isEmpty()){
            try {
                int quantityInt = Integer.parseInt(quantityString);
                if (quantityInt <= equipment.getInStock()){
                    if (selectedJobPosition < 0){
                        Toast.makeText(parentStage, "Выбери работу!", 2000, 300, 300);
                    } else {
                        equipment.updateJobsInfo(selectedJobPosition, quantityInt);
                        Globals.items.set(Globals.position, equipment);
                        issueWindow.close();
                    }
                } else {
                    int inStock = equipment.getInStock();
                    String alertString = String.format("Ошибка : Невозможно отдать больше чем %d", inStock);
                    Toast.makeText(parentStage, alertString, 2000, 300, 300);
                }
            } catch (NumberFormatException e){
                Toast.makeText(parentStage, "Ошибка : количество пиши цифрами!", 2000, 300, 300);
            }
        } else {
            Toast.makeText(parentStage, "Напиши сколько отдать!", 2000, 300, 300);
        }
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
    }
}
