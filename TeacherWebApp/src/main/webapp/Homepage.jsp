<%@ page import="com.example.DatabaseUtil" %>
<%@ page import="jakarta.servlet.http.*, jakarta.servlet.*" %>
<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException" %>

<%
    if (session == null || session.getAttribute("idTeacher") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer idTeacher = (Integer) session.getAttribute("idTeacher");
    String firstName = "";
    List<String> classNames = new ArrayList<>();
    List<Integer> classIds = new ArrayList<>();

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

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT idClass, ClassName FROM Class WHERE Teacher_idTeacher = ?")) {
            statement.setInt(1, idTeacher);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    classIds.add(resultSet.getInt("idClass"));
                    classNames.add(resultSet.getString("ClassName"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>
<html>
    <head>
        <script>
        function openForm() {
          document.getElementById("popupForm").style.display = "block";
        }

        function closeForm() {
            document.getElementById("popupForm").style.display = "none";
        }
        </script>

        <style>
        <%@include file="/WEB-INF/css/style.css"%>
        <%@include file="/WEB-INF/css/popupform.css"%>
        </style>
    </head>
    <body>
        <div id="layout">
            <div id="top-bar">
                <div id="cssportal-grid">
                    <img src="images/logo.png" class="logo2">
                    <p class="welkomTekst">Welkom <%= firstName %>!</p>
                    <p class="paginaInfo">Klassenoverzicht</p>
                </div>
            </div>
            <div id="side-menu">
                <a href="Homepage.jsp" class="nav-button2">Klassen</a>
                <a href="modules.jsp" class="nav-button2">Modules</a>
                <a href="index.jsp" class="logout-button">Uitloggen</a>
            </div>
            <div id="content">
                <%
                    if (!classNames.isEmpty()) {
                        for (int i = 0; i < classNames.size(); i++) {
                %>
                            <p><a href="ClassDetails.jsp?id=<%= classIds.get(i) %>"><%= classNames.get(i) %></a></p>
                <%
                        }
                    } else {
                %>
                        <p>Je hebt nog geen klassen maak er een aan.</p>
                <%
                    }
                %>
                <button onclick="openForm()">Nieuwe klass aan maken</button>

                <div id="popupForm" class="popup-form">
                    <form action="CreateClassServlet" method="post" class="form-container">
                        <h2>Nieuwe klass aan maken</h2>
                        <label for="className">Klass Naam:</label>
                        <input type="text" id="className" name="className" required>
                        <button type="submit" class="btn">Maak aan</button>
                        <button type="button" class="btn cancel" onclick="closeForm()">Sluiten</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
