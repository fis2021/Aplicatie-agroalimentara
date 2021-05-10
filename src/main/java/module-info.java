module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires nitrite;

    opens org.example to javafx.fxml;
    exports org.example;
}