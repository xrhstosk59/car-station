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
//import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RegisterController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField uname;
    @FXML
    TextField pass;
    @FXML
    TextField mail;
    @FXML
    TextField name;
    @FXML
    TextField lname;

    @FXML
    public void handleBack(ActionEvent event) throws URISyntaxException, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

    public void handleRegister(ActionEvent event) throws IOException {
        try {
            try (Connection connection = DriverManager.getConnection(DatabaseConfig.jdbcUrl())) {
                int userId = getNextUserId(connection);

                PreparedStatement loginStatement = connection.prepareStatement(
                        "insert into users(rowid, username, password, role) values(?,?,?,'Customer')");
                loginStatement.setInt(1, userId);
                loginStatement.setString(2, uname.getText().trim());
                loginStatement.setString(3, pass.getText().trim());
                loginStatement.execute();

                PreparedStatement statement = connection.prepareStatement(
                        "insert into user_info(user_id,name,last_name,email) values(?,?,?,?)");
                statement.setInt(1, userId);
                statement.setString(2, name.getText().trim());
                statement.setString(3, lname.getText().trim());
                statement.setString(4, mail.getText().trim());
                statement.execute();

                UserIDSingleton.getInstance().setUser(userId);
                Parent root = FXMLLoader.load(getClass().getResource("UserStart.fxml"));
                Scene scene = new Scene(root);
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getNextUserId(Connection connection) throws SQLException {
        int maxUserRowId = 0;
        int maxUserInfoId = 0;

        try (ResultSet userRows = connection.createStatement()
                .executeQuery("select coalesce(max(rowid), 0) max_id from users")) {
            if (userRows.next()) {
                maxUserRowId = userRows.getInt("max_id");
            }
        }

        try (ResultSet userInfoRows = connection.createStatement()
                .executeQuery("select coalesce(max(user_id), 0) max_id from user_info")) {
            if (userInfoRows.next()) {
                maxUserInfoId = userInfoRows.getInt("max_id");
            }
        }

        return Math.max(maxUserRowId, maxUserInfoId) + 1;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
