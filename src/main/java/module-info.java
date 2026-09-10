module com.orderup.orderup {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires static lombok;

    exports com.orderup;
    exports com.orderup.controller;
    exports com.orderup.view;
    opens com.orderup.view to javafx.fxml;
}
