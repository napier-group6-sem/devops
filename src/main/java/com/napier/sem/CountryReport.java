package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

/**
 * File: CountryReport.java
 * Description: Provides functionality to generate various country-based population reports,
 *              including sorting and filtering by world, continent, and region. It connects
 *              to the database, executes SQL queries, and displays formatted output.
 * Authors: Danylo Vanin, Stanislav Dvoryannikov, Lisa Burns, Tharun Siddharth Shyam
 * Date: 15 October 2025
 */
public class CountryReport {

    // just returns the name of this report so the main menu can show it
    public String name() { return "Country Report"; }

    // main entry point for running the country report menu
    public void run(Connection con) {
        Scanner in = new Scanner(System.in);
        while (true) {
            // lil menu for different report options
            System.out.println("""
                \n[Country Report]
                1) All countries in the world (DESC)
                2) All countries in a continent (DESC)
                3) All countries in a region (DESC)
                4) Top N countries in the world
                5) Top N countries in a continent
                6) Top N countries in a region
                0) Back
                """);
            System.out.print("Choose: ");
            String c = in.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> queryWorld(con); // show all countries
                    case "2" -> { System.out.print("Continent: "); queryByContinent(con, in.nextLine().trim()); }
                    case "3" -> { System.out.print("Region: ");    queryByRegion(con, in.nextLine().trim()); }
                    case "4" -> { System.out.print("N: ");         queryTopWorld(con, Integer.parseInt(in.nextLine().trim())); }
                    case "5" -> {
                        // asking for both continent and number of countries
                        System.out.print("Continent: "); String cont = in.nextLine().trim();
                        System.out.print("N: ");        int n = Integer.parseInt(in.nextLine().trim());
                        queryTopContinent(con, cont, n);
                    }
                    case "6" -> {
                        // same as above but for region
                        System.out.print("Region: "); String reg = in.nextLine().trim();
                        System.out.print("N: ");      int n = Integer.parseInt(in.nextLine().trim());
                        queryTopRegion(con, reg, n);
                    }
                    case "0" -> { return; } // back to main menu
                    default -> System.out.println("Unknown option."); // user typed something weird
                }
            } catch (SQLException e) {
                // db didn't like something
                System.out.println("SQL error: " + e.getMessage());
            } catch (NumberFormatException e) {
                // user typed letters instead of a number or something
                System.out.println("Invalid number.");
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
             ResultSet rs = ps.executeQuery()) { print(rs); }
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
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
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
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
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
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
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
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
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
            try (ResultSet rs = ps.executeQuery()) { print(rs); }
        }
    }

    // prints out the results in a table-like format
    private void print(ResultSet rs) throws SQLException {
        String[] head = {"Code","Name","Continent","Region","Population","Capital"};
        int[] w = {5,44,13,26,12,30};
        printRow(w, head);
        printSep(w);
        while (rs.next()) {
            printRow(w,
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
    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = i < cells.length && cells[i] != null ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        System.out.println(sb);
    }

    // prints a line of dashes between header and data
    private static void printSep(int[] w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        System.out.println(sb);
    }

    // returns an empty string if null, so we don't print "null" everywhere
    private static String nz(String s) { return s == null ? "" : s; }
}
