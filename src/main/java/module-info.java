module com.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.cincuentazo to javafx.fxml;
    opens com.cincuentazo.controller to javafx.fxml;
    exports com.cincuentazo;
    exports com.cincuentazo.controller;
}