<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=2">
    <title>Daily Planner - Register</title>
</head>
<body>

<!-- Header -->
<div class="header">
    <h2>USER REGISTRATION</h2>
</div>

<!-- Registration Form -->
<div class="register-container">
    <form action="register" method="post">
        <input type="text" name="username" placeholder="Username" required minlength="3" title="At least 3 characters required">
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required minlength="6" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}" title="At least 6 characters, including uppercase, lowercase and a number">
        <button type="submit">REGISTER</button>
    </form>

    <%-- Display Error Message --%>
    <%
        String error = request.getParameter("error");
        if (error != null) {
            if ("email_exists".equals(error)) {
    %>
    <p style="color: red;">Email already exists! Please try another.</p>
    <%
    } else if ("invalid_input".equals(error)) {
    %>
    <p style="color: red;">Invalid input. Please check your details and try again.</p>
    <%
            }
        }
    %>

    <p>Already have an account?<a href="login.jsp">Log in here</a></p>
</div>

</body>
</html>
