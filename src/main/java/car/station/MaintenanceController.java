/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package car.station;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class MaintenanceController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleAppointment(ActionEvent event) throws SQLException {
        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Προσοχή", "Επέλεξε ημερομηνία ραντεβού.");
            return;
        }

        int userId = UserIDSingleton.getInstance().getUser();
        if (userId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Προσοχή", "Δεν βρέθηκε ενεργός χρήστης.");
            return;
        }

        LocalDate selectedDate = datePicker.getValue();
        LocalDateTime selectedDatetime = selectedDate.atTime(hourSpinner.getValue(), minuteSpinner.getValue());

        try (Connection connection = DriverManager.getConnection(DatabaseConfig.jdbcUrl());
             PreparedStatement appointmentStatement = connection.prepareStatement("insert into appointments values (?,?)")) {
            appointmentStatement.setInt(1, userId);
            appointmentStatement.setString(2, selectedDatetime.toString());
            appointmentStatement.execute();
            showAlert(Alert.AlertType.INFORMATION, "Ολοκληρώθηκε", "Το ραντεβού καταχωρήθηκε.");
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Σφάλμα", "Δεν ήταν δυνατή η καταχώρηση του ραντεβού.");
            throw ex;
        }

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
