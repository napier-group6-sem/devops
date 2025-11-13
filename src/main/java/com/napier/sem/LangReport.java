
package com.napier.sem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LangReport {


    public String name() {
        return "Language Report";
    }

    // Main execution method
    public void run(Connection con) {
        System.out.println("\n=== " + name() + " ===");

        try {
            Statement stmt = con.createStatement();

            // Get total world population
            long worldPop = 0;
            ResultSet rsPop = stmt.executeQuery("SELECT SUM(population) FROM country");
            if (rsPop.next()) {
                worldPop = rsPop.getLong(1);
            }
            if (worldPop == 0) {
                System.out.println("World population not found.");
                return;
            }

            // Query speakers for required languages
            String sql = """
                    SELECT language,
                           SUM(countrylanguage.percentage * country.population / 100) AS speakers
                    FROM countrylanguage
                    JOIN country ON country.code = countrylanguage.countrycode
                    WHERE language IN ('Chinese','English','Hindi','Spanish','Arabic')
                    GROUP BY language
                    ORDER BY speakers DESC
                    """;

            ResultSet rs = stmt.executeQuery(sql);

            // Format of table
            int[] w = {15, 20, 20};
            String[] head = {"Language", "Speakers", "% of World"};

            printRow(w, head);
            printSep(w);

            while (rs.next()) {
                String lang = rs.getString("language");
                long speakers = rs.getLong("speakers");

                double percent = (speakers * 100.0) / worldPop;

                printRow(w, new String[]{
                        nz(lang),
                        String.format("%,d", speakers),
                        String.format("%.2f%%", percent)
                });
            }

            printSep(w);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void printRow(int[] w, String[] cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append(String.format("%-" + w[i] + "s", nz(cols[i])));
            if (i < w.length - 1)
                sb.append(" | ");
        }
        System.out.println(sb);
    }

    private static void printSep(int[] w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w.length; i++) {
            sb.append("-".repeat(w[i]));
            if (i < w.length - 1)
                sb.append("-+-");
        }
        System.out.println(sb);
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }
}
