package model;

/***********************************************************
 * A Manager adds products, restocks, changes prices, reviews the 
 * transaction history and can void transactions.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-11 
 ***********************************************************/

public class Manager extends User {

	public Manager(String username, String password, String fullName) {
		super(username, password, fullName);
	}

	// the name of this role, shown in the menu bar
	@Override
	public String getRole() {
		return "Manager";
	}

	// only the Manager can add products, restock and change prices
	@Override
	public boolean canManageProducts() {
		return true;
	}

	// the Manager has full access, so the Manager can void transactions too
	@Override
	public boolean canVoidTransactions() {
		return true;
	}
}