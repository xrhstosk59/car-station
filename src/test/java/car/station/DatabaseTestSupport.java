package car.station;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class DatabaseTestSupport {

    private static final String DB_PATH_PROPERTY = "car.station.db.path";

    private DatabaseTestSupport() {
    }

    static Path createTempDatabaseCopy() throws IOException {
        Path tempDb = Files.createTempFile("car-station-test-", ".db");
        Files.copy(Path.of("data/station.db"), tempDb, StandardCopyOption.REPLACE_EXISTING);
        System.setProperty(DB_PATH_PROPERTY, tempDb.toString());
        return tempDb;
    }

    static void clearDatabaseOverride(Path tempDb) throws IOException {
        System.clearProperty(DB_PATH_PROPERTY);
        Files.deleteIfExists(tempDb);
    }

    static Connection connect() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.jdbcUrl());
    }
}
