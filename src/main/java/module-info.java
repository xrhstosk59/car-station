module car.station {
    requires java.sql;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    exports car.station;
    opens car.station to javafx.fxml;
}
