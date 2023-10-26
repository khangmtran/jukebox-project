package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The SongSelector class represents a table of songs. It allows songs to be
 * added to the table and provides functionality for selecting a song and
 * displays them in a sortable TableView
 * 
 * Users can click on the Title, Artist, and Time to sort them in ascending
 * order.
 * 
 * @author Yen Lai
 */

@SuppressWarnings("serial")
public class SongSelector implements Serializable {
	// Create a counter for the total number of songs added
	public int totalSongsAdded = 0;

	// A TableView to display songs.
	private TableView<Song> songTable;
	// Controls song playback and stops it when user logs out.
	public MediaPlayer mediaPlayer;

	// This uses the Account class to track the number of songs played by the user.
	private Account account;

	// The playList that hold all the songs
	private PlayList playList = new PlayList();

	// Label text to show users how many songs they have selected so far.
	private Label messageLabel = new Label();
	
	// An ObservableList to hold Song objects.
	// It's a list that allows listeners to track changes when they occur.
	private ObservableList<Song> songs;

	// Create a ListView to display the selected song
	private ObservableList<Song> selectedSongList;
	private ListView<Song> selectedSongListView;

	/**
	 * This class represents a song selector that allows users to select songs from
	 * a table.
	 * 
	 * The songs are loaded from a directory named "songfiles".
	 */
	@SuppressWarnings("unchecked")
	public SongSelector(Account account) {
		this.account = account;

		// Initialize the ObservableList with an empty observable array list
		songs = FXCollections.observableArrayList();
		selectedSongList = FXCollections.observableArrayList();
		// Initialize the TableView with the ObservableList
		songTable = new TableView<>(songs);
		selectedSongListView = new ListView<>(selectedSongList);
		// Create a TableColumn for the song title. This will be one of the columns in
		// our table
		TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		// Allow the column to be sorted by clicking on its header
		titleColumn.setSortable(true);

		// Similar setup for "Artist" and "Time" columns
		TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
		artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
		artistColumn.setSortable(true);

		TableColumn<Song, String> timeColumn = new TableColumn<>("Time");
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("playtime"));
		timeColumn.setSortable(true);

		// This will delete the extra column making it show only 3 columns
		double tableWidth = 650;
		titleColumn.setPrefWidth(tableWidth / 4);
		artistColumn.setPrefWidth(tableWidth / 4);
		timeColumn.setPrefWidth(tableWidth / 4);

		// Add all columns to the TableView. This makes them visible in the table
		songTable.getColumns().addAll(titleColumn, artistColumn, timeColumn);
		selectedSongListView.setItems(selectedSongList);

