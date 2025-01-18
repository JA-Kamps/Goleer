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


@WebServlet("/createQuestion")
public class CreateQuestionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String question = request.getParameter("question");
        String answers = request.getParameter("answers");
        int correctAnswer = Integer.parseInt(request.getParameter("correctAnswer"));
        int moduleId = Integer.parseInt(request.getParameter("moduleId"));

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO AnswerValue (Question, Answers, CorrectAnswer, Module_idModule) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, question);
            statement.setString(2, answers);
            statement.setInt(3, correctAnswer);
            statement.setInt(4, moduleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("ModuleDetails.jsp?id=" + moduleId);
    }
}
