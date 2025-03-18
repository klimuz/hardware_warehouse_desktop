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

public class Return {
    private TextField fieldQuantity;
    private int selectedJobPosition = -1;
    private Equipment equipment;
    private Stage returnWindow;
    private Label labelInStock;
    private Label labelInThisJob;

    public void openReturnWindow(Stage parentStage){
        equipment = Globals.items.get(Globals.position);
        returnWindow = new Stage();
        returnWindow.getIcons().add(Globals.icon);
        returnWindow.initModality(Modality.WINDOW_MODAL);
        returnWindow.initOwner(parentStage);

        Label labelItem = new Label(equipment.getName());
        Label labelInStockText = new Label("На складе :");
        labelInStock = new Label(String.valueOf(equipment.getInStock()));
        labelInThisJob = new Label("0");

        ComboBox<String> comboBoxJobs = new ComboBox<>(Globals.jobs);
        comboBoxJobs.setMaxWidth(Double.MAX_VALUE);  // Устанавливаем ширину на всю область
        comboBoxJobs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobPosition = newValue.intValue();
            labelInThisJob.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
            labelInStock.setText(String.valueOf(equipment.getInStock()));
            fieldQuantity.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
        });

        Label labelQuantityToReturnText = new Label("Вернуть :");
        fieldQuantity = new TextField();
        fieldQuantity.setMaxWidth(50);
        Button buttonReturnAll = new Button("Всё");
        Button buttonReturn = new Button("Вернуть");
        Button buttonClose = new Button("Закрыть");
        buttonReturnAll.setMaxWidth(Double.MAX_VALUE);
        buttonReturn.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);

        buttonReturnAll.setOnAction(e -> returnAll(returnWindow));
        buttonReturn.setOnAction(e -> returnEquipment(returnWindow));
        buttonClose.setOnAction(e -> returnWindow.close());

        HBox hBoxItem = new HBox(5, labelItem, labelInThisJob);
        hBoxItem.setAlignment(Pos.CENTER);

        HBox hBoxReturn = new HBox(5,labelQuantityToReturnText, fieldQuantity, buttonReturnAll);
        hBoxReturn.setAlignment(Pos.CENTER);

        HBox hBoxInStock = new HBox(5, labelInStockText, labelInStock);
        hBoxInStock.setAlignment(Pos.CENTER);

        HBox hBoxButtons = new HBox(10, buttonClose, buttonReturn);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(10);

        buttonReturnAll.setMaxWidth(Double.MAX_VALUE);
        buttonReturn.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);

        VBox vBoxMain = new VBox(10, hBoxItem, comboBoxJobs, hBoxReturn, hBoxInStock, hBoxButtons);
        vBoxMain.setPadding(new Insets(20));
        vBoxMain.setPrefWidth(300);

        Scene scene = new Scene(vBoxMain, 300, 240);
        returnWindow.setTitle("Возврат");
        returnWindow.setScene(scene);
        returnWindow.show();
    }

    private void returnAll(Stage returnWindow) {
        int quantityToReturn = equipment.getJobsInfo(selectedJobPosition);
        equipment.returnToStock(selectedJobPosition, quantityToReturn);
        fieldQuantity.clear();
        labelInStock.setText(String.valueOf(equipment.getInStock()));
        labelInThisJob.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
        MainApp.tableView.refresh();
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
        returnWindow.close();
    }

    private void returnEquipment(Stage returnWindow) {
        try {
            int quantityToReturn = Integer.parseInt(fieldQuantity.getText());
            if (quantityToReturn < equipment.getJobsInfo(selectedJobPosition)) {
                equipment.returnToStock(selectedJobPosition, quantityToReturn);
                fieldQuantity.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
                labelInThisJob.setText(String.valueOf(equipment.getJobsInfo(selectedJobPosition)));
                MainApp.tableView.refresh();
            } else {
                String alertImpossible = String.format("Невозможно вернуть больше чем %d", equipment.getJobsInfo(selectedJobPosition));
                Toast.makeText(returnWindow, alertImpossible,2000, 300, 300);
            }
        } catch (NumberFormatException e){
            Toast.makeText(returnWindow, "Ошибка : Введи количество цифрами!", 2000, 300, 300);
        }
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
    }
}
