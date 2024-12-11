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

public class Leerling_Controller {


    @FXML
    public Button loginKnop;
    @FXML
    public Button MeedoenKnop;



    public void leerlingInloggen (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling login.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }
    public void Submit (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling - homepage.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}