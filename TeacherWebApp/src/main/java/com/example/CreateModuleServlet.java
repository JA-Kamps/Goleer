package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/createModule")
public class CreateModuleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String moduleName = request.getParameter("moduleName");
        String moduleType = request.getParameter("moduleType");
        int idTeacher = Integer.parseInt(request.getParameter("idTeacher"));

        // Set the upload date to now
        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Module (Name, UploadDate, ModuleType, Teacher_idTeacher) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, moduleName);
            statement.setDate(2, sqlDate);
            statement.setString(3, moduleType);
            statement.setInt(4, idTeacher);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("modules.jsp");
    }
}


