package org.example.dailyplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String url = "jdbc:mysql://localhost:3306/DailyPlanner";
    private String user = "root";
    private String password = "AngelTBSXxxX_1";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
