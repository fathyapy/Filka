import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Connector {
	public static Connection conn;
	public static Statement st;

	public static void initialize() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/filka", "root", "");
			st = conn.createStatement();
		}catch (Exception e) {}
	}
}
