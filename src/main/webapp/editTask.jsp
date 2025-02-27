<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="org.example.dailyplanner.Task" %>
<%@ page import="org.example.dailyplanner.DBConnection" %>
<%@ page import="org.example.dailyplanner.TaskDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
  HttpSession sessionUser = request.getSession(false);
  if (sessionUser == null || sessionUser.getAttribute("username") == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  String username = (String) sessionUser.getAttribute("username");
  int taskId = Integer.parseInt(request.getParameter("taskId"));

  Connection conn = null;
  Task task = null;

  try {
    DBConnection dbConn = new DBConnection();
    conn = dbConn.getConnection();
    TaskDAO taskDAO = new TaskDAO(conn);
    task = taskDAO.getTaskById(taskId);
  } catch (Exception e) {
    e.printStackTrace();
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Task</title>
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=4">
</head>
<body>
<div class="header">
  <h2>Edit Task</h2>
</div>

<div class="new-task-container">

  <!-- Forum -->
  <form action="${pageContext.request.contextPath}/EditTaskServlet" method="post">
    <input type="hidden" name="taskId" value="<%= task.getTaskId() %>">

    <label for="title">Task Name:</label>
    <input type="text" name="title" id="title" value="<%= task.getTitle() %>" required>

    <label for="startTime">Start Time:</label>
    <input type="time" name="startTime" id="startTime" value="<%= task.getStartTime().toString() %>" required>

    <label for="endTime">End Time:</label>
    <input type="time" name="endTime" id="endTime" value="<%= task.getEndTime().toString() %>" required>

    <label for="description">Notes:</label>
    <textarea name="description" id="description" rows="5" cols="30"><%= task.getDescription() %></textarea>

    <label for="completed">Completed:</label>
    <input type="checkbox" name="completed" id="completed" <%= task.isCompleted() ? "checked" : "" %>>

    <button type="submit">Update Task</button>
  </form>

  <br><a href="dashboard.jsp" class="button">Back to Dashboard</a>
</div>
</body>
</html>
