module WebApp {
    requires com.fazecast.jSerialComm;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    opens com.example.webapp to javafx.fxml;
    exports com.example.webapp;

}