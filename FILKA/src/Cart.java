import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Cart {
	private String userId;
	private String itemId;
	private String itemName;
	private String itemDesc;
	private int itemPrice;
	private int quantity;
	private int totalPrice;
	
	public Cart(String userId, String itemId, String itemName, String itemDesc, int itemPrice, int quantity,
			int totalPrice) {
		super();
		this.userId = userId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDesc = itemDesc;
		this.itemPrice = itemPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public static ArrayList<Cart> getAll(String userId){
		ArrayList<Cart> arr = new ArrayList<>();
		
		try {
			PreparedStatement s = Connector.conn.prepareStatement("SELECT c.userID, c.itemID, i.itemName, i.itemDescription, i.price, c.quantity FROM cart c JOIN item i ON i.itemID = c.itemID WHERE c.userID = ?");
			s.setString(1, userId);
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				String rUserId = rs.getString("userID");
				String itemId = rs.getString("itemID");
				String itemName = rs.getString("itemName");
				String itemDesc = rs.getString("itemDescription");
				int itemPrice = rs.getInt("price");
				int itemQty = rs.getInt("quantity");
				int totalPrice = itemPrice * itemQty;
				
				arr.add(new Cart(rUserId, itemId, itemName, itemDesc, itemPrice, itemQty, totalPrice));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;
	}

	public boolean insertOrUpdate() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO cart (userID, itemID, quantity) VALUES (?, ?, ?)");
			s.setString(1, this.userId);
			s.setString(2, this.itemId);
			s.setInt(3,  this.quantity);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Item possibly already existing in cart, trying to update cart instead.");
			PreparedStatement s;
			try {
				s = Connector.conn.prepareStatement("UPDATE cart SET quantity = quantity + ? WHERE userID = ? AND itemID = ?");
				s.setInt(1, this.quantity);
				s.setString(2, this.userId);
				s.setString(3, this.itemId);
				
				
				count = s.executeUpdate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return (count > 0);
	}
	
	public boolean delete() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("DELETE FROM cart WHERE userID = ? AND itemID = ?");
			s.setString(1, this.userId);
			s.setString(2, this.itemId);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
}
