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
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class StaffParkingController implements Initializable {
     @FXML
    Label vacant;
    @FXML
    Label taken;
    @FXML
    ComboBox<String> vehicles;

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StaffStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
     @FXML
    public void handleVehicle(ActionEvent event) throws URISyntaxException, IOException {
        try {
            String currentVehicle = ((String) vehicles.getValue());
            Connection connection = DriverManager.getConnection(DatabaseConfig.jdbcUrl());
            PreparedStatement available_statement = connection.prepareStatement("select available,total-available taken from parking where type=?");
            available_statement.setString(1,currentVehicle);
            ResultSet available = available_statement.executeQuery();
            available.next();
            vacant.setText(Integer.toString(available.getInt("available")));
            taken.setText(Integer.toString(available.getInt("taken")));
        } catch (SQLException ex) {
            Logger.getLogger(AdParkingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
