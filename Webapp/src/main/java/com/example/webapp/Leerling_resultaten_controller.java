package com.example.webapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class Leerling_resultaten_controller {



    @FXML
    public Button persoonlijkeResultatenKnop;
    @FXML
    public Button rekenenKnop;
    @FXML
    public Label moduleNaam;
    @FXML
    public Label moduleNummer;
    @FXML
    public Button afgelopenModulesKnop;
    @FXML
    public Button modulesKnop;
    @FXML
    public Button resultatenKnop;
    @FXML
    public Button uitlogKnop;
    @FXML
    public Button homeKnop;

    public void openModuleResultaten (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("module resultaten leerliong.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void openRekenen (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Module-Resultaat.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


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
    }
    public void openModules (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void uitloggen (ActionEvent event) throws IOException {
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
}
