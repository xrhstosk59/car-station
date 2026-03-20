module car.station {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    exports car.station;
    opens car.station to javafx.fxml;
}
