package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "register", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voornaam = request.getParameter("voornaam");
        String achternaam = request.getParameter("achternaam");
        String email = request.getParameter("email");
        String wachtwoord = request.getParameter("password");
        String bevestigWachtwoord = request.getParameter("password2");
        System.out.println("hello");

        if (voornaam == null || voornaam.isEmpty() || achternaam == null || achternaam.isEmpty() || email == null || email.isEmpty() || wachtwoord == null || wachtwoord.isEmpty() || bevestigWachtwoord == null || bevestigWachtwoord.isEmpty()) {
            request.setAttribute("errorMessage", "Voor- en achternaam, email en wachtwoord zijn verplicht.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!wachtwoord.equals(bevestigWachtwoord)) {
            request.setAttribute("errorMessage", "Wachtwoorden komen niet overeen.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String gehashedWachtwoord = PasswordUtil.hashPassword(wachtwoord);

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO Teacher (Email, FirstName, LastName, Password) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, voornaam);
            statement.setString(3, achternaam);
            statement.setString(4, gehashedWachtwoord);
            statement.executeUpdate();
            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Er is een fout opgetreden. Probeer het opnieuw.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
