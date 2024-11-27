module com.example.webapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.webapp to javafx.fxml;
    exports com.example.webapp;
}