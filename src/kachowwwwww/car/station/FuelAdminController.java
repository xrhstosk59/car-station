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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FuelAdminController implements Initializable {

    @FXML
    TextField petq;
    @FXML
    TextField amoSimpq;
    @FXML
    TextField amoq;
    @FXML
    TextField superq;
    @FXML
    private TextField lit;
    @FXML
    private TextField price;
    @FXML
    private ToggleGroup fuelType;
    @FXML
    private TextField amoSimpli;
    @FXML
    private TextField amolit;
    @FXML
    private TextField petlit;
    @FXML
    private TextField superlit;
    @FXML
    private TextField amoprice;
    @FXML
    private TextField amoSimprice;
    @FXML
    private TextField petprice;
    @FXML
    private TextField superprice;
    Double newLit;
    Connection connection;

    /**
     * Initializes the controller class.
     */
    public HashMap<String, Double> currentPrices = new HashMap();
    DecimalFormat format = new DecimalFormat("0");

    @FXML
    public void handleLit(ActionEvent event) {
        try {
            TextField current_textbox = (TextField) event.getSource();
            newLit = format.parse(current_textbox.getText()).doubleValue();
            Double newPrice = newLit * currentPrices.get(current_textbox.getId());
            ((TextField) (((HBox) current_textbox.getParent())).getChildren().get(2)).setText(format.format(newPrice));
        } catch (ParseException ex) {

        }
    }

    @FXML
    public void handlePrice(ActionEvent event) {
        try {
            TextField current_textbox = (TextField) event.getSource();
            newLit = (format.parse(current_textbox.getText())).doubleValue() / currentPrices.get(current_textbox.getId());
            ((TextField) (((HBox) current_textbox.getParent())).getChildren().get(1)).setText(format.format(newLit));
        } catch (ParseException ex) {

        }
    }

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AdminStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    public void handleFuelOrder(ActionEvent event) {
        try {
            TextField[] litFields = {
                petlit,
                amoSimpli,
                amolit,
                superlit};
            TextField[] amountTextFields = {petq, amoSimpq, amoq, superq};
            PreparedStatement statement = connection.prepareStatement("update fuel set amount=amount+? where type=?");
            for (int i = 0; i < litFields.length; i++) {
                String text = litFields[i].getText();
                double oldLit = format.parse(amountTextFields[i].getText()).doubleValue();
                double addedLit = format.parse(text).doubleValue();
                if (oldLit + addedLit >= 400) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Προσοχή");
                    alert.setContentText("Προσοχή,ξεπεράστηκε η χωρητικότητα δεξαμενής");
                    alert.show();
                    continue;
                }
                if (!text.trim().equals("")) {
                    statement.setDouble(1, addedLit);
                    statement.setString(2, litFields[i].getId());
                    statement.execute();
                    amountTextFields[i].setText(format.format(oldLit + addedLit));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FuelAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void handleClear(ActionEvent event) throws SQLException {
        TextField[] allFields = {
            amoSimpli,
            amolit,
            petlit,
            superlit,
            amoprice,
            amoSimprice,
            petprice,
            superprice};
        for (TextField field : allFields) {
            field.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:station.db");
            PreparedStatement statement = connection.prepareStatement("select * from fuel");
            ResultSet rs = statement.executeQuery();
            TextField[] amountTextFields = {petq, amoSimpq, amoq, superq};
            while (rs.next()) {
                currentPrices.put(rs.getString("type"), rs.getDouble("wholesale_price"));
                TextField currentTextField = amountTextFields[rs.getRow() - 1];
                currentTextField.setText(format.format(rs.getInt("amount")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuelAdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
