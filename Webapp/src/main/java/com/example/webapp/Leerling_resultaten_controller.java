package com.example.webapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Leerling_resultaten_controller {


    @FXML
    public Button rekenenKnop;
    @FXML
    public Button persoonlijkeResultatenKnop;
    @FXML
    public Button taalKnop;
    @FXML
    public void openRekenen (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Rekenen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
    public void openTaal (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Taal.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void openVakken (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Persoonlijke resultaten.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
}
