module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires nitrite;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens org.example to javafx.fxml;
    opens org.example.model to javafx.fxml;
    opens org.example.customer to javafx.fxml;
    opens org.example.admin to javafx.fxml;
    opens org.example.store to javafx.fxml;
    exports org.example;
    exports org.example.model;
    exports org.example.customer;
    exports org.example.admin;
    exports org.example.store;
}