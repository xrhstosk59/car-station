/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
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
 *
 * @author jimman
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    public String[] getUser(String user, String password) throws URISyntaxException, IOException {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:data/station.db");
            PreparedStatement statement = connection.prepareStatement("select rowid,role from users where username=? and password=?");
            statement.setString(1, user);
            statement.setString(2, password);
            //statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String[] userRes = {rs.getString("rowid"), rs.getString("role")};
            return userRes;

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    private static void AlertMessage(Alert.AlertType atype, String title, String message) {
        Alert alert = new Alert(atype);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    public void handleRegister(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @FXML
    public void handleLogin(ActionEvent event) throws URISyntaxException, IOException {
        if (username.getText().isEmpty()) {
            AlertMessage(Alert.AlertType.ERROR, "Error", "Please enter a username!");
            return;
        }

        if (password.getText().isEmpty()) {
            AlertMessage(Alert.AlertType.ERROR, "Error", "Please enter a password!");
            return;
        }

        String[] user = getUser(username.getText(), password.getText());
        if (user == null) {
            AlertMessage(Alert.AlertType.ERROR, "Error", "Incorrect credentials!");
            return;
        }

        String role = user[1];
        UserIDSingleton UserIDInstance = UserIDSingleton.getInstance();
        if (user[0] != null) {
            UserIDInstance.setUser(Integer.parseInt(user[0]));
        }
        if (role != null) {
            String userScene = null;
            switch (role) {
                case "Admin":
                    userScene = "AdminStart.fxml";
                    break;
                case "Staff":
                    userScene = "StaffStart.fxml";
                    break;
                case "Customer":
                    userScene = "UserStart.fxml";
                    break;

            }
            Parent root = FXMLLoader.load(getClass().getResource(userScene));
            Scene scene = new Scene(root);
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
        } else {
            AlertMessage(Alert.AlertType.ERROR, "Error", "Incorrect credentials!");
            return;
        }

    }

    @FXML
    public void handleAbout(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginabout.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
