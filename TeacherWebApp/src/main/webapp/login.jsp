
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="nl">
<head>
    <style><%@include file="/WEB-INF/css/login.css"%></style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Pagina</title>
</head>
<body>
<div class="login-container">
    <h2>Inloggen</h2>
    <form action="login.jsp" method="post">
        <input type="text" placeholder="Email" id="email" name="email" required>
        <input type="password" placeholder="Wachtwoord" id="password" name="password" required>
        <input type="submit" value="Inloggen">
    </form>
</div>
</body>
</html>

