package org.klimuz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.klimuz.google.DownloadDB;

import java.io.IOException;

public class InternetWarning {
    public void openInternetWarningWindow(Stage parentStage){

        Stage internetWarningWindow = new Stage();
        internetWarningWindow.getIcons().add(Globals.icon);
        internetWarningWindow.initModality(Modality.WINDOW_MODAL);
        internetWarningWindow.initOwner(parentStage);

        Label labelWarning = new Label("Нет выхода в интернет, попробовать ещё раз \n или работать оффлайн?");
        labelWarning.setTextFill(Color.WHITE);

        Button buttonOffline = new Button("Оффлайн");
        Button buttonTryAgain = new Button("Перезагрузить");
        buttonOffline.setMaxWidth(Double.MAX_VALUE);
        buttonTryAgain.setMaxWidth(Double.MAX_VALUE);
        buttonOffline.setOnAction(event -> {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.loadDataFromDatabase();
            internetWarningWindow.close();
        });
        buttonTryAgain.setOnAction(event -> {
            if (Globals.isInternetAvailable()) {
                DownloadDB.downloadDatabase();
                if (Globals.isDownloaded) {
                    DatabaseManager databaseManager = new DatabaseManager();
                    databaseManager.loadDataFromDatabase();
                    internetWarningWindow.close();
                } else  {
                    Toast.makeText(internetWarningWindow, "Не загружено, попробуй ещё раз!", 2000, 300, 300);
                }
            } else {
                Toast.makeText(internetWarningWindow, "Интернет отсутствует!", 2000, 300, 300);
            }
        });

        HBox hBoxButtons = new HBox(10, buttonOffline,buttonTryAgain);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(10);

        VBox vBoxMain = new VBox(10, labelWarning, hBoxButtons);
        vBoxMain.setPadding(new Insets(10));
//        vBoxMain.setPrefWidth(400);
        vBoxMain.setBackground(new Background(new BackgroundFill(Color.web("#282828"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(vBoxMain, 400, 100);
        internetWarningWindow.setTitle("Проблема с интернетом!");
        internetWarningWindow.setScene(scene);
        internetWarningWindow.show();
    }
}
