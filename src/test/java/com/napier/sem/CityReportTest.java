package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CityReport class.
 * Ensures name(), nz(), and header/row formatting behave correctly.
 */
class CityReportTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void name_returns_expected_title() {
        assertEquals(
                "City Report",
                new CityReport().name(),
                "name() should return 'City Report'"
        );
    }

    @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
    @Test
    void nz_returns_empty_for_null_and_same_for_value() throws Exception {
        Method nz = CityReport.class.getDeclaredMethod("nz", String.class);
        nz.setAccessible(true);

        assertEquals(
                "",
                (String) nz.invoke(null, (Object) null),
                "nz(null) should return empty string"
        );

        assertEquals(
                "Paris",
                (String) nz.invoke(null, "Paris"),
                "nz(\"Paris\") should return the same non-null value"
        );
    }

    @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
    @Test
    void header_uses_expected_titles_and_separators() throws Exception {
        Method printRow = CityReport.class.getDeclaredMethod("printRow", int[].class, String[].class);
        Method printSep = CityReport.class.getDeclaredMethod("printSep", int[].class);
        printRow.setAccessible(true);
        printSep.setAccessible(true);

        String[] head = {"Name", "Country", "District", "Population"};
        int[] w = {35, 35, 20, 12};

        printRow.invoke(null, (Object) w, (Object) head);
        printSep.invoke(null, (Object) w);

        String s = out.toString();

        for (String h : head) {
            assertTrue(
                    s.contains(h),
                    "Header should contain column '" + h + "'"
            );
        }

        assertTrue(
                s.contains("-+-"),
                "Header should contain '-+-' separators between columns"
        );
    }
}
