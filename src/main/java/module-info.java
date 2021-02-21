module lineage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.lineage to javafx.fxml;
    exports com.lineage;
}