		// Load songs from a directory named "songfiles"
		loadSongsFromDirectory("songfiles");
	}

	/**
	 * This method creates and returns an HBox layout for the music player. The
	 * layout includes a TableView for the song list, a "Play" button, and a
	 * ListView for the song queue.
	 * 
	 * When the "Play" button is clicked, it adds the selected song to both the
	 * ListView and playList, and starts playing the song if no song is currently
	 * playing.
	 * 
	 * A maximum of 3 songs can be added to the playList. If this limit is reached
	 * or if the account has already played 3 songs, a warning pop-up is shown and
	 * no more songs can be added.
	 *
	 * @return HBox layout for the music player
	 */
	public HBox getLayout() {
		// Create a new HBox layout
		HBox layout = new HBox(5);
		layout.setAlignment(Pos.CENTER); // move the view to the right a little bit

		// Create a label for the TableView
		Label tableViewLabel = new Label("Song List");
		tableViewLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		// Add the label and song table to a VBox layout
		// now the tableViewLayout holds the label "Song List" as well as the actual
		// song table
		VBox tableViewLayout = new VBox(10); // adjust the spacing
		tableViewLayout.getChildren().addAll(tableViewLabel, songTable);

		// Add the VBox to the HBox layout
		layout.getChildren().add(tableViewLayout);

		// Create a new "Play" button
		Button playButton = new Button("Play");

		// Display to users that '0 songs selected' before any songs are chosen
		if (totalSongsAdded == 0) {
	        messageLabel.setText("0 songs selected");
		}
		// Set an action for when the button is clicked
	    // Once songs are chosen, the '0 songs selected' message will be updated
		playButton.setOnAction(e -> {
			// Limit song additions to three and also check if this account has played 3
			// songs or not
			if (totalSongsAdded < 3 & account.getSongPlayed() < 3) {
				// Get the song selected by the user
				Song selectedSong = getSelectedSong();
				if (selectedSong != null) {
					// Add the selected song to both the ListView and playList
					selectedSongList.add(selectedSong);
					// add the next song to the PlayList
					playList.queueUpNextSong("songfiles/" + selectedSong.getFileName());

					// If no song is playing, start the added song
					if (mediaPlayer == null) {
						// this just peek, not remove yet
						playSong(playList.startNextSong());
					}

					// Increment the total number of songs added
					totalSongsAdded++;
					// Update the message label with the number of songs selected
		            messageLabel.setText("You have selected " + totalSongsAdded + " song(s) so far.");
				}
			} else {
				// Show a warning pop-up when user tries to listen more than 3 songs!
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("3 songs have already been played today.");
				alert.showAndWait();
			}
			
		});
		
		
		// Add the "Play" button to the HBox layout
		// this will make the button stays in the middle
		layout.getChildren().add(playButton);

		// Create a label for the ListView
		Label listViewLabel = new Label("Song Queue");
		listViewLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		// Create a new VBox layout for the ListView
		VBox listViewLayout = new VBox(10); // adjust the spacing
		listViewLayout.getChildren().addAll(listViewLabel, selectedSongListView, messageLabel);

		// Add the VBox to the HBox layout
		layout.getChildren().add(listViewLayout);

		return layout;
	}

	public void playNextSong() {
		if (!getSongListView().isEmpty()) {
			String nextSongPath = getPlayList().startNextSong();
			if (nextSongPath != null) {
				playSong(nextSongPath);
			} else {

				// Log an error if nextSongPath is null (indicating an issue with the playlist)
				System.err.println("Error: Next song path is null.");
			}
		} else {
			// Log a message if the song list is empty
			System.out.println("No songs in the playlist.");
		}
	}

	/**
	 * This method plays the song specified by the songFileName parameter. If a song
	 * is already playing, it stops that song before starting the new one. 
	 * When the media has reached its end, it sets an action to play the next song in the queue.
	 *
	 * @param songFileName The file name of the song to be played
	 */
	private void playSong(String songFileName) {
		// Check if there is any song left to play
		if (songFileName != null) {

			// Stop the current song if it's playing
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			// Create a File object for the song
			File file = new File(songFileName);
			// Convert the file path to a URI
			URI uri = file.toURI();
			Media media = new Media(uri.toString());
			// Create a new MediaPlayer to play the media
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();

			System.out.println("Played " + account.getSongPlayed() + " song(s).");

			// Set an action for when the media has reached its end
			mediaPlayer.setOnEndOfMedia(new Waiter(songFileName));
		}
	}

	/**
	 * This class represents a waiter that waits for a song to finish playing.
	 * 
	 * When the song ends, it pauses for 2 seconds before playing the next song in
	 * the queue. It also removes the song from the ListView in a FIFO order.
	 * It also increments the count of songs played by the account. 
	 * 
	 * If 3 or more songs have already been played, it does not play another song.
	 */
	private class Waiter implements Runnable {

		@SuppressWarnings("unused")
		private String songPath;

		public Waiter(String songPath) {
			this.songPath = songPath;
		}

		@Override
		public void run() {
			System.out.println("Song ended. Play next in the queue after a pause");
			try {
				// now we can remove the song
				playList.playNextSong();
				Thread.sleep(2000); // Pause for 2 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("After a pause");
			if (account.getSongPlayed() <= 3) {
				// Play the next song in the queue only if less than 3 songs have been played
				Platform.runLater(() -> {
					playSong(playList.startNextSong());
					// Remove the song from the ListView in FIFO order
					selectedSongList.remove(0);
				});
				// increment song++
				account.recordSongPlayed();
				System.out.println("Remove successful");
			}
			mediaPlayer = null; // Set mediaPlayer to null after each song
		}
	}

	/*
	 * This method loads song files from a specified directory and displays them in
	 * the table.
	 */
	public void loadSongsFromDirectory(String directoryPath) {
		// Get the directory as a File object
		File directory = new File(directoryPath);

		// Get all files in the directory.
		File[] files = directory.listFiles();

		// Manually add songs
		Map<String, Song> song = new HashMap<>();
		// Manually add songs to the map. Each song is represented as a Song object,
		// which includes the title, artist, duration, and filename of the song.
		song.put("Capture.mp3", new Song("Caught a Pokemon!", "Game Freak", "0:05", "Capture.mp3"));
		song.put("DanseMacabreViolinHook.mp3",
				new Song("Danse Macabre", "Kevin MacLeod", "0:34", "DanseMacabreViolinHook.mp3"));
		song.put("DeterminedTumbao.mp3",
				new Song("Determined Tumbao", "FreePlay Music", "0:20", "DeterminedTumbao.mp3"));
		song.put("LongingInTheirHearts.mp3",
				new Song("Longing In Their Hearts", "Bonnie Raitt", "4:48", "LongingInTheirHearts.mp3"));
		song.put("LopingSting.mp3", new Song("Loping Sting", "Kevin MacLeod", "0:05", "LopingSting.mp3"));
		song.put("SwingCheese.mp3", new Song("Swing Cheese 15", "Artist for Swing Cheese", "0:15", "SwingCheese.mp3"));
		song.put("TheCurtainRises.mp3", new Song("The Curtain Rises", "FreePlay Music", "0:28", "TheCurtainRises.mp3"));
		song.put("UntameableFire.mp3", new Song("UntameableFire", "Pierre Langer", "4:42", "UntameableFire.mp3"));

		// For each file in the directory...
		for (File file : files) {
			// If it's an MP3 file
			if (file.getName().endsWith(".mp3")) {
				// Extract the filename of the MP3 file
				String songName = file.getName();
				// If not, add it to our map. We don't know the artist or duration,
				// so we set these fields to "Unknown Artist" and "Unknown Duration".
				if (!song.containsKey(songName)) {
					song.put(songName, new Song(songName, "Unknown Artist", "Unknown Duration", songName));

				}
				// Now that we've ensured our song is in the map,
				// we can add it to our list of songs.
				songs.add(song.get(songName));
			}
		}

	}

    // Getter for totalSongsAdded
    public int getTotalSongsAdded() {
        return totalSongsAdded;
    }

    // Setter for totalSongsAdded
    public void setTotalSongsAdded(int totalSongsAdded) {
        this.totalSongsAdded = totalSongsAdded;
    }
    
	/*
	 * Add a song to the ObservableList. This will update the TableView
	 * automatically!
	 */
	public void addSong(Song song) {
		songs.add(song);
	}

	/*
	 * Return the currently selected song in the TableView
	 */
	public Song getSelectedSong() {
		return songTable.getSelectionModel().getSelectedItem();
	}

	/*
	 * This will refresh the TableView. This is useful if items in the list have
	 * been changed directly and you want to ensure that the table displays the
	 * current data.
	 */
	public void refresh() {
		songTable.refresh();
	}

	/*
	 * Return the TableView. This is useful if you want to add it to a scene or
	 * modify its properties elsewhere in your code.
	 */
	public TableView<Song> getSongTable() {
		return songTable;
	}

	/*
	 * This will stop the music once user logout
	 */
	public void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}

	/*
	 * Retrieves the ObservableList of songs displayed in the SongSelector.
	 */
	public ObservableList<Song> getSongListView() {
		return selectedSongList;
	}

	/*
	 * Sets the ObservableList of songs displayed in the SongSelector and updates
	 * the UI accordingly.
	 */
	public void setSongListView(ObservableList<Song> songs) {
		selectedSongList = songs;
		selectedSongListView.setItems(selectedSongList);
	}

	/*
	 * Return the playList
	 */
	public PlayList getPlayList() {
		return playList;
	}

	/*
	 * Sets the playList of the SongSelector with the provided PlayList object.
	 */
	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}

	/*
	 * Retrieves the MediaPlayer associated with the SongSelector.
	 */
	public MediaPlayer getMediaPlayer() {
		return this.mediaPlayer;
	}

	/*
	 * Sets the list of songs in the SongSelector.
	 * 
	 * @param songs The list of songs to be set in the SongSelector. It also
	 * triggers a refresh of the SongSelector display.
	 */
	public void setSongs(List<Song> songs) {
		this.songs = FXCollections.observableArrayList(songs);
		refresh();
	}

	// Getter for messageLabel
    public Label getMessageLabel() {
        return messageLabel;
    }

    // Setter for messageLabel
    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }
}