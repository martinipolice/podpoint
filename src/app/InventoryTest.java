package app;

import java.util.ArrayList;
import model.Inventory;
import model.InventoryItem;
import model.Product;
import util.FileHandler;

/***********************************************************
 * Quick test of Inventory. Not part of the final app.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-26
 ***********************************************************/

public class InventoryTest {

	public static void main(String[] args) {
		// reuse the products we saved earlier
		ArrayList<Product> products = FileHandler.loadProducts();

		// every product starts with 20 in stock; "low" means 5 or fewer
		Inventory inventory = new Inventory();
		for (Product p : products) {
			inventory.addItem(new InventoryItem(p, 20, 5));
		}

		// pretend most of one item was sold
		InventoryItem tablea = inventory.findItem("Premium Chocolate Tablea (100% Pure)", "72g");
		tablea.setQuantity(3);
		System.out.println("Tablea 72g low? " + tablea.isLow());

		// the Manager restocks it
		boolean ok = inventory.restock("Premium Chocolate Tablea (100% Pure)", "72g", 30);
		System.out.println("Restock worked? " + ok);
		System.out.println("Tablea 72g quantity: " + tablea.getQuantity());
		System.out.println("Tablea 72g low? " + tablea.isLow());

		// an item that does not exist
		System.out.println("Restock fake item? " + inventory.restock("Fake", "1g", 5));
		System.out.println("Items in inventory: " + inventory.getItems().size());

		// save, then load into a brand new Inventory object
		FileHandler.saveInventory(inventory);
		Inventory loaded = FileHandler.loadInventory();

		InventoryItem loadedTablea = loaded.findItem("Premium Chocolate Tablea (100% Pure)", "72g");
		System.out.println("Loaded items: " + loaded.getItems().size());
		System.out.println("Loaded tablea 72g quantity: " + loadedTablea.getQuantity());
	}
}