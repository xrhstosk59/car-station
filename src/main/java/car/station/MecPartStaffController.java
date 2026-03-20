/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package car.station;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class MecPartStaffController implements Initializable {

    public HashMap<String, Integer> currentParts = new HashMap<>();
    public HashMap<String, Double> currentPrices = new HashMap<>();
    DecimalFormat format = new DecimalFormat("0");
    Connection connection;
    @FXML
    private TextField price;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StaffStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void addtoMecOrder(ActionEvent event) {
        TextField textfield = ((TextField) event.getSource());
        String partType = resolvePartType(textfield.getId());
        String currentText = textfield.getText().trim();

        if (!currentPrices.containsKey(partType)) {
            currentParts.remove(partType);
            updateTotalPrice();
            return;
        }

        try {
            int quantity = Integer.parseInt(currentText);
            if (quantity <= 0) {
                currentParts.remove(partType);
            } else {
                currentParts.put(partType, quantity);
            }
        } catch (NumberFormatException e) {
            currentParts.remove(partType);
        }

        updateTotalPrice();
    }

    @FXML
    public void handleOrder(ActionEvent event) throws SQLException {
        for (Map.Entry<String, Integer> part : currentParts.entrySet()) {
            try (PreparedStatement partsStatement = connection.prepareStatement(
                    "update parts set amount=amount-? where type=? and amount>=?")) {
                partsStatement.setInt(1, part.getValue());
                partsStatement.setString(2, part.getKey());
                partsStatement.setInt(3, part.getValue());
                if (partsStatement.executeUpdate() == 0) {
                    showAlert(Alert.AlertType.ERROR, "Προσοχή", "Δεν υπάρχει επαρκές απόθεμα για: " + part.getKey());
                }
            }
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0d;
        for (Map.Entry<String, Integer> part : currentParts.entrySet()) {
            Double unitPrice = currentPrices.get(part.getKey());
            if (unitPrice != null) {
                totalPrice += unitPrice * part.getValue();
            }
        }
        price.setText(totalPrice > 0 ? format.format(totalPrice) : "");
    }

    private String resolvePartType(String partId) {
        if ("Στροφαλοφόροι".equals(partId)) {
            return "Στροφαλοφόρος";
        }
        return partId;
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
            connection = DriverManager.getConnection(DatabaseConfig.jdbcUrl());
            Statement price_statement = connection.createStatement();
            ResultSet prices = price_statement.executeQuery("select type,retail_price from parts");
            while (prices.next()) {
                currentPrices.put(prices.getString("type"), prices.getDouble("retail_price"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MecPartController.class.getName()).log(Level.SEVERE, null, ex);
        }
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);
    }
}
