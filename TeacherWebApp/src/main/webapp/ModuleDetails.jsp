<%@ page import="com.example.DatabaseUtil" %>
<%@ page import="java.sql.*, jakarta.servlet.http.*, jakarta.servlet.*" %>
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
    String moduleName = "";
    String uploadDate = "";
    String moduleType = "";
    Integer moduleId = null;
    List<Map<String, String>> questions = new ArrayList<>();
    List<Map<String, String>> linkedClasses = new ArrayList<>(); // Initialize linkedClasses
    List<Map<String, String>> teacherClasses = new ArrayList<>(); // Initialize teacherClasses

    if (request.getParameter("id") != null) {
        moduleId = Integer.parseInt(request.getParameter("id"));
    }

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

    if (moduleId != null) {
        // Get module details
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Name, UploadDate, ModuleType FROM Module WHERE idModule = ? AND Teacher_idTeacher = ?")) {
            statement.setInt(1, moduleId);
            statement.setInt(2, idTeacher);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    moduleName = resultSet.getString("Name");
                    uploadDate = resultSet.getString("UploadDate");
                    moduleType = resultSet.getString("ModuleType");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Get questions related to the module
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT idAnswerValue, Question, Answers, CorrectAnswer FROM AnswerValue WHERE Module_idModule = ?")) {
            statement.setInt(1, moduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> question = new HashMap<>();
                    question.put("id", String.valueOf(resultSet.getInt("idAnswerValue")));
                    question.put("question", resultSet.getString("Question"));
                    question.put("answers", resultSet.getString("Answers"));
                    question.put("correctAnswer", String.valueOf(resultSet.getInt("CorrectAnswer")));
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Get classes for the dropdown excluding already linked classes
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                "SELECT idClass, ClassName FROM Class WHERE Teacher_idTeacher = ? AND idClass NOT IN (SELECT Class_idClass FROM Module_has_Class WHERE Module_idModule = ?)")) {
            statement.setInt(1, idTeacher);
            statement.setInt(2, moduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> teacherClass = new HashMap<>();
                    teacherClass.put("classId", String.valueOf(resultSet.getInt("idClass")));
                    teacherClass.put("className", resultSet.getString("ClassName"));
                    teacherClasses.add(teacherClass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Get linked classes
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Class.idClass, Class.ClassName, Module_has_Class.Status FROM Module_has_Class JOIN Class ON Module_has_Class.Class_idClass = Class.idClass WHERE Module_has_Class.Module_idModule = ?")) {
            statement.setInt(1, moduleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> linkedClass = new HashMap<>();
                    linkedClass.put("classId", String.valueOf(resultSet.getInt("idClass")));
                    linkedClass.put("className", resultSet.getString("ClassName"));
                    linkedClass.put("status", resultSet.getString("Status"));
                    linkedClasses.add(linkedClass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
%>
<html>
<head>
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
            <p class="paginaInfo">Module bibliotheek</p>
        </div>
    </div>
    <div id="side-menu">
        <a href="Homepage.jsp" class="nav-button2">Klassen</a>
        <a href="modules.jsp" class="nav-button2">Modules</a>
        <a href="index.jsp" class="logout-button">Uitloggen</a>
    </div>
    <div id="content">
        <h2>Module Details</h2>
        <p><strong>Module Name:</strong> <%= moduleName %></p>
        <p><strong>Upload Date:</strong> <%= uploadDate %></p>
        <p><strong>Module Type:</strong> <%= "1".equals(moduleType) ? "Flappy Bird" : moduleType %></p>

        <h2>Questions</h2>
        <table>
            <tr>
                <th>Question</th>
                <th>Answers</th>
                <th>Correct Answer</th>
                <th>Actions</th>
            </tr>
            <%
                for (Map<String, String> question : questions) {
                    String[] answerOptions = question.get("answers").split(", ");
                    int correctAnswerIndex = Integer.parseInt(question.get("correctAnswer"));
            %>
            <tr>
                <td><%= question.get("question") %></td>
                <td><%= String.join(", ", answerOptions) %></td>
                <td><%= correctAnswerIndex %></td>
                <td>
                    <form action="deleteQuestion" method="get" onsubmit="return confirm('Are you sure you want to delete this question?');">
                        <input type="hidden" name="id" value="<%= question.get("id") %>">
                        <input type="hidden" name="moduleId" value="<%= moduleId %>">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
            <%
                }
                if (questions.isEmpty()) {
            %>
            <tr>
                <td colspan="4">No questions found.</td>
            </tr>
            <%
                }
            %>
        </table>


        <!-- Button to show the popup -->
        <button type="button" onclick="showPopup()">Add New Question</button>

        <!-- Popup for adding a new question -->
        <div id="popupForm" class="popup-form">
            <div class="form-container">
                <form action="createQuestion" method="post">
                    <input type="hidden" name="moduleId" value="<%= moduleId %>">
                    <label for="question">Question:</label>
                    <input type="text" id="question" name="question" required>
                    <label for="answers">Answers (separated by a comma):</label>
                    <input type="text" id="answers" name="answers" required>
                    <label for="correctAnswer">Correct Answer (index starting from 0):</label>
                    <input type="number" id="correctAnswer" name="correctAnswer" required>
                    <button type="submit" class="btn">Submit</button>
                    <button type="button" class="btn cancel" onclick="hidePopup()">Close</button>
                </form>
            </div>
        </div>

       <!-- Form to link module to a class -->
       <h2>Link Module to Class</h2>
       <form action="linkModuleToClass" method="post">
           <input type="hidden" name="moduleId" value="<%= moduleId %>">
           <label for="classId">Class:</label>
           <select id="classId" name="classId" required>
               <%
                   for (Map<String, String> teacherClass : teacherClasses) {
                       String classId = teacherClass.get("classId");
                       String className = teacherClass.get("className");
               %>
               <option value="<%= classId %>"><%= className %></option>
               <%
                   }
               %>
           </select>
           <label for="status">Status:</label>
           <select id="status" name="status" required>
               <option value="0">Paused</option>
               <option value="1">Available</option>
           </select>
           <button type="submit" class="btn">Link Module</button>
       </form>


       <h2>Linked Classes</h2>
       <table>
           <tr>
               <th>Class</th>
               <th>Status</th>
               <th>Actions</th>
           </tr>
           <%
               for (Map<String, String> linkedClass : linkedClasses) {
                   String classId = linkedClass.get("classId");
                   String className = linkedClass.get("className");
                   String status = linkedClass.get("status");
           %>
           <tr>
               <td><%= className %></td>
               <td><%= "0".equals(status) ? "Paused" : "Available" %></td>
               <td>
                   <form action="editModuleClassLink" method="post">
                       <input type="hidden" name="moduleId" value="<%= moduleId %>">
                       <input type="hidden" name="classId" value="<%= classId %>">
                       <label for="status">Status:</label>
                       <select name="status" required>
                           <option value="0" <%= "0".equals(status) ? "selected" : "" %>>Paused</option>
                           <option value="1" <%= "1".equals(status) ? "selected" : "" %>>Available</option>
                       </select>
                       <button type="submit">Update</button>
                   </form>
                   <form action="deleteModuleClassLink" method="post" onsubmit="return confirm('Are you sure you want to unlink this class?');">
                       <input type="hidden" name="moduleId" value="<%= moduleId %>">
                       <input type="hidden" name="classId" value="<%= classId %>">
                       <button type="submit">Unlink</button>
                   </form>
               </td>
           </tr>
           <%
               }
               if (linkedClasses.isEmpty()) {
           %>
           <tr>
               <td colspan="3">No linked classes found.</td>
           </tr>
           <%
               }
           %>
       </table>




        <!-- Button to delete the module -->
        <form action="deleteModule" method="post" onsubmit="return confirm('Are you sure you want to delete this module?');">
            <input type="hidden" name="id" value="<%= moduleId %>">
            <button type="submit" class="btn delete-module-btn">Delete Module</button>
        </form>

        <!-- Overlay to darken the background -->
        <div id="overlay" class="overlay"></div>
    </div>
</div>
<script>
    function showPopup() {
        document.getElementById("popupForm").style.display = "block";
        document.getElementById("overlay").style.display = "block";
    }

    function hidePopup() {
        document.getElementById("popupForm").style.display = "none";
        document.getElementById("overlay").style.display = "none";
    }
</script>
</body>
</html>
