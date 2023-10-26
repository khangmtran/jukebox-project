package model;

import java.io.Serializable;
/**
 * The AccountCollection class represents a set that holds all of the accounts. 
 * Every time an account is created, it is added to the set. 
 * It also gives the user authorization to an account
 * 
 * @author Khang Tran
 */
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AccountCollection implements Serializable{
	// Use a set to create unique user name
	private Set<Account> accountSet;

	public AccountCollection() {
		accountSet = new HashSet<>();
		
	    // Hard code the accounts
	    accountSet.add(new Account("Chris", "1"));
	    accountSet.add(new Account("Devon", "22"));
	    accountSet.add(new Account("River", "333"));
	    accountSet.add(new Account("Ryan", "4444"));
	}

	/*
	 * method to create an account. If the userName has already existed, throw an
	 * exception. If not, then create a new account and put it to the set
	 */
	public Account createAccount(String userName, String passWord) {
		for (Account acc : accountSet) {
			if (acc.getID().equals(userName))
				throw new IllegalArgumentException("Username already exists!");
		}
		Account accnt = new Account(userName, passWord);
		accountSet.add(accnt);
		return accnt;
	}

	/*
	 * remove the account from the JukeBox
	 */
	public void removeAccount(Account acc) {
		accountSet.remove(acc);
	}

	/*
	 * method to check if the userName and password is correct. Let the user login
	 * if it's correct
	 */
	public Account authorize(String id, String pw) {
		for (Account acc : accountSet) {
			if (acc.getID().equals(id))
				if (acc.getPassWord().equals(pw))
					return acc;
		}
		return null;
	}

	public String toString() {
		String accounts = null;
		for (Account acc : accountSet) {
			accounts += acc.getID();
		}
		return accounts;
	}

	/*
	 * method to get an account by its userName.
	 */
	public Account getAccount(String userName) {
		// Iterate over each account in the set
		for (Account acc : accountSet) {
			// If the current account's ID matches the provided username
			if (acc.getID().equals(userName)) {
				// Return the matching account
				return acc;
			}
		}
		// If no matching account was found, return null
		return null;
	}

}
