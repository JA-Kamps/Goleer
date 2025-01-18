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

@WebServlet("/deleteModuleClassLink")
public class DeleteModuleClassLinkServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int moduleId = Integer.parseInt(request.getParameter("moduleId"));
        int classId = Integer.parseInt(request.getParameter("classId"));

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Module_has_Class WHERE Module_idModule = ? AND Class_idClass = ?")) {
            statement.setInt(1, moduleId);
            statement.setInt(2, classId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("ModuleDetails.jsp?id=" + moduleId);
    }
}
