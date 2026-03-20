/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package kachowwwwww.car.station;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author User
 */
public class ParkingController implements Initializable {

    @FXML
    Label p;
    @FXML
    Label p1;
    @FXML
    Label p2;
    @FXML
    Label p3;
    @FXML
    Label p4;
    @FXML
    Label p5;
    @FXML
    Label p6;
    @FXML
    Label p7;
    @FXML
    Label p8;
    @FXML
    ComboBox vehicles;
    @FXML
    ToggleGroup parkingGroup;
    @FXML
    Label vacant;
    Connection connection;

    DecimalFormat format = new DecimalFormat("0");
    HashMap<Label, Double> labelPrices = new HashMap();

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleVehicle(ActionEvent event) {
        String currentVehicle = ((String) vehicles.getValue());
        if (currentVehicle == null) {
            return;
        }

        try {

            PreparedStatement available_statement = connection.prepareStatement("select available from parking where type=?");
            available_statement.setString(1, currentVehicle);
            ResultSet available = available_statement.executeQuery();
            if (available.next()) {
                vacant.setText(Integer.toString(available.getInt("available")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ParkingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        labelPrices.clear();
        switch (currentVehicle) {
            case ("Ι.Χ"): {
                labelPrices.put(p, 2d);
                labelPrices.put(p1, 3.5d);
                labelPrices.put(p2, 5d);
                labelPrices.put(p3, 20d);
                labelPrices.put(p4, 27d);
                labelPrices.put(p5, 30d);
                labelPrices.put(p6, 60d);
                labelPrices.put(p7, 100d);
                labelPrices.put(p8, 120d);
                break;
            }
            case ("Μοτοσικλέτα"): {
                labelPrices.put(p, 1d);
                labelPrices.put(p1, 1.75d);
                labelPrices.put(p2, 2.5d);
                labelPrices.put(p3, 10d);
                labelPrices.put(p4, 13.5d);
                labelPrices.put(p5, 15d);
                labelPrices.put(p6, 30d);
                labelPrices.put(p7, 50d);
                labelPrices.put(p8, 60d);
                break;
            }
            case ("Φορτηγό"): {
                labelPrices.put(p, 4d);
                labelPrices.put(p1, 7d);
                labelPrices.put(p2, 10d);
                labelPrices.put(p3, 40d);
                labelPrices.put(p4, 54d);
                labelPrices.put(p5, 60d);
                labelPrices.put(p6, 120d);
                labelPrices.put(p7, 200d);
                labelPrices.put(p8, 240d);
                break;
            }
        }
        for (Map.Entry<Label, Double> entry : labelPrices.entrySet()) {
            entry.getKey().setText(format.format(entry.getValue()));
        }
    }

    @FXML
    public void handleParking(ActionEvent event) {
        try {
            String currentVehicle = ((String) vehicles.getValue());
            if (currentVehicle == null) {
                showAlert(Alert.AlertType.ERROR, "Προσοχή", "Επέλεξε τύπο οχήματος.");
                return;
            }

            PreparedStatement readStatement = connection.prepareStatement("select available from parking where type=?");
            readStatement.setString(1, currentVehicle);
            ResultSet available = readStatement.executeQuery();
            if (!available.next()) {
                showAlert(Alert.AlertType.ERROR, "Σφάλμα", "Δεν βρέθηκε διαθεσιμότητα για το επιλεγμένο όχημα.");
                return;
            }

            int availableSpots = available.getInt("available");
            if (availableSpots <= 0) {
                showAlert(Alert.AlertType.ERROR, "Προσοχή", "Δεν υπάρχουν διαθέσιμες θέσεις.");
                return;
            }

            PreparedStatement available_statement = connection.prepareStatement("update parking set available=available-1 where type=?");
            available_statement.setString(1, currentVehicle);
            available_statement.execute();
            vacant.setText(Integer.toString(availableSpots - 1));
        } catch (SQLException ex) {
            Logger.getLogger(ParkingController.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:station.db");
        } catch (SQLException ex) {
            Logger.getLogger(ParkingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);
    }

}
