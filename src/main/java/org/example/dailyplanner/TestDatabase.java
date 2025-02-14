package org.example.dailyplanner;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection conn = dbConnection.getConnection();

            if (conn != null) {
                System.out.println("Connection established");
                conn.close();
            }

            else{
                System.out.println("Connection failed");
            }

        } catch (SQLException e) {
            System.out.println("Connection error: "+ e.getMessage());
        }
    }
}
