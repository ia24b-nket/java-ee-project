package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;

        try {
            // Establish database connection
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();

            // Initialize UserDAO
            UserDAO userDAO = new UserDAO(conn);

            // Check if email is already taken
            if (userDAO.isEmailTaken(email)) {
                response.sendRedirect("register.jsp?error=Email already in use");
                return;
            }

            // Hash the password
            String hashedPassword = PasswordUtil.hashPassword(password);

            User newUser = new User(username, email, hashedPassword);

            // Save user in database
            if (userDAO.insertUser(newUser)) {
                response.sendRedirect("register.jsp?success=registered");
            } else {
                response.sendRedirect("register.jsp?error=Registration failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Database error");
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
