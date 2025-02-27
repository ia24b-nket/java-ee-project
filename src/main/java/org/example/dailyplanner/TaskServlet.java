package org.example.dailyplanner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import org.apache.commons.io.IOUtils;

@WebServlet("/TaskServlet")
@MultipartConfig
public class TaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve form data
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
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

            // Handle File Upload
            byte[] fileData = null;
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream fileContent = filePart.getInputStream()) {
                    fileData = IOUtils.toByteArray(fileContent);
                }
            }

            // Create new Task object
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setStartTime(LocalTime.parse(startTimeStr));
            newTask.setEndTime(LocalTime.parse(endTimeStr));
            newTask.setCompleted(completed);
            newTask.setFileData(fileData);

            // Insert Task into DB with userId
            boolean success = taskDAO.insertTask(newTask, userId);

            if (success) {
                conn.commit(); // Commit transaction
                response.sendRedirect("dashboard.jsp");
            } else {
                conn.rollback(); // Rollback if insert failed
                response.sendRedirect("newTask.jsp?error=1");
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
