package com.napier.sem;

import java.sql.*;

public class Database {
    private Connection conn;

    public void connect(String host, int port, String dbName, String user, String pass) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName +
                "?useSSL=false&allowPublicKeyRetrieval=true";
        try {
            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(true);
            System.out.println("Connected to DB");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to DB: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        if (conn == null) throw new IllegalStateException("DB not connected");
        return conn;
    }

    public void disconnect() {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
