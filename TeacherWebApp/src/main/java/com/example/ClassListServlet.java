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

@WebServlet("/ClassListServlet")
public class ClassListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idTeacher = (Integer) request.getSession().getAttribute("idTeacher");
        List<String> classNames = new ArrayList<>();

        if (idTeacher != null) {
            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT ClassName FROM Classes WHERE idTeacher = ?")) {
                statement.setInt(1, idTeacher);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        classNames.add(resultSet.getString("ClassName"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("classNames", classNames);
        request.getRequestDispatcher("Homepage.jsp").forward(request, response);
    }
}
