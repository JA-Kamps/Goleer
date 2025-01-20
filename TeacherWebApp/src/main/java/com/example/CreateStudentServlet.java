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

@WebServlet("/createStudent")
public class CreateStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        int classId = Integer.parseInt(request.getParameter("classId"));
        Integer idTeacher = (Integer) request.getSession().getAttribute("idTeacher");

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Student (Name, Password, Class_idClass, Teacher_idTeacher) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setInt(3, classId);
            statement.setInt(4, idTeacher);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("ClassDetails.jsp?id=" + classId);
    }
}
