package com.example.webapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeedoenController {

    @FXML
    private TextField classCodeField;
    @FXML
    private Label messageLabel;
    @FXML
    public Button MeedoenKnop;

    private List<Node> elements;
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        elements = new ArrayList<>();
        elements.add(classCodeField);
        elements.add(MeedoenKnop);

        // Set focus on the first element
        Platform.runLater(() -> classCodeField.requestFocus());

        // Add event handler for Enter key on the TextField
        classCodeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                currentIndex = (currentIndex + 1) % elements.size();
                elements.get(currentIndex).requestFocus();
                event.consume(); // Prevent default behavior
            }
        });
    }

    public void Submit(ActionEvent event) throws IOException {
        String inputCode = classCodeField.getText();
        if (isClassCodeValid(inputCode)) {
            ClassCodeHolder.getInstance().setClassCode(inputCode);
            Parent root = FXMLLoader.load(getClass().getResource("Leerling-Login.fxml"));
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
            String query = "SELECT COUNT(*) FROM Class WHERE ClassCode = ?";
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

}