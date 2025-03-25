package org.klimuz;

import org.klimuz.google.DownloadDB;
import org.klimuz.google.UploadDB;
import org.klimuz.google.InitializeAPI;

import com.google.api.services.drive.Drive;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;





import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {
    public static TableView<Equipment> tableView;
    private Boolean isOnline = true;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(Globals.icon);

        Label labelItemName = new Label();
        labelItemName.setTextFill(Color.WHITE);
        labelItemName.setStyle("-fx-font-size: 60px;");

        tableView = new TableView<>();

        TableColumn<Equipment, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Equipment, Integer> totalQuantityColumn = new TableColumn<>("Количество");
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));

        TableColumn<Equipment, Integer> inStockColumn = new TableColumn<>("Склад");
        inStockColumn.setCellValueFactory(new PropertyValueFactory<>("inStock"));

        TableColumn<Equipment, Integer> inUseColumn = new TableColumn<>("Работы");
        inUseColumn.setCellValueFactory(new PropertyValueFactory<>("inUse"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(totalQuantityColumn);
        tableView.getColumns().add(inStockColumn);
        tableView.getColumns().add(inUseColumn);
        tableView.setStyle("-fx-background-color: #76770a;");
        tableView.setItems(Globals.items);
        tableView.setRowFactory(tv -> {
            TableRow<Equipment> row = new TableRow<Equipment>() {
                @Override
                protected void updateItem(Equipment item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("-fx-background-color: #76770a;");
                    } else {
                        // Устанавливаем цвет фона и линии для каждой строки
                        setStyle("-fx-background-color: #a4a6ff; -fx-border-color: black; -fx-border-width: 1 0 1 0;");
                    }
                }
            };
            // Обработчик событий мыши
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Globals.position = row.getIndex();
                    labelItemName.setText(Globals.items.get(Globals.position).getName());
                }
            });

            return row;
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button buttonAdd = new Button("Добавить");
        Button buttonDelete = new Button("Удалить");
        Button buttonEdit = new Button("Изменить");
        Button buttonIssue = new Button("Выдать");
        Button buttonReturn = new Button("Вернуть");
        Button buttonXls = new Button("*.xls");
        Button buttonJobs = new Button("Работы");
        Button buttonInternet = new Button("Интернет");

        GridPane buttonGrid = new GridPane();
        buttonGrid.add(buttonAdd, 0, 0);
        buttonGrid.add(buttonDelete, 1, 0);
        buttonGrid.add(buttonEdit, 2, 0);
        buttonGrid.add(buttonJobs, 3, 0);
        buttonGrid.add(buttonIssue, 0, 1);
        buttonGrid.add(buttonReturn, 1, 1);
        buttonGrid.add(buttonXls, 2, 1);
        buttonGrid.add(buttonInternet, 3, 1);
        buttonGrid.setHgap(20); // Увеличение горизонтального зазора между кнопками
        buttonGrid.setVgap(20); // Увеличение вертикального зазора между кнопками
        buttonGrid.setAlignment(Pos.CENTER); // Центрирование кнопок в контейнере

        double buttonWidth = 100;
        double buttonHeight = 40;

        buttonAdd.setPrefSize(buttonWidth, buttonHeight);
        buttonDelete.setPrefSize(buttonWidth, buttonHeight);
        buttonEdit.setPrefSize(buttonWidth, buttonHeight);
        buttonIssue.setPrefSize(buttonWidth, buttonHeight);
        buttonReturn.setPrefSize(buttonWidth, buttonHeight);
        buttonXls.setPrefSize(buttonWidth, buttonHeight);
        buttonJobs.setPrefSize(buttonWidth, buttonHeight);
        buttonInternet.setPrefSize(buttonWidth, buttonHeight);

        VBox buttonContainer = new VBox(labelItemName, buttonGrid);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(50, 50, 50, 50)); // Установка зазоров
        VBox.setMargin(labelItemName, new Insets(10, 15, 200, 15));
        buttonContainer.setPrefWidth(800); // Увеличиваем ширину контейнера для кнопок
        VBox.setVgrow(buttonContainer, Priority.ALWAYS);

        buttonAdd.setOnAction(e -> {
            AddEquipment addApp = new AddEquipment();
            addApp.openAddEquipmentWindow(primaryStage);
        });

        buttonJobs.setOnAction(event -> {
            JobsWindow jobsWindow = new JobsWindow();
            jobsWindow.openJobsWindow(primaryStage);
        });

        buttonIssue.setOnAction(e -> {
            if (Globals.position < 0){
                Toast.makeText(primaryStage, "Выбери предмет выдачи!", 2000, 300, 300);
            } else {
                if (!Globals.jobs.isEmpty()) {
                    Issue issue = new Issue();
                    issue.openIssueWindow(primaryStage);
                } else {
                    Toast.makeText(primaryStage, "Создай работу!", 2000, 300, 300);
                }
            }
        });

        buttonReturn.setOnAction(event -> {
            if (Globals.position < 0){
                Toast.makeText(primaryStage, "Выбери оборудование!", 2000, 300, 300);
            } else {
                if (Globals.items.get(Globals.position).getInUse() > 0) {
                    Return returnWindow = new Return();
                    returnWindow.openReturnWindow(primaryStage);
                } else {
                    Toast.makeText(primaryStage, "Нечего возвращать!", 2000, 300, 300);
                }
            }
        });

        buttonEdit.setOnAction(event -> {
            if (Globals.position < 0){
                Toast.makeText(primaryStage, "Выбери оборудование!", 2000, 300, 300);
            } else {
                EditEquipment editEquipment = new EditEquipment();
                editEquipment.startEditWindow(primaryStage);
            }
        });

        buttonDelete.setOnAction(event -> {
            if (Globals.position < 0){
                Toast.makeText(primaryStage, "Выбери, что удалить!", 2000, 300, 300);
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Подтверждение");
                alert.setHeaderText("Внимание!");
                alert.setContentText("Действительно удалить?");
                ButtonType buttonTypeYes = new ButtonType("Да");
                ButtonType buttonTypeNo = new ButtonType("Нет");
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(Globals.icon);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == buttonTypeYes) {

                    Globals.items.remove(Globals.position);
                    DatabaseManager databaseManager = new DatabaseManager();
                    databaseManager.saveDataToDatabase();
                    MainApp.tableView.refresh();

                } else {
                    System.out.println("Пользователь выбрал 'Нет'");
                }
            }
        });

        buttonXls.setOnAction(event -> {
            XlsWindow xlsWindow = new XlsWindow();
            xlsWindow.openXlsWindow(primaryStage);
        });

        buttonInternet.setOnAction(event -> {
            Internet internet = new Internet();
            internet.openInternetWindow(primaryStage);
        });



        VBox listViewContainer = new VBox(tableView);
        listViewContainer.setPrefWidth(200);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        HBox contentContainer = new HBox(listViewContainer, buttonContainer);
        HBox.setHgrow(listViewContainer, Priority.ALWAYS);
        HBox.setHgrow(buttonContainer, Priority.ALWAYS);
        contentContainer.setSpacing(50); // Установка зазора между ListView и кнопками

        VBox mainContainer = new VBox(contentContainer);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.web("#282828"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Склад оборудования");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Растягиваем окно на весь экран

        primaryStage.setOnCloseRequest(event -> {
//            try {
//                java.io.File file = new java.io.File("inventory.db");
//                FileUploaderWithOverwrite.uploadOrReplaceFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        });

        primaryStage.show();

        if (!Globals.isInternetAvailable()){
            InternetWarning internetWarning = new InternetWarning();
            internetWarning.openInternetWarningWindow(primaryStage);
        }
    }

    public static void main(String[] args) {
        if (Globals.isInternetAvailable()) {
//            try {
//                String fileId = FileChecker.findFileByName("inventory.db");
//                FileDownloader.downloadFile(fileId, "inventory.db");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            DownloadDB.downloadDatabase();
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.loadDataFromDatabase();
            launch(args);
        } else {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.loadDataFromDatabase();
            launch(args);
        }
    }
}






