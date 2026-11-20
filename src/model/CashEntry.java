package model;

/***********************************************************
 * A cash in or cash out entry in the shop's cash box (not a product sale).
 * It is a Transaction, so it has an id, date, clerk and a voided flag.
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-04 
***********************************************************/

public class CashEntry extends Transaction {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	private String description;
	private double amount;
	private boolean cashIn;

	public CashEntry(String clerkName, String description, double amount, boolean cashIn) {
		super(clerkName);
		if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
			throw new IllegalArgumentException("Amount must be more than 0.");
		}
		this.description = description;
		this.amount = amount;
		this.cashIn = cashIn;
	}


	public double getAmount() {
		return amount;
	}

	public boolean isCashIn() {
		return cashIn;
	}

	// money in counts as plus, money out counts as minus
	@Override
	public double getTotal() {
		if (cashIn) {
			return amount;
		}
		return -amount;
	}
	
	@Override
	public String getDescription() {
		if (cashIn) {
			return "Cash in: " + description;
		}
		return "Cash out: " + description;
	}
}