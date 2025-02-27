package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;

        try {
            // Establish database connection
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();

            // Initialize UserDAO
            UserDAO userDAO = new UserDAO(conn);

            // Get stored user by email
            User user = userDAO.getUserByEmail(email);

            // Check if user exists and passwords match
            if (user != null && PasswordUtil.hashPassword(password).equals(user.getPassword())) {
                // Successful login
                HttpSession session = request.getSession();
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userId", Integer.valueOf(user.getUserId()));
                response.sendRedirect("dashboard.jsp");
            } else {
                // Invalid credentials
                response.sendRedirect("login.jsp?error=Invalid email or password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Database error");
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
