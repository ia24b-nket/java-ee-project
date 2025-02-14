package org.example.dailyplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private Connection conn;

    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

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
                        rs.getTime("startTime"),
                        rs.getTime("endTime"),
                        rs.getBoolean("completed"),
                        rs.getBytes("fileData")
                ));
            }
        }

        return tasks;
    }

    public boolean insertTask(Task task) throws SQLException {
        String sql = "INSERT INTO Tasks (title, description, startTime, endTime, completed, fileData) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, task.getTitle());
        stmt.setString(2, task.getDescription());
        stmt.setTime(3, Time.valueOf(task.getStartTime()));
        stmt.setTime(4, Time.valueOf(task.getEndTime()));
        stmt.setBoolean(5, task.isCompleted());
        stmt.setBytes(6, task.getFileData());

        return stmt.executeUpdate() > 0;
    }
    public boolean updateTask(Task task) throws SQLException {
        String sql = "UPDATE Tasks SET title = ?, description = ?, startTime = ?, endTime = ?, completed = ?, fileData = ? WHERE taskId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTime(3, Time.valueOf(task.getStartTime())); // Fix: LocalTime zu SQL Time
            stmt.setTime(4, Time.valueOf(task.getEndTime()));   // Fix: LocalTime zu SQL Time
            stmt.setBoolean(5, task.isCompleted());
            stmt.setBytes(6, task.getFileData());
            stmt.setInt(7, task.getTaskId()); // Fix: WHERE-Bedingung korrekt setzen

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM Task WHERE taskId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            return stmt.executeUpdate() > 0;
        }
    }
}
