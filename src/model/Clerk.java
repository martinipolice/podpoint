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

	// the name of this role, shown in the menu bar
	@Override
	public String getRole() {
		return "Clerk";
	}

	// only the Manager can add products, restock and change prices
	@Override
	public boolean canManageProducts() {
		return false;
	}

	// a Clerk can void transactions 
	@Override
	public boolean canVoidTransactions() {
		return true;
	}
}