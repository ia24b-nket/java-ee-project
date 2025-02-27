package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hashing the password for security
        String hashedPassword = hashPassword(password);

        Connection conn = null;
        try {
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();

            // Check if email already exists
            String checkEmailSQL = "SELECT email FROM Users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSQL)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    response.sendRedirect("register.jsp?error=EmailAlreadyExists");
                    return;
                }
            }

            // Insert new user
            String insertSQL = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, hashedPassword);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Registration successful, redirect to login
                    response.sendRedirect("login.jsp?success=Registered");
                } else {
                    // Registration failed
                    response.sendRedirect("register.jsp?error=RegistrationFailed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=DatabaseError");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to hash the password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
