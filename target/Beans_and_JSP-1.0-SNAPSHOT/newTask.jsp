<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sess = request.getSession(false);
    if (sess == null || sess.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Daily Planner - Add New Task</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=4">
</head>
<body>

<div class="header">
    <h2>NEW TASK</h2>
</div>

<div class="new-task-container">
    <form action="TaskServlet" method="post" enctype="multipart/form-data">
        <label for="title">Task Name:</label>
        <input type="text" name="title" id="title" placeholder="Enter task name..." required>

        <label for="startTime">Start Time:</label>
        <input type="time" name="startTime" id="startTime" required>

        <label for="endTime">End Time:</label>
        <input type="time" name="endTime" id="endTime" required>

        <label for="description">Notes:</label>
        <textarea name="description" id="description" rows="5" cols="30" placeholder="Enter your notes here... (optional)"></textarea>

        <label for="file">Attach File... (Optional):</label>
        <input type="file" name="file" id="file">

        <label for="completed">Completed:</label>
        <input type="checkbox" name="completed" id="completed" value="true">

        <button type="submit">Save Task</button>
    </form>

    <br><a href="dashboard.jsp" class="button">Back to Dashboard</a>
</div>

</body>
</html>
