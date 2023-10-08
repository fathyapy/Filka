import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

enum Page{
	Login,
	Register,
	Home
}

enum HomeState{
	Welcome,
	User,
	Item,
	Transaction,
	Market,
	Cart,
	History
}

public class Main extends Application{
	private static User auth = null;
	private static Stage stage;
	
	private static HomeState homeState = HomeState.Welcome;
	
	private static Scene loginScene;
	private static Scene registerScene;
	private static Scene homeScene;
	private VBox loginParent;
	private VBox registerParent;
	private BorderPane homeParent;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);
	}
	
	public static void changePage(Stage st, Page page) {
		switch(page) {
			case Login:{
				st.setScene(loginScene);
				st.setTitle("FILKA - Login");
				st.show();
				break;
			}
			case Register:{
				st.setScene(registerScene);
				st.setTitle("FILKA - Register");
				st.show();
				break;
			}
			case Home:{
				st.setScene(homeScene);
				st.setTitle("FILKA - Home " + ((auth != null && auth.getRole().equals("admin")) ? "(Admin)" : "(User)"));
				st.show();
				break;
			}
		}
	}
	
	public void setLayoutLogin() {
		loginParent.setSpacing(3);
		
		GridPane gpLogin = new GridPane();
		gpLogin.setVgap(6);
		gpLogin.setHgap(6);
		gpLogin.setAlignment(Pos.CENTER);
		gpLogin.getColumnConstraints().add(new ColumnConstraints(100));
		gpLogin.getColumnConstraints().add(new ColumnConstraints(150)); 
		gpLogin.getColumnConstraints().add(new ColumnConstraints(50)); 
		gpLogin.getRowConstraints().add(new RowConstraints(100));
		
		Text lbTitle = new Text("Login to FILKA");
		lbTitle.setTextAlignment(TextAlignment.CENTER);
		lbTitle.setFont(Font.font("", FontWeight.BOLD, 20));
		
		Text lbUsername = new Text("Username ");
		TextField tfUsername = new TextField();
		Text lbPassword = new Text("Password ");
		PasswordField pfPassword = new PasswordField();
		
		Button btnRegister = new Button("Register Account Page");
		btnRegister.setOnAction(e -> {
			//RESET
			tfUsername.clear();
			pfPassword.clear();
			
			changePage(stage, Page.Register);
		});
		
		Button btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> {
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			
			if(username.isEmpty() || password.isEmpty()) {
				alert("Error - Login Failed", "Username and password cannot be empty!", AlertType.ERROR);
				return;
			}else {
				User a = User.auth(username, password);
				if(a == null) {
					alert("Error - Login Failed", "Invalid user credentials!", AlertType.ERROR);
					return;
				}else {
					Main.auth = a;
					
					alert("Successful Login", "Click OK to continue", AlertType.INFORMATION);
					
					//RESET
					tfUsername.clear();
					pfPassword.clear();
					
					if(Main.auth != null) {
						Main.homeState = HomeState.Welcome;
						setLayoutHome();
						changePage(stage, Page.Home);
					}
				}
			}
		});
		
		gpLogin.add(lbTitle, 1, 0, 2, 1);
		gpLogin.add(lbUsername, 0, 1, 1, 1);
		gpLogin.add(tfUsername, 1, 1, 2, 1);
		gpLogin.add(lbPassword, 0, 2, 1, 1);
		gpLogin.add(pfPassword, 1, 2, 2, 1);
		gpLogin.add(btnRegister, 1, 3, 1, 1);
		gpLogin.add(btnLogin, 2, 3, 1, 1);
		
		loginParent.getChildren().add(gpLogin);
		
	}
	
	public boolean alphanumeric(String str) {
	    for (int i = 0; i < str.length(); i++) {
	        char c = str.charAt(i);
	        if ((c < '0' && c > '9') && (c < 'A' && c > 'Z') && (c < 'a' && c > 'z')) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public boolean email(String str) {
		return !str.startsWith("@") && str.endsWith(".com") && str.contains("@");
	}
	
	public void setLayoutRegister() {
		registerParent.setSpacing(3);
		
		GridPane gpRegister = new GridPane();
		gpRegister.setVgap(6);
		gpRegister.setHgap(6);
		gpRegister.setAlignment(Pos.CENTER);
		gpRegister.getColumnConstraints().add(new ColumnConstraints(100));
		gpRegister.getColumnConstraints().add(new ColumnConstraints(100)); 
		gpRegister.getColumnConstraints().add(new ColumnConstraints(100)); 
		gpRegister.getRowConstraints().add(new RowConstraints(100));
		
		Text lbTitle = new Text("Register");
		lbTitle.setTextAlignment(TextAlignment.CENTER);
		lbTitle.setFont(Font.font("", FontWeight.BOLD, 36));
		
		Text lbUserid = new Text("User ID ");
		TextField tfUserid = new TextField(User.getNextId());
		tfUserid.setDisable(true);
		
		Text lbUsername = new Text("Username ");
		TextField tfUsername = new TextField();
		Text lbPassword = new Text("Password ");
		PasswordField pfPassword = new PasswordField();
		Text lbEmail = new Text("Email ");
		TextField tfEmail = new TextField();
		Text lbPhone = new Text("Phone Number ");
		TextField tfPhone = new TextField();
		Text lbAge = new Text("Age ");
		Spinner<Integer> spAge = new Spinner<>(15, 70, 16);
		Text lbGender = new Text("Gender ");
		RadioButton rbMale = new RadioButton("Male");
		RadioButton rbFemale = new RadioButton("Female");
		ToggleGroup tgGender = new ToggleGroup();
		rbMale.setToggleGroup(tgGender);
		rbFemale.setToggleGroup(tgGender);
		
		Button btnLogin = new Button("Login Page");
		btnLogin.setOnAction(e -> {
			//RESET
			tfUserid.setText(User.getNextId());;
			tfUsername.clear();
			pfPassword.clear();
			tfEmail.clear();
			tfPhone.clear();
			spAge.getValueFactory().setValue(16);
			tgGender.selectToggle(null);
			
			changePage(stage, Page.Login);
		});
		
		Button btnRegister = new Button("Register");
		btnRegister.setOnAction(e -> {
			String userId = tfUserid.getText();
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			String email = tfEmail.getText();
			String phone = tfPhone.getText();
			int age = spAge.getValue();
			String gender = "UNSELECTED";
			gender = tgGender.getSelectedToggle() != null ? ((RadioButton)tgGender.getSelectedToggle()).getText() : "UNSELECTED";
			
			if(username.length() < 5 || username.length() > 20) {
				alert("Error - Registration Failed", "Username must be between 5 - 20 characters!", AlertType.ERROR);
				return;
			}else if(password.length() < 5 || password.length() > 20) {
				alert("Error - Registration Failed", "Password must be between 5 - 20 characters!", AlertType.ERROR);
				return;
			}else if(!alphanumeric(password)) {
				alert("Error - Registration Failed", "Password must only be alphanumeric (only letters & digits)!", AlertType.ERROR);
				return;
			}else if(!email(email)) {
				alert("Error - Registration Failed", "Email must contain '@' and must not be the first character, and ends with '.com'", AlertType.ERROR);
				return;
			}else if(phone.length() < 9 || phone.length() > 12) {
				alert("Error - Registration Failed", "Phone number must be between 9 - 12 characters!", AlertType.ERROR);
				return;
			}else if(age < 17 || age > 60) {
				alert("Error - Registration Failed", "Age range must be between 17 - 60!", AlertType.ERROR);
				return;
			}else if(gender.equals("UNSELECTED")) {
				alert("Error - Registration Failed", "Gender must be selected!", AlertType.ERROR);
				return;
			}
			
			User nu = new User(userId, username, password, email, gender, phone, age, "user");
			nu.insert();
			
			alert("Successful Registration", "Click OK to continue", AlertType.INFORMATION);
			
			//RESET
			tfUserid.setText(User.getNextId());
			tfUsername.clear();
			pfPassword.clear();
			tfEmail.clear();
			tfPhone.clear();
			spAge.getValueFactory().setValue(16);
			tgGender.selectToggle(null);
			
			changePage(stage, Page.Login);
		});
		
		gpRegister.add(lbTitle, 1, 0, 2, 1);
		gpRegister.add(lbUserid, 0, 1, 1, 1);
		gpRegister.add(tfUserid, 1, 1, 2, 1);
		
		gpRegister.add(lbUsername, 0, 2, 1, 1);
		gpRegister.add(tfUsername, 1, 2, 2, 1);
		gpRegister.add(lbPassword, 0, 3, 1, 1);
		gpRegister.add(pfPassword, 1, 3, 2, 1);
		gpRegister.add(lbEmail, 0, 4, 1, 1);
		gpRegister.add(tfEmail, 1, 4, 2, 1);
		gpRegister.add(lbPhone, 0, 5, 1, 1);
		gpRegister.add(tfPhone, 1, 5, 2, 1);
		gpRegister.add(lbAge, 0, 6, 1, 1);
		gpRegister.add(spAge, 1, 6, 2, 1);
		gpRegister.add(lbGender, 0, 7, 1, 1);
		gpRegister.add(rbMale, 1, 7, 1, 1);
		gpRegister.add(rbFemale, 2, 7, 1, 1);
		
		gpRegister.add(btnLogin, 1, 8, 1, 1);
		gpRegister.add(btnRegister, 2, 8, 1, 1);
		
		registerParent.getChildren().add(gpRegister);
	}
	
	public void setLayoutHome() {
		//REMOVE ALL CHILDREN
		homeParent.getChildren().clear();
		
		//TOP LAYOUTS
		//For any state
		//-> Menubar
		MenuBar mbHome = new MenuBar();
		Menu menu = new Menu("Menu");
		mbHome.getMenus().add(menu);

		MenuItem miLogout = new MenuItem("Logout");
		miLogout.setOnAction(e -> {
			Main.auth = null;
			changePage(stage, Page.Login);
		});
		
		MenuItem miManageUser = new MenuItem("Manage Users");
		miManageUser.setOnAction(e -> {
			Main.homeState = HomeState.User;
			setLayoutHome();
		});
		
		MenuItem miManageItem = new MenuItem("Manage Items");
		miManageItem.setOnAction(e -> {
			Main.homeState = HomeState.Item;
			setLayoutHome();
		});
		
		
		MenuItem miTransactions = new MenuItem("View Transactions");
		miTransactions.setOnAction(e -> {
			Main.homeState = HomeState.Transaction;
			setLayoutHome();
		});
		
		
		MenuItem miMarket = new MenuItem("Item Market");
		miMarket.setOnAction(e -> {
			Main.homeState = HomeState.Market;
			setLayoutHome();
		});
		
		
		MenuItem miCart = new MenuItem("Cart Items");
		miCart.setOnAction(e -> {
			Main.homeState = HomeState.Cart;
			setLayoutHome();
		});
		
		
		MenuItem miHistory = new MenuItem("Transaction History");
		miHistory.setOnAction(e -> {
			Main.homeState = HomeState.History;
			setLayoutHome();
		});
		
		
		if(Main.auth.getRole().equals("admin")) {
			menu.getItems().addAll(miManageUser, miManageItem, miTransactions, miLogout);
		}else {
			menu.getItems().addAll(miMarket, miCart, miHistory, miLogout);
		}
		
		homeParent.setTop(mbHome);
		
		if(homeState == HomeState.Welcome) {
			//-> Welcome Text
			Text welcome = new Text("Welcome, " + Main.auth.getUsername());
			welcome.setFont(Font.font("", FontWeight.EXTRA_BOLD, 48));
			
			homeParent.setCenter(welcome);
			BorderPane.setAlignment(welcome, Pos.CENTER);
		}else if(homeState == HomeState.User) {
			//CENTER
			TableView<User> userTable = new TableView<>();
			TableColumn<User, String> col1 = new TableColumn<>("User ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("id"));
			TableColumn<User, String> col2 = new TableColumn<>("Username");
			col2.setCellValueFactory(new PropertyValueFactory<>("username"));
			TableColumn<User, String> col3 = new TableColumn<>("Password");
			col3.setCellValueFactory(new PropertyValueFactory<>("password"));
			TableColumn<User, String> col4 = new TableColumn<>("Email");
			col4.setCellValueFactory(new PropertyValueFactory<>("email"));
			TableColumn<User, String> col5 = new TableColumn<>("Phone Number");
			col5.setCellValueFactory(new PropertyValueFactory<>("phone"));
			TableColumn<User, Integer> col6 = new TableColumn<>("Age");
			col6.setCellValueFactory(new PropertyValueFactory<>("age"));
			TableColumn<User, String> col7 = new TableColumn<>("Gender");
			col7.setCellValueFactory(new PropertyValueFactory<>("gender"));
			
			userTable.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);
			userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			homeParent.setCenter(userTable);
			
			//RIGHT
			GridPane gpManageUser = new GridPane();
			gpManageUser.setVgap(6);
			gpManageUser.setHgap(6);
			gpManageUser.setAlignment(Pos.TOP_CENTER);
			gpManageUser.setPadding(new Insets(15));
			gpManageUser.getColumnConstraints().add(new ColumnConstraints(100));
			gpManageUser.getColumnConstraints().add(new ColumnConstraints(100)); 
			gpManageUser.getColumnConstraints().add(new ColumnConstraints(100)); 
			
			Text lbUserid = new Text("User ID ");
			TextField tfUserid = new TextField();
			tfUserid.setDisable(true);
			
			Text lbUsername = new Text("Username ");
			TextField tfUsername = new TextField();
			tfUsername.setDisable(true);
			
			Text lbPassword = new Text("Password ");
			TextField tfPassword = new TextField();
			tfPassword.setDisable(true);
			
			Text lbEmail = new Text("Email ");
			TextField tfEmail = new TextField();
			Text lbPhone = new Text("Phone Number ");
			TextField tfPhone = new TextField();
			Text lbAge = new Text("Age ");
			Spinner<Integer> spAge = new Spinner<>(15, 70, 16);
			Text lbGender = new Text("Gender ");
			RadioButton rbMale = new RadioButton("Male");
			RadioButton rbFemale = new RadioButton("Female");
			ToggleGroup tgGender = new ToggleGroup();
			rbMale.setToggleGroup(tgGender);
			rbFemale.setToggleGroup(tgGender);
			
			Button btnUpdate = new Button("Update User");
			btnUpdate.setOnAction(e -> {
				
				if(userTable.getSelectionModel().getSelectedItem() != null) {
					String email = tfEmail.getText();
					String phone = tfPhone.getText();
					int age = spAge.getValue();
					String gender = "UNSELECTED";
					gender = tgGender.getSelectedToggle() != null ? ((RadioButton)tgGender.getSelectedToggle()).getText() : "UNSELECTED";
					
					if(!email(email)) {
						alert("Error - Update Failed", "Email must contain '@' and must not be the first character, and ends with '.com'", AlertType.ERROR);
						return;
					}else if(phone.length() < 9 || phone.length() > 12) {
						alert("Error - Update Failed", "Phone number must be between 9 - 12 characters!", AlertType.ERROR);
						return;
					}else if(age < 17 || age > 60) {
						alert("Error - Update Failed", "Age range must be between 17 - 60!", AlertType.ERROR);
						return;
					}else if(gender.equals("UNSELECTED")) {
						alert("Error - Update Failed", "Gender must be selected!", AlertType.ERROR);
						return;
					}
					
					User nu = new User(tfUserid.getText(), tfUsername.getText(), tfPassword.getText(), email, gender, phone, age, "user");
					nu.update();
					
					alert("Successful Update", "Click OK to continue", AlertType.INFORMATION);
					
					userTable.getItems().clear();
					userTable.getItems().addAll(User.getAll(false));
				}else {
					alert("Error - Update Failed", "Must select user to update!", AlertType.ERROR);
				}
			});
			
			Button btnDelete = new Button("Delete User");
			btnDelete.setOnAction(e -> {
				if(userTable.getSelectionModel().getSelectedItem() != null) {
					User nu = new User(tfUserid.getText(), "", "", "", "", "", 0, "user");
					nu.delete();
					
					alert("Successful Delete", "Click OK to continue", AlertType.INFORMATION);
					
					userTable.getItems().clear();
					userTable.getItems().addAll(User.getAll(false));
				}else {
					alert("Error - Delete Failed", "Must select user to delete!", AlertType.ERROR);
				}
			});
			
			HBox hbButtons = new HBox();
			hbButtons.setSpacing(10);
			hbButtons.getChildren().addAll(btnUpdate, btnDelete);
			
			gpManageUser.add(lbUserid, 0, 1, 1, 1);
			gpManageUser.add(tfUserid, 1, 1, 2, 1);
			
			gpManageUser.add(lbUsername, 0, 2, 1, 1);
			gpManageUser.add(tfUsername, 1, 2, 2, 1);
			gpManageUser.add(lbPassword, 0, 3, 1, 1);
			gpManageUser.add(tfPassword, 1, 3, 2, 1);
			gpManageUser.add(lbEmail, 0, 4, 1, 1);
			gpManageUser.add(tfEmail, 1, 4, 2, 1);
			gpManageUser.add(lbPhone, 0, 5, 1, 1);
			gpManageUser.add(tfPhone, 1, 5, 2, 1);
			gpManageUser.add(lbAge, 0, 6, 1, 1);
			gpManageUser.add(spAge, 1, 6, 2, 1);
			gpManageUser.add(lbGender, 0, 7, 1, 1);
			gpManageUser.add(rbMale, 1, 7, 1, 1);
			gpManageUser.add(rbFemale, 2, 7, 1, 1);
			gpManageUser.add(hbButtons, 1, 8, 3, 1);
			
			homeParent.setRight(gpManageUser);
			
			userTable.getSelectionModel().selectedItemProperty().addListener((item, oldval, newval) -> {
				if(item != null && item.getValue() != null) {
					User u = item.getValue();
					
					tfUserid.setText(u.getId());
					tfUsername.setText(u.getUsername());
					tfPassword.setText(u.getPassword());
					tfEmail.setText(u.getEmail());
					tfPhone.setText(u.getPhone());
					spAge.getValueFactory().setValue(u.getAge());
					tgGender.selectToggle(u.getGender().equalsIgnoreCase("Male") ? rbMale : rbFemale);
				}
			});
			
			userTable.getItems().clear();
			userTable.getItems().addAll(User.getAll(false));
			
		}else if(homeState == HomeState.Item) {
			//CENTER
			TableView<Item> itemTable = new TableView<>();
			TableColumn<Item, String> col1 = new TableColumn<>("Item ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("id"));
			TableColumn<Item, String> col2 = new TableColumn<>("Item Name");
			col2.setCellValueFactory(new PropertyValueFactory<>("name"));
			TableColumn<Item, String> col3 = new TableColumn<>("Item Description");
			col3.setCellValueFactory(new PropertyValueFactory<>("description"));
			TableColumn<Item, Integer> col4 = new TableColumn<>("Price");
			col4.setCellValueFactory(new PropertyValueFactory<>("price"));
			TableColumn<Item, Integer> col5 = new TableColumn<>("Quantity (Stock)");
			col5.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			
			itemTable.getColumns().addAll(col1, col2, col3, col4, col5);
			itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			homeParent.setCenter(itemTable);
			
			//RIGHT
			GridPane gpManageItem = new GridPane();
			gpManageItem.setVgap(6);
			gpManageItem.setHgap(6);
			gpManageItem.setAlignment(Pos.TOP_CENTER);
			gpManageItem.setPadding(new Insets(15));
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100));
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100)); 
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100)); 
			
			Text lbItemid = new Text("Item ID ");
			TextField tfItemid = new TextField(Item.getNextId());
			tfItemid.setDisable(true);
			
			Text lbName = new Text("Item Name ");
			TextField tfName = new TextField();
			Text lbDesc = new Text("Item Description ");
			TextField tfDesc = new TextField();
			Text lbPrice = new Text("Price ");
			Spinner<Integer> spPrice = new Spinner<>(0, Integer.MAX_VALUE, 0, 1000);
			Text lbQuantity = new Text("Quantity ");
			Spinner<Integer> spQuantity = new Spinner<>(0, Integer.MAX_VALUE, 0);
			
			Button btnInsert = new Button("Insert Item");
			btnInsert.setDisable(false);
			btnInsert.setOnAction(e -> {
				if(itemTable.getSelectionModel().getSelectedItem() == null) {
					String id = tfItemid.getText();
					String name = tfName.getText();
					String desc = tfDesc.getText();
					int price = spPrice.getValue();
					int quantity = spQuantity.getValue();

					if(name.length() < 5 || name.length() > 100) {
						alert("Error - Insert Failed", "Item name must be between 5 - 100 chars", AlertType.ERROR);
						return;
					}else if(desc.length() < 10 || desc.length() > 200) {
						alert("Error - Insert Failed", "Item description must be between 5 - 200 chars", AlertType.ERROR);
						return;
					}else if(price <= 0) {
						alert("Error - Insert Failed", "Price must be greater than 0!", AlertType.ERROR);
						return;
					}else if(quantity <= 0) {
						alert("Error - Insert Failed", "Quantity must be greater than 0!", AlertType.ERROR);
						return;
					}
					
					Item ni = new Item(id, name, desc, price, quantity);
					ni.insert();
					
					alert("Successful Item Adding", "Click OK to continue", AlertType.INFORMATION);
					
					itemTable.getItems().clear();
					itemTable.getItems().addAll(Item.getAll());
					
					itemTable.getSelectionModel().clearSelection();
					tfItemid.setText(Item.getNextId());
				}
			});
			
			Button btnUpdate = new Button("Update Item");
			btnUpdate.setDisable(true);
			btnUpdate.setOnAction(e -> {
				
				if(itemTable.getSelectionModel().getSelectedItem() != null) {
					String name = tfName.getText();
					String desc = tfDesc.getText();
					int price = spPrice.getValue();
					int quantity = spQuantity.getValue();

					if(name.length() < 5 || name.length() > 100) {
						alert("Error - Update Failed", "Item name must be between 5 - 100 chars", AlertType.ERROR);
						return;
					}else if(desc.length() < 10 || desc.length() > 200) {
						alert("Error - Update Failed", "Item description must be between 5 - 200 chars", AlertType.ERROR);
						return;
					}else if(price <= 0) {
						alert("Error - Update Failed", "Price must be greater than 0!", AlertType.ERROR);
						return;
					}else if(quantity <= 0) {
						alert("Error - Update Failed", "Quantity must be greater than 0!", AlertType.ERROR);
						return;
					}
					
					Item ni = new Item(tfItemid.getText(), name, desc, price, quantity);
					ni.update();
					
					alert("Successful Update", "Click OK to continue", AlertType.INFORMATION);
					
					itemTable.getItems().clear();
					itemTable.getItems().addAll(Item.getAll());
					
					tfItemid.setText(Item.getNextId());
				}else {
					alert("Error - Update Failed", "Must select user to update!", AlertType.ERROR);
				}
			});
			
			Button btnDelete = new Button("Delete Item");
			btnDelete.setDisable(true);
			btnDelete.setOnAction(e -> {
				if(itemTable.getSelectionModel().getSelectedItem() != null) {
					Item ni = new Item(tfItemid.getText(), "", "", 0, 0);
					ni.delete();
					
					alert("Successful Delete", "Click OK to continue", AlertType.INFORMATION);
					
					itemTable.getItems().clear();
					itemTable.getItems().addAll(Item.getAll());
				}else {
					alert("Error - Delete Failed", "Must select item to delete!", AlertType.ERROR);
				}
			});
			
			HBox hbButtons = new HBox();
			hbButtons.setSpacing(10);
			hbButtons.getChildren().addAll(btnInsert, btnUpdate, btnDelete);
			
			Button btnClear = new Button("Clear Form");
			btnClear.setMinWidth(300);
			btnClear.setOnAction(e -> {
				if(itemTable.getSelectionModel().getSelectedItem() != null) {
					tfItemid.setText(Item.getNextId());
				}
				tfName.clear();
				tfDesc.clear();
				spPrice.getValueFactory().setValue(0);
				spQuantity.getValueFactory().setValue(0);
			});
			
			gpManageItem.add(lbItemid, 0, 1, 1, 1);
			gpManageItem.add(tfItemid, 1, 1, 2, 1);
			gpManageItem.add(lbName, 0, 2, 1, 1);
			gpManageItem.add(tfName, 1, 2, 2, 1);
			gpManageItem.add(lbDesc, 0, 3, 1, 1);
			gpManageItem.add(tfDesc, 1, 3, 2, 1);
			gpManageItem.add(lbPrice, 0, 4, 1, 1);
			gpManageItem.add(spPrice, 1, 4, 2, 1);
			gpManageItem.add(lbQuantity, 0, 5, 1, 1);
			gpManageItem.add(spQuantity, 1, 5, 2, 1);
			gpManageItem.add(hbButtons, 0, 8, 3, 1);
			gpManageItem.add(btnClear, 0, 9, 3, 1);
			
			homeParent.setRight(gpManageItem);
			
			itemTable.getSelectionModel().selectedItemProperty().addListener((item, oldval, newval) -> {
				if(item != null && item.getValue() != null) {
					btnInsert.setDisable(true);
					btnUpdate.setDisable(false);
					btnDelete.setDisable(false);
					
					Item i = item.getValue();
					
					tfItemid.setText(i.getId());
					tfName.setText(i.getName());
					tfDesc.setText(i.getDescription());
					spPrice.getValueFactory().setValue(i.getPrice());
					spQuantity.getValueFactory().setValue(i.getQuantity());
				}else {
					btnInsert.setDisable(false);
					btnUpdate.setDisable(true);
					btnDelete.setDisable(true);
					
					tfItemid.clear();
					tfName.clear();
					tfDesc.clear();
					spPrice.getValueFactory().setValue(0);
					spQuantity.getValueFactory().setValue(0);
				}
			});
			
			itemTable.getItems().clear();
			itemTable.getItems().addAll(Item.getAll());
			
		}else if(homeState == HomeState.Transaction) {
			TableView<TransactionDetail> transTable = new TableView<>();
			TableColumn<TransactionDetail, String> col1 = new TableColumn<>("Transaction ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
			TableColumn<TransactionDetail, String> col2 = new TableColumn<>("Buyer");
			col2.setCellValueFactory(new PropertyValueFactory<>("userName"));
			TableColumn<TransactionDetail, String> col3 = new TableColumn<>("Item Name");
			col3.setCellValueFactory(new PropertyValueFactory<>("itemName"));
			TableColumn<TransactionDetail, Integer> col4 = new TableColumn<>("Item Description");
			col4.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));
			TableColumn<TransactionDetail, Integer> col5 = new TableColumn<>("Price");
			col5.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
			TableColumn<TransactionDetail, Integer> col6 = new TableColumn<>("Quantity");
			col6.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			TableColumn<TransactionDetail, Integer> col7 = new TableColumn<>("Total Price");
			col7.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
			TableColumn<TransactionDetail, Date> col8 = new TableColumn<>("Transaction Date");
			col8.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
			col8.setCellFactory(column -> {
		        TableCell<TransactionDetail, Date> cell = new TableCell<TransactionDetail, Date>() {
		            private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");

		            @Override
		            protected void updateItem(Date item, boolean empty) {
		                super.updateItem(item, empty);
		                if(empty) {
		                    setText(null);
		                }
		                else {
		                    this.setText(format.format(item));

		                }
		            }
		        };

		        return cell;
		    });
			
			transTable.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8);
			transTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			transTable.getItems().clear();
			transTable.getItems().addAll(TransactionDetail.getAll());
			
			homeParent.setCenter(transTable);
			
		}else if(homeState == HomeState.Market) {
			TableView<Item> marketTable = new TableView<>();
			TableColumn<Item, String> col1 = new TableColumn<>("Item ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("id"));
			TableColumn<Item, String> col2 = new TableColumn<>("Item Name");
			col2.setCellValueFactory(new PropertyValueFactory<>("name"));
			TableColumn<Item, String> col3 = new TableColumn<>("Item Description");
			col3.setCellValueFactory(new PropertyValueFactory<>("description"));
			TableColumn<Item, Integer> col4 = new TableColumn<>("Price");
			col4.setCellValueFactory(new PropertyValueFactory<>("price"));
			TableColumn<Item, Integer> col5 = new TableColumn<>("Quantity (Stock)");
			col5.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			
			marketTable.getColumns().addAll(col1, col2, col3, col4, col5);
			marketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			homeParent.setCenter(marketTable);
			
			//RIGHT
			GridPane gpManageItem = new GridPane();
			gpManageItem.setVgap(6);
			gpManageItem.setHgap(6);
			gpManageItem.setAlignment(Pos.TOP_CENTER);
			gpManageItem.setPadding(new Insets(15));
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100));
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100)); 
			gpManageItem.getColumnConstraints().add(new ColumnConstraints(100)); 
			
			Text lbItemid = new Text("Item ID ");
			TextField tfItemid = new TextField(Item.getNextId());
			tfItemid.setDisable(true);
			
			Text lbName = new Text("Item Name ");
			TextField tfName = new TextField();
			tfName.setDisable(true);
			Text lbDesc = new Text("Item Description ");
			TextField tfDesc = new TextField();
			tfDesc.setDisable(true);
			Text lbPrice = new Text("Price ");
			Spinner<Integer> spPrice = new Spinner<>(0, Integer.MAX_VALUE, 0, 1000);
			spPrice.setDisable(true);
			Text lbQuantity = new Text("Quantity ");
			Spinner<Integer> spQuantity = new Spinner<>(0, Integer.MAX_VALUE, 0);
			
			Button btnAddCart = new Button("Add to Cart");
			btnAddCart.setDisable(true);
			btnAddCart.setOnAction(e -> {
				if(marketTable.getSelectionModel().getSelectedItem() != null) {
					String id = tfItemid.getText();
					String name = tfName.getText();
					String desc = tfDesc.getText();
					int price = spPrice.getValue();
					int quantity = spQuantity.getValue();

					if(quantity <= 0) {
						alert("Error - Add to Cart Failed", "Item quantity must be greater than 0!", AlertType.ERROR);
						return;
					}else if(quantity > marketTable.getSelectionModel().getSelectedItem().getQuantity()) {
						alert("Error - Add to Cart Failed", "Item quantity cannot be greater than item stock!", AlertType.ERROR);
						return;
					}
					
					Cart c = new Cart(Main.auth.getId(), id, name, desc, price, quantity, price * quantity);
					c.insertOrUpdate();
					
					alert("Successful Cart Item Adding", "Click OK to continue", AlertType.INFORMATION);
					
					marketTable.getItems().clear();
					marketTable.getItems().addAll(Item.getAll());
					
					marketTable.getSelectionModel().clearSelection();
					tfItemid.setText(Item.getNextId());
				}
			});
			
			Button btnClear = new Button("Clear Form");
			btnClear.setDisable(true);
			btnClear.setOnAction(e -> {
				spQuantity.getValueFactory().setValue(0);
			});
			
			HBox hbButtons = new HBox();
			hbButtons.setSpacing(10);
			hbButtons.getChildren().addAll(btnClear, btnAddCart);
			
			gpManageItem.add(lbItemid, 0, 1, 1, 1);
			gpManageItem.add(tfItemid, 1, 1, 2, 1);
			gpManageItem.add(lbName, 0, 2, 1, 1);
			gpManageItem.add(tfName, 1, 2, 2, 1);
			gpManageItem.add(lbDesc, 0, 3, 1, 1);
			gpManageItem.add(tfDesc, 1, 3, 2, 1);
			gpManageItem.add(lbPrice, 0, 4, 1, 1);
			gpManageItem.add(spPrice, 1, 4, 2, 1);
			gpManageItem.add(lbQuantity, 0, 5, 1, 1);
			gpManageItem.add(spQuantity, 1, 5, 2, 1);
			gpManageItem.add(hbButtons, 1, 8, 2, 1);
			
			homeParent.setRight(gpManageItem);
			
			marketTable.getSelectionModel().selectedItemProperty().addListener((item, oldval, newval) -> {
				if(item != null && item.getValue() != null) {
					btnAddCart.setDisable(false);
					btnClear.setDisable(false);
					
					Item i = item.getValue();
					
					tfItemid.setText(i.getId());
					tfName.setText(i.getName());
					tfDesc.setText(i.getDescription());
					spPrice.getValueFactory().setValue(i.getPrice());
					if(!item.getValue().equals(oldval)) {
						spQuantity.getValueFactory().setValue(0);
					}
					
				}else {
					btnAddCart.setDisable(true);
					btnClear.setDisable(true);
					
					tfItemid.clear();
					tfName.clear();
					tfDesc.clear();
					spPrice.getValueFactory().setValue(0);
					spQuantity.getValueFactory().setValue(0);
				}
			});
			
			marketTable.getItems().clear();
			marketTable.getItems().addAll(Item.getAll());
			
		}else if(homeState == HomeState.Cart) {
			TableView<Cart> cartTable = new TableView<>();
			TableColumn<Cart, String> col1 = new TableColumn<>("Item ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("itemId"));
			TableColumn<Cart, String> col2 = new TableColumn<>("Item Name");
			col2.setCellValueFactory(new PropertyValueFactory<>("itemName"));
			TableColumn<Cart, String> col3 = new TableColumn<>("Item Description");
			col3.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));
			TableColumn<Cart, Integer> col4 = new TableColumn<>("itemPrice");
			col4.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
			TableColumn<Cart, Integer> col5 = new TableColumn<>("Quantity");
			col5.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			TableColumn<Cart, Integer> col6 = new TableColumn<>("Total Price");
			col6.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
			
			cartTable.getColumns().addAll(col1, col2, col3, col4, col5, col6);
			cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			homeParent.setCenter(cartTable);
			
			//BOTTOM
			HBox hbButtons2 = new HBox();
			hbButtons2.setSpacing(10);
			hbButtons2.setPadding(new Insets(10));
			
			Button btnCheckout = new Button("Checkout");
			btnCheckout.setOnAction(e -> {
				ArrayList<TransactionDetail> pendingTds = new ArrayList<>();

				for(Cart c : cartTable.getItems()) {
				    Item i = Item.get(c.getItemId());
				    int itemStock = i.getQuantity();
				    
				    Transaction t = new Transaction(Transaction.getNextId(), Main.auth.getId(), new Date(Instant.now().toEpochMilli()));
									
				    if(c.getQuantity() <= itemStock) {
				        TransactionDetail td = new TransactionDetail(t.getId(), "", c.getItemId(), c.getItemName(), "", 0, c.getQuantity(), 0, null);
				        pendingTds.add(td);
				    }else {
					alert("Error - Cart Checkout Failed", "Item (" + i.getId() + " - " + i.getName() + ") is out of stock. Please remove it from your cart or wait until it is restocked.", AlertType.ERROR);
					return;
				    }
				    
				    t.insert();
				}
				
				

				String alertmsg = "Successfully checkout cart with the following items: \n ";
				for(TransactionDetail td : pendingTds) {
					td.insert();
					alertmsg+= "- (" + td.getItemId() + ") " + td.getItemName() + " [x" + td.getQuantity() + "] \n";
				}
				alertmsg+=" \n\nClick OK to continue";
				
				alert("Successful Cart Checkout", alertmsg, AlertType.INFORMATION);
				
				for(Cart c : cartTable.getItems()) {
					Item.decrementStock(c.getItemId(), c.getQuantity());
					c.delete();
				}
				
				cartTable.getItems().clear();
				cartTable.getItems().addAll(Cart.getAll(Main.auth.getId()));
				if(cartTable.getItems().size() == 0) {
					btnCheckout.setDisable(true);
				}
			});
			
			btnCheckout.setPadding(new Insets(10));
			
			Button btnRemove = new Button("Remove from Cart");
			btnRemove.setDisable(true);
			btnRemove.setOnAction(e -> {
				if(cartTable.getSelectionModel().getSelectedItem() != null) {
					Cart c = cartTable.getSelectionModel().getSelectedItem();
					c.delete();
					
					alert("Successful Cart Item Removal", "Item removed from cart. Click OK to continue", AlertType.INFORMATION);
					cartTable.getItems().clear();
					cartTable.getItems().addAll(Cart.getAll(Main.auth.getId()));
					if(cartTable.getItems().size() == 0) {
						btnCheckout.setDisable(true);
					}
				}
			});
			
			btnRemove.setPadding(new Insets(10));
			
			hbButtons2.getChildren().addAll(btnRemove, btnCheckout);
			
			homeParent.setBottom(hbButtons2);
			
			BorderPane.setAlignment(hbButtons2, Pos.BOTTOM_CENTER);
			hbButtons2.setAlignment(Pos.BOTTOM_CENTER);
			hbButtons2.setMinHeight(150);
			
			cartTable.getItems().clear();
			cartTable.getItems().addAll(Cart.getAll(Main.auth.getId()));
			
			if(cartTable.getItems().size() == 0) {
				btnCheckout.setDisable(true);
			}
			
			cartTable.getSelectionModel().selectedItemProperty().addListener((item, oldval, newval) -> {
				if(item != null && item.getValue() != null) {
					btnRemove.setDisable(false);
				}else {
					btnRemove.setDisable(true);
				}
			});
			
		}else if(homeState == HomeState.History) {
			TableView<TransactionDetail> historyTable = new TableView<>();
			TableColumn<TransactionDetail, String> col1 = new TableColumn<>("Transaction ID");
			col1.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
			TableColumn<TransactionDetail, String> col2 = new TableColumn<>("Item Name");
			col2.setCellValueFactory(new PropertyValueFactory<>("itemName"));
			TableColumn<TransactionDetail, Integer> col3 = new TableColumn<>("Item Description");
			col3.setCellValueFactory(new PropertyValueFactory<>("itemDesc"));
			TableColumn<TransactionDetail, Integer> col4 = new TableColumn<>("Price");
			col4.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
			TableColumn<TransactionDetail, Integer> col5 = new TableColumn<>("Quantity");
			col5.setCellValueFactory(new PropertyValueFactory<>("quantity"));
			TableColumn<TransactionDetail, Integer> col6 = new TableColumn<>("Total Price");
			col6.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
			TableColumn<TransactionDetail, Date> col7 = new TableColumn<>("Transaction Date");
			col7.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
			col7.setCellFactory(column -> {
		        TableCell<TransactionDetail, Date> cell = new TableCell<TransactionDetail, Date>() {
		            private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");

		            @Override
		            protected void updateItem(Date item, boolean empty) {
		                super.updateItem(item, empty);
		                if(empty) {
		                    setText(null);
		                }
		                else {
		                    this.setText(format.format(item));

		                }
		            }
		        };

		        return cell;
		    });
			
			historyTable.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);
			historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			historyTable.getItems().clear();
			historyTable.getItems().addAll(TransactionDetail.getAll(Main.auth.getId()));
			
			homeParent.setCenter(historyTable);
		}
	}
	
	public void alert(String title, String message, AlertType type) {
		Alert a = new Alert(type);
		a.setTitle(title);
		a.setHeaderText(null);
		a.setContentText(message);
		a.show();
	}
	
	public void setLayouts() {
		setLayoutLogin();
		setLayoutRegister();
	}

	@Override
	public void start(Stage st) throws Exception {
		// TODO Auto-generated method stub
		Connector.initialize();
		stage = st;
		
		loginParent = new VBox();
		registerParent = new VBox();
		homeParent = new BorderPane();
		
		loginScene = new Scene(loginParent, 350, 220);
		registerScene = new Scene(registerParent, 350, 360);
		homeScene = new Scene(homeParent, 1000, 700);
		
		setLayouts();
		changePage(st, Page.Login);
	}

}
