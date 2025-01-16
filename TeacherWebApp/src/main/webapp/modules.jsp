<%@ page import="com.example.DatabaseUtil" %>
<%@ page import="java.sql.*, javax.servlet.http.*, javax.servlet.*" %>
<%
    if (session == null || session.getAttribute("idTeacher") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer idTeacher = (Integer) session.getAttribute("idTeacher");
    String firstName = "";

    if (idTeacher != null) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT FirstName FROM Teacher WHERE idTeacher = ?")) {
            statement.setInt(1, idTeacher);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    firstName = resultSet.getString("FirstName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
<html>
<head>
    <style><%@include file="/WEB-INF/css/style.css"%></style>
</head>
<body>
<div id="layout">
    <div id="top-bar">
        <div id="cssportal-grid">
            <img src="images/logo.png" class="logo2">
            <p class="welkomTekst">Welkom <%= firstName %>!</p>
            <p class="paginaInfo">Module bibliotheek</p>
        </div>
    </div>
    <div id="side-menu">
        <a href="Homepage.jsp" class="nav-button2">Klassen</a>
        <a href="modules.jsp" class="nav-button2">Modules</a>
        <a href="index.jsp" class="logout-button">Uitloggen</a>
    </div>
    <div id="content">content</div>
</div>
</body>