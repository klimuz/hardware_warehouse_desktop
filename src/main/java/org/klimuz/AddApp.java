package org.klimuz;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class AddApp {
    public void openAddWindow(Stage parentStage) {
        // Новое окно
        Stage addWindow = new Stage();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo600.png")));
        addWindow.getIcons().add(icon);
        addWindow.initModality(Modality.WINDOW_MODAL);
        addWindow.initOwner(parentStage);

        // Поля ввода
        Label nameLabel = new Label("Название:");
        nameLabel.setTextFill(Color.WHITE);
        TextField nameField = new TextField();

        Label quantityLabel = new Label("Количество:");
        quantityLabel.setTextFill(Color.WHITE);
        TextField quantityField = new TextField();

        double buttonWidth = 100;
        double buttonHeight = 40;

        // Кнопки
        Button cancelButton = new Button("Отмена");
        cancelButton.setPrefSize(buttonWidth, buttonHeight);
        cancelButton.setOnAction(e -> addWindow.close());

        Button okButton = new Button("Ок");
        okButton.setPrefSize(buttonWidth, buttonHeight);
        okButton.setOnAction(e -> {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                int quantity = 0;
                String quantityString = quantityField.getText();
                if (!quantityString.isEmpty()){
                    quantity = Integer.parseInt(quantityField.getText());

                    Equipment equipment = new Equipment(name, quantity);
                    Globals.items.add(equipment);

//                    MainApp.refresh();


                    addWindow.close();
                } else {
                    // Обработка ошибки ввода для количества
                    Toast.makeText(parentStage, "Ошибка: введите количество!", 3000, 500, 500);
                }
            } else {
                Toast.makeText(parentStage, "Ошибка: заполни все поля!", 3000, 500, 500);
            }
        });

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
        addWindow.setScene(scene);
        addWindow.setTitle("Добавить");
        addWindow.show();
    }
}

