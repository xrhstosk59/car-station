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
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FuelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField lit;
    @FXML
    private TextField price;
    @FXML
    private ToggleGroup fuelType;

    Double newLit;
    public HashMap<String, Double> currentPrices = new HashMap();
    DecimalFormat format = new DecimalFormat("0");
    Connection connection;

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource(getHomeScene()));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleLit(ActionEvent event) {
        ToggleButton selectedFuel = (ToggleButton) fuelType.getSelectedToggle();
        if (selectedFuel == null) {
            return;
        }

        try {
            newLit = (format.parse(lit.getText())).doubleValue();
            Double newPrice = newLit * currentPrices.get(selectedFuel.getId());
            price.setText(format.format(newPrice));
        } catch (ParseException ex) {

        }

    }

    @FXML
    public void handlePrice(ActionEvent event) {
        ToggleButton selectedFuel = (ToggleButton) fuelType.getSelectedToggle();
        if (selectedFuel == null) {
            return;
        }

        try {
            newLit = (format.parse(price.getText())).doubleValue() / currentPrices.get(selectedFuel.getId());
            lit.setText(format.format(newLit));
        } catch (ParseException ex) {

        }

    }

    @FXML
    public void handleFuelOrder(ActionEvent event) {
        try {
            ToggleButton selectedFuel = (ToggleButton) fuelType.getSelectedToggle();
            if (selectedFuel == null) {
                showAlert(Alert.AlertType.ERROR, "Προσοχή", "Επέλεξε τύπο καυσίμου.");
                return;
            }
            if (newLit == null || newLit <= 0) {
                showAlert(Alert.AlertType.ERROR, "Προσοχή", "Δώσε έγκυρη ποσότητα καυσίμου.");
                return;
            }

            PreparedStatement statement = connection.prepareStatement("select amount from fuel where type=?");
            statement.setString(1, selectedFuel.getId());
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Σφάλμα", "Δεν βρέθηκε ο επιλεγμένος τύπος καυσίμου.");
                return;
            }
            double oldLit = rs.getDouble("amount");
            double diff = oldLit - newLit;
            if (diff >= 0) {
                PreparedStatement update_statement = connection.prepareStatement("update fuel set amount=amount-? where type=?");
                update_statement.setDouble(1, newLit);
                update_statement.setString(2, selectedFuel.getId());
                update_statement.execute();
            } else {
                showAlert(Alert.AlertType.ERROR, "Προσοχή", "Το απόθεμα δεν είναι επαρκές.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getHomeScene() {
        int userId = UserIDSingleton.getInstance().getUser();
        if (userId <= 0) {
            return "Login.fxml";
        }

        try (Connection roleConnection = DriverManager.getConnection("jdbc:sqlite:station.db");
             PreparedStatement statement = roleConnection.prepareStatement("select role from users where rowid=?")) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (rs.getString("role")) {
                    case "Admin":
                        return "AdminStart.fxml";
                    case "Staff":
                        return "StaffStart.fxml";
                    case "Customer":
                        return "UserStart.fxml";
                    default:
                        break;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuelController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "Login.fxml";
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
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:station.db");
            Statement price_statement = connection.createStatement();
            ResultSet prices = price_statement.executeQuery("select type,retail_price from fuel");
            while (prices.next()) {
                currentPrices.put(prices.getString("type"), prices.getDouble("retail_price"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuelController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
