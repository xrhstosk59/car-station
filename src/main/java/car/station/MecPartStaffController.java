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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class MecPartStaffController implements Initializable {

    public HashMap<String, Integer> currentParts = new HashMap();
    public HashMap<String, Double> currentPrices = new HashMap();
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
        String currentText = textfield.getText().trim();
        try {
            currentParts.put(textfield.getId(), Integer.valueOf(currentText));
        } catch (NumberFormatException e) {
            currentParts.remove(textfield.getId());
        }
        Double totalPrice = 0d;
        for (Map.Entry<String, Integer> part : currentParts.entrySet()) {
            totalPrice += currentPrices.get(part.getKey());
        }
        price.setText(format.format(totalPrice));
    }

    @FXML
    public void handleOrder(ActionEvent event) throws SQLException {
        for (Map.Entry<String, Integer> part : currentParts.entrySet()) {
            PreparedStatement partsStatement = connection.prepareStatement("update parts set amount=amount-? where type=?");
            partsStatement.setInt(1, part.getValue());
            partsStatement.setString(2, part.getKey());
            partsStatement.execute();
        }
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
