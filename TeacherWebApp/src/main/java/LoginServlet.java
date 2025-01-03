import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gebruikersnaam = request.getParameter("username");
        String wachtwoord = request.getParameter("password");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT wachtwoord FROM gebruikers WHERE gebruikersnaam = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String gehashedWachtwoord = resultSet.getString("wachtwoord");

                if (PasswordUtil.checkPassword(wachtwoord, gehashedWachtwoord)) {
                    response.sendRedirect("klassen.jsp");
                } else {
                    request.setAttribute("errorMessage", "Ongeldige gebruikersnaam of wachtwoord.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Ongeldige gebruikersnaam of wachtwoord.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp");
        }
    }
}
