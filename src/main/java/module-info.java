module com.orderup.orderup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.orderup to javafx.fxml;
    exports com.orderup;
    exports com.orderup.controller;
    opens com.orderup.controller to javafx.fxml;
    exports com.orderup.view;
    opens com.orderup.view to javafx.fxml;
}
