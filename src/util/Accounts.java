package util;

import java.util.ArrayList;
import model.Clerk;
import model.Manager;
import model.User;

/***********************************************************
 * The fixed accounts of Pod Point and the login check.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class Accounts {

	// builds the list of accounts (one Manager and two Clerks)
	public static ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<>();
		users.add(new Manager("manager", "1234", "Maria Manager"));
		users.add(new Clerk("clerk1", "1111", "Carlo Clerk"));
		users.add(new Clerk("clerk2", "2222", "Dana Clerk"));
		return users;
	}

	// returns the matching User, or null if the username or password is wrong
	public static User login(String username, String password) {
		for (User u : getUsers()) {
			if (u.getUsername().equals(username) && u.checkPassword(password)) {
				return u;
			}
		}
		return null;
	}
}