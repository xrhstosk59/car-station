package car.station;

public final class DatabaseConfig {

    private static final String DB_PATH_PROPERTY = "car.station.db.path";
    private static final String DEFAULT_DB_PATH = "data/station.db";

    private DatabaseConfig() {
    }

    public static String jdbcUrl() {
        return "jdbc:sqlite:" + System.getProperty(DB_PATH_PROPERTY, DEFAULT_DB_PATH);
    }
}
