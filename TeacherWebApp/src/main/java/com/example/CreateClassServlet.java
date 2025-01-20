package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CreateClassServlet")
public class CreateClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idTeacher") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer idTeacher = (Integer) session.getAttribute("idTeacher");
        String className = request.getParameter("className");

        if (idTeacher != null && className != null && !className.isEmpty()) {
            String classCode;
            boolean isUnique;

            do {
                classCode = generateRandomClassCode();
                isUnique = true;

                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) FROM Class WHERE ClassCode = ?")) {
                    checkStatement.setString(1, classCode);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) > 0) {
                            isUnique = false;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } while (!isUnique);

            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Class (ClassName, ClassCode, Teacher_idTeacher) VALUES (?, ?, ?)")) {
                insertStatement.setString(1, className);
                insertStatement.setString(2, classCode);
                insertStatement.setInt(3, idTeacher);
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.sendRedirect("Homepage.jsp");
        }
    }

    private String generateRandomClassCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder classCode = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * characters.length());
            classCode.append(characters.charAt(index));
        }
        return classCode.toString();
    }
}
