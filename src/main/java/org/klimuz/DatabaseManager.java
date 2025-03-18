package org.klimuz;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private final String currentDir = System.getProperty("user.dir");
    private final String databaseUrl = "jdbc:sqlite:" + currentDir + "/inventory.db";

    public DatabaseManager() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl);
             Statement statement = connection.createStatement()) {
            String createJobsTable = "CREATE TABLE IF NOT EXISTS jobs (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";
            String createEquipmentTable = "CREATE TABLE IF NOT EXISTS equipment (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, totalQuantity INTEGER NOT NULL, jobsList TEXT NOT NULL)";
            statement.execute(createJobsTable);
            statement.execute(createEquipmentTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDataToDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM jobs");
                statement.executeUpdate("DELETE FROM equipment");
            }

            String insertJobQuery = "INSERT INTO jobs (name) VALUES (?)";
            try (PreparedStatement jobStatement = connection.prepareStatement(insertJobQuery)) {
                for (String job : Globals.jobs) {
                    jobStatement.setString(1, job);
                    jobStatement.executeUpdate();
                }
            }

            String insertEquipmentQuery = "INSERT INTO equipment (name, totalQuantity, jobsList) VALUES (?, ?, ?)";
            try (PreparedStatement equipmentStatement = connection.prepareStatement(insertEquipmentQuery)) {
                for (Equipment equipment : Globals.items) {
                    equipmentStatement.setString(1, equipment.getName());
                    equipmentStatement.setInt(2, equipment.getTotalQuantity());
                    equipmentStatement.setString(3, equipment.getJobsList().toString());
                    equipmentStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            Globals.jobs.clear();
            Globals.items.clear();

            String selectJobsQuery = "SELECT name FROM jobs";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectJobsQuery)) {
                while (resultSet.next()) {
                    Globals.jobs.add(resultSet.getString("name"));
                }
            }

            String selectEquipmentQuery = "SELECT name, totalQuantity, jobsList FROM equipment";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectEquipmentQuery)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int totalQuantity = resultSet.getInt("totalQuantity");
                    String jobsListString = resultSet.getString("jobsList");

                    ArrayList<Integer> jobsList = new ArrayList<>();
                    for (String job : jobsListString.replace("[", "").replace("]", "").split(",\\s*")) { // Разделитель с учётом пробелов
                        try {
                            // Удаляем пробелы и преобразуем только корректные числа
                            if (job != null && !job.isEmpty()) {
                                jobsList.add(Integer.parseInt(job.trim()));
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Некорректное значение при преобразовании: " + job);
                        }
                    }
                    Equipment equipment = new Equipment(name, totalQuantity);
                    equipment.setJobsList(jobsList);
                    Globals.items.add(equipment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


