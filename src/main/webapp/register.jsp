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
    <form action="<%= request.getContextPath() %>/register" method="post" novalidate>
        <!-- Username Field -->
        <input type="text" name="username" placeholder="Username" required minlength="3"
               title="At least 3 characters required" pattern="^[a-zA-Z0-9_]{3,}$"
               aria-label="Username" autocomplete="username">

        <!-- Email Field -->
        <input type="email" name="email" placeholder="Email" required
               title="Please enter a valid email address" aria-label="Email"
               autocomplete="email">

        <!-- Password Field -->
        <input type="password" name="password" placeholder="Password" required minlength="6"
               pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}"
               title="At least 6 characters, including uppercase, lowercase and a number"
               aria-label="Password" autocomplete="new-password">

        <!-- Register Button -->
        <button type="submit">REGISTER</button>
    </form>

    <%-- Display Success Message --%>
    <%
        String success = request.getParameter("success");
        if (success != null && "registered".equals(success)) {
    %>
    <p class="success-message">You have successfully been registered! 🎉</p>
    <%
        }
    %>

<%-- Display Error Messages --%>
    <%
        String error = request.getParameter("error");
        if (error != null) {
            if ("email_exists".equals(error)) {
    %>
    <p class="error-message">Email already exists! Please try another.</p>
    <%
    } else if ("invalid_input".equals(error)) {
    %>
    <p class="error-message">Invalid input. Please check your details and try again.</p>
    <%
            }
        }
    %>


    <!-- Link to Login Page -->
    <p>Already have an account? <a href="login.jsp">Log in here</a></p>
</div>

</body>
</html>
