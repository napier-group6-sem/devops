package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() { originalOut = System.out; out = new ByteArrayOutputStream(); System.setOut(new PrintStream(out)); }
    @AfterEach  void tearDown() { System.setOut(originalOut); }

    @Test void name_returns_expected_title() {
        assertEquals("City Report", new CityReport().name());
    }

    @Test void nz_returns_empty_for_null_and_same_for_value() throws Exception {
        Method nz = CityReport.class.getDeclaredMethod("nz", String.class);
        nz.setAccessible(true);
        assertEquals("", (String) nz.invoke(null, (Object) null));
        assertEquals("Paris", (String) nz.invoke(null, "Paris"));
    }

    @Test void header_uses_expected_titles_and_separators() throws Exception {
        Method printRow = CityReport.class.getDeclaredMethod("printRow", int[].class, String[].class);
        Method printSep = CityReport.class.getDeclaredMethod("printSep", int[].class);
        printRow.setAccessible(true); printSep.setAccessible(true);
        String[] head = {"Name","Country","District","Population"};
        int[] w = {35,35,20,12};
        printRow.invoke(null, (Object) w, (Object) head);
        printSep.invoke(null, (Object) w);
        String s = out.toString();
        for (String h : head) assertTrue(s.contains(h));
        assertTrue(s.contains("-+-"));
    }
}
