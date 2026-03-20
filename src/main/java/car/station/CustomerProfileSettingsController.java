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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CustomerProfileSettingsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField name;
    @FXML
    private TextField uname;
    @FXML
    private TextField lname;
    @FXML
    private TextField adress;
    @FXML
    private TextField mail;
    @FXML
    private TextField tk;
    @FXML
    private TextField phone;
    

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
    @FXML
    public void handleVehicle(ActionEvent event) throws URISyntaxException, IOException {
       Parent root = FXMLLoader.load(getClass().getResource("vehicles.fxml"));
       Scene scene = new Scene(root);
       ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    @FXML
    public void handlePass(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CustomerPass.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }
    @FXML
    public void handleSave(ActionEvent event) throws URISyntaxException, IOException {
        
        //System.out.println(((Stage) ((Node) event.getSource()).getScene().getWindow()).getUserData());
    }

    public void displayUserInfo(int user_id) {
        if (user_id <= 0) {
            return;
        }

        try {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:data/station.db");
                 PreparedStatement usernameStatement = connection.prepareStatement("select username from users where rowid=?");
                 PreparedStatement infoStatement = connection.prepareStatement("select * from user_info where user_id=?")) {
                usernameStatement.setInt(1, user_id);
                ResultSet userRs = usernameStatement.executeQuery();
                if (userRs.next()) {
                    uname.setText(userRs.getString("username"));
                }

                infoStatement.setInt(1, user_id);
                ResultSet rs = infoStatement.executeQuery();
                if (rs.next()) {
                    name.setText(rs.getString("name"));
                    lname.setText(rs.getString("last_name"));
                    adress.setText(rs.getString("address"));
                    tk.setText(rs.getString("tk"));
                    mail.setText(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerProfileSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       displayUserInfo(UserIDSingleton.getInstance().getUser());
    }

}
