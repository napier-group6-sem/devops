package com.napier.sem;

import java.io.PrintStream;
import java.sql.*;
import java.util.Scanner;

/**
 * CityReport generates various reports about cities from the world database.
 * It supports filtering by continent, region, country, district, and top-N queries.
 */
public class CityReport {

    public String name() { return "City Report"; }

    public void run(Connection con) {
        Scanner in = new Scanner(System.in);
        while (true) {
            out().println("""
                \n[City Report]
                1) All cities in the world (DESC)
                2) All cities in a continent (DESC)
                3) All cities in a region (DESC)
                4) All cities in a country (DESC)
                5) All cities in a district (DESC)
                6) Top N cities in the world
                7) Top N cities in a continent
                8) Top N cities in a region
                9) Top N cities in a country
                10) Top N cities in a district
                0) Back
                """);
            out().print("Choose: ");
            String c = in.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> queryWorld(con);
                    case "2" -> { out().print("Continent: "); queryByContinent(con, in.nextLine().trim()); }
                    case "3" -> { out().print("Region: ");    queryByRegion(con, in.nextLine().trim()); }
                    case "4" -> { out().print("Country: ");   queryByCountry(con, in.nextLine().trim()); }
                    case "5" -> { out().print("District: ");  queryByDistrict(con, in.nextLine().trim()); }
                    case "6" -> { out().print("N: ");         queryTopWorld(con, Integer.parseInt(in.nextLine().trim())); }
                    case "7" -> {
                        out().print("Continent: "); String cont = in.nextLine().trim();
                        out().print("N: ");         int n = Integer.parseInt(in.nextLine().trim());
                        queryTopContinent(con, cont, n);
                    }
                    case "8" -> {
                        out().print("Region: "); String reg = in.nextLine().trim();
                        out().print("N: ");       int n = Integer.parseInt(in.nextLine().trim());
                        queryTopRegion(con, reg, n);
                    }
                    case "9" -> {
                        out().print("Country: "); String country = in.nextLine().trim();
                        out().print("N: ");       int n = Integer.parseInt(in.nextLine().trim());
                        queryTopCountry(con, country, n);
                    }
                    case "10" -> {
                        out().print("District: "); String dist = in.nextLine().trim();
                        out().print("N: ");        int n = Integer.parseInt(in.nextLine().trim());
                        queryTopDistrict(con, dist, n);
                    }
                    case "0" -> { return; }
                    default -> out().println("Unknown option.");
                }
            } catch (SQLException e) {
                out().println("SQL error: " + e.getMessage());
            } catch (NumberFormatException e) {
                out().println("Invalid number.");
            }
        }
    }

    private void queryWorld(Connection con) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            ORDER BY ci.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { print(rs); }
    }

    private void queryByContinent(Connection con, String continent) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Continent = ?
            ORDER BY ci.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, continent);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryByRegion(Connection con, String region) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Region = ?
            ORDER BY ci.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, region);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryByCountry(Connection con, String country) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Name = ?
            ORDER BY ci.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, country);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryByDistrict(Connection con, String district) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE ci.District = ?
            ORDER BY ci.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, district);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryTopWorld(Connection con, int n) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            ORDER BY ci.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryTopContinent(Connection con, String continent, int n) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Continent = ?
            ORDER BY ci.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, continent);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryTopRegion(Connection con, String region, int n) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Region = ?
            ORDER BY ci.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, region);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryTopCountry(Connection con, String country, int n) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE co.Name = ?
            ORDER BY ci.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, country);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    private void queryTopDistrict(Connection con, String district, int n) throws SQLException {
        String sql = """
            SELECT ci.Name, co.Name AS Country, ci.District, ci.Population
            FROM city ci
            JOIN country co ON ci.CountryCode = co.Code
            WHERE ci.District = ?
            ORDER BY ci.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, district);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    // --- Output Formatting ---
    private void print(ResultSet rs) throws SQLException {
        String[] head = {"Name","Country","District","Population"};
        int[] w = {35,35,20,12};
        printRow(w, head);
        printSep(w);
        while (rs.next()) {
            printRow(w,
                    nz(rs.getString("Name")),
                    nz(rs.getString("Country")),
                    nz(rs.getString("District")),
                    String.valueOf(rs.getLong("Population"))
            );
        }
    }

    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = i < cells.length && cells[i] != null ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        out().println(sb);
    }

    private static void printSep(int... w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        out().println(sb);
    }

    private static String nz(String s) { return s == null ? "" : s; }

    private static PrintStream out() {
        return System.out;
    }
}
