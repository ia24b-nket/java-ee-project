package org.example.dailyplanner;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    public static void main(String[] args) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection conn = dbConnection.getConnection();

            if (conn != null) {
                System.out.println("Verbindung erfolgreich!");
                conn.close();
            } else {
                System.out.println("Verbindung fehlgeschlagen.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei der Verbindung: " + e.getMessage());
        }


    }
}
