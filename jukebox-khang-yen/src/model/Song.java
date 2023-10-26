package model;

import java.io.Serializable;

/**
 * The Song class represents a song in the music player application. It stores
 * information about a song, including its title, artist, playtime, and file
 * name. This information is used in the SongSelector and Playlist classes to
 * display song details and play songs.
 * 
 * Each Song object corresponds to a single song file. The title, artist, and
 * playtime are displayed in the SongSelector table, and the file name is used
 * to locate the file when the song is played.
 * 
 * @author Yen Lai
 */

@SuppressWarnings("serial")
public class Song implements Serializable {
	private String title;
	private String artist;
	private String playtime;
	private String fileName;

	/**
	 * Constructs a new Song with the given title, artist, playtime, and file name.
	 */
	public Song(String title, String artist, String playtime, String fileName) {
		this.title = title;
		this.artist = artist;
		this.playtime = playtime;
		this.fileName = fileName;
	}

	// Getters

	/**
	 * @return the title of this song
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the artist of this song
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @return the playtime of this song
	 */
	public String getPlaytime() {
		return playtime;
	}

	/**
	 * @return the name of the file that contains this song
	 */
	public String getFileName() {
		return fileName;
	}
	
	/*
	 * This will display the actual song title when 
	 * the user click the song and add that to their queue 
	 */
    @Override
    public String toString() {
        return title;  
    }




}
