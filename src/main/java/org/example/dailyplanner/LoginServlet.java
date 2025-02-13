package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("LoginServlet wurde aufgerufen!");
        System.out.println("E-Mail: " + email);
        System.out.println("Passwort: " + password);

        try (Connection conn = new DBConnection().getConnection()) {
            if (conn == null) {
                request.setAttribute("error", "Datenbankverbindung fehlgeschlagen!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String sql = "SELECT userId, username FROM Users WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {

                        HttpSession session = request.getSession();
                        session.setAttribute("userId", rs.getInt("userId"));
                        session.setAttribute("username", rs.getString("username"));

                        System.out.println("🔹 Session gespeichert für: " + session.getAttribute("username"));

                        response.resetBuffer();


                        try {
                            response.sendRedirect("./dashboard.jsp");
                        } catch (Exception e) {
                            System.out.println("sendRedirect() fehlgeschlagen, versuche forward()...");
                            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                        }
                    } else {

                        request.setAttribute("error", "Ungültige E-Mail oder Passwort!");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Datenbankfehler: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
