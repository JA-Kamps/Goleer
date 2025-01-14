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
import java.util.Objects;

public class Leerling_Controller {


    @FXML
    public Button loginKnop;
    @FXML
    public Button MeedoenKnop;
    @FXML
    public Button wifiKnop;
    @FXML
    public Button verbindenKnop;
    @FXML
    public TextField classCodeField;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    public Label messageLabel2;



    public void leerlingInloggen (ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (isLoginValid(username, password)) {
            Parent root = FXMLLoader.load(getClass().getResource("Leerling - homepage.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            messageLabel2.setText("Ongeldige gebruikersnaam of wachtwoord. Probeer het opnieuw.");
        }
    }

    private boolean isLoginValid(String username, String password) {
        boolean isValid = false;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT COUNT(*) FROM Gebruikers WHERE gebruikersnaam = ? AND wachtwoord = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public void Submit (ActionEvent event) throws IOException {
        String inputCode = classCodeField.getText();
        if (isClassCodeValid(inputCode)) {
            Parent root = FXMLLoader.load(getClass().getResource("Leerling login.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            messageLabel.setText("Ongeldige klassencode. Probeer het opnieuw.");
        }
    }

    private boolean isClassCodeValid(String inputCode) {
        boolean isValid = false;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT COUNT(*) FROM Klassen WHERE code = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, inputCode);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public void openNetwerken (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("wifi-netwerken.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void verbind (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Meedoen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}









