package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private static final String DB_URL = "jdbc:sqlite:villa_booking.db";
    private static Connection connection;

    private DB() {}

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC"); // ensure driver loaded
                connection = DriverManager.getConnection(DB_URL);
                initializeSchema(connection);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.");
        }

        return connection;
    }

    private static void initializeSchema(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            // Table: villas
            stmt.execute("CREATE TABLE IF NOT EXISTS villas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "address TEXT NOT NULL" +
                    ")");

            // Table: room_types
            stmt.execute("CREATE TABLE IF NOT EXISTS room_types (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "villa INTEGER NOT NULL," +
                    "name TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL DEFAULT 0," +
                    "capacity INTEGER NOT NULL DEFAULT 0," +
                    "price INTEGER NOT NULL DEFAULT 0," +
                    "bed_size TEXT NOT NULL," +
                    "has_desk INTEGER DEFAULT 0," +
                    "has_ac INTEGER DEFAULT 0," +
                    "has_tv INTEGER DEFAULT 0," +
                    "has_wifi INTEGER DEFAULT 0," +
                    "has_shower INTEGER DEFAULT 0," +
                    "has_hotwater INTEGER DEFAULT 0," +
                    "has_fridge INTEGER DEFAULT 0," +
                    "FOREIGN KEY (villa) REFERENCES villas(id)" +
                    ")");

            System.out.println("✅ Database initialized.");

        } catch (SQLException e) {
            System.err.println("❌ Failed to initialize schema: " + e.getMessage());
        }
    }
}
