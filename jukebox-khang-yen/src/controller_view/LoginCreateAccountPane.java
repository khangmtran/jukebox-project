package controller_view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Account;
import model.AccountCollection;


/**
 * The LoginCreateAccountPane class represents a login and account creation
 * form. It provides text fields for entering a userName and password, and
 * buttons for logging in and creating an account.
 * 
 * The form checks if the userName and password fields are not empty before
 * allowing the user to log in or create an account.
 * 
 * The password field displays dots instead of the actual characters entered.
 * 
 * The account saving feature is not yet implemented, but users can still create
 * accounts or log in. The implementation will be finished soon!
 * 
 * @author Khang Tran and Yen Lai
 */
public class LoginCreateAccountPane extends GridPane {

	private Button login = new Button("Log in");
	private Button createAccount = new Button("Create Account");
	private Label userName = new Label("User Name");
	private Label password = new Label("Password");
	private TextField userID = new TextField();
	private PasswordField userPW = new PasswordField();
	private AccountCollection setAccount;
	private Account account;
	private JukeboxGUI jukeboxGUI;

    private Label messageLabel = new Label();  // Label to display error messages

	public boolean isSetUp = false;

	public LoginCreateAccountPane(JukeboxGUI jukeboxGUI) {
		this.jukeboxGUI = jukeboxGUI;
		setAccount = new AccountCollection();
		account = new Account();
		initializePanel();
		registerHandlers();
        this.add(messageLabel, 0, 3, 2, 1);

	}

	private void initializePanel() {
		// User Input
		this.add(userName, 0, 0);
		this.add(userID, 1, 0);
		this.add(password, 0, 1);
		this.add(userPW, 1, 1);

		// Buttons
		HBox hbox = new HBox(10);
		// This will set the login and createAccount to the right
		hbox.setAlignment(Pos.CENTER_RIGHT);
		hbox.getChildren().addAll(login, createAccount);
		this.add(hbox, 0, 2, 2, 1);

		// padding and alignment
		this.setPadding(new Insets(20));
		this.setVgap(15);
		this.setHgap(10);
		this.setAlignment(Pos.CENTER);
		
        // Clear the message label when the text fields are clicked
        userID.setOnMouseClicked(e -> messageLabel.setText(""));
        userPW.setOnMouseClicked(e -> messageLabel.setText(""));
	}

	/**
	 * This method registers event handlers for the 'login' and 'create account' buttons. 
	 * It performs the following operations when a button is clicked:
	 * 
	 * 1. If either the 'userName' or 'password' field is empty, the event handler will not perform any action.
	 * 
	 * 2. If the 'create account' button is clicked and both fields are filled:
	 *    - It checks if an account with the given userName already exists.
	 *    - If an account exists, it notifies the user and does not create a new account.
	 *    - If no account exists, it creates a new account and logs the user in.
	 *
	 * 3. If the 'login' button is clicked and both fields are filled:
	 *    - It checks if an account with the given userName and password exists.
	 *    - If an account exists, it logs the user in.
	 *    - If no account exists, it notifies the user that they need to create an account.
	 *
	 * The appropriate methods in the JukeboxGUI class are called to handle login and account creation.
	 */
	private void registerHandlers() {
		createAccount.setOnAction((event) -> {
			if (userID.getText().trim().isEmpty() || userPW.getText().trim().isEmpty()) {
				// If either field is empty, do nothing and return
				return;
			}
	        // Check if an account with the given userName already exists
	        if (setAccount.getAccount(userID.getText()) != null) {
	            // If it does, display a message and return
	            Alert alert = new Alert(AlertType.WARNING);
	            alert.setTitle("Error");
	            alert.setHeaderText(null);
	            alert.setContentText("Username already exists!");
	            alert.showAndWait();
	            return;
	        }
			account = setAccount.createAccount(userID.getText().trim(), userPW.getText().trim());
			// If account creation is successful, display the Table View
			userPW.clear();
			userID.clear();
			jukeboxGUI.userLoggedIn(account);
			
		});

		login.setOnAction((event) -> {
		    String id = userID.getText().trim();
		    String pw = userPW.getText().trim();
		    if (id.isEmpty() || pw.isEmpty()) {
		        // If either field is empty, do nothing and return
		        return;
		    }
		    account = setAccount.authorize(id, pw);
		    // If login is successful, display the Table View
		    if (account != null) {
		        userPW.clear();
		        userID.clear();
		        
		        
		        jukeboxGUI.userLoggedIn(account);
		        
		        
		    } else {
		        // Display a clear message asking the user to create an account 
		    	// if they attempt to log in without having one
		        Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Error");
		        alert.setHeaderText(null);
		        alert.setContentText("No account found with this username. Please create an account.");
		        alert.showAndWait();
		    }
		});

	}
	/*
	 * getter and setter for setAccount
	 */
	public AccountCollection getAccountCollection() {
		return this.setAccount;
	}
	public void setAccountCollection(AccountCollection accountCollection) {
	    this.setAccount = accountCollection;
	}

	public String getUserID() {
		return this.userID.getText();
	}

}
