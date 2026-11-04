package app;

import java.util.ArrayList;
import model.CashEntry;
import model.Product;
import model.SaleLine;
import model.SaleTransaction;
import model.Transaction;

/***
 * Console test for CashEntry together with SaleTransaction.
 * @author Charles Gian L. Santos
 * @created_date 2026-11-04 
 ***/

public class CashTest {
	public static void main(String[] args) {
		// a product made by hand, so the test does not depend on the saved data
		Product tablea = new Product("Premium Chocolate Tablea (100% Pure)",
				"Unsweetened pure chocolate tablets", "72g", 155.0, "Tablea");

		// one list can hold both kinds of transactions (polymorphism)
		ArrayList<Transaction> history = new ArrayList<Transaction>();

		CashEntry start = new CashEntry("Clerk1", "Starting cash", 500, true);
		SaleTransaction sale = new SaleTransaction("Clerk1");
		sale.addLine(new SaleLine(tablea, 2));
		CashEntry fee = new CashEntry("Clerk1", "Delivery fee", 100, false);

		history.add(start);
		history.add(sale);
		history.add(fee);

		for (Transaction t : history) {
			System.out.println("#" + t.getId() + " " + t.getClass().getSimpleName()
					+ " total: " + t.getTotal());
		}
		System.out.println("Balance: " + balance(history));

		// a voided transaction should not count
		fee.voidTransaction();
		System.out.println("After voiding the delivery fee: " + balance(history));

		// a cash entry of 0 is refused
		try {
			new CashEntry("Clerk1", "Bad entry", 0, true);
		} catch (IllegalArgumentException e) {
			System.out.println("Refused: " + e.getMessage());
		}
	}

	// adds up the totals of the transactions that are not voided
	public static double balance(ArrayList<Transaction> list) {
		double sum = 0;
		for (Transaction t : list) {
			if (!t.isVoided()) {
				sum = sum + t.getTotal();
			}
		}
		return sum;
	}
}