package com.example.webapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Leerling-Meedoen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the controller instance
            MeedoenController controller = loader.getController();


            root.requestFocus();

            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}