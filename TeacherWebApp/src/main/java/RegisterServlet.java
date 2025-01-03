import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voornaam = request.getParameter("voornaam");
        String achternaam = request.getParameter("achternaam");
        String email = request.getParameter("email");
        String wachtwoord = request.getParameter("password");
        String bevestigWachtwoord = request.getParameter("password2");

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
            String sql = "INSERT INTO teacher (voornaam, achternaam, email, wachtwoord) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, voornaam);
            statement.setString(2, achternaam);
            statement.setString(3, email);
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
