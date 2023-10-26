package controller_view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Account;
import model.AccountCollection;
import model.JukeboxAccount;

/**
 * This class represents an event-driven program with a graphical user
 * interface. It has event handlers to mediate between the login and create
 * account view and the logged in view. User can play songs when logged in.
 * 
 * @author Khang Tran and Yen Lai
 */
public class JukeboxGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private LoginCreateAccountPane loginPane;
	private BorderPane everything;
	private JukeboxAccount jukeBoxAccount;
	private Account lastLoggedInAccount;
	// so JukeboxAccount will hold account
	private Map<Account, JukeboxAccount> accountMap = new HashMap<>();
	private MenuItem item = new MenuItem("Log Out");
	private Menu option = new Menu("Option");
	private MenuBar menuBar = new MenuBar();

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Call the LayoutGUI method to set up the graphical user interface
		LayoutGUI();
		// Call the setAlert method to display a confirmation dialog for loading
		// persisted data
		setAlert();
		// Call the logoutListener method to handle logout actions
		logoutListener();
		Scene scene = new Scene(everything, 800, 470);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jukebox");
		primaryStage.show();

		// Handling window close event. Allow users to save the current state and data
		// or leave unchanged from startup
		primaryStage.setOnCloseRequest(event -> {

			// Create a confirmation dialog for closing the window
			Alert alertClose = new Alert(AlertType.CONFIRMATION);
			alertClose.setHeaderText("Click cancel to not save any changes");
			alertClose.setContentText("Save data?");

			// Show the confirmation dialog and wait for user input
			Optional<ButtonType> result = alertClose.showAndWait();
			// If the user clicks OK, save data to a serialized file and exit the
			// application
			if (result.get() == ButtonType.OK) {
				saveState();

				// Exit the application
				Platform.exit();
				System.exit(0);

			} else {
				// If the user clicks Cancel, simply return and keep the application running
				return;
			}
		});
	}

	public void saveState() {
		try {

			// Create output streams for writing objects to a file
			FileOutputStream bytesToDisk = new FileOutputStream("objects.ser");
			ObjectOutputStream outFile = new ObjectOutputStream(bytesToDisk);

			// Save various objects including account data and song list to the file
			outFile.writeObject(loginPane.getAccountCollection());
			outFile.writeObject(accountMap);
			outFile.writeObject(lastLoggedInAccount);

			outFile.close();

		} catch (IOException ioe) {
			// Handle IO exception if occurred during saving objects
			System.out.println("Writing objects failed");
			ioe.printStackTrace();
			// Exit the application
			Platform.exit();
			System.exit(0);
		}
	}

	/*
	 * set alert for loading persisted data. Allow users to start with saved data
	 * stored in serialized file or start fresh
	 */
	private void setAlert() {
		// Create a confirmation dialog for loading persisted data
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Click cancel to start fresh");
		alert.setContentText("Click OK to read saved data");
		// Show the confirmation dialog and wait for user input
		Optional<ButtonType> result = alert.showAndWait();
		// If the user clicks OK, load data from a serialized file
		if (result.get() == ButtonType.OK) {
			loadState();
		} else {
			// If the user clicks Cancel, simply return and start with fresh data
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public void loadState() {
		try {

			// Create input streams for reading objects from a file
			FileInputStream rawBytes = new FileInputStream("objects.ser");
			ObjectInputStream inFile = new ObjectInputStream(rawBytes);

			// Deserialize objects and load data back into the application
			AccountCollection deserializedCollection = (AccountCollection) inFile.readObject();
			loginPane.setAccountCollection(deserializedCollection);

			this.accountMap = (Map<Account, JukeboxAccount>) inFile.readObject();
			this.lastLoggedInAccount = (Account) inFile.readObject();
			if (lastLoggedInAccount != null)
				userLoggedIn(lastLoggedInAccount);



			inFile.close();

		} catch (FileNotFoundException e) {
			// Handle file not found exception
			e.printStackTrace();
		} catch (IOException e) {
			// Handle IO exception if occurred during reading objects
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// Handle class not found exception
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the layout of the application, displaying login and create
	 * account options.
	 */
	private void LayoutGUI() {
		option.getItems().add(item);
		menuBar.getMenus().add(option);
		// This is the main view users can see
		everything = new BorderPane();
		everything.setTop(menuBar);
		// This will show the login and create account when the program first run
		loginPane = new LoginCreateAccountPane(this);
		everything.setCenter(loginPane);

	} 

	/**
	 * Updates the GUI after a user successfully logs in, displaying song titles,
	 * artists, and time.
	 */
	public void userLoggedIn(Account account) {

		lastLoggedInAccount = account;
		if (accountMap.containsKey(account)) {
			// Retrieve existing JukeboxAccount if present
			jukeBoxAccount = accountMap.get(account);

		} else {
			// Create a new JukeboxAccount if not present
			jukeBoxAccount = new JukeboxAccount(account);
			// SongSelector class contains the table view with Title, Artist, and Time
			accountMap.put(account, jukeBoxAccount);
		}

		// Add the SongSelector to the layout after a user has logged in
		// Now the user can view their song titles, artists, and time
		everything.setCenter(jukeBoxAccount.getSongSelector().getSongTable());
		// play the song ( Song Selector inside JukeboxAccount)
		jukeBoxAccount.getSongSelector().playNextSong();
		// This is for the temporary playButton that will be grade in Iteration1
		everything.setCenter(jukeBoxAccount.getSongSelector().getLayout());

	}

	/*
	 * Logout listener. User can logout through menu
	 */
	private void logoutListener() {
		item.setOnAction((ActionEvent arg0) -> {
			MenuItem menuClicked = (MenuItem) arg0.getSource();
			if (menuClicked.getText().equals("Log Out")) {
				// Stop the music
				jukeBoxAccount.getSongSelector().stopMusic();

				// Return to the login view
				everything.setCenter(loginPane);
			}
		});
	}

}
