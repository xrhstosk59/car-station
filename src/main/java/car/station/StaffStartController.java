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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class StaffStartController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    MenuButton bell;
    boolean onceShown = false;

    @FXML
    public void handlePart(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MecPartStaff.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleFuel(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Fuel.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleProfile(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("staffprofile.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleParking(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StaffParking.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleInfo(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StaffAbout.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleVehicle(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("vehiclesstaff.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleNotifications(MouseEvent event) {
        try {
            if (onceShown) {
                return;
            }
            bell.getItems().clear();
            Connection connection = DriverManager.getConnection("jdbc:sqlite:data/station.db");
            int user_id = UserIDSingleton.getInstance().getUser();
            PreparedStatement notificationStatement = connection.prepareStatement("select * from notifications where user_id=?");
            notificationStatement.setInt(1, user_id);
            ResultSet notifications = notificationStatement.executeQuery();
            while (notifications.next()) {
                MenuItem newMenu = new MenuItem(notifications.getString("text"));
                bell.getItems().add(newMenu);
            }
            onceShown = true;
        } catch (SQLException ex) {
            Logger.getLogger(UserStartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    public void handleLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
