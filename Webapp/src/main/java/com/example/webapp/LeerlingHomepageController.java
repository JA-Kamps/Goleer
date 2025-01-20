package com.example.webapp;

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

public class LeerlingHomepageController {
    @FXML
    public Button resultatenKnop;
    @FXML
    public Button homeKnop;
    @FXML
    public Button modulesKnop;
    @FXML
    public Button uitlogKnop;
    @FXML
    private Label naamLabel;
    @FXML
    private Label klasLabel;

    @FXML
    public void initialize() {
        String naam = "";
        String klasnaam = "";
        String ClassCode = ClassCodeHolder.getInstance().getClassCode();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Name, ClassName FROM Student JOIN Class ON Student.Class_idClass = Class.idClass WHERE ClassCode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ClassCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                naam = resultSet.getString("Name");
                klasnaam = resultSet.getString("ClassName");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        naamLabel.setText(naam);
        klasLabel.setText(klasnaam);
    }

    public void openResultaten(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Scorebord.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void openModules(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void openHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Homepage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void uitloggen(ActionEvent event) throws IOException {
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