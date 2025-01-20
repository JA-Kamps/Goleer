<%@ page import="com.example.DatabaseUtil" %>
<%@ page import="java.sql.*, javax.servlet.http.*, javax.servlet.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%
    if (session == null || session.getAttribute("idTeacher") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer idTeacher = (Integer) session.getAttribute("idTeacher");
    String firstName = "";
    List<Map<String, String>> modules = new ArrayList<>();

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

        // Query to get modules associated with the teacher
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT idModule, Name, UploadDate, ModuleType FROM Module WHERE Teacher_idTeacher = ?")) {
            statement.setInt(1, idTeacher);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> module = new HashMap<>();
                    module.put("id", String.valueOf(resultSet.getInt("idModule")));
                    module.put("name", resultSet.getString("Name"));
                    module.put("uploadDate", resultSet.getString("UploadDate"));
                    module.put("moduleType", String.valueOf(resultSet.getInt("ModuleType")));
                    modules.add(module);
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
    <div id="content">
        <h2>Modules</h2>
        <table>
            <tr>
                <th>Module Name</th>
                <th>Upload Date</th>
                <th>Module Type</th>
            </tr>
            <%
                for (Map<String, String> module : modules) {
            %>
            <tr>
                <td><a href="ModuleDetails.jsp?id=<%= module.get("id") %>"><%= module.get("name") %></a></td>
                <td><%= module.get("uploadDate") %></td>
                <td>
                    <%
                        if ("1".equals(module.get("moduleType"))) {
                            out.print("Flappy Bird");
                        } else {
                            out.print(module.get("moduleType"));
                        }
                    %>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <button type="button" onclick="showPopup()">Create New Module</button>

        <!-- Popup Form for Creating New Module -->
        <div id="popupForm" style="display:none;">
            <form action="createModule" method="post">
                <input type="hidden" name="idTeacher" value="<%= idTeacher %>">
                <label for="moduleName">Module Name:</label>
                <input type="text" id="moduleName" name="moduleName" required>
                <label for="moduleType">Module Type:</label>
                <select id="moduleType" name="moduleType" required>
                    <option value="1">Flappy Bird</option>
                </select>
                <button type="submit">Submit</button>
                <button type="button" onclick="hidePopup()">Close</button>
            </form>
        </div>
    </div>
</div>
<script>
    function showPopup() {
        document.getElementById("popupForm").style.display = "block";
    }

    function hidePopup() {
        document.getElementById("popupForm").style.display = "none";
    }
</script>
</body>
</html>
