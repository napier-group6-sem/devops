package com.napier.sem;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
/**
 * The main class of the application that connects to the database, starts the idle timeout watcher.
 * Displays the menu, and lets the user access all available reports.
 */

public class App {

    // keeps track of when the user last did something, so we can detect idleness
    private static final AtomicLong LAST_ACTIVITY = new AtomicLong(System.currentTimeMillis());

    public static void main(String[] args) {
        // getting db connection info from environment vars or just using defaults
        String host = getenvOr("DB_HOST", "localhost");
        int port    = Integer.parseInt(getenvOr("DB_PORT", "3307"));
        String name = getenvOr("DB_NAME", "world");
        String user = getenvOr("DB_USER", "root");
        String pass = getenvOr("DB_PASS", "example");

        // creating a db object and trying to connect
        Database db = new Database();
        db.connect(host, port, name, user, pass);

        // Idle timeout watchdog
        int idleSec = Integer.parseInt(getenvOr("APP_IDLE_SECONDS", "30"));

        // little watchdog thread that checks if the app's been idle for too long
        Thread watchdog = new Thread(() -> {
            while (true) {
                long idle = System.currentTimeMillis() - LAST_ACTIVITY.get();
                if (idle > idleSec * 1000L) {
                    System.out.println("\nNo input for " + idleSec + " seconds. Exiting...");
                    try { db.disconnect(); } catch (Exception ignored) {}
                    System.exit(0);
                }
                // just chill for a bit before checking again
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

        // main menu loop thing
        mainloop:
        while (true) {
            System.out.println("""
                \n=== Reports ===
                1) Country Report
                2) City Report
                3) Capital City Report
                4) Language Report
                0) Exit
                """);
            System.out.print("Choose: ");
            String c = in.nextLine().trim();
            LAST_ACTIVITY.set(System.currentTimeMillis()); // reset idle timer

            switch (c) {
                case "1" -> country.run(db.getConnection());
                case "2" -> city.run(db.getConnection());
                case "3" -> capitalcity.run(db.getConnection());
                case "4" -> new LangReport().run(db.getConnection());
                case "0" -> { break mainloop; }
                default -> System.out.println("Unknown option.");
            }
        }

        // close db connection before quitting
        db.disconnect();
        System.out.println("Bye!");
    }

    // gets an env variable or just uses a default if it's not set
    private static String getenvOr(String k, String def) {
        String v = System.getenv(k);
        return (v == null || v.isEmpty()) ? def : v;
    }
}
