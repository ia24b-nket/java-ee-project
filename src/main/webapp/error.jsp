<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error - Daily Planner</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            text-align: center;
            padding: 50px;
        }

        .error-container {
            background-color: #ffdddd;
            color: #d8000c;
            border: 1px solid #d8000c;
            padding: 20px;
            border-radius: 10px;
            display: inline-block;
            max-width: 600px;
            word-wrap: break-word;
        }

        h1 {
            color: #d8000c;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #C065E9;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1>Oops! Something went wrong.</h1>
    <p>There was an issue while processing your request.</p>
    <h3>Error Details:</h3>
    <p>
        <%= (request.getParameter("message") != null && !request.getParameter("message").isEmpty())
                ? request.getParameter("message")
                : "No detailed error message provided."
        %>
    </p>
    <a href="dashboard.jsp">Go Back to Dashboard</a>
</div>
</body>
</html>
