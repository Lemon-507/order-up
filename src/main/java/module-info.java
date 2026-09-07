module com.orderup.orderup {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.orderup.orderup to javafx.fxml;
    exports com.orderup.orderup;
}