package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CapitalCityReportTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() { originalOut = System.out; out = new ByteArrayOutputStream(); System.setOut(new PrintStream(out)); }
    @AfterEach  void tearDown() { System.setOut(originalOut); }

    @Test void name_returns_expected_title() {
        assertEquals("Capital City Report", new CapitalCityReport().name());
    }

    @Test void nz_returns_empty_for_null_and_same_for_value() throws Exception {
        Method nz = CapitalCityReport.class.getDeclaredMethod("nz", String.class);
        nz.setAccessible(true);
        assertEquals("", (String) nz.invoke(null, (Object) null));
        assertEquals("Madrid", (String) nz.invoke(null, "Madrid"));
    }

    @Test void header_uses_expected_titles_and_separators() throws Exception {
        Method printRow = CapitalCityReport.class.getDeclaredMethod("printRow", int[].class, String[].class);
        Method printSep = CapitalCityReport.class.getDeclaredMethod("printSep", int[].class);
        printRow.setAccessible(true); printSep.setAccessible(true);
        String[] head = {"Capital","Country","Continent","Region","Population"};
        int[] w = {30,30,15,25,12};
        printRow.invoke(null, (Object) w, (Object) head);
        printSep.invoke(null, (Object) w);
        String s = out.toString();
        for (String h : head) assertTrue(s.contains(h));
        assertTrue(s.contains("-+-"));
    }
}

