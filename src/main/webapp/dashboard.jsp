<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.dailyplanner.DBConnection" %>
<%@ page import="org.example.dailyplanner.TaskDAO" %>
<%@ page import="org.example.dailyplanner.Task" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.Clock" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // Check user session
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) sessionUser.getAttribute("username");

    // Initialize DB connection and tasks list
    Connection conn = null;
    List<Task> tasks = null;
    String errorMessage = null;

    try {
        DBConnection dbConn = new DBConnection();
        conn = dbConn.getConnection();
        TaskDAO taskDAO = new TaskDAO(conn);
        tasks = taskDAO.getTasksByUsername(username);
    } catch (Exception e) {
        e.printStackTrace();
        errorMessage = "Failed to load tasks. Please try again later.";
    } finally {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Daily Planner - Dashboard</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=2">
</head>
<body>

<div class="header">
    <h2>USER DASHBOARD</h2>
    <a href="login.jsp" class="logout">Logout</a>
</div>

<div class="dashboard">
    <h3><%= LocalDate.now(Clock.systemDefaultZone()).format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) %></h3>
    <div class="schedule">
        <h3>SCHEDULE</h3>

        <!-- Display Error Message if Failed to Load Tasks -->
        <%
            if (errorMessage != null) {
        %>
        <div class="error-message">
            <p><%= errorMessage %></p>
        </div>
        <%
            }
        %>

        <!-- Display Tasks -->
        <%
            if (tasks != null && !tasks.isEmpty()) {
                for (Task task : tasks) {
        %>
        <div class="time-slot">
            <h4><%= task.getTitle() %></h4>
            <p>Time: <%= task.getStartTime() %> - <%= task.getEndTime() %></p>
            <p>Description: <%= task.getDescription() %></p>
            <p>Completed: <%= task.isCompleted() ? "Yes" : "No" %></p>

            <!-- Display File Attachment -->
            <%
                if (task.getFileData() != null && task.getFileData().length > 0) {
            %>
            <a href="DownloadFileServlet?taskId=<%= task.getTaskId() %>" class="button">Download Attached File</a>
            <%
                }
            %>

            <!-- Edit Button -->
            <a href="editTask.jsp?taskId=<%= task.getTaskId() %>" class="button">✏️</a>

            <!-- Delete Button -->
            <form action="DeleteTaskServlet" method="post" style="display:inline;">
                <input type="hidden" name="taskId" value="<%= task.getTaskId() %>">
                <button type="submit" class="delete-button">🗑️</button>
            </form>
        </div>

        <%
            }
        } else {
        %>
        <p>No tasks found.</p>
        <%
            }
        %>
    </div>

    <div class="add-task">
        <a href="newTask.jsp" class="button">+ Add New Task</a>
    </div>
</div>

</body>
</html>
