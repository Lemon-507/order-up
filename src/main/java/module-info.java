module com.orderup.orderup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.orderup to javafx.fxml;
    exports com.orderup;
    exports com.orderup.controller;
    opens com.orderup.controller to javafx.fxml;
}
