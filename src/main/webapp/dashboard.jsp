<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.dailyplanner.TaskDAO" %>
<%@ page import="org.example.dailyplanner.Task" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // Session check
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) sessionUser.getAttribute("username");

    // Retrieve tasks for logged-in user
    TaskDAO taskDAO = new TaskDAO(DatabaseConnection.getConnection()); // Fix: Connection übergeben
    List<Task> tasks = taskDAO.getTasksByUsername(username); // Fix: ResultSet durch List<Task> ersetzt
%>

<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1">
    <title>Daily Planner - Dashboard</title>
</head>
<body>

<!-- Header -->
<div class="header">
    <h2>USER DASHBOARD</h2>
</div>

<!-- Dashboard -->
<div class="dashboard">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center">
            <h3>FEBRUARY 7 2025</h3>
            <a href="logout.jsp" class="button">Logout</a>
        </div>

        <div class="row">
            <!-- Schedule Section -->
            <div class="schedule">
                <h3>SCHEDULE</h3>
                <form action="updateSchedule" method="post">
                    <%
                        for (Task task : tasks) {
                    %>
                    <div class="time-slot">
                        <input type="text" name="title" value="<%= task.getTitle() %>" required>
                        <input type="time" name="startTime" value="<%= task.getStartTime().toString().substring(0, 5) %>" required>
                        <input type="time" name="endTime" value="<%= task.getEndTime().toString().substring(0, 5) %>" required>
                        <input type="hidden" name="taskId" value="<%= task.getTaskId() %>">
                        <button type="submit" class="button">Edit</button>
                        <a href="deleteTask?taskId=<%= task.getTaskId() %>" class="delete-btn">X</a>
                    </div>
                    <%
                        }
                    %>
                </form>
            </div>

            <!-- Add Task Section -->
            <div class="add-task">
                <a href="newTask.jsp" class="button">+ Add New Task</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
