module lineage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fazecast.jSerialComm;

    opens com.lineage to javafx.fxml;
    exports com.lineage;
}