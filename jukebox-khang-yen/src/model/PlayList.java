package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The PlayList class represents a playList of songs in a Jukebox. It maintains
 * a queue of songs to be played in a First-In-First-Out (FIFO) order. When a
 * song finishes playing, the next song in the queue is automatically queued up
 * to play.
 * 
 * This class is particularly useful for managing the play order of songs. It
 * ensures that songs are played in the order they were added (clicked on), and
 * it automatically handles the transition from one song to the next.
 *
 * @author Yen Lai
 */
@SuppressWarnings("serial")
public class PlayList implements Serializable {
	private Queue<String> songQueue;

	/**
	 * Constructs a new PlayList object. Initializes the song queue as a LinkedList.
	 */
	public PlayList() {
		songQueue = new LinkedList<>();
	}

	/**
	 * Adds a song to the end of the queue.
	 *
	 * @param songToAdd The song to be added to the queue.
	 */
	public void queueUpNextSong(String songToAdd) {
		songQueue.add(songToAdd);
	}

	/**
	 * Plays the next song in the queue.
	 *
	 * @return The next song to be played. Returns null if the queue is empty.
	 */
	public String playNextSong() {
		return songQueue.poll();
	}

	/*
	 * Checks if the song queue is empty.
	 */
	public boolean isEmpty() {
		return songQueue.size() == 0;
	}

	/*
	 * Gets the number of songs in the song queue.
	 */
	public int size() {
		return songQueue.size();
	}

	/*
	 * Retrieves the name of the next song to be played without removing it from the
	 * queue.
	 */
	public String startNextSong() {
		return songQueue.peek();
	}

	/*
	 * Gets the song queue.
	 */
	public Queue<String> getSongQueue() {
		return songQueue;
	}

	/**
	 * Sets the song queue using a LinkedList of song names.
	 * 
	 * @param linkedList The LinkedList of song names to be set as the song queue.
	 */
	public void setSongQueue(LinkedList<String> linkedList) {
		songQueue = linkedList;
	}

}
