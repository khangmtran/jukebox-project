package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

import javafx.collections.FXCollections;

/**
 * This class represents an account when user has logged in. It keeps track of
 * the account's music record.
 * 
 * @author Khang Tran
 */
@SuppressWarnings("serial")
public class JukeboxAccount implements Serializable {
	// today variable to keep track of today's date
	private LocalDate today;
	// lastplayDate keeps track of the last song was played on the account
	private LocalDate lastPlayDate;
	private Account account;
	private SongSelector songSelector;

	/*
	 * constructor
	 */
	public JukeboxAccount(Account account) {
		this.account = account;
		songSelector = new SongSelector(account);
	}

	/*
	 * method to check if the account can play more song
	 */
	public boolean canPlaySong() {
		today = LocalDate.now();
		// if today is a new day then reset the song count
		// of the account and return true to let to user play song
		if (!today.equals(lastPlayDate)) {
			account.resetSongCount();
			return true;
		}
		// if today is not a new day but the account has not played
		// more than 3 songs then return true and let the user play song
		else if (today.equals(lastPlayDate) && account.getSongPlayed() < 3)
			return true;
		// if today is not a new day and the account has played
		// more than 3 songs then return false
		return false;
	}

	/*
	 * method to let the user play a song if the account can play song
	 */
	public void playASong() {
		// check if the account can play song
		if (canPlaySong()) {
			// record the last play date and play the song
			lastPlayDate = LocalDate.now();
			account.recordSongPlayed();
		}
	}

	// This method returns the number of songs that have been selected today.
	public int songsSelectedToday() {
		return account.getSongPlayed();
	}

	// Add current date by one day
	// Increase the current date by one day and resets the count of songs selected
	// today to zero.
	public void pretendItsTomorrow() {
		today = today.plusDays(1);
		account.resetSongCount();
	}

	/*
	 * Gets the SongSelector associated with this JukeboxAccount.
	 */
	public SongSelector getSongSelector() {
		return this.songSelector;
	}

	/*
	 * Sets the SongSelector for this JukeboxAccount.
	 */
	public void setSongSelector(SongSelector songSelector) {
		this.songSelector = songSelector;
	}

	/**
	 * Custom serialization process for saving the state of the JukeboxAccount
	 * object. This method saves the current date, last play date, song list-view,
	 * play-list, and associated account.
	 * 
	 * @param out ObjectOutputStream to write the object's state
	 * @throws IOException if an I/O error occurs while writing the object
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		// Serialize(save) today's date and last play date
		out.writeObject(today);
		out.writeObject(lastPlayDate);

		// Save the song list from the SongSelector's ListView
		out.writeObject(new ArrayList<>(songSelector.getSongListView()));

		// Save the playList from the SongSelector's PlayList
		out.writeObject(new ArrayList<>(songSelector.getPlayList().getSongQueue()));

		// Save the associated Account object
		out.writeObject(account);
		// Save the number of songs played
		out.writeInt(songSelector.getTotalSongsAdded()); 
	}

	/**
	 * This method restores the saved state of the JukeboxAccount object. It READS
	 * the saved current date, last play date, song list view, play-list, and
	 * associated account.
	 * 
	 * @param in ObjectInputStream to read the object's state
	 * @throws IOException            if there is an error in reading the object's
	 *                                state
	 * @throws ClassNotFoundException if the class of a serialized object is not
	 *                                found
	 */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		// Deserialize today's date and last play date
		today = (LocalDate) in.readObject();
		lastPlayDate = (LocalDate) in.readObject();

		// Deserialize the song list view from the input stream
		@SuppressWarnings("unchecked")
		ArrayList<Song> songs = (ArrayList<Song>) in.readObject();

		// Deserialize the playList from the input stream
		@SuppressWarnings("unchecked")
		ArrayList<String> playlistSongs = (ArrayList<String>) in.readObject();

		// Deserialize the associated Account object
		account = (Account) in.readObject();

		// Deserialize the number of songs played
		// this prevents users from adding more songs to the list if they already have 3 songs!
		int totalAdd = in.readInt();

		// Initialize a new SongSelector object with deserialized data
		songSelector = new SongSelector(account);
		songSelector.setSongs(FXCollections.observableArrayList(songs));
		songSelector.setTotalSongsAdded(totalAdd);

		// Create a new playList and add the old playList back
		PlayList playList = new PlayList();
		playList.setSongQueue(new LinkedList<>(playlistSongs));

		// Set the old playList to SongSelector
		songSelector.setPlayList(playList);
		songSelector.setSongListView(FXCollections.observableArrayList(songs));
		
		// Check if users have already added songs to the list and display it on the pane
		if (songSelector.getTotalSongsAdded() == 0) {
		    songSelector.getMessageLabel().setText("0 songs selected");
		} else {
		    songSelector.getMessageLabel().setText("You have selected " + songSelector.getTotalSongsAdded() + " song(s) so far.");
		}

	}

}
