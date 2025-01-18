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
    Integer classId = Integer.parseInt(request.getParameter("id"));

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

        // Check if the teacher is associated with the class
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT idClass FROM Class WHERE idClass = ? AND Teacher_idTeacher = ?")) {
            statement.setInt(1, classId);
            statement.setInt(2, idTeacher);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    response.sendRedirect("login.jsp");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Map<String, String>> students = new ArrayList<>();
    if (classId != null) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT idStudent, Name, Password FROM Student WHERE Class_idClass = ?")) {
            statement.setInt(1, classId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> student = new HashMap<>();
                    student.put("id", String.valueOf(resultSet.getInt("idStudent")));
                    student.put("name", resultSet.getString("Name"));
                    student.put("password", resultSet.getString("Password"));
                    students.add(student);
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
    <script>
        function togglePassword(id) {
            var passwordField = document.getElementById("password-" + id);
            if (passwordField.type === "password") {
                passwordField.type = "text";
            } else {
                passwordField.type = "password";
            }
        }

        function confirmDelete(studentId) {
            if (confirm("Are you sure you want to delete this student?")) {
                window.location.href = "deleteStudent?id=" + studentId + "&classId=<%= classId %>";
            }
        }

        function showPopup() {
            document.getElementById("popupForm").style.display = "block";
        }

        function hidePopup() {
            document.getElementById("popupForm").style.display = "none";
        }
    </script>
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
        <h2>Students</h2>
        <table>
            <tr>
                <th>Name</th>
                <th>Password</th>
                <th>Actions</th>
            </tr>
            <%
                for (Map<String, String> student : students) {
            %>
            <tr>
                <td><%= student.get("name") %></td>
                <td>
                    <input type="password" id="password-<%= student.get("id") %>" value="<%= student.get("password") %>" readonly>
                    <button type="button" onclick="togglePassword(<%= student.get("id") %>)">Show/Hide</button>
                </td>
                <td>
                    <button type="button" onclick="confirmDelete(<%= student.get("id") %>)">Delete</button>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <button type="button" onclick="showPopup()">Create New Student</button>

        <!-- Popup Form -->
        <div id="popupForm" style="display:none;">
            <form action="createStudent" method="post">
                <input type="hidden" name="classId" value="<%= classId %>">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" required>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
                <button type="submit">Submit</button>
                <button type="button" onclick="hidePopup()">Close</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
