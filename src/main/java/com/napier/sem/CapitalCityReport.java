package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * The CapitalCityReport class generates reports on capital cities.
 * It allows users to view and compare capital cities by population,
 * grouped by world, continent, or region, and to view the top N capitals.
 */
@SuppressWarnings("PMD.GuardLogStatement")
public class CapitalCityReport {

    private static final Logger LOGGER = Logger.getLogger(CapitalCityReport.class.getName());

    public String name() {
        return "Capital City Report";
    }

    public void run(Connection con) {
        Scanner in = new Scanner(System.in);

        while (true) {
            LOGGER.info("""
                \n[Capital City Report]
                1) All capital cities in the world (largest → smallest)
                2) All capital cities in a continent (largest → smallest)
                3) All capital cities in a region (largest → smallest)
                4) Top N capital cities in the world
                5) Top N capital cities in a continent
                6) Top N capital cities in a region
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

    private void queryWorld(Connection con) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            ORDER BY ci.Population DESC
        """;
        executeQuery(con, sql);
    }

    private void queryByContinent(Connection con, String continent) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            WHERE c.Continent = ?
            ORDER BY ci.Population DESC
        """;
        executeQuery(con, sql, continent);
    }

    private void queryByRegion(Connection con, String region) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            WHERE c.Region = ?
            ORDER BY ci.Population DESC
        """;
        executeQuery(con, sql, region);
    }

    private void queryTopWorld(Connection con, int n) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            ORDER BY ci.Population DESC
            LIMIT ?
        """;
        executeQuery(con, sql, n);
    }

    private void queryTopContinent(Connection con, String continent, int n) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            WHERE c.Continent = ?
            ORDER BY ci.Population DESC
            LIMIT ?
        """;
        executeQuery(con, sql, continent, n);
    }

    private void queryTopRegion(Connection con, String region, int n) throws SQLException {
        String sql = """
            SELECT ci.Name AS Capital, c.Name AS Country, c.Continent, c.Region, ci.Population
            FROM city ci
            JOIN country c ON ci.ID = c.Capital
            WHERE c.Region = ?
            ORDER BY ci.Population DESC
            LIMIT ?
        """;
        executeQuery(con, sql, region, n);
    }

    private void executeQuery(Connection con, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String str) {
                    ps.setString(i + 1, str);
                } else if (params[i] instanceof Integer num) {
                    ps.setInt(i + 1, num);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                print(rs);
            }
        }
    }

    private void print(ResultSet rs) throws SQLException {
        String[] head = {"Capital", "Country", "Continent", "Region", "Population"};
        int[] w = {30, 30, 15, 25, 12};
        printRow(w, head);
        printSep(w);

        while (rs.next()) {
            printRow(w,
                    nz(rs.getString("Capital")),
                    nz(rs.getString("Country")),
                    nz(rs.getString("Continent")),
                    nz(rs.getString("Region")),
                    String.valueOf(rs.getLong("Population"))
            );
        }
    }

    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = (i < cells.length && cells[i] != null) ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        LOGGER.info(sb.toString());
    }

    private static void printSep(int... w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        LOGGER.info(sb.toString());
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}
