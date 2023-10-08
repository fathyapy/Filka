import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDetail {
	private String transactionId;
	private String userName;
	private String itemId;
	private String itemName;
	private String itemDesc;
	private int itemPrice;
	private int quantity;
	private int totalPrice;
	private Date transactionDate;
	
	public TransactionDetail(String transactionId, String userName, String itemId, String itemName, String itemDesc, int itemPrice, int quantity,
			int totalPrice, Date transactionDate) {
		super();
		this.transactionId = transactionId;
		this.userName = userName;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDesc = itemDesc;
		this.itemPrice = itemPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}



	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	public String getItemDesc() {
		return itemDesc;
	}



	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}



	public int getItemPrice() {
		return itemPrice;
	}



	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}



	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public int getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}



	public Date getTransactionDate() {
		return transactionDate;
	}



	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public static ArrayList<TransactionDetail> getAll(){
		ArrayList<TransactionDetail> arr = new ArrayList<>();
		
		try {
			
			PreparedStatement s = Connector.conn.prepareStatement("SELECT th.transactionID, u.username, i.itemID, itemName, itemDescription, price, td.quantity, transactionDate FROM transaction th JOIN transactiondetail td ON th.transactionID = td.transactionID JOIN user u ON u.userID = th.userID JOIN item i ON i.itemID = td.itemID");
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				String transactionId = rs.getString("transactionID");
				String userName = rs.getString("userName");
				String itemId = rs.getString("itemID");
				String itemName = rs.getString("itemName");
				String itemDesc = rs.getString("itemDescription");
				int itemPrice = rs.getInt("price");
	            int quantity = rs.getInt("quantity");
				int totalPrice = quantity * itemPrice;
				Date transactionDate = rs.getDate("transactionDate");
				
				arr.add(new TransactionDetail(transactionId, userName, itemId, itemName, itemDesc, itemPrice, quantity, totalPrice, transactionDate));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;
	}
	
	public static ArrayList<TransactionDetail> getAll(String userId){
		ArrayList<TransactionDetail> arr = new ArrayList<>();
		
		try {
			
			PreparedStatement s = Connector.conn.prepareStatement("SELECT th.transactionID, u.username, i.itemID, itemName, itemDescription, price, td.quantity, transactionDate FROM transaction th JOIN transactiondetail td ON th.transactionID = td.transactionID JOIN user u ON u.userID = th.userID JOIN item i ON i.itemID = td.itemID WHERE th.userID = ?");
			s.setString(1, userId);
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				String transactionId = rs.getString("transactionID");
				String userName = rs.getString("userName");
				String itemId = rs.getString("itemID");
				String itemName = rs.getString("itemName");
				String itemDesc = rs.getString("itemDescription");
				int itemPrice = rs.getInt("price");
	            int quantity = rs.getInt("quantity");
				int totalPrice = quantity * itemPrice;
				Date transactionDate = rs.getDate("transactionDate");
				
				arr.add(new TransactionDetail(transactionId, userName, itemId, itemName, itemDesc, itemPrice, quantity, totalPrice, transactionDate));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;
	}

//	public void insert(ArrayList<TransactionDetail> tds) {
//	    for (TransactionDetail td : tds) {
//	        try {
//	            PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO transactiondetail (transactionID, itemID, quantity) VALUES (?,?,?)");
//	            s.setString(1, td.getTransactionId());
//	            s.setString(2, td.getItemId());
//	            s.setInt(3, td.getQuantity());
//	            s.executeUpdate();
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	}
	
	public boolean insert() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO transactiondetail (transactionID, itemID, quantity) VALUES (?, ?, ?)");
			s.setString(1, this.transactionId);
			s.setString(2, this.itemId);
			s.setInt(3,  this.quantity);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}

	
}
