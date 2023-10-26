package model;

import java.io.Serializable;

/**
 * Account class which holds an account, each has a user name, password, and the
 * songs that account has played throughout the day.
 * 
 * @author Khang Tran
 */

@SuppressWarnings("serial")
public class Account implements Serializable{
	private String userName;
	private String passWord;
	private int songPlayed;


	/**
	 * constructor
	 * 
	 * @param userName
	 * @param passWord
	 */
	public Account(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
		this.songPlayed = 0;
	}

	/*
	 * default constructor
	 */
	public Account() {
		this.userName = null;
		this.passWord = null;
	}

	/**
	 * method to get user name of the account
	 * 
	 * @return user name of the account
	 */
	public String getID() {
		return this.userName;
	}

	/**
	 * method to get password of the account
	 * 
	 * @return password of the account
	 */
	public String getPassWord() {
		return this.passWord;
	}

	/**
	 * method to get how many songs the account has played
	 * 
	 * @return songPlayed
	 */
	public int getSongPlayed() {
		return this.songPlayed;
	}

	/**
	 * Method that increments the song that the account has played everytime a song
	 * is played
	 */
	public void recordSongPlayed() {
		songPlayed++;
	}

	/**
	 * Method to reset songPlayed of the account to 0 when a new day starts
	 */
	public void resetSongCount() {
		songPlayed = 0;
	}

}
