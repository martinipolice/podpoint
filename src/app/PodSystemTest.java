package app;

import java.util.ArrayList;
import model.CashEntry;
import model.Inventory;
import model.InventoryItem;
import model.Product;
import model.SaleLine;
import model.SaleTransaction;
import model.Transaction;
import util.PodSystem;

/***********************************************************
 * Console test for the rules in PodSystem and the guards in the model:
 * the starting catalog, the balance, the void rule, the void guard and
 * the restock limit. Nothing is saved to a file.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-11-20
 ***********************************************************/

public class PodSystemTest {

	public static void main(String[] args) throws Exception {
		// ----- the starting catalog -----
		Inventory catalog = PodSystem.defaultInventory();
		System.out.println("Starting products: " + catalog.getItems().size() + " (expect 10)");
		System.out.println("First item stock: " + catalog.getItems().get(0).getQuantity() + " (expect 20)");

		// ----- the balance: Cash In 200, Cash Out 150 -----
		ArrayList<Transaction> history = new ArrayList<Transaction>();
		CashEntry cashIn = new CashEntry("Carlo Clerk", "Starting cash", 200.0, true);
		CashEntry cashOut = new CashEntry("Carlo Clerk", "Delivery fee", 150.0, false);
		history.add(cashIn);
		history.add(cashOut);
		System.out.println("Balance: " + PodSystem.balanceOf(history) + " (expect 50.0)");

		// ----- the void rule -----
		// voiding the Cash In would leave 50 - 200 = -150, so it is refused
		System.out.println("Void Cash In: " + PodSystem.voidProblem(history, cashIn));
		// voiding the Cash Out is fine (the balance goes up), so the answer is null
		System.out.println("Void Cash Out: " + PodSystem.voidProblem(history, cashOut) + " (expect null)");

		// ----- the void guard: a second void does nothing -----
		Product p = new Product("Test", "A test product", "100g", 100.0, "Test");
		Inventory inventory = new Inventory();
		InventoryItem item = new InventoryItem(p, 10, 5);
		inventory.addItem(item);

		item.removeStock(3);
		SaleTransaction sale = new SaleTransaction("Carlo Clerk");
		sale.addLine(new SaleLine(p, 3));
		System.out.println("Stock after the sale: " + item.getQuantity() + " (expect 7)");

		if (sale.voidTransaction()) {
			sale.undoEffect(inventory);
		}
		System.out.println("Stock after the void: " + item.getQuantity() + " (expect 10)");

		if (sale.voidTransaction()) {
			sale.undoEffect(inventory);
		}
		System.out.println("Stock after a second void: " + item.getQuantity() + " (expect 10)");
		history.add(sale);
		System.out.println("Void an already voided sale: " + PodSystem.voidProblem(history, sale));

		// ----- the restock limit -----
		System.out.println("Restock 50: " + inventory.restock("Test", "100g", 50) + " (expect true)");
		System.out.println("Restock 2147483647: " + inventory.restock("Test", "100g", Integer.MAX_VALUE) + " (expect false)");
		System.out.println("Restock 0: " + inventory.restock("Test", "100g", 0) + " (expect false)");
		System.out.println("Stock now: " + item.getQuantity() + " (expect 60)");
		System.out.println("Restock up to the limit: "
				+ inventory.restock("Test", "100g", Inventory.MAX_STOCK) + " (expect false, it would pass "
				+ Inventory.MAX_STOCK + ")");
	}
}
