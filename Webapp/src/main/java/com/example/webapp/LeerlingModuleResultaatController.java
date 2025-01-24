package com.example.webapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeerlingModuleResultaatController {
    @FXML
    public Button spelenKnop;
    @FXML
    private Label cijferLabel;
    @FXML
    private Label moduleLabel;
    @FXML
    private Label gespeeldLabel;

    @FXML
    public void initialize () {
        Platform.runLater(() -> spelenKnop.requestFocus());
    }



    public void openGame(ActionEvent event) throws IOException, SQLException {
        int moduleID = ModuleIDHolder.getInstance().getModuleID();
        int studentID = StudentHolder.getInstance().getStudentId();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Module_has_Class_has_Student.Score FROM Module_has_Class_has_Student WHERE Module_has_Class_Module_idModule = ? AND Student_idStudent = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, moduleID);
            statement.setInt(2, studentID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                gespeeldLabel.setText("Je hebt deze module al afgemaakt!");
            }
            else {
                Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
    }
    public void showModuleResults(ActionEvent event, int moduleId) throws IOException, SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Score FROM Module_has_Class_has_Student WHERE Student_idStudent = ? AND Module_has_Class_Module_idModule = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            int studentId = StudentHolder.getInstance().getStudentId();
            ModuleIDHolder.getInstance().setModuleID(moduleId);
            statement.setInt(1, studentId);
            statement.setInt(2, moduleId);
            ResultSet resultSet = statement.executeQuery();
            int score = 0;
            if (resultSet.next()) {
                score = resultSet.getInt("Score");
                cijferLabel.setText(score + "");
            }

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
    public void openResultaten (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Scorebord.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void openModules (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void openHome (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Homepage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void uitloggen (ActionEvent event) throws IOException {
        ClassCodeHolder.getInstance().ClearCode();
        ModuleIDHolder.getInstance().clearModuleID();
        StudentHolder.getInstance().ClearID();
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Meedoen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
