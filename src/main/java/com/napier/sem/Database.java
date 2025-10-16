package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection conn;

    public void connect(String host, int port, String dbName, String user, String pass) {

        String h = (host == null || host.isBlank()) ? "db" : host;

        String url = "jdbc:mysql://" + h + ":" + port + "/" + dbName
                + "?useSSL=false&allowPublicKeyRetrieval=true";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            throw new RuntimeException(e);
        }

        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(1000);
                conn = DriverManager.getConnection(url, user, pass);
                System.out.println("Successfully connected");

                Thread.sleep(1000);
                return;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }

        throw new RuntimeException("Cannot connect to DB after " + retries + " attempts.");
    }

    public Connection getConnection() {
        if (conn == null)
            throw new IllegalStateException("DB not connected");
        return conn;
    }

    public void disconnect() {
        if (conn != null) {
            try { conn.close(); }
            catch (Exception e) { System.out.println("Error closing connection to database"); }
            finally { conn = null; }
        }
    }
}
