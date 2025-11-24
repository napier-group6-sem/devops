package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Handles all database connection operations for the application, including
 * establishing and closing MySQL connections, retry logic for connection
 * attempts, and providing access to the active Connection instance.
 */
public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    private Connection conn;

    // connects to the database using the given info
    public void connect(String host, int port, String dbName, String user, String pass) {

        // if host is blank, just assume "db" (probably docker or smth)
        String h = (host == null || host.isBlank()) ? "db" : host;

        // building the jdbc url manually
        String url = "jdbc:mysql://" + h + ":" + port + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true";

        try {
            // loading the mysql driver (yeah still gotta do this sometimes)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.severe("Could not load SQL driver");
            }
            throw new RuntimeException(e);
        }

        // just keep trying to connect a bunch of times before giving up
        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Connecting to database...");
            }
            try {
                // wait a sec between attempts (literally)
                Thread.sleep(1000);

                conn = DriverManager.getConnection(url, user, pass);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Successfully connected!");
                }

                // ensure conn is not null and mark as used for static analysis
                Objects.requireNonNull(conn);

                Thread.sleep(1000); // small pause
                return;
            } catch (SQLException sqle) {
                // connection failed, try again
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Failed to connect to database attempt " + i);
                    LOGGER.warning(sqle.getMessage());
                }
            } catch (InterruptedException ie) {
                // honestly this shouldn't ever happen but ok
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Thread interrupted while trying to connect to database.");
                }
                Thread.currentThread().interrupt();
            }
        }

        // after all retries, just give up and throw
        throw new RuntimeException("Cannot connect to DB after " + retries + " attempts.");
    }

    // returns the connection if it's ready, otherwise throws an error
    public Connection getConnection() {
        if (conn == null) {
            throw new IllegalStateException("DB not connected");
        }
        return conn;
    }

    // cleanly closes the connection when we're done
    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Error closing connection to database: " + e.getMessage());
                }
            } finally {
                conn = null; // just to make sure it's really closed
            }
        }
    }
}
