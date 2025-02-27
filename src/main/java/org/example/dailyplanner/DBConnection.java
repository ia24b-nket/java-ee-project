package org.example.dailyplanner;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private String url;
    private String user;
    private String password;

    public DBConnection() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }
            props.load(input);

            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");

            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load database properties", ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);

        // Enable auto-commit
        connection.setAutoCommit(false);

        return connection;
    }
}
