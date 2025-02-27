package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/DeleteTaskServlet")
public class DeleteTaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get taskId from form
        int taskId = Integer.parseInt(request.getParameter("taskId"));

        // Get userId from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userId");

        Connection conn = null;

        try {
            // Establish DB Connection
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();
            TaskDAO taskDAO = new TaskDAO(conn);

            // Check if the task belongs to the current user
            Task existingTask = taskDAO.getTaskById(taskId);
            if (existingTask == null || existingTask.getUser().getUserId() != userId) {
                // Task does not exist or does not belong to user
                response.sendRedirect("dashboard.jsp?error=Unauthorized");
                return;
            }

            // Delete the task
            boolean success = taskDAO.deleteTask(taskId);

            if (success) {
                // Commit the transaction
                conn.commit();
                System.out.println("DEBUG: Task deleted successfully");
                response.sendRedirect("dashboard.jsp?success=deleted");
            } else {
                System.out.println("DEBUG: Failed to delete task");
                response.sendRedirect("dashboard.jsp?error=deletionFailed");
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
