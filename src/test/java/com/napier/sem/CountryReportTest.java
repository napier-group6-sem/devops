package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CountryReportTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setup() { originalOut = System.out; out = new ByteArrayOutputStream(); System.setOut(new PrintStream(out)); }

    @AfterEach
    void tearDown() { System.setOut(originalOut); }

    @Test
    void name_returns_expected_title() {
        assertEquals("Country Report", new CountryReport().name());
    }

    @Test
    void nz_returns_empty_for_null_and_same_for_value() throws Exception {
        Method nz = CountryReport.class.getDeclaredMethod("nz", String.class);
        nz.setAccessible(true);
        assertEquals("", (String) nz.invoke(null, (Object) null));
        assertEquals("Paris", (String) nz.invoke(null, "Paris"));
    }

    @Test
    void header_uses_expected_titles_and_separators() throws Exception {
        Method printRow = CountryReport.class.getDeclaredMethod("printRow", int[].class, String[].class);
        Method printSep = CountryReport.class.getDeclaredMethod("printSep", int[].class);
        printRow.setAccessible(true); printSep.setAccessible(true);
        String[] head = {"Code","Name","Continent","Region","Population","Capital"};
        int[] w = {5,44,13,26,12,30};
        printRow.invoke(null, (Object) w, (Object) head);
        printSep.invoke(null, (Object) w);
        String s = out.toString();
        for (String h : head) assertTrue(s.contains(h));
        assertTrue(s.contains("-+-"));
    }
}
