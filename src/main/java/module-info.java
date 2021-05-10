module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires nitrite;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
}