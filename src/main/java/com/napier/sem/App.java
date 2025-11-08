package com.napier.sem;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class App {

    private static final AtomicLong LAST_ACTIVITY = new AtomicLong(System.currentTimeMillis());

    public static void main(String[] args) {
        String host = getenvOr("DB_HOST", "localhost");
        int port    = Integer.parseInt(getenvOr("DB_PORT", "3307"));
        String name = getenvOr("DB_NAME", "world");
        String user = getenvOr("DB_USER", "root");
        String pass = getenvOr("DB_PASS", "example");

        Database db = new Database();
        db.connect(host, port, name, user, pass);

        // Idle timeout watchdog
        int idleSec = Integer.parseInt(getenvOr("APP_IDLE_SECONDS", "30"));
        Thread watchdog = new Thread(() -> {
            while (true) {
                long idle = System.currentTimeMillis() - LAST_ACTIVITY.get();
                if (idle > idleSec * 1000L) {
                    System.out.println("\nNo input for " + idleSec + " seconds. Exiting...");
                    try { db.disconnect(); } catch (Exception ignored) {}
                    System.exit(0);
                }
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        });
        watchdog.setDaemon(true);
        watchdog.start();

        // Reports
        Scanner in = new Scanner(System.in);
        CountryReport country = new CountryReport();
        CityReport city = new CityReport();
        CapitalCityReport capitalcity = new CapitalCityReport();

        mainloop:
        while (true) {
            System.out.println("""
                \n=== Reports ===
                1) Country Report
                2) City Report
                3) Capital City Report
                0) Exit
                """);
            System.out.print("Choose: ");
            String c = in.nextLine().trim();
            LAST_ACTIVITY.set(System.currentTimeMillis());

            switch (c) {
                case "1" -> country.run(db.getConnection());
                case "2" -> city.run(db.getConnection());
                case "3" -> capitalcity.run(db.getConnection());
                case "0" -> { break mainloop; }
                default -> System.out.println("Unknown option.");
            }
        }

        db.disconnect();
        System.out.println("Bye!");
    }

    private static String getenvOr(String k, String def) {
        String v = System.getenv(k);
        return (v == null || v.isEmpty()) ? def : v;
    }
}
