package com.napier.sem;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String host = getenvOr("DB_HOST", "localhost");
        int    port = Integer.parseInt(getenvOr("DB_PORT", "3306"));
        String name = getenvOr("DB_NAME", "world");
        String user = getenvOr("DB_USER", "root");
        String pass = getenvOr("DB_PASS", "example");

        Database db = new Database();
        db.connect(host, port, name, user, pass);

        Scanner in = new Scanner(System.in);
        CountryReport country = new CountryReport();
        mainloop:
        while (true) {
            System.out.println("""
                \n=== Reports ===
                1) Country Report
                0) Exit
                """);
            System.out.print("Choose: ");
            String c = in.nextLine().trim();
            switch (c) {
                case "1" -> country.run(db.getConnection());
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
