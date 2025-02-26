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

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        System.out.println("DEBUG: Received title = " + title);
        System.out.println("DEBUG: Received startTime = " + startTimeStr);
        System.out.println("DEBUG: Received endTime = " + endTimeStr);
        System.out.println("DEBUG: Received completed = " + completed);

        Connection conn = null;
        try {
            // Establish DB Connection
            DBConnection dbConn = new DBConnection();
            conn = dbConn.getConnection();
            TaskDAO taskDAO = new TaskDAO(conn);

            // Handle File Upload
            InputStream fileContent = null;
            byte[] fileData = null;
            Part filePart = request.getPart("file");
            if (filePart != null && filePart.getSize() > 0) {
                fileContent = filePart.getInputStream();
                fileData = IOUtils.toByteArray(fileContent);
                System.out.println("DEBUG: File uploaded with size = " + fileData.length);
            }

            // Create new Task object
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setStartTime(LocalTime.parse(startTimeStr));
            newTask.setEndTime(LocalTime.parse(endTimeStr));
            newTask.setCompleted(completed);
            newTask.setFileData(fileData);

            // Insert Task into DB
            boolean success = taskDAO.insertTask(newTask, username);

            if (success) {
                System.out.println("DEBUG: Task successfully saved to DB");
                response.sendRedirect("dashboard.jsp");
            } else {
                System.out.println("DEBUG: Failed to save task to DB");
                response.sendRedirect("newTask.jsp?error=1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=" + e.getMessage());
        } catch (Exception e) {
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
