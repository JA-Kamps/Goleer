package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/editModuleClassLink")
public class EditModuleClassLinkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int moduleId = Integer.parseInt(request.getParameter("moduleId"));
        int classId = Integer.parseInt(request.getParameter("classId"));
        String status = request.getParameter("status");

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Module_has_Class SET Status = ? WHERE Module_idModule = ? AND Class_idClass = ?")) {
            statement.setString(1, status);
            statement.setInt(2, moduleId);
            statement.setInt(3, classId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("ModuleDetails.jsp?id=" + moduleId);
    }
}
