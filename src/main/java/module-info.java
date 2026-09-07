module com.orderup.orderup {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.orderup to javafx.fxml;
    exports com.orderup;
}