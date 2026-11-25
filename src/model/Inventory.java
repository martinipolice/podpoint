package model;

import java.io.Serializable;
import java.util.ArrayList;

/***********************************************************
 * The shop's inventory: a list of items with their stock.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-26
 ***********************************************************/

public class Inventory implements Serializable {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	// the most that one item can have in stock 
	public static final int MAX_STOCK = 100000;

	private ArrayList<InventoryItem> items;

	public Inventory() {
		items = new ArrayList<InventoryItem>();
	}

	public ArrayList<InventoryItem> getItems() {
		return items;
	}

	// adds a new item to the inventory
	public void addItem(InventoryItem item) {
		items.add(item);
	}

	// finds an item by product name AND size; returns null if there is none
	public InventoryItem findItem(String name, String size) {
		for (InventoryItem item : items) {
			Product p = item.getProduct();
			if (p.getName().equals(name) && p.getSize().equals(size)) {
				return item;
			}
		}
		return null;
	}

	// adds stock to an existing item; returns false if the item is not found,
	// the amount is not above 0, or the stock would go above MAX_STOCK
	public boolean restock(String name, String size, int amount) {
		InventoryItem item = findItem(name, size);
		if (item == null || amount <= 0 || amount > MAX_STOCK - item.getQuantity()) {
			return false;
		}
		item.setQuantity(item.getQuantity() + amount);
		return true;
	}
}