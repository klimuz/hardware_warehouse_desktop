package org.klimuz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class XlsWindow {
    private int selectedJobPosition = -1;
    private Stage xlsWindow;


    public void openXlsWindow(Stage parentStage){
        xlsWindow = new Stage();
        xlsWindow.getIcons().add(Globals.icon);
        xlsWindow.initModality(Modality.WINDOW_MODAL);
        xlsWindow.initOwner(parentStage);

        ComboBox<String> comboBoxJobs = new ComboBox<>(Globals.jobs);
        comboBoxJobs.setMaxWidth(Double.MAX_VALUE);  // Устанавливаем ширину на всю область
        comboBoxJobs.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobPosition = newValue.intValue();
        });

        Button buttonCreate = new Button("Создать");
        Button buttonClose = new Button("Закрыть");
        buttonCreate.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setMaxWidth(Double.MAX_VALUE);
        buttonClose.setOnAction(event -> xlsWindow.close());
        buttonCreate.setOnAction(event -> createXls());

        HBox hBoxButtons = new HBox(10, buttonClose, buttonCreate);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(10);

        VBox vBoxMain = new VBox(10, comboBoxJobs, hBoxButtons);
        vBoxMain.setPadding(new Insets(20));
        vBoxMain.setPrefWidth(300);

        Scene scene = new Scene(vBoxMain, 300, 100);
        xlsWindow.setTitle("Создать таблицу");
        xlsWindow.setScene(scene);
        xlsWindow.show();

    }

    private void createXls() {
        if (selectedJobPosition < 0){
            Toast.makeText(xlsWindow, "Выбери работу!", 2000, 300, 300);
        } else {
            ExcelExporter.createExcelFile(selectedJobPosition);
        }
    }
}
