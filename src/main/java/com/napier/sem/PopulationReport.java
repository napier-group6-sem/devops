package com.napier.sem;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * PopulationReport produces various population-based summaries for the world database.
 * It can show, for each continent / region / country, how many people live in cities
 * vs not in cities (with percentages), and it can also show the total population of
 * a given area (world, continent, region, country, district, city).
 */
@SuppressWarnings("PMD.GuardLogStatement")
public class PopulationReport {

    private static final Logger LOGGER = Logger.getLogger(PopulationReport.class.getName());
    private static final Logger TABLE = Logger.getLogger("PopulationReport.Table");

    public String name() {
        return "Population Report";
    }

    public void run(Connection con) {
        Scanner in = new Scanner(System.in);

        while (true) {
            LOGGER.info("""
                
                [Population Report]
                1) Population by continent (cities vs non-cities)
                2) Population by region    (cities vs non-cities)
                3) Population by country   (cities vs non-cities)
                4) Population of the world
                5) Population of a continent
                6) Population of a region
                7) Population of a country
                8) Population of a district
                9) Population of a city
                0) Back
                """);

            LOGGER.info("Choose: ");
            String c = in.nextLine().trim();

            try {
                switch (c) {
                    case "1" -> popByContinent(con);
                    case "2" -> popByRegion(con);
                    case "3" -> popByCountry(con);
                    case "4" -> popWorld(con);
                    case "5" -> {
                        LOGGER.info("Continent: ");
                        popContinent(con, in.nextLine().trim());
                    }
                    case "6" -> {
                        LOGGER.info("Region: ");
                        popRegion(con, in.nextLine().trim());
                    }
                    case "7" -> {
                        LOGGER.info("Country: ");
                        popCountry(con, in.nextLine().trim());
                    }
                    case "8" -> {
                        LOGGER.info("District: ");
                        popDistrict(con, in.nextLine().trim());
                    }
                    case "9" -> {
                        LOGGER.info("City: ");
                        popCity(con, in.nextLine().trim());
                    }
                    case "0" -> { return; }
                    default -> LOGGER.warning("Unknown option.");
                }
            } catch (SQLException e) {
                LOGGER.severe("SQL error: " + e.getMessage());
            }
        }
    }

    // population by continent
    private void popByContinent(Connection con) throws SQLException {
        String sql = """
            SELECT
                c.Continent AS Name,
                SUM(c.Population) AS TotalPopulation,
                SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation,
                SUM(c.Population) - SUM(COALESCE(cp.CityPopulation, 0)) AS NonCityPopulation
            FROM country c
            LEFT JOIN (
                SELECT CountryCode, SUM(Population) AS CityPopulation
                FROM city
                GROUP BY CountryCode
            ) cp ON c.Code = cp.CountryCode
            GROUP BY c.Continent
            ORDER BY TotalPopulation DESC
            """;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            printCityVsNonCityTable(rs);
        }
    }

    // population by region
    private void popByRegion(Connection con) throws SQLException {
        String sql = """
            SELECT
                c.Region AS Name,
                SUM(c.Population) AS TotalPopulation,
                SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation,
                SUM(c.Population) - SUM(COALESCE(cp.CityPopulation, 0)) AS NonCityPopulation
            FROM country c
            LEFT JOIN (
                SELECT CountryCode, SUM(Population) AS CityPopulation
                FROM city
                GROUP BY CountryCode
            ) cp ON c.Code = cp.CountryCode
            GROUP BY c.Region
            ORDER BY TotalPopulation DESC
            """;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            printCityVsNonCityTable(rs);
        }
    }

    // population by country
    private void popByCountry(Connection con) throws SQLException {
        String sql = """
            SELECT
                c.Name AS Name,
                c.Population AS TotalPopulation,
                COALESCE(cp.CityPopulation, 0) AS CityPopulation,
                c.Population - COALESCE(cp.CityPopulation, 0) AS NonCityPopulation
            FROM country c
            LEFT JOIN (
                SELECT CountryCode, SUM(Population) AS CityPopulation
                FROM city
                GROUP BY CountryCode
            ) cp ON c.Code = cp.CountryCode
            ORDER BY TotalPopulation DESC
            """;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            printCityVsNonCityTable(rs);
        }
    }

    private void printCityVsNonCityTable(ResultSet rs) throws SQLException {
        String[] head = {"Name","Total Population","In Cities","% In Cities","Not In Cities","% Not In Cities"};
        int[] w = {30,18,18,12,18,14};

        printRow(w, head);
        printSep(w);

        while (rs.next()) {
            String name = nz(rs.getString("Name"));
            long total  = rs.getLong("TotalPopulation");
            long inCity = rs.getLong("CityPopulation");
            long nonCity = rs.getLong("NonCityPopulation");

            double inPct  = total == 0 ? 0.0 : (inCity  * 100.0) / total;
            double outPct = total == 0 ? 0.0 : (nonCity * 100.0) / total;

            printRow(w,
                    name,
                    String.format("%,d", total),
                    String.format("%,d", inCity),
                    String.format("%.2f%%", inPct),
                    String.format("%,d", nonCity),
                    String.format("%.2f%%", outPct)
            );
        }
    }

    private void popWorld(Connection con) throws SQLException {
        String sql = "SELECT SUM(Population) AS Population FROM country";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                long pop = rs.getLong("Population");
                LOGGER.info(String.format("World population: %,d", pop));
            }
        }
    }

    private void popContinent(Connection con, String continent) throws SQLException {
        String sql = "SELECT SUM(Population) AS Population FROM country WHERE Continent = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, continent);
            try (ResultSet rs = stmt.executeQuery()) {
                printSingle("continent", continent, rs);
            }
        }
    }

    private void popRegion(Connection con, String region) throws SQLException {
        String sql = "SELECT SUM(Population) AS Population FROM country WHERE Region = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, region);
            try (ResultSet rs = stmt.executeQuery()) {
                printSingle("region", region, rs);
            }
        }
    }

    private void popCountry(Connection con, String country) throws SQLException {
        String sql = "SELECT Population AS Population FROM country WHERE Name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, country);
            try (ResultSet rs = stmt.executeQuery()) {
                printSingle("country", country, rs);
            }
        }
    }

    private void popDistrict(Connection con, String district) throws SQLException {
        String sql = "SELECT SUM(Population) AS Population FROM city WHERE District = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, district);
            try (ResultSet rs = stmt.executeQuery()) {
                printSingle("district", district, rs);
            }
        }
    }

    private void popCity(Connection con, String city) throws SQLException {
        String sql = "SELECT SUM(Population) AS Population FROM city WHERE Name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                printSingle("city", city, rs);
            }
        }
    }

    // prints a simple "Population of X" message (or "not found")
    private void printSingle(String kind, String name, ResultSet rs) throws SQLException {
        if (rs.next() && rs.getLong("Population") > 0) {
            long pop = rs.getLong("Population");
            LOGGER.info(String.format("Population of %s %s: %,d", kind, name, pop));
        } else {
            LOGGER.warning(String.format("No population data found for %s: %s", kind, name));
        }
    }

    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = (i < cells.length && cells[i] != null) ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        TABLE.info(sb.toString());
    }

    private static void printSep(int... w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        TABLE.info(sb.toString());
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}
