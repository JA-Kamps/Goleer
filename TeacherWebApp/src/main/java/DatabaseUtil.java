import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String MYSQL_URI = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(MYSQL_URI);
    }

}
