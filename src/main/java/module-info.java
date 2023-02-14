module myview.software1java {
    requires javafx.controls;
    requires javafx.fxml;


    opens myview.software1java to javafx.fxml;
    exports myview.software1java;
    exports model;
}