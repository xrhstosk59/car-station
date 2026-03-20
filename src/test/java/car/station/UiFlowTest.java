package car.station;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UiFlowTest {

    private Path tempDb;

    @BeforeAll
    static void setUpToolkit() throws Exception {
        FxTestSupport.initToolkit();
    }

    @BeforeEach
    void setUpDatabase() throws Exception {
        tempDb = DatabaseTestSupport.createTempDatabaseCopy();
    }

    @AfterEach
    void tearDownDatabase() throws Exception {
        DatabaseTestSupport.clearDatabaseOverride(tempDb);
    }

    @Test
    void adminLoginNavigatesToAdminStart() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Login.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            ((TextField) loader.getNamespace().get("username")).setText("Director");
            ((PasswordField) loader.getNamespace().get("password")).setText("Diri123!");

            ((Button) loader.getNamespace().get("login")).fire();

            assertTrue(sceneContainsText(stage.getScene().getRoot(), "Διαχείριση\nΛογαριασμών"));
        });
    }

    @Test
    void staffLoginNavigatesToStaffStart() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Login.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            ((TextField) loader.getNamespace().get("username")).setText("Empy");
            ((PasswordField) loader.getNamespace().get("password")).setText("Loid456!");

            ((Button) loader.getNamespace().get("login")).fire();

            assertTrue(sceneContainsText(stage.getScene().getRoot(), "Στοιχεία\nΟχημάτων"));
        });
    }

    @Test
    void customerLoginNavigatesToUserStart() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Login.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            ((TextField) loader.getNamespace().get("username")).setText("Tomy");
            ((PasswordField) loader.getNamespace().get("password")).setText("Cuss789!");

            ((Button) loader.getNamespace().get("login")).fire();

            assertTrue(sceneContainsText(stage.getScene().getRoot(), "Πληροφορίες"));
        });
    }

    @Test
    void registerCreatesNewCustomerInDatabase() throws Throwable {
        String username = "test-user";

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("register.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            ((TextField) loader.getNamespace().get("uname")).setText(username);
            ((TextField) loader.getNamespace().get("pass")).setText("Pass123!");
            ((TextField) loader.getNamespace().get("name")).setText("Test");
            ((TextField) loader.getNamespace().get("lname")).setText("User");
            ((TextField) loader.getNamespace().get("mail")).setText("test@example.com");

            ((Button) loader.getNamespace().get("register")).fire();

            assertTrue(sceneContainsText(stage.getScene().getRoot(), "Πληροφορίες"));
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select role from users where username=?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals("Customer", rs.getString("role"));
        }
    }

    @Test
    void maintenanceAppointmentIsStoredForCurrentUser() throws Throwable {
        UserIDSingleton.getInstance().setUser(3);
        String expectedDatetime = "2030-01-15T10:30";

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Maintenance.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            ((DatePicker) loader.getNamespace().get("datePicker")).setValue(LocalDate.of(2030, 1, 15));
            ((Spinner<Integer>) loader.getNamespace().get("hourSpinner")).getValueFactory().setValue(10);
            ((Spinner<Integer>) loader.getNamespace().get("minuteSpinner")).getValueFactory().setValue(30);

            ((Button) loader.getNamespace().get("appoinment")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select count(*) count from appointments where user_id=? and datetime=?")) {
            statement.setInt(1, 3);
            statement.setString(2, expectedDatetime);
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("count"));
        }
    }

    @Test
    void customerProfileScreenLoadsUserData() throws Throwable {
        UserIDSingleton.getInstance().setUser(3);

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("CustomerProfileSettings.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            assertEquals("Tomy", ((TextField) loader.getNamespace().get("name")).getText());
            assertEquals("Tomy", ((TextField) loader.getNamespace().get("uname")).getText());
            assertEquals("MacQueen", ((TextField) loader.getNamespace().get("lname")).getText());
            assertEquals("Radiator Springs 66", ((TextField) loader.getNamespace().get("adress")).getText());
            assertEquals("12345", ((TextField) loader.getNamespace().get("tk")).getText());
            assertEquals("tomy@mcqueen.com", ((TextField) loader.getNamespace().get("mail")).getText());
            assertEquals("", ((TextField) loader.getNamespace().get("phone")).getText());
        });
    }

    @Test
    void customerNotificationsLoadOnceWithoutDuplicates() throws Throwable {
        UserIDSingleton.getInstance().setUser(3);

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("UserStart.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            UserStartController controller = loader.getController();
            MenuButton bell = (MenuButton) loader.getNamespace().get("bell");

            controller.handleNotifications(null);
            assertEquals(1, bell.getItems().size());
            assertEquals("Ληξιπρόθεσμος λογαριασμός", bell.getItems().get(0).getText());

            controller.handleNotifications(null);
            assertEquals(1, bell.getItems().size());
        });
    }

    @Test
    void parkingReservationReducesAvailableSpots() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Parking.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            ParkingController controller = loader.getController();

            ComboBox<String> vehicles = (ComboBox<String>) loader.getNamespace().get("vehicles");
            vehicles.setValue("Ι.Χ");
            controller.handleVehicle(new ActionEvent(vehicles, vehicles));

            ((Button) loader.getNamespace().get("confirm")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select available from parking where type='Ι.Χ'")) {
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(19, rs.getInt("available"));
        }
    }

    @Test
    void parkingWithoutVehicleDoesNotChangeAvailability() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Parking.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            ((Button) loader.getNamespace().get("confirm")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select available from parking where type='Ι.Χ'")) {
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(20, rs.getInt("available"));
        }
    }

    @Test
    void fuelOrderReducesFuelStock() throws Throwable {
        UserIDSingleton.getInstance().setUser(3);

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Fuel.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");
            FuelController controller = loader.getController();

            RadioButton fuel = (RadioButton) loader.getNamespace().get("amosimp");
            fuel.fire();

            TextField liters = (TextField) loader.getNamespace().get("lit");
            liters.setText("10");
            controller.handleLit(new ActionEvent(liters, liters));

            ((Button) loader.getNamespace().get("accept")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select amount from fuel where type='gas95'")) {
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(92.0, rs.getDouble("amount"));
        }
    }

    @Test
    void fuelOrderWithoutSelectionDoesNotChangeFuelStock() throws Throwable {
        UserIDSingleton.getInstance().setUser(3);

        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("Fuel.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            ((TextField) loader.getNamespace().get("lit")).setText("10");
            ((Button) loader.getNamespace().get("accept")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select amount from fuel where type='gas95'")) {
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(102.0, rs.getDouble("amount"));
        }
    }

    @Test
    void adminFuelOrderIncreasesSelectedFuelOnly() throws Throwable {
        FxTestSupport.runOnFxThread(() -> {
            Stage stage = loadStage("FuelAdmin.fxml");
            FXMLLoader loader = (FXMLLoader) stage.getProperties().get("loader");

            TextField liters = (TextField) loader.getNamespace().get("amoSimpli");
            liters.setText("10");

            ((Button) loader.getNamespace().get("accept")).fire();
        });

        try (Connection connection = DatabaseTestSupport.connect();
             PreparedStatement statement = connection.prepareStatement("select amount from fuel where type='gas95'")) {
            ResultSet rs = statement.executeQuery();
            assertTrue(rs.next());
            assertEquals(112.0, rs.getDouble("amount"));
        }
    }

    private Stage loadStage(String fxmlFile) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/car/station/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.getProperties().put("loader", loader);
        return stage;
    }

    private boolean sceneContainsText(Node node, String text) {
        assertNotNull(node);

        if (node instanceof Labeled) {
            String currentText = ((Labeled) node).getText();
            if (text.equals(currentText)) {
                return true;
            }
        }

        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                if (sceneContainsText(child, text)) {
                    return true;
                }
            }
        }

        return false;
    }
}
