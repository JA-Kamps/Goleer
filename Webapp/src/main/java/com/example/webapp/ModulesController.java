package com.example.webapp;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModulesController {
    @FXML
    private VBox modulesVBox;
    @FXML
    public Button resultatenKnop;
    @FXML
    public Button modulesKnop;
    @FXML
    public Button homeKnop;
    @FXML
    public Button uitlogKnop;

    private List<Node> elements;
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        elements = new ArrayList<>();
        String classCode = ClassCodeHolder.getInstance().getClassCode();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT DISTINCT idModule, Name " +
                    "FROM Module_has_Class " +
                    "JOIN Module ON Module_has_Class.Module_idModule = Module.idModule" +
                    " JOIN Class ON Module_has_Class.Class_idClass " +
                    "WHERE ClassCode = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, classCode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int moduleId = resultSet.getInt("idModule");
                String moduleName = resultSet.getString("Name");
                Button moduleButton = new Button(moduleName);
                moduleButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Leerling-Module-Resultaat.fxml"));
                        Parent root = loader.load();
                        LeerlingModuleResultaatController controller = loader.getController();
                        controller.showModuleResults(event, moduleId);

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                modulesVBox.getChildren().add(moduleButton);
                elements.add(moduleButton); // Add module button to the elements list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add side buttons to the elements list
        elements.add(resultatenKnop);
        elements.add(modulesKnop);
        elements.add(homeKnop);
        elements.add(uitlogKnop);

        // Set focus on the first element after the stage is shown
        Platform.runLater(() -> {
            if (!elements.isEmpty()) {
                elements.get(currentIndex).requestFocus();
            }
        });

        Platform.runLater(() -> {
            Scene scene = modulesVBox.getScene();
            if (scene != null) {
                scene.setOnKeyPressed(this::handleKeyPress);
            }
        });

    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN:
                currentIndex = (currentIndex + 1) % elements.size();
                elements.get(currentIndex).requestFocus();
                break;
            case UP:
                currentIndex = (currentIndex - 1 + elements.size()) % elements.size();
                elements.get(currentIndex).requestFocus();
                break;
            default:
                break;
        }
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