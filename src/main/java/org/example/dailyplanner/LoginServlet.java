package org.example.dailyplanner;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

@WebServlet(name = "login", value = "/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("user");
        String password = req.getParameter("password");
        String logTimeParam = req.getParameter("logTime");

        LocalDateTime logTime = (logTimeParam != null && !logTimeParam.isEmpty())
                ? LocalDateTime.parse(logTimeParam)
                : LocalDateTime.now();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Verbindung zur MySQL-Datenbank
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DailyPlanner", "root", ""); // Falls nötig, Passwort anpassen

            // Benutzer in der Datenbank suchen
            String sql = "SELECT userId, username FROM Users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Benutzer gefunden -> Session erstellen
                HttpSession session = req.getSession();
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("userId", rs.getInt("userId"));
                session.setAttribute("loginTime", logTime);

                // Weiterleitung zum Dashboard
                resp.sendRedirect("dashboard.jsp");
            } else {
                // Benutzer nicht gefunden
                req.setAttribute("error", "Invalid username or password");
                RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
                rd.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Internal server error");
            RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
            rd.forward(req, resp);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }
}
