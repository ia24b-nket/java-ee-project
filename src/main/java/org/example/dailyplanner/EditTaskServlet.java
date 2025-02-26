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

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        boolean completed = request.getParameter("completed") != null;

        Connection conn = null;
        try {
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();
            TaskDAO taskDAO = new TaskDAO(conn);

            Task task = taskDAO.getTaskById(taskId);
            if (task != null) {
                task.setTitle(title);
                task.setDescription(description);
                task.setStartTime(LocalTime.parse(startTimeStr));
                task.setEndTime(LocalTime.parse(endTimeStr));
                task.setCompleted(completed);

                boolean success = taskDAO.updateTask(task);
                if (success) {
                    response.sendRedirect("dashboard.jsp");
                } else {
                    response.sendRedirect("editTask.jsp?taskId=" + taskId + "&error=1");
                }
            } else {
                response.sendRedirect("dashboard.jsp?error=TaskNotFound");
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
