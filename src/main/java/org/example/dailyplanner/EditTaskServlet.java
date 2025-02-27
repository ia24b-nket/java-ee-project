package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;

@WebServlet("/EditTaskServlet")
public class EditTaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get task details from form
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
        LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
        boolean completed = request.getParameter("completed") != null;

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

            // Create a new Task object
            Task taskToUpdate = new Task();
            taskToUpdate.setTaskId(taskId);
            taskToUpdate.setTitle(title);
            taskToUpdate.setDescription(description);
            taskToUpdate.setStartTime(startTime);
            taskToUpdate.setEndTime(endTime);
            taskToUpdate.setCompleted(completed);

            // Update the task
            boolean success = taskDAO.updateTask(taskToUpdate);

            if (success) {
                // Commit the transaction
                conn.commit();
                System.out.println("DEBUG: Task updated successfully");
                response.sendRedirect("dashboard.jsp?success=updated");
            } else {
                System.out.println("DEBUG: Failed to update task");
                response.sendRedirect("editTask.jsp?taskId=" + taskId + "&error=1");
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
