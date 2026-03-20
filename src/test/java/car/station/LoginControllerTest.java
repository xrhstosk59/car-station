package car.station;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LoginControllerTest {

    @Test
    void returnsAdminUserForKnownCredentials() throws Exception {
        LoginController controller = new LoginController();

        String[] user = controller.getUser("Director", "Diri123!");

        assertArrayEquals(new String[]{"1", "Admin"}, user);
    }

    @Test
    void returnsNullForInvalidCredentials() throws Exception {
        LoginController controller = new LoginController();

        String[] user = controller.getUser("nobody", "wrong-password");

        assertNull(user);
    }
}
