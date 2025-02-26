package org.example.dailyplanner;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "Tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean completed = false;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] fileData;

    // ✅ No-arg constructor (required by JPA)
    public Task() {
    }

    // ✅ Full-arg constructor
    public Task(int taskId, User user, String title, String description, LocalTime startTime, LocalTime endTime, boolean completed, byte[] fileData) {
        this.taskId = taskId;
        this.user = user;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completed = completed;
        this.fileData = fileData;
    }

    // ✅ Constructor for tasks without file data
    public Task(int taskId, User user, String title, String description, LocalTime startTime, LocalTime endTime, boolean completed) {
        this.taskId = taskId;
        this.user = user;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completed = completed;
    }

    // ✅ Constructor for TaskDAO (when using userId)
    public Task(int taskId, int userId, String title, String description, LocalTime startTime, LocalTime endTime, boolean completed) {
        this.taskId = taskId;
        this.user = new User();
        this.user.setUserId(userId);
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.completed = completed;
    }

    // Getter und Setter
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
