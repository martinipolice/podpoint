package model;

/***********************************************************
 * The parent of all users of Pod Point. It is abstract, so
 * nobody can create a plain User, only a Manager or a Clerk.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11
 ***********************************************************/

public abstract class User {

	private String username;
	private String password;
	private String fullName;

	public User(String username, String password, String fullName) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	// returns true only if the typed password is the same as the saved one
	public boolean checkPassword(String attempt) {
		return password.equals(attempt);
	}

	// every kind of user answers these in its own way
	public abstract String getRole();

	public abstract boolean canManageProducts();
}