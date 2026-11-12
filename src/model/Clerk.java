package model;

/***********************************************************
 * A Clerk records sales and cash transactions, checks the
 * inventory and voids transactions. A Clerk cannot change products.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class Clerk extends User {

	public Clerk(String username, String password, String fullName) {
		super(username, password, fullName);
	}

	public String getRole() {
		return "Clerk";
	}

	public boolean canManageProducts() {
		return false;
	}
	
	public boolean canVoidTransactions() {
		return true;
	}
}