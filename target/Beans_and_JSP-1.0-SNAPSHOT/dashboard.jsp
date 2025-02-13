<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.example.dailyplanner.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // Check if user is logged in
    HttpSession sess = request.getSession(false);
    String username = (sess != null) ? (String) sess.getAttribute("username") : null;
    int userId = (sess != null && sess.getAttribute("userId") != null) ? (int) sess.getAttribute("userId") : -1;

    if (username == null || userId == -1) {
        response.sendRedirect("login.jsp");
        return;
    }

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = new DBConnection().getConnection();
        String sql = "SELECT taskId, title, timeSlot FROM Tasks WHERE userId = ? ORDER BY timeSlot";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
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

<div class="header">
    <h2>FEBRUARY 7 2025</h2>
</div>

<div class="dashboard">
    <div class="schedule">
        <h3>SCHEDULE</h3>
        <form action="updateSchedule" method="post">
            <%
                while (rs.next()) {
            %>
            <div class="time-slot">
                <input type="text" name="title" value="<%= rs.getString("title") %>" required>
                <input type="time" name="timeSlot" value="<%= rs.getTime("timeSlot").toString().substring(0,5) %>" required>
                <input type="hidden" name="taskId" value="<%= rs.getInt("taskId") %>">
                <button type="submit">Edit</button>
                <a href="deleteTask?taskId=<%= rs.getInt("taskId") %>" class="delete-btn">X</a>
            </div>
            <%
                }
            %>
        </form>
    </div>

    <div class="add-task">
        <a href="newTask.jsp" class="button">+ Add New Task</a>
    </div>
</div>

</body>
</html>

<%
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    }
%>
