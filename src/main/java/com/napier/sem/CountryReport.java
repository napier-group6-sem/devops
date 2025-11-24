package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Description: Provides functionality to generate various country-based population reports,
 *              including sorting and filtering by world, continent, and region. It connects
 *              to the database, executes SQL queries, and displays formatted output.
 */
@SuppressWarnings("PMD.GuardLogStatement")
public class CountryReport {

    private static final Logger LOGGER = Logger.getLogger(CountryReport.class.getName());

    // just returns the name of this report so the main menu can show it
    public String name() {
        return "Country Report";
    }

    // main entry point for running the country report menu
    public void run(Connection con) {
        Scanner in = new Scanner(System.in);
        while (true) {
            LOGGER.info("""
                
                [Country Report]
                1) All countries in the world (DESC)
                2) All countries in a continent (DESC)
                3) All countries in a region (DESC)
                4) Top N countries in the world
                5) Top N countries in a continent
                6) Top N countries in a region
                0) Back
                """);

            LOGGER.info("Choose: ");
            String c = in.nextLine().trim();

            try {
                switch (c) {
                    case "1" -> queryWorld(con);
                    case "2" -> {
                        LOGGER.info("Continent: ");
                        queryByContinent(con, in.nextLine().trim());
                    }
                    case "3" -> {
                        LOGGER.info("Region: ");
                        queryByRegion(con, in.nextLine().trim());
                    }
                    case "4" -> {
                        LOGGER.info("N: ");
                        queryTopWorld(con, Integer.parseInt(in.nextLine().trim()));
                    }
                    case "5" -> {
                        LOGGER.info("Continent: ");
                        String cont = in.nextLine().trim();
                        LOGGER.info("N: ");
                        int n = Integer.parseInt(in.nextLine().trim());
                        queryTopContinent(con, cont, n);
                    }
                    case "6" -> {
                        LOGGER.info("Region: ");
                        String reg = in.nextLine().trim();
                        LOGGER.info("N: ");
                        int n = Integer.parseInt(in.nextLine().trim());
                        queryTopRegion(con, reg, n);
                    }
                    case "0" -> {
                        return;
                    }
                    default -> LOGGER.warning("Unknown option.");
                }
            } catch (SQLException e) {
                LOGGER.severe("SQL error: " + e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid number.");
            }
        }
    }

    // gets all countries sorted by population
    private void queryWorld(Connection con) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            ORDER BY c.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            print(rs);
        }
    }

    // same thing but filtered by continent
    private void queryByContinent(Connection con, String continent) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            WHERE c.Continent = ?
            ORDER BY c.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, continent);
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    // again, same but for region
    private void queryByRegion(Connection con, String region) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            WHERE c.Region = ?
            ORDER BY c.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, region);
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    // gets top N countries in the whole world
    private void queryTopWorld(Connection con, int n) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            ORDER BY c.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    // top N countries in a specific continent
    private void queryTopContinent(Connection con, String continent, int n) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            WHERE c.Continent = ?
            ORDER BY c.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, continent);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    // top N countries in a specific region
    private void queryTopRegion(Connection con, String region, int n) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            WHERE c.Region = ?
            ORDER BY c.Population DESC LIMIT ?
        """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, region);
            ps.setInt(2, n);
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    // prints out the results in a table-like format
    private void print(ResultSet rs) throws SQLException {
        String[] head = {"Code", "Name", "Continent", "Region", "Population", "Capital"};
        int[] w = {5, 44, 13, 26, 12, 30};
        printRow(w, head);
        printSep(w);
        while (rs.next()) {
            printRow(
                    w,
                    nz(rs.getString("Code")),
                    nz(rs.getString("Name")),
                    nz(rs.getString("Continent")),
                    nz(rs.getString("Region")),
                    String.valueOf(rs.getLong("Population")),
                    nz(rs.getString("Capital"))
            );
        }
    }

    // helper that prints a row nicely aligned
    private static final Logger TABLE_LOGGER = Logger.getLogger("CountryReport.Table");

    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = (i < cells.length && cells[i] != null) ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        TABLE_LOGGER.info(sb.toString());
    }

    // prints a line of dashes between header and data
    private static void printSep(int... w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        TABLE_LOGGER.info(sb.toString());
    }

    private static String nz(String s) {
        return (s == null) ? "" : s;
    }
}
