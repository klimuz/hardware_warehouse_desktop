package org.klimuz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.util.*;

public class Globals {

    public static ObservableList<Equipment> items = FXCollections.observableArrayList();

    public static ArrayList<String> jobs = new ArrayList<>();

    public static int position = 0;

    public static void createJob(String name, Stage ownerStage) {
        if (!jobs.contains(name)) {
            jobs.add(name);
            for (Equipment equipment : items) {
                equipment.setJobsInfo(0);
            }
        } else {
            String alreadyContains = String.format("List already contains %s, overwrite?", name);
            Toast.makeText(ownerStage, alreadyContains, 3000, 500, 500);
        }
    }

}
