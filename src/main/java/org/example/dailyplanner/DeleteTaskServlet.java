package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/DeleteTaskServlet")
public class DeleteTaskServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        Connection conn = null;

        try {
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();
            TaskDAO taskDAO = new TaskDAO(conn);

            boolean success = taskDAO.deleteTask(taskId);

            if (success) {
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("dashboard.jsp?error=DeleteFailed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=" + e.getMessage());
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
