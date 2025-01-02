
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="nl">
<head>
    <style><%@include file="/WEB-INF/css/register.css"%></style>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Pagina</title>
</head>
<body>

<div class="register-container">
    <h2>Registreren</h2>
    <form action="login.jsp" method="post">
        <input type="text" placeholder="Voornaam" id="voornaam" name="voornaam" required>
        <input type="text" placeholder="Achternaam" id="achternaam" name="achternaam" required>
        <input type="text" placeholder="Email" id="email" name="email" required>
        <input type="password" placeholder="Wachtwoord" id="password" name="password" required>
        <input type="password" placeholder="Herhaal Wachtwoord" id="password2" name="password2">
        <input type="submit" value="Registreren">
    </form>
</div>
</body>
</html>
