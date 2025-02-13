<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1">
    <title>Daily Planner - Login</title>
</head>
<body>

<!-- Header -->
<div class="header">
    <h2>USER LOGIN</h2>
</div>

<!-- Login Form -->
<div class="login-container">
    <form action="login" method="post">
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">LOGIN</button>
    </form>

    <%-- Display login error message --%>
    <%
        String error = request.getParameter("error");
        if (error != null) {
    %>
    <p style="color: red;">Invalid email or password!</p>
    <%
        }
    %>

    <p><a href="#">Forgot password?</a></p>
</div>

</body>
</html>
