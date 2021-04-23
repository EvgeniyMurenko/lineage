module lineage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fazecast.jSerialComm;
    requires javafx.swing;

    opens com.lineage.controller to javafx.fxml;
    exports com.lineage.controller;

    opens com.lineage to javafx.graphics;
    exports com.lineage;
}