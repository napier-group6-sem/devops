package com.napier.sem;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * The main class of the application that connects to the database, starts the idle timeout watcher.
 * Displays the menu, and lets the user access all available reports.
 */
@SuppressWarnings("PMD.GuardLogStatement")
public class App {

    private static final AtomicLong LAST_ACTIVITY = new AtomicLong(System.currentTimeMillis());
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

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
                    LOGGER.warning("\nNo input for " + idleSec + " seconds. Exiting...");
                    try {
                        db.disconnect();
                    } catch (Exception ignored) {
                        // ignore
                    }
                    System.exit(0);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    // ignore
                }
            }
        });
        watchdog.setDaemon(true);
        watchdog.start();

        // Reports
        Scanner in = new Scanner(System.in);
        CountryReport country = new CountryReport();
        CityReport city = new CityReport();
        CapitalCityReport capitalcity = new CapitalCityReport();
        PopulationReport population = new PopulationReport();

        mainloop:
        while (true) {
            LOGGER.info("""
                
                === Reports ===
                1) Country Report
                2) City Report
                3) Capital City Report
                4) Population Report
                5) Language Report
                0) Exit
                """);

            LOGGER.info("Choose: ");
            String c = in.nextLine().trim();
            LAST_ACTIVITY.set(System.currentTimeMillis());

            switch (c) {
                case "1" -> country.run(db.getConnection());
                case "2" -> city.run(db.getConnection());
                case "3" -> capitalcity.run(db.getConnection());
                case "4" -> population.run(db.getConnection());
                case "5" -> new LangReport().run(db.getConnection());
                case "0" -> { break mainloop; }
                default -> LOGGER.warning("Unknown option.");
            }
        }

        db.disconnect();
        LOGGER.info("Bye!");
    }

    private static String getenvOr(String k, String def) {
        String v = System.getenv(k);
        return (v == null || v.isEmpty()) ? def : v;
    }
}
