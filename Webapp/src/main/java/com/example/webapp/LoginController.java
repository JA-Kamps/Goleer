package com.example.webapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {


    @FXML
    public Button loginKnop;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label messageLabel2;



    public void leerlingInloggen (ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String classCode = ClassCodeHolder.getInstance().getClassCode();
        if (isLoginValid(username, password, classCode)) {
            Parent root = FXMLLoader.load(getClass().getResource("Leerling-Homepage.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            messageLabel2.setText("Ongeldige gebruikersnaam of wachtwoord. Probeer het opnieuw.");
        }
    }

    private boolean isLoginValid(String username, String password, String classCode) {
        boolean isValid = false;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idStudent FROM Student JOIN Class ON Student.Class_idClass = Class.idClass WHERE Name = ? AND Password = ? AND ClassCode = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, classCode);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int studentId = resultSet.getInt("idStudent");
                        StudentHolder.getInstance().setStudentId(studentId);
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}









