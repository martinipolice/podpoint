package model;

/***********************************************************
 * A Manager adds products, restocks, changes prices and
 * reviews the transaction history.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class Manager extends User {

	public Manager(String username, String password, String fullName) {
		super(username, password, fullName);
	}

	public String getRole() {
		return "Manager";
	}

	public boolean canManageProducts() {
		return true;
	}
	
	public boolean canVoidTransactions() {
		return false;
	}
}