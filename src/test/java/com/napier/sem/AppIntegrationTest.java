package com.napier.sem;

import org.junit.jupiter.api.*;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the application.
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppIntegrationTest {

    private static Database db;

    @BeforeAll
    static void init() {

        db = new Database();
        db.connect("localhost", 3307, "world", "root", "example");
    }

    @AfterAll
    static void cleanup() {
        if (db != null) {
            db.disconnect();
        }
    }

    @Test
    @Order(1)
    void connectionIsEstablished() throws Exception {
        Connection con = db.getConnection();
        assertNotNull(con, "Connection should not be null");

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            assertTrue(rs.next(), "Should return one row");
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    @Order(2)
    void countryReportWorldRunsWithoutException() throws Exception {
        Connection con = db.getConnection();
        CountryReport report = new CountryReport();

        Method m = CountryReport.class.getDeclaredMethod("queryWorld", Connection.class);
        m.setAccessible(true);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try {
            assertDoesNotThrow(() -> {
                try {
                    m.invoke(report, con);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, "CountryReport.queryWorld should run without exceptions against real DB");

            String s = out.toString();
            assertTrue(s.contains("Code") && s.contains("Name"),
                    "Output should contain table header");
        } finally {
            System.setOut(originalOut);
        }
    }
}