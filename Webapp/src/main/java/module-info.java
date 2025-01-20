module com.example.webapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.webapp to javafx.fxml;
    exports com.example.webapp;
}