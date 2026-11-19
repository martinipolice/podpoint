package app;

import java.util.ArrayList;
import model.CashEntry;
import model.Product;
import model.SaleLine;
import model.SaleTransaction;
import model.Transaction;
import util.FileHandler;

/***********************************************************
 * Console test: saves the transaction history to a file and loads it back.
 * Run it twice to see the saved transactions come back.
 * @author Charles Gian L. Santos
 * @created_date 2026-11-05 
 ***********************************************************/

public class TransactionFileTest {
	public static void main(String[] args) {
		// load what was saved before (empty the first time)
		ArrayList<Transaction> history = FileHandler.loadTransactions();
		System.out.println("Loaded transactions: " + history.size());
		for (Transaction t : history) {
			System.out.println("#" + t.getId() + " " + t.getClass().getSimpleName()
					+ " total: " + t.getTotal());
		}

		// add one sale and one cash entry, then save everything
		Product tablea = new Product("Premium Chocolate Tablea (100% Pure)",
				"Unsweetened pure chocolate tablets", "72g", 155.0, "Tablea");
		SaleTransaction sale = new SaleTransaction("Clerk1");
		sale.addLine(new SaleLine(tablea, 1));
		CashEntry entry = new CashEntry("Clerk1", "Starting cash", 500, true);

		history.add(sale);
		history.add(entry);
		FileHandler.saveTransactions(history);
		System.out.println("Saved. Now " + history.size() + " transactions.");
	}
}