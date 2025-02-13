package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // For demo purposes, we are using hardcoded values. In real applications, fetch from the database.
        if ("tiyi@xyz.com".equals(email) && "2008".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", "Tiyi"); // Setting username in session
            session.setAttribute("userId", 1); // Assuming user ID is 1 for demo purposes
            resp.sendRedirect("dashboard.jsp");
        } else {
            resp.sendRedirect("login.jsp?error=true");
        }
    }
}
