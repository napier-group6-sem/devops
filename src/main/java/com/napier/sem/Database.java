package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * File: Database.java
 * Description: Handles all database connection operations for the application, including
 *              establishing and closing MySQL connections, retry logic for connection
 *              attempts, and providing access to the active Connection instance.
 * Authors: Danylo Vanin, Stanislav Dvoryannikov, Lisa Burns, Tharun Siddharth Shyam
 * Date: 14 October 2025
 */

public class Database {
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
            System.out.println("Could not load SQL driver");
            throw new RuntimeException(e);
        }

        // just keep trying to connect a bunch of times before giving up
        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // wait a sec between attempts (literally)
                Thread.sleep(1000);
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Successfully connected");

                // small pause just for dramatic effect lol
                Thread.sleep(1000);
                return;
            } catch (SQLException sqle) {
                // connection failed, try again
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                // honestly this shouldn't ever happen but ok
                System.out.println("Thread interrupted? Should not happen.");
            }
        }

        // after all retries, just give up and throw
        throw new RuntimeException("Cannot connect to DB after " + retries + " attempts.");
    }

    // returns the connection if it's ready, otherwise throws an error
    public Connection getConnection() {
        if (conn == null)
            throw new IllegalStateException("DB not connected");
        return conn;
    }

    // cleanly closes the connection when we're done
    public void disconnect() {
        if (conn != null) {
            try { conn.close(); }
            catch (Exception e) { System.out.println("Error closing connection to database"); }
            finally { conn = null; } // just to make sure it's really closed
        }
    }
}
