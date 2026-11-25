package model;

import java.io.Serializable;
import util.InsufficientStockException;

/***********************************************************
 * One product in the inventory, with how many are in stock.
 *
 * @author CHARLES GIAN L. SANTOS
 * @created_date 2026-10-26
 ***********************************************************/

public class InventoryItem implements Serializable {
	// fixed version number of this class, so saved data stays readable after small code changes
	private static final long serialVersionUID = 1L;

	// this item HAS a product
	private Product product;
	private int quantity;
	// when quantity falls to this number or lower, the item is "low stock"
	private int reorderLevel;

	public InventoryItem(Product product, int quantity, int reorderLevel) {
		this.product = product;
		this.quantity = quantity;
		this.reorderLevel = reorderLevel;
	}

	public Product getProduct() { return product; }
	public int getQuantity() { return quantity; }
	public int getReorderLevel() { return reorderLevel; }

	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

	// true when the stock is at or below the reorder level
	public boolean isLow() {
		return quantity <= reorderLevel;
	}
	
	// removes stock when something is sold; refuses if there is not enough
	public void removeStock(int amount) throws InsufficientStockException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be more than 0.");
		}
		if (amount > quantity) { // call the exception if removing more than the owned quantity
			throw new InsufficientStockException("Only " + quantity + " left of " + product.getName() + " (" + product.getSize() + ").");
		}
		quantity = quantity - amount;
	}
}