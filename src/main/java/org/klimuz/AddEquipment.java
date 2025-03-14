package org.klimuz;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class AddEquipment {

    private TextField nameField;
    private TextField quantityField;
    private Stage addEquipmentWindow;

    public void openAddEquipmentWindow(Stage parentStage) {
        // Новое окно
        addEquipmentWindow = new Stage();
        addEquipmentWindow.getIcons().add(Globals.icon);
        addEquipmentWindow.initModality(Modality.WINDOW_MODAL);
        addEquipmentWindow.initOwner(parentStage);

        // Поля ввода
        Label nameLabel = new Label("Название:");
        nameLabel.setTextFill(Color.WHITE);
        nameField = new TextField();

        Label quantityLabel = new Label("Количество:");
        quantityLabel.setTextFill(Color.WHITE);
        quantityField = new TextField();

        double buttonWidth = 100;
        double buttonHeight = 40;

        // Кнопки
        Button cancelButton = new Button("Отмена");
        cancelButton.setPrefSize(buttonWidth, buttonHeight);
        cancelButton.setOnAction(e -> addEquipmentWindow.close());

        Button okButton = new Button("Ок");
        okButton.setPrefSize(buttonWidth, buttonHeight);
        okButton.setOnAction(e -> addEquipment());

        // Размещение элементов на GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(quantityLabel, 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(cancelButton, 0, 2);
        grid.add(okButton, 1, 2);
        grid.setBackground(new Background(new BackgroundFill(Color.web("#282828"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(grid, 300, 200);
        addEquipmentWindow.setScene(scene);
        addEquipmentWindow.setTitle("Добавить");
        addEquipmentWindow.show();
    }

    private void addEquipment(){
        String name = nameField.getText();
        if (!name.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                Equipment equipment = new Equipment(name, quantity);
                if (!Globals.items.contains(equipment)) {
                    if (!Globals.jobs.isEmpty()) {
                        for (int i = 0; i < Globals.jobs.size(); i++) {
                            equipment.setJobsInfo(0);
                        }
                    }
                    Globals.items.add(equipment);
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Подтверждение");
                    alert.setHeaderText("Внимание!");
                    String alertString = String.format("Список уже содержит %s, заменить?", name);
                    alert.setContentText(alertString);

                    ButtonType buttonTypeYes = new ButtonType("Да");
                    ButtonType buttonTypeNo = new ButtonType("Нет");

                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(Globals.icon);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == buttonTypeYes) {
                        int index = getIndexByName(Globals.items, name);
                        Globals.items.set(index, equipment);
                    } else {
                        System.out.println("Пользователь выбрал 'Нет'");
                    }
                }
                addEquipmentWindow.close();

            } catch (NumberFormatException e) {
                // Обработка ошибки ввода для количества
                Toast.makeText(addEquipmentWindow, "Ошибка: введите количество цифрами!", 3000, 500, 500);
            }
        } else {
            Toast.makeText(addEquipmentWindow, "Ошибка: заполни все поля!", 3000, 500, 500);
        }
    }

    private int getIndexByName(ObservableList<Equipment> itemList, String name) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

}

