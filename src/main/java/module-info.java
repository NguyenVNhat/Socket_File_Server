module com.example.PBL4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires java.sql;

    opens com.example.PBL4 to javafx.fxml;
    exports com.example.PBL4.Server;

    opens com.example.PBL4.Server to javafx.fxml;
}