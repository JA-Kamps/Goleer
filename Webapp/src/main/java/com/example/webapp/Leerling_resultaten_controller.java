package com.example.webapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Leerling_resultaten_controller {



    @FXML
    public Button persoonlijkeResultatenKnop;
    @FXML
    public Label moduleNaam;
    @FXML
    public Button modulesKnop;
    @FXML
    public Button resultatenKnop;
    @FXML
    public Button uitlogKnop;
    @FXML
    public Button homeKnop;
    @FXML
    public VBox modulesVBox;
    @FXML
    public Label moduleLabel;
    @FXML
    public Label cijferLabel;
    @FXML
    public Label scoreLabel1;
    @FXML
    public Label scoreLabel2;
    @FXML
    public Label scoreLabel3;
    @FXML
    public Label scoreLabel4;
    @FXML
    public Label scoreLabel5;
    @FXML
    public Label scoreLabel6;
    @FXML
    public Button spelenKnop;


    public void openVakken (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
    public void openResultaten (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-resultaten.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        String query = "SELECT Name FROM Student JOIN Module_has_Class_has_Student ON Student.idStudent = Module_has_Class_has_Student.Student_idStudent WHERE Module_has_Class_Class_idClass = ? ORDER BY SUM(Score) DESC LIMIT 6";
        String classCode = ClassCodeHolder.getInstance().getClassCode();


        String[] namen = new String[6];;
        try (Connection connection = DatabaseUtil.getConnection()) {;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, classCode);
            ResultSet resultSet = statement.executeQuery();
            int teller = 0;
            while (resultSet.next()) {
                namen[teller] = resultSet.getString("Name");
                teller++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        scoreLabel1.setText(namen[0]);
        scoreLabel2.setText(namen[1]);
        scoreLabel3.setText(namen[2]);
        scoreLabel4.setText(namen[3]);
        scoreLabel5.setText(namen[4]);
        scoreLabel6.setText(namen[5]);
    }

    public void openModules (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        initialize();
    }
    public void uitloggen (ActionEvent event) throws IOException {
        ClassCodeHolder.getInstance().ClearCode();
        StudentHolder.getInstance().ClearID();
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Meedoen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void openHome (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling - homepage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void initialize() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idModule, Name FROM Module";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int moduleId = resultSet.getInt("idModule");
                    String moduleName = resultSet.getString("Name");
                    Button moduleButton = new Button(moduleName);
                    moduleButton.setOnAction(event -> {
                        try {
                            showModuleResults(event, moduleId);
                        } catch (IOException | SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    modulesVBox.getChildren().add(moduleButton);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showModuleResults(ActionEvent event,int moduleId) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Module-Resultaat.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Score FROM Module_has_Class_has_Student WHERE Student_idStudent = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            int studentId = StudentHolder.getInstance().getStudentId();
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            int score = 0;
            if (resultSet.next()) {
                score = resultSet.getInt("Score");
            }
            cijferLabel.setText(String.valueOf(score));
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Name FROM Module WHERE idModule = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, moduleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                moduleLabel.setText(resultSet.getString("Name"));
            }
        }

    }
    public void openGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        
    }
}

