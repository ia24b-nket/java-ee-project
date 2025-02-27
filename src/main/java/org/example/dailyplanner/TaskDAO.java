package org.example.dailyplanner;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private Connection conn;

    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

    // Get all tasks from the database
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("taskId"),
                        rs.getInt("userId"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTime("startTime").toLocalTime(),
                        rs.getTime("endTime").toLocalTime(),
                        rs.getBoolean("completed")
                ));
            }
        }
        return tasks;
    }

    // Get task by ID
    public Task getTaskById(int taskId) throws SQLException {
        String sql = "SELECT * FROM Tasks WHERE taskId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Task(
                        rs.getInt("taskId"),
                        rs.getInt("userId"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTime("startTime").toLocalTime(),
                        rs.getTime("endTime").toLocalTime(),
                        rs.getBoolean("completed")
                );
            }
        }
        return null;
    }

    // Get tasks by User ID ordered by completion and start time
    public List<Task> getTasksByUserId(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks WHERE userId = ? ORDER BY completed ASC, startTime ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("taskId"),
                        rs.getInt("userId"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTime("startTime").toLocalTime(),
                        rs.getTime("endTime").toLocalTime(),
                        rs.getBoolean("completed")
                ));
            }
        }
        return tasks;
    }

    // Get tasks by Username
    public List<Task> getTasksByUsername(String username) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.* FROM Tasks t JOIN Users u ON t.userId = u.userId WHERE u.username = ? ORDER BY t.startTime ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("taskId"),
                        rs.getInt("userId"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTime("startTime").toLocalTime(),
                        rs.getTime("endTime").toLocalTime(),
                        rs.getBoolean("completed")
                ));
            }
        }
        return tasks;
    }

    // Insert new task into the database
    public boolean insertTask(Task task, int userId) throws SQLException {
        String sql = "INSERT INTO Tasks (userId, title, description, startTime, endTime, completed, fileData) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setTime(4, Time.valueOf(task.getStartTime()));
            stmt.setTime(5, Time.valueOf(task.getEndTime()));
            stmt.setBoolean(6, task.isCompleted());
            stmt.setBytes(7, task.getFileData());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG: Rows affected by insertTask: " + rowsAffected);
            return rowsAffected > 0;
        }
    }


    // Update an existing task
    public boolean updateTask(Task task) throws SQLException {
        String sql = "UPDATE Tasks SET title = ?, description = ?, startTime = ?, endTime = ?, completed = ? " +
                "WHERE taskId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTime(3, Time.valueOf(task.getStartTime()));
            stmt.setTime(4, Time.valueOf(task.getEndTime()));
            stmt.setBoolean(5, task.isCompleted());
            stmt.setInt(6, task.getTaskId());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG: Rows affected by updateTask: " + rowsAffected);

            // Commit the update
            conn.commit();

            return rowsAffected > 0;
        }
    }



    // Delete task by ID
    public boolean deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE taskId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG: Rows affected by deleteTask: " + rowsAffected);

            // Commit the deletion
            conn.commit();

            return rowsAffected > 0;
        }
    }


    // Mark task as completed
    public boolean markTaskAsCompleted(int taskId) throws SQLException {
        String sql = "UPDATE Tasks SET completed = ? WHERE taskId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Delete all tasks by User ID
    public boolean deleteAllTasksByUser(int userId) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE userId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }
}
