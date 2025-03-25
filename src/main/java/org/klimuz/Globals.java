package org.klimuz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Globals {

    public static ObservableList<Equipment> items = FXCollections.observableArrayList();
    public static ObservableList<String> jobs = FXCollections.observableArrayList();

    public static Boolean isDownloaded = false;
    public static Boolean isUploaded = false;

    public static int position = -1;

    public static Image icon = new Image(Objects.requireNonNull(Globals.class.getResourceAsStream("/images/logo600.png")));

    public static boolean isInternetAvailable() {
        try (Socket socket = new Socket()) {
            String host = "8.8.8.8"; // Google DNS
            int port = 53; // DNS порт
            int timeout = 2000; // Таймаут в миллисекундах
            socket.connect(new InetSocketAddress(host, port), timeout);
            return true; // Если соединение успешно, значит интернет доступен
        } catch (IOException e) {
            return false; // Если произошла ошибка, интернета нет
        }
    }

}
