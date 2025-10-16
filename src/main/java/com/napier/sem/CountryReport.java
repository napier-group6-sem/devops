package com.napier.sem;

import java.sql.*;
import java.util.Scanner;

public class CountryReport {

    public String name() { return "Country Report"; }

    public void run(Connection con) {
        Scanner in = new Scanner(System.in);
        while (true) {
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
                    case "1" -> queryWorld(con);
                    case "2" -> { System.out.print("Continent: "); queryByContinent(con, in.nextLine().trim()); }
                    case "3" -> { System.out.print("Region: ");    queryByRegion(con, in.nextLine().trim()); }
                    case "4" -> { System.out.print("N: ");         queryTopWorld(con, Integer.parseInt(in.nextLine().trim())); }
                    case "5" -> {
                        System.out.print("Continent: "); String cont = in.nextLine().trim();
                        System.out.print("N: ");        int n = Integer.parseInt(in.nextLine().trim());
                        queryTopContinent(con, cont, n);
                    }
                    case "6" -> {
                        System.out.print("Region: "); String reg = in.nextLine().trim();
                        System.out.print("N: ");      int n = Integer.parseInt(in.nextLine().trim());
                        queryTopRegion(con, reg, n);
                    }
                    case "0" -> { return; }
                    default -> System.out.println("Unknown option.");
                }
            } catch (SQLException e) {
                System.out.println("SQL error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private void queryWorld(Connection con) throws SQLException {
        String sql = """
            SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital
            FROM country c LEFT JOIN city cap ON c.Capital = cap.ID
            ORDER BY c.Population DESC
        """;
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { print(rs); }
    }

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

    private static void printRow(int[] w, String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            String c = i < cells.length && cells[i] != null ? cells[i] : "";
            sb.append(String.format("%-" + w[i] + "s", c));
            if (i < w.length - 1) sb.append(" | ");
        }
        System.out.println(sb);
    }

    private static void printSep(int[] w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1) sb.append("-+-");
        }
        System.out.println(sb);
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
