package car.station;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FxmlSmokeTest {

    @BeforeAll
    static void setUpJavaFx() throws Exception {
        UserIDSingleton.getInstance().setUser(3);
        FxTestSupport.initToolkit();
    }

    @Test
    void allFxmlViewsLoad() throws Exception {
        List<String> fxmlFiles;
        try (var stream = Files.list(Path.of("src/main/resources/car/station"))) {
            fxmlFiles = stream
                    .filter(path -> path.getFileName().toString().endsWith(".fxml"))
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .collect(Collectors.toList());
        }

        for (String fxmlFile : fxmlFiles) {
            try {
                FxTestSupport.runOnFxThread(() -> loadFxml(fxmlFile));
            } catch (Throwable throwable) {
                throw new AssertionError("Failed to load FXML: " + fxmlFile, throwable);
            }
        }
    }

    private void loadFxml(String fxmlFile) throws IOException {
        URL resource = getClass().getResource("/car/station/" + fxmlFile);
        assertNotNull(resource, () -> "Missing FXML resource: " + fxmlFile);
        assertNotNull(new FXMLLoader(resource).load(), () -> "Failed to load FXML: " + fxmlFile);
    }
}
