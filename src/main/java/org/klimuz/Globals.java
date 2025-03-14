package org.klimuz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import java.util.*;

public class Globals {

    public static ObservableList<Equipment> items = FXCollections.observableArrayList();

    public static ObservableList<String> jobs = FXCollections.observableArrayList();

    public static int position = -1;

    public static Image icon = new Image(Objects.requireNonNull(Globals.class.getResourceAsStream("/images/logo600.png")));

}
