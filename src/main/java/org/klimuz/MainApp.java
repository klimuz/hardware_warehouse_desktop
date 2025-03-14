package org.klimuz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static TableView<Equipment> tableView;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(Globals.icon);

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
//                    Toast.makeText(primaryStage, String.valueOf(Globals.position), 3000, 500, 500);
                }
            });

            return row;
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Создание 6 кнопок и расположение их в 2 ряда
        Button buttonAdd = new Button("Добавить");
        Button buttonDelete = new Button("Удалить");
        Button buttonEdit = new Button("Изменить");
        Button buttonIssue = new Button("Выдать");
        Button buttonReturn = new Button("Вернуть");
        Button buttonSave = new Button("Сохранить");
        Button buttonJobs = new Button("Работы");
        Button buttonRefresh = new Button("Обновить");

        GridPane buttonGrid = new GridPane();
        buttonGrid.add(buttonAdd, 0, 0);
        buttonGrid.add(buttonDelete, 1, 0);
        buttonGrid.add(buttonEdit, 2, 0);
        buttonGrid.add(buttonJobs, 3, 0);
        buttonGrid.add(buttonIssue, 0, 1);
        buttonGrid.add(buttonReturn, 1, 1);
        buttonGrid.add(buttonSave, 2, 1);
        buttonGrid.add(buttonRefresh, 3, 1);
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
        buttonSave.setPrefSize(buttonWidth, buttonHeight);
        buttonJobs.setPrefSize(buttonWidth, buttonHeight);
        buttonRefresh.setPrefSize(buttonWidth, buttonHeight);

        // Создание контейнера для кнопок с зазором и увеличенного размера
        VBox buttonContainer = new VBox(buttonGrid);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(50, 50, 50, 50)); // Установка зазоров
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
                Toast.makeText(primaryStage, "Выбери предмет выдачи!", 3000, 500, 500);
            } else {
                if (!Globals.jobs.isEmpty()) {
                    Issue issue = new Issue();
                    issue.openIssueWindow(primaryStage);
                } else {
                    Toast.makeText(primaryStage, "Создай работу!", 3000, 500, 500);
                }
            }
        });

        // Создание контейнера для ListView с правым ограничением
        VBox listViewContainer = new VBox(tableView);
        listViewContainer.setPrefWidth(200);
//        listViewContainer.setMaxWidth(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Создание горизонтального контейнера для ListView и кнопок
        HBox contentContainer = new HBox(listViewContainer, buttonContainer);
        HBox.setHgrow(listViewContainer, Priority.ALWAYS);
        HBox.setHgrow(buttonContainer, Priority.ALWAYS);
        contentContainer.setSpacing(50); // Установка зазора между ListView и кнопками


        // Создание основного контейнера и установка коричневого фона
        VBox mainContainer = new VBox(contentContainer);
        mainContainer.setBackground(new Background(new BackgroundFill(Color.web("#282828"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setVgrow(contentContainer, Priority.ALWAYS);

        // Создание сцены
        Scene scene = new Scene(mainContainer, 800, 600);
        primaryStage.setScene(scene);

        // Настройка основного окна приложения
        primaryStage.setTitle("Склад оборудования");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Растягиваем окно на весь экран
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (Globals.items.isEmpty()) {
            Globals.items.add(new Equipment("микрофон", 200));
            Globals.items.add(new Equipment("пульт", 10));
            Globals.items.add(new Equipment("стойка", 300));
            Globals.items.add(new Equipment("кабель", 300));
        }
        if (Globals.jobs.isEmpty()){
            Globals.jobs.add("театр");
            Globals.jobs.add("клуб");
            Globals.jobs.add("панорамка");
            Globals.jobs.add("дружба");
            for (Equipment equipment : Globals.items){
                for (int i = 0; i < Globals.jobs.size(); i++){
                    equipment.setJobsInfo(0);
                }
            }
        }
        launch(args);
    }

}





