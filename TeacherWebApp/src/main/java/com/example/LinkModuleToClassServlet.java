package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/linkModuleToClass")
public class LinkModuleToClassServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idTeacher") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idTeacher = (int) session.getAttribute("idTeacher");
        int moduleId = Integer.parseInt(request.getParameter("moduleId"));
        int classId = Integer.parseInt(request.getParameter("classId"));
        String status = request.getParameter("status");

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Module_has_Class (Module_idModule, Class_idClass, Class_Teacher_idTeacher, Status) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, moduleId);
            statement.setInt(2, classId);
            statement.setInt(3, idTeacher);
            statement.setString(4, status);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ensure correct redirection
        response.sendRedirect(request.getContextPath() + "/ModuleDetails.jsp?id=" + moduleId);
    }
}
