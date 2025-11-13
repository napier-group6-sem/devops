package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PopulationReport class.
 * Verifies name(), nz(), and table header / separator formatting.
 */
class PopulationReportTest {
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
        assertEquals("Population Report", new PopulationReport().name());
    }

    @Test
    void nz_returns_empty_for_null_and_same_for_value() throws Exception {
        Method nz = PopulationReport.class.getDeclaredMethod("nz", String.class);
        nz.setAccessible(true);

        assertEquals("", (String) nz.invoke(null, (Object) null));
        assertEquals("Test", (String) nz.invoke(null, "Test"));
    }

    @Test
    void header_uses_expected_titles_and_separators() throws Exception {
        Method printRow = PopulationReport.class.getDeclaredMethod("printRow", int[].class, String[].class);
        Method printSep = PopulationReport.class.getDeclaredMethod("printSep", int[].class);
        printRow.setAccessible(true);
        printSep.setAccessible(true);

        String[] head = {"Name","Total Population","In Cities","% In Cities","Not In Cities","% Not In Cities"};
        int[] w = {30,18,18,12,18,14};

        printRow.invoke(null, (Object) w, (Object) head);
        printSep.invoke(null, (Object) w);

        String s = out.toString();
        for (String h : head) {
            assertTrue(s.contains(h));
        }
        assertTrue(s.contains("-+-"));
    }
}
