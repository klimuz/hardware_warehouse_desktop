package org.klimuz;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditEquipment {
    private TextField textFieldName;
    private TextField textFieldQuantity;
    private Stage editEquipmentWindow;
    public void startEditWindow(Stage parentStage){

        editEquipmentWindow = new Stage();
        editEquipmentWindow.getIcons().add(Globals.icon);
        editEquipmentWindow.initModality(Modality.WINDOW_MODAL);
        editEquipmentWindow.initOwner(parentStage);

        Label labelName = new Label("Название:");
        labelName.setTextFill(Color.WHITE);
        textFieldName = new TextField();

        Label labelQuantity = new Label("Количество:");
        labelQuantity.setTextFill(Color.WHITE);
        textFieldQuantity = new TextField();

        double buttonWidth = 100;
        double buttonHeight = 40;
        Button buttonCancel = new Button("Отмена");
        buttonCancel.setPrefSize(buttonWidth, buttonHeight);
        buttonCancel.setOnAction(e -> editEquipmentWindow.close());

        Button buttonEdit = new Button("Изменить");
        buttonEdit.setPrefSize(buttonWidth, buttonHeight);
        buttonEdit.setOnAction(e -> editEquipment());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(labelName, 0, 0);
        grid.add(textFieldName, 1, 0);
        grid.add(labelQuantity, 0, 1);
        grid.add(textFieldQuantity, 1, 1);
        grid.add(buttonCancel, 0, 2);
        grid.add(buttonEdit, 1, 2);
        grid.setBackground(new Background(new BackgroundFill(Color.web("#282828"), CornerRadii.EMPTY, Insets.EMPTY)));

        textFieldName.setText(Globals.items.get(Globals.position).getName());
        textFieldQuantity.setText(String.valueOf(Globals.items.get(Globals.position).getTotalQuantity()));

        Scene scene = new Scene(grid, 300, 200);
        editEquipmentWindow.setScene(scene);
        editEquipmentWindow.setTitle("Изменить");
        editEquipmentWindow.show();
    }

    private void editEquipment() {
        Equipment equipment = Globals.items.get(Globals.position);
        String name = textFieldName.getText();
        if (!name.isEmpty()){
            try {
                int quantity = Integer.parseInt(textFieldQuantity.getText());
                equipment.setTotalQuantity(quantity);
                equipment.setName(name);
                MainApp.tableView.refresh();
                editEquipmentWindow.close();
            } catch (NumberFormatException e) {
                Toast.makeText(editEquipmentWindow, "Ошибка : введи количество цифрами!", 2000, 300, 300);
            }
        } else {
            Toast.makeText(editEquipmentWindow, "Ошибка : название не должно быть пустым!",2000, 300, 300);
        }
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.saveDataToDatabase();
    }
}
