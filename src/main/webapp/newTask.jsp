<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sess = request.getSession(false);
    if (sess == null || sess.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <title>Daily Planner - Add New Task</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1">
</head>
<body>

<div class="header">
    <h2>NEW TASK</h2>
</div>

<div class="new-task-container">
    <form action="dashboard.jsp" method="post" enctype="multipart/form-data">
        <label for="title">Task Name:</label>
        <input type="text" name="title" id="title" placeholder="Enter task name..." required>

        <label for="timeSlot">Time:</label>
        <input type="time" name="timeSlot" id="timeSlot" required>

        <label for="notes">Notes:</label>
        <textarea name="notes" id="notes" rows="5" cols="30" placeholder="Enter your notes here... (optional)"></textarea>

        <label for="file">Attach File... (Optional):</label>
        <input type="file" name="file" id="file">

        <button type="submit">Save Task</button>
    </form>

    <a href="dashboard.jsp" class="button">Back to Dashboard</a>
</div>

</body>
</html>
