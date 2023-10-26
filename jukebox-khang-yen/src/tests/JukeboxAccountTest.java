package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.embed.swing.JFXPanel;
import model.Account;
import model.JukeboxAccount;

/**
 * This unit test simulates a newly created account and play songs. An account
 * can play 3 songs a day, if 3 songs has been played by the same account, it
 * has to wait until a new day to start playing another songs.
 * @author Khang Tran
 */
class JukeboxAccountTest {
	 @BeforeAll
	    public static void setupJavaFX() {
			final JFXPanel fxPanel = new JFXPanel();
	    }

	@Test
	void testGetters() {
		//create an account and give access to the jukebox account
		Account acc = new Account("khang", "a");
		JukeboxAccount aJBA = new JukeboxAccount(acc);
		//0 songs has been played since it's a newly created account
		assertEquals(0, acc.getSongPlayed());
		//user name should be khang
		assertEquals("khang", acc.getID());
		//password should be a
		assertEquals("a", acc.getPassWord());
		//it's true that it can play a song
		assertTrue(aJBA.canPlaySong());
	}

	@Test
	void testCanPlaySong() {
		//create an account has yen as the username
		Account acc2 = new Account("yen", "a");
		JukeboxAccount test = new JukeboxAccount(acc2);
		assertTrue(test.canPlaySong());
		// play 3 songs
		test.playASong();
		test.playASong();
		test.playASong();
		//it cannot play any more song as it has played 3 songs
		assertFalse(test.canPlaySong());
	}

	@Test
	public void testChangeOfDateWithAFewTimes() {
		//create a new account with username Casey
		Account acc3 = new Account("Casey", "1111");
		JukeboxAccount user = new JukeboxAccount(acc3);
		//play songs and return the numbers of songs the account has played
		assertEquals(0, user.songsSelectedToday());
		user.playASong();
		assertEquals(1, user.songsSelectedToday());
		user.playASong();
		assertTrue(user.canPlaySong());
		user.playASong();
		assertEquals(3, user.songsSelectedToday());
		assertFalse(user.canPlaySong());
		//pretend it's a new day so the songs that account has played reset to 0
		user.pretendItsTomorrow();
		//play more songs until 3 songs has been played
		assertEquals(0, user.songsSelectedToday());
		user.playASong();
		;
		user.playASong();
		;
		assertTrue(user.canPlaySong());
		user.playASong();
		;
		assertEquals(3, user.songsSelectedToday());
		assertFalse(user.canPlaySong());
	}

}
