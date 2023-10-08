import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Item {
	private String id;
	private String name; 
	private String description;
	private int price;
	private int quantity;
	
	public Item(String id, String name, String description, int price, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean insert() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("INSERT INTO item (itemID, itemName, itemDescription, price, quantity) VALUES (?, ?, ?, ?, ?)");
			s.setString(1, this.id);
			s.setString(2, this.name);
			s.setString(3,  this.description);
			s.setInt(4, this.price);
			s.setInt(5, this.quantity);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public boolean update() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("UPDATE item SET itemName = ?, itemDescription = ?, price = ?, quantity = ? WHERE itemID = ?");
			s.setString(1, this.name);
			s.setString(2, this.description);
			s.setInt(3, this.price);
			s.setInt(4, this.quantity);
			s.setString(5, this.id);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public static boolean decrementStock(String itemId, int by) {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("UPDATE item SET quantity = quantity - ? WHERE itemID = ?");
			s.setInt(1, by);
			s.setString(2, itemId);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public boolean delete() {
		int count = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("DELETE FROM item WHERE itemID = ?");
			s.setString(1, this.id);
			
			count = s.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (count > 0);
	}
	
	public static Item get(String id){
		Item i = null;
		try {
			
			PreparedStatement s = Connector.conn.prepareStatement("SELECT * FROM item WHERE itemID = ?");
			s.setString(1, id);
			
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				String itemid = rs.getString("itemID");
				String itemname = rs.getString("itemName");
				String itemdesc = rs.getString("itemDescription");
				int itemprice = rs.getInt("price");
				int itemqty = rs.getInt("quantity");

				i = new Item(itemid, itemname, itemdesc, itemprice, itemqty);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return i;
	}
	
	
	public static ArrayList<Item> getAll(){
		ArrayList<Item> arr = new ArrayList<>();
		
		try {
			
			PreparedStatement s = Connector.conn.prepareStatement("SELECT * FROM item");
			
			ResultSet rs = s.executeQuery();
			while(rs.next()) {
				String itemid = rs.getString("itemID");
				String itemname = rs.getString("itemName");
				String itemdesc = rs.getString("itemDescription");
				int itemprice = rs.getInt("price");
				int itemqty = rs.getInt("quantity");

				arr.add(new Item(itemid, itemname, itemdesc, itemprice, itemqty));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arr;
	}
	
	public static String getNextId() {
		int idNum = 0;
		try {
			PreparedStatement s = Connector.conn.prepareStatement("SELECT itemID FROM item WHERE itemID IN (SELECT MAX(itemID) FROM item)");
			
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				idNum = Integer.parseInt(rs.getString("itemID").substring(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String id = String.format("IT%03d", idNum+1);
		
		return id;
	}
}
