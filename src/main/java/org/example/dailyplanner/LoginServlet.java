package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Connection conn = null;
        try {
            // Get database connection
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();

            // Prepare SQL query to check email and password
            String sql = "SELECT userId, username, password FROM Users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            // Check if user exists and password matches
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) { // In production, hash and compare passwords
                    int userId = rs.getInt("userId");
                    String username = rs.getString("username");

                    // Create a session and set user attributes
                    HttpSession session = req.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("userId", userId);

                    // Redirect to dashboard
                    resp.sendRedirect("dashboard.jsp");
                    return;
                }
            }

            // If authentication fails, redirect to login with error
            resp.sendRedirect("login.jsp?error=true");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp?message=" + e.getMessage());
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
}
