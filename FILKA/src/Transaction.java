import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {
	private String id;
	private String userId;
	private Date date;
	
	public Transaction(String id, String userId, Date date) {
		super();
		this.id = id;
		this.userId = userId;
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean insert() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO transaction (transactionID, userID, transactionDate) VALUES (?, ?, ?)");
			s.setString(1, this.id);
			s.setString(2, this.userId);
			s.setDate(3,  this.date);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public static String getNextId() {
		int idNum = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("SELECT transactionID FROM transaction WHERE transactionID IN (SELECT MAX(transactionID) FROM transaction)");
			
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				idNum = Integer.parseInt(rs.getString("transactionID").substring(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String id = String.format("TR%03d", idNum+1);
		
		return id;
	}
}
