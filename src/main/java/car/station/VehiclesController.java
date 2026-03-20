/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package car.station;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class VehiclesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    public void handlePass(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CustomerPass.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleProfile(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CustomerProfileSettings.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserStart.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    @FXML
    public void handlePayment(ActionEvent event) throws URISyntaxException, IOException {
       Parent root = FXMLLoader.load(getClass().getResource("accountcuss.fxml"));
       Scene scene = new Scene(root);
       ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
