<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // Session check
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) sessionUser.getAttribute("username");

    // Database connection
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/DailyPlanner", "root", "AngelTBSXxxX_1");

        // Retrieve tasks for logged in user
        String sql = "SELECT taskId, title, timeSlot FROM Tasks WHERE userId = (SELECT userId FROM Users WHERE username = ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        rs = stmt.executeQuery();
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
                        while (rs.next()) {
                    %>
                    <div class="time-slot">
                        <input type="text" name="title" value="<%= rs.getString("title") %>" required>
                        <input type="time" name="timeSlot" value="<%= rs.getTime("timeSlot").toString().substring(0, 5) %>" required>
                        <input type="hidden" name="taskId" value="<%= rs.getInt("taskId") %>">
                        <button type="submit" class="button">Edit</button>
                        <a href="deleteTask?taskId=<%= rs.getInt("taskId") %>" class="delete-btn">X</a>
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

<%
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
        if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
    }
%>
