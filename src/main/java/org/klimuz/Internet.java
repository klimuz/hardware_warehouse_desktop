package org.klimuz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.klimuz.google.DownloadDB;
import org.klimuz.google.UploadDB;

import java.io.IOException;

public class Internet {
    public void openInternetWindow(Stage parentStage){
        Stage internetWindow = new Stage();
        internetWindow.getIcons().add(Globals.icon);
        internetWindow.initModality(Modality.WINDOW_MODAL);
        internetWindow.initOwner(parentStage);

        Button buttonCancel = new Button("Отмена");
        Button buttonSend = new Button("Отправить");
        Button buttonReceive = new Button("Получить");
        buttonCancel.setMaxWidth(Double.MAX_VALUE);
        buttonSend.setMaxWidth(Double.MAX_VALUE);
        buttonReceive.setMaxWidth(Double.MAX_VALUE);
        buttonCancel.setOnAction(event -> internetWindow.close());
        buttonSend.setOnAction(event -> sendToInternet(internetWindow));
        buttonReceive.setOnAction(event -> receiveFromInternet(internetWindow));

        HBox hBoxButtons = new HBox(5,buttonCancel, buttonSend, buttonReceive);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(10);
        hBoxButtons.setPadding(new Insets(20));
        hBoxButtons.setPrefWidth(300);

        Scene scene = new Scene(hBoxButtons, 400, 100);
        internetWindow.setTitle("Интернет");
        internetWindow.setScene(scene);
        internetWindow.show();
    }

    private void receiveFromInternet(Stage parentStage) {
        if (Globals.isInternetAvailable()){
            DownloadDB.downloadDatabase();
            if (Globals.isDownloaded) {
                DatabaseManager databaseManager = new DatabaseManager();
                databaseManager.loadDataFromDatabase();
                parentStage.close();
            } else {
                Toast.makeText(parentStage, "Не загружено! попробуй нщё раз!", 2000, 300, 300);
            }
        } else {
            Toast.makeText(parentStage, "Интернет недоступен!", 2000, 300, 300);
        }
    }

    private void sendToInternet(Stage parentStage) {
        if (Globals.isInternetAvailable()){
            UploadDB.uploadDatabase();
            if (Globals.isUploaded) {
                parentStage.close();
            } else {
                Toast.makeText(parentStage, "Не отправлено! попробуй нщё раз!", 2000, 300, 300);
            }
        } else {
            Toast.makeText(parentStage, "Интернет недоступен!", 2000, 300, 300);
        }
    }
}
