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
import java.util.ArrayList;
import java.util.List;

public class ScorebordController {
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
        String classCode = ClassCodeHolder.getInstance().getClassCode();
        int classID = -1;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idClass FROM Class WHERE ClassCode = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, classCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                classID = resultSet.getInt("idClass");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String query = "SELECT Name FROM Student JOIN Module_has_Class_has_Student ON Student.idStudent = Module_has_Class_has_Student.Student_idStudent WHERE Module_has_Class_Class_idClass = ? GROUP BY Name ORDER BY SUM(Score) DESC LIMIT 6";
        String[] namen = new String[6];
        try (Connection connection = DatabaseUtil.getConnection()) {;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, classID);
            ResultSet resultSet = statement.executeQuery();
            int teller = 0;
            while (resultSet.next()) {
                namen[teller] = resultSet.getString("Name");
                teller++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (namen[0] != null) scoreLabel1.setText(namen[0]);
        if (namen[1] != null) scoreLabel2.setText(namen[1]);
        if (namen[2] != null) scoreLabel3.setText(namen[2]);
        if (namen[3] != null) scoreLabel4.setText(namen[3]);
        if (namen[4] != null) scoreLabel5.setText(namen[4]);
        if (namen[5] != null) scoreLabel6.setText(namen[5]);
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
        ModuleIDHolder.getInstance().clearModuleID();
        StudentHolder.getInstance().ClearID();
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Meedoen.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }






}

