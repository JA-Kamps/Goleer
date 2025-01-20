package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println(email);
        System.out.println(password);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Password, idTeacher FROM Teacher WHERE Email = ?")) {
            statement.setString(1, email);
            System.out.println("Executing query: " + statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Query executed");
                if (resultSet.next()) {
                    String gehashedWachtwoord = resultSet.getString("Password");
                    int idTeacher = resultSet.getInt("idTeacher");
                    System.out.println("Retrieved password: " + gehashedWachtwoord);

                    if (PasswordUtil.checkPassword(password, gehashedWachtwoord)) {
                        // Invalidate the existing session if it exists
                        HttpSession existingSession = request.getSession(false);
                        if (existingSession != null) {
                            existingSession.invalidate();
                        }

                        // Create a new session and store the teacher ID
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute("idTeacher", idTeacher);

                        response.sendRedirect("Homepage.jsp");
                    } else {
                        request.setAttribute("errorMessage", "Ongeldige wachtwoord.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "Ongeldige email of wachtwoord.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Er is een fout opgetreden bij het verwerken van uw verzoek.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
