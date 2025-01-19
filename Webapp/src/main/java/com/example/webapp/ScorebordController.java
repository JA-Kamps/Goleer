package com.example.webapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.*;

public class ScorebordController {



    @FXML
    public Button persoonlijkeResultatenKnop;
    @FXML
    private Label scoreLabel1;
    @FXML
    private Label scoreLabel2;
    @FXML
    private Label scoreLabel3;
    @FXML
    private Label scoreLabel4;
    @FXML
    private Label scoreLabel5;
    @FXML
    private Label scoreLabel6;

    public void initialize() {
        vulScorebord();
    }


    @FXML
    public void vulScorebord() {
        String query = "SELECT Name FROM Student JOIN Module_has_Class_has_Student ON Student.idStudent = Module_has_Class_has_Student.Student_idStudent WHERE Module_has_Class_Class_idClass = ? GROUP BY Name ORDER BY SUM(Score) DESC LIMIT 6";
        String classCode = ClassCodeHolder.getInstance().getClassCode();


        String[] namen = new String[6];;
        try (Connection connection = DatabaseUtil.getConnection()) {;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, classCode);
            ResultSet resultSet = statement.executeQuery();
            int teller = 0;
            while (resultSet.next()) {
                namen[teller] = resultSet.getString("Name");
                teller++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        scoreLabel1.setText(namen[0]);
        scoreLabel2.setText(namen[1]);
        scoreLabel3.setText(namen[2]);
        scoreLabel4.setText(namen[3]);
        scoreLabel5.setText(namen[4]);
        scoreLabel6.setText(namen[5]);
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
        StudentHolder.getInstance().ClearID();
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Meedoen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }






}